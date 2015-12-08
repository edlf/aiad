package taxitycoon.behaviours.taxi;

import java.util.LinkedList;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.CyclicBehaviour;
import taxitycoon.agents.TaxiAgent;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.taxi.AcceptRide;
import taxitycoon.messages.taxi.RequestPassenerDestination;
import taxitycoon.messages.taxi.UpdatePassengerLocation;
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

	private int _currentState = 0;
	private boolean stateBegin = true;

	private boolean _outOfGas = false;
	private boolean _gasOnReserve = false;

	private Pair<Integer, Integer> _currentDestination;
	LinkedList<Pair<Integer, Integer>> _pathToDestination;

	private TaxiAgent _taxiAgent;
	private TaxiStop _taxiStop;

	public TaxiBehaviour() {
		super();
		_taxiAgent = null;
		_taxiStop = null;
	}

	@Override
	public void action() {
		if (_taxiAgent == null) {
			_taxiAgent = (TaxiAgent) myAgent;
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

		case STATE_NO_GAS:

			noGasState();
			break;

		default:
			_currentState = STATE_ERROR;
			break;
		}

	}

	private void transportingPassengersState() {
		if (stateBegin) {
			stateBegin = false;
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
			changeStateTo(STATE_START);
			return;
		}

		_taxiAgent.increaseInTransitTick();
		move();
	}

	private void goToNearestStopState() {
		if (stateBegin) {
			stateBegin = false;
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

	private void startState() {
		if (stateBegin) {

			stateBegin = false;
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

		/* If not go to the nearest stop */
		if (!_taxiAgent.isOnTaxiStop()) {
			changeStateTo(STATE_GO_TO_NEAREST_STOP);
		}

	}

	private void noGasState() {
		if (stateBegin) {

			stateBegin = false;
		}
	}

	private void waitingState() {
		if (stateBegin) {

			stateBegin = false;
		}

		_taxiAgent.increaseWaitingTick();
	}

	private void taxiStopState() {
		if (stateBegin) {
			if (_taxiAgent.isOnTaxiStop()) {
				_taxiStop = TaxiCentral.getTaxiStopAt(_taxiAgent.getPosition());
			} else {
				System.out.println("BUG: Taxi stop state on non stop tile");
			}

			_taxiStop.addTaxiToQueue(_taxiAgent);

			stateBegin = false;
		}

		/* Check for messages */
		ACLMessage msg = _taxiAgent.receive();
		if (msg != null) {
			String title = msg.getContent();
			jade.core.AID senderAID = msg.getSender();
			System.out.println("MSG: Taxi got message:" + title);

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

			case ACLMessage.ACCEPT_PROPOSAL:

				break;

			case ACLMessage.REJECT_PROPOSAL:

				break;

			default:
				System.out.println("MSG: Taxi received unkown message.");
				break;
			}
		}
		_taxiAgent.increaseWaitingTick();
	}
	
	/*** Common methods ***/
	
	/* Get taxi status */
	private void getTaxiStatus() {
		/* Check if we ran out of gas */
		if (_taxiAgent.getGasInTank() == 0) {
			_outOfGas = true;
		}

		/* Check if we are on reserve */
		if (_taxiAgent.isGasOnReserve()) {
			_gasOnReserve = true;
		}
	}
	
	private void changeStateTo(int newState) {
		System.out.println(_taxiAgent.getLocalName() + " state: " + newState);
		stateBegin = true;
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
