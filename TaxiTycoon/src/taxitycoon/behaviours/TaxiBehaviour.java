package taxitycoon.behaviours;

import java.util.LinkedList;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;
import sajas.core.AID;
import sajas.core.behaviours.CyclicBehaviour;
import taxitycoon.agents.SimAgent;
import taxitycoon.agents.TaxiAgent;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.taxi.AcceptRide;
import taxitycoon.messages.taxi.RequestPreferentialStop;
import taxitycoon.messages.taxi.UpdatePassengerLocation;
import taxitycoon.staticobjects.RefuelStation;
import taxitycoon.staticobjects.TaxiStop;

public class TaxiBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 2163115385831517999L;

	private static final int STATE_ERROR = -1;
	private static final int STATE_START = 0;
	private static final int STATE_WAIT = 1;
	private static final int STATE_TAXI_STOP = 2;
	private static final int STATE_NO_GAS = 3;
	private static final int STATE_GO_TO_NEAREST_STOP = 4;
	private static final int STATE_REFUELING_NO_PASSENGERS = 5;
	private static final int STATE_TRANSPORTING_PASSENGERS = 6;
	private static final int STATE_GO_TO_STOP = 7;
	private static final int STATE_TAXI_ON_REFUEL_STOP = 8;

	private int _currentState = 0;
	private boolean _stateBegin = true;
	private static final int _defaultTimeOut = 5;
	private static final int _RefuelTime = 8;
	private int currentTimeOut = 0;

	private boolean _outOfGas = false;
	private boolean _gasOnReserve = false;

	private Pair<Integer, Integer> _currentDestination;
	LinkedList<Pair<Integer, Integer>> _pathToDestination;

	private TaxiAgent _taxiAgent;
	private TaxiStop _taxiStop;
	private RefuelStation _refuelStation;
	private jade.core.AID _taxiCentralAID;

	public TaxiBehaviour() {
		super();
		_taxiAgent = null;
		_taxiStop = null;
		_taxiCentralAID = null;
	}

	/* Current state method call */
	@Override
	public void action() {
		if (_taxiAgent == null) {
			_taxiAgent = (TaxiAgent) myAgent;
			_taxiCentralAID = new AID(TaxiCentral.class.getSimpleName(), AID.ISLOCALNAME);
		}

		getTaxiStatus();

		switch (_currentState) {
		case STATE_ERROR:
			break;

		case STATE_START:
			startState();
			break;

		case STATE_WAIT:
			waitingState();
			break;

		case STATE_REFUELING_NO_PASSENGERS:
			refuelingNoPassengersState();
			break;

		case STATE_GO_TO_NEAREST_STOP:
			goToNearestStopState();
			break;

		case STATE_TAXI_STOP:
			taxiStopState();
			break;

		case STATE_TRANSPORTING_PASSENGERS:
			transportingPassengersState();
			break;
			
		case STATE_GO_TO_STOP:
			goToStopState();
			break;

		case STATE_NO_GAS:
			noGasState();
			break;
			
		case STATE_TAXI_ON_REFUEL_STOP:
			onRefuelStopState();
			break;

		default:
			_currentState = STATE_ERROR;
			break;
		}

	}
	
	private void onRefuelStopState() {
		if (_stateBegin) {
			if (_taxiAgent.isOnRefuelStation()) {
				_refuelStation = SimAgent.getRefuelStationAt(_taxiAgent.getPosition());
			} else {
				System.out.println("BUG: Taxi refuel state on non refuel tile");
			}

			_refuelStation.addTaxiToQueue(_taxiAgent);
			
			currentTimeOut = 0;
			_stateBegin = false;
		}
		
		if(_refuelStation.isMyTurn(_taxiAgent)){
			if (currentTimeOut > _RefuelTime){
				_taxiAgent.gasRefuel();
				_refuelStation.removeTaxiFromQueue(_taxiAgent);
				changeStateTo(STATE_START);
			} else {
				currentTimeOut++;
			}
		}
	}

	private void refuelingNoPassengersState(){
		if (_stateBegin) {
			_stateBegin = false;
			_currentDestination = _taxiAgent.getNearestRefuelStation().getPosition();
			_pathToDestination = _taxiAgent.getShortestPathTo(_currentDestination);

			/* No path obtained */
			if (_pathToDestination.isEmpty()) {
				System.out.println("Taxi cannot find a path to destination! " + _currentDestination);
				changeStateTo(STATE_ERROR);
				return;
			}
		}

		/* Check if travel has ended */
		if (_taxiAgent.getPosition().equals(_currentDestination)) {
			changeStateTo(STATE_TAXI_ON_REFUEL_STOP);
			return;
		}

		_taxiAgent.increaseRefuelingTick();
		move();
	}

	private void transportingPassengersState() {
		if (_stateBegin) {
			_stateBegin = false;
			_pathToDestination = _taxiAgent.getShortestPathTo(_currentDestination);

			/* No path obtained */
			if (_pathToDestination.isEmpty()) {
				System.out.println("Taxi cannot find a path to destination! " + _currentDestination);
				changeStateTo(STATE_ERROR);
				return;
			}
		}

		/* Check if travel has ended */
		if (_taxiAgent.getPosition().equals(_currentDestination)) {
			_taxiAgent.clearPassengers();
			changeStateTo(STATE_START);
			return;
		}

		_taxiAgent.increaseInTransitTick();
		move();
	}

	/* Go to nearest stop behaviour (DONE) */
	private void goToNearestStopState() {
		if (_stateBegin) {
			_stateBegin = false;
			_currentDestination = _taxiAgent.getNearestTaxiStop().getPosition();
			_pathToDestination = _taxiAgent.getShortestPathTo(_currentDestination);

			/* No path obtained */
			if (_pathToDestination.isEmpty()) {
				System.out.println("Taxi cannot find a path to destination! " + _currentDestination);
				changeStateTo(STATE_ERROR);
				return;
			}
		}

		/* Check if travel has ended */
		if (_taxiAgent.getPosition().equals(_currentDestination)) {
			changeStateTo(STATE_TAXI_STOP);
			return;
		}

		_taxiAgent.increaseInTransitTick();

		move();
	}
	
	/* Go to specific stop behaviour (DONE) */
	private void goToStopState() {
		if (_stateBegin) {
			_stateBegin = false;
			_pathToDestination = _taxiAgent.getShortestPathTo(_currentDestination);

			/* No path obtained */
			if (_pathToDestination.isEmpty()) {
				System.out.println("Taxi cannot find a path to destination! " + _currentDestination);
				changeStateTo(STATE_ERROR);
				return;
			}
		}

		/* Check if travel has ended */
		if (_taxiAgent.getPosition().equals(_currentDestination)) {
			changeStateTo(STATE_TAXI_STOP);
			return;
		}

		_taxiAgent.increaseInTransitTick();

		move();
	}

	private void startState() {
		if (_stateBegin) {
			currentTimeOut = 0;
			_stateBegin = false;
		}
		
		/* Check if we ran out of gas */
		if (_outOfGas) {
			changeStateTo(STATE_NO_GAS);
			return;
		}

		/* Check if we are on reserve */
		if (_gasOnReserve) {
			changeStateTo(STATE_REFUELING_NO_PASSENGERS);
			return;
		}

		/* Check if taxi central wants us to go to a specific stop */
		if (currentTimeOut == 0){
			RequestPreferentialStop requestPreferentialStop = new RequestPreferentialStop(_taxiCentralAID);
			_taxiAgent.send(requestPreferentialStop);
		}
		
		if (currentTimeOut < _defaultTimeOut) {
			ACLMessage msg = _taxiAgent.receive();
			if (msg != null) {
				String title = msg.getContent();		
				switch (msg.getPerformative()) {
				
				case ACLMessage.CONFIRM:
					String[] pos = title.split(",");
					int x = Integer.parseInt(pos[0]);
					int y = Integer.parseInt(pos[1]);
					_currentDestination = new Pair<Integer, Integer> (x,y);		
					changeStateTo(STATE_GO_TO_STOP);
					break;
					
				case ACLMessage.DISCONFIRM:
					changeStateTo(STATE_GO_TO_NEAREST_STOP);
					break;
					
				default:
					break;
				}
			}
			
			currentTimeOut++;
		} else {
			/* Timeout: go to the nearest stop */
			if (!_taxiAgent.isOnTaxiStop()) {
				changeStateTo(STATE_GO_TO_NEAREST_STOP);
			}
		}
	}

	private void noGasState() {
		if (_stateBegin) {

			_stateBegin = false;
		}
		
		/* Ask another taxi for gas? */
	}

	private void waitingState() {
		if (_stateBegin) {

			_stateBegin = false;
		}

		_taxiAgent.increaseWaitingTick();
	}

	private void taxiStopState() {
		if (_stateBegin) {
			if (_taxiAgent.isOnTaxiStop()) {
				_taxiStop = TaxiCentral.getTaxiStopAt(_taxiAgent.getPosition());
			} else {
				System.out.println("BUG: Taxi stop state on non stop tile");
			}

			_taxiStop.addTaxiToQueue(_taxiAgent);

			_stateBegin = false;
		}
		_taxiAgent.increaseWaitingTick();
		
		/* Check if we are on reserve */
		if (_gasOnReserve) {
			changeStateTo(STATE_REFUELING_NO_PASSENGERS);
			return;
		}
		
		/* Check for messages */
		ACLMessage msg = _taxiAgent.receive();
		if (msg != null) {
			String title = msg.getContent();
			jade.core.AID senderAID = msg.getSender();

			switch (msg.getPerformative()) {

			case ACLMessage.REQUEST:
				System.out.println("MSG: Taxi: AcceptRideMessage");
				AcceptRide acceptRideMessage = new AcceptRide(_taxiStop.getPassengerAtHeadOfQueue().getAID());
				_taxiAgent.send(acceptRideMessage);
				_taxiAgent.addPassenger(senderAID);
				_taxiStop.removeTaxiFromQueue(_taxiAgent);
				// TODO FIX HARDCODED DESTINATION
				_currentDestination = new Pair<Integer, Integer>(28, 28);
				changeStateTo(STATE_TRANSPORTING_PASSENGERS);

				/* TODO Ask other passengers for the destination */
				// RequestPassenerDestination requestPassenerDestination = new
				// RequestPassenerDestination();
				// _taxiAgent.send(askTaxiForTravelMessage);

				break;

				/* Taxi central preferred stop */
			case ACLMessage.INFORM:
				String[] pos = title.split(",");
				int x = Integer.parseInt(pos[0]);
				int y = Integer.parseInt(pos[1]);
				_currentDestination = new Pair<Integer, Integer> (x,y);	
				changeStateTo(STATE_GO_TO_STOP);
				
				break;
				
				/* Passenger responses to destination location */
			case ACLMessage.ACCEPT_PROPOSAL:

				break;

			case ACLMessage.REJECT_PROPOSAL:

				break;

			default:
				break;
			}
		}
	}
	
	/*** Common methods ***/
	
	/* Get taxi status */
	private void getTaxiStatus() {
		/* Check if we ran out of gas */
		_outOfGas = (_taxiAgent.getGasInTank() == 0);

		/* Check if we are on reserve */
		_gasOnReserve = _taxiAgent.isGasOnReserve();
	}
	
	private void changeStateTo(int newState) {
		// System.out.println(_taxiAgent.getLocalName() + " state: " + newState);
		_stateBegin = true;
		_currentState = newState;
	}
	
	private void move() {
		if (_pathToDestination == null || _pathToDestination.isEmpty()) {
			return;
		}

		/* Move towards destination */
		Pair<Integer, Integer> nextPos = _pathToDestination.getFirst();

		if (_taxiAgent.hasPassengers()) {
			for (jade.core.AID passengerAID : _taxiAgent.getPassengers()) {
				UpdatePassengerLocation updatePassengerLocation = new UpdatePassengerLocation(passengerAID, nextPos);
				_taxiAgent.send(updatePassengerLocation);
			}
		}

		_taxiAgent.move(nextPos);
		_pathToDestination.removeFirst();
	}
}
