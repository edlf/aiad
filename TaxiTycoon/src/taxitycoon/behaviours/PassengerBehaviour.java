package taxitycoon.behaviours;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.passenger.AskTaxiForTravel;
import taxitycoon.staticobjects.TaxiStop;

public class PassengerBehaviour extends Behaviour {
	private static final long serialVersionUID = -842341654868835344L;
	private static final int STATE_ERROR = -1;
	private static final int STATE_START = 0;
	private static final int STATE_TRAVEL_COMPLETE = 1;
	private static final int STATE_WAITING_ON_STOP = 2;
	private static final int STATE_WALK_TO_STOP = 3;
	private static final int STATE_WALK_TO_DESTINATION = 4;
	private static final int STATE_IN_TAXI = 5;

	private PassengerAgent _passengerAgent;
	private int _currentState = 0;
	private boolean stateBegin = true;
	private boolean _waitingForReply = false;
	private int tic = 0;

	private Pair<Integer, Integer> _currentDestination;
	private TaxiStop _taxiStop;

	public PassengerBehaviour() {
		super();
		_passengerAgent = null;
	}

	@Override
	public void action() {
		if (_passengerAgent == null) {
			_passengerAgent = (PassengerAgent) myAgent;
		}

		switch (_currentState) {
		case STATE_ERROR:
			break;

		case STATE_START:
			startState();
			break;

		case STATE_TRAVEL_COMPLETE:
			travelCompleteState();
			break;

		case STATE_WAITING_ON_STOP:
			waitingOnStopState();
			break;

		case STATE_WALK_TO_STOP:
			walkToStopState();
			break;

		case STATE_WALK_TO_DESTINATION:
			walkToDestinationState();
			break;

		case STATE_IN_TAXI:
			inTaxiState();
			break;

		default:
			_currentState = STATE_ERROR;
			break;
		}
	}

	private void inTaxiState() {
		/*
		 * Should never happen (unless taxi is out of gas). In case it does go
		 * to the initial behaviour
		 */
		if (!_passengerAgent.isOnTaxi()) {
			changeStateTo(STATE_START);
			return;
		}

		_passengerAgent.increaseInTaxiTick();

		/* Update our location from the taxi */
		ACLMessage msg = _passengerAgent.receive();
		if (msg != null) {
			String title = msg.getContent();
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				String[] pos = title.split(",");
				int x = Integer.parseInt(pos[0]);
				int y = Integer.parseInt(pos[1]);
				Pair<Integer, Integer> newPos = new Pair<Integer, Integer>(x, y);
				_passengerAgent.move(newPos);
				break;

			default:
				System.out.println("MSG: Passenger received unkown message.");
				break;
			}
		}

		/* Check if we are currently inside a taxi and not on the destination */
		if (_passengerAgent.isOnTaxi() && !_passengerAgent.hasReachedDestination()) {
			return;
		}

		/* Check if we have reached the destination */
		if (_passengerAgent.hasReachedDestination()) {
			/* Inform taxi that we exited the taxi */

			/* Change behaviour to travel complete */
			changeStateTo(STATE_TRAVEL_COMPLETE);
			return;
		}

	}

	private void walkToStopState() {
		if (stateBegin) {
			stateBegin = false;
			tic = 0;
		}

		/* If on taxi stop */
		if (_passengerAgent.isOnStop()) {
			changeStateTo(STATE_WAITING_ON_STOP);
			return;
		}

		/* Walk to destination (every 8 tics) */
		tic++;
		if (tic % 8 == 0) {
			tic = 0;

			int deltaX = _passengerAgent.getPosX() - _currentDestination.getValue0();
			int deltaY = _passengerAgent.getPosY() - _currentDestination.getValue1();

			if (deltaX != 0) {
				if (deltaX < 0) {
					_passengerAgent.relativeMove(new Pair<Integer, Integer>(+1, 0));
				} else {
					_passengerAgent.relativeMove(new Pair<Integer, Integer>(-1, 0));
				}
			} else if (deltaY != 0) {
				if (deltaY < 0) {
					_passengerAgent.relativeMove(new Pair<Integer, Integer>(0, +1));
				} else {
					_passengerAgent.relativeMove(new Pair<Integer, Integer>(0, -1));
				}
			}
		}

		_passengerAgent.increaseWalkingTick();
	}

	private void walkToDestinationState() {
		if (stateBegin) {
			stateBegin = false;
			tic = 0;
		}

		/* If on destination */
		if (_passengerAgent.hasReachedDestination()) {
			changeStateTo(STATE_TRAVEL_COMPLETE);
			return;
		}

		/* Walk to destination (every 8 tics) */
		tic++;
		if (tic % 8 == 0) {
			tic = 0;

			int deltaX = _passengerAgent.getPosX() - _currentDestination.getValue0();
			int deltaY = _passengerAgent.getPosY() - _currentDestination.getValue1();

			if (deltaX != 0) {
				if (deltaX < 0) {
					_passengerAgent.relativeMove(new Pair<Integer, Integer>(+1, 0));
				} else {
					_passengerAgent.relativeMove(new Pair<Integer, Integer>(-1, 0));
				}
			} else if (deltaY != 0) {
				if (deltaY < 0) {
					_passengerAgent.relativeMove(new Pair<Integer, Integer>(0, +1));
				} else {
					_passengerAgent.relativeMove(new Pair<Integer, Integer>(0, -1));
				}
			}
		}

		_passengerAgent.increaseWalkingTick();
	}

	private void startState() {
		if (stateBegin) {
			stateBegin = false;
		}

		/* If on destination (do nothing) */
		if (_passengerAgent.hasReachedDestination()) {
			changeStateTo(STATE_TRAVEL_COMPLETE);
			return;
		}

		/* If on stop, change behaviour to waiting */
		if (_passengerAgent.isOnStop()) {
			changeStateTo(STATE_WAITING_ON_STOP);
			return;
		}

		/* Looks like we are walking, check were we are going */

		/* Get nearest stop */
		Pair<Integer, Integer> nearestStop = _passengerAgent.getNearestStop();

		/* Check if we should walk or take a cab */
		if (_passengerAgent.getCostToPoint(nearestStop) > _passengerAgent.getCostToDestination()) {
			_currentDestination = _passengerAgent.getDestination();
			changeStateTo(STATE_WALK_TO_DESTINATION);
		} else {
			_currentDestination = nearestStop;
			changeStateTo(STATE_WALK_TO_STOP);
		}
	}

	private void waitingOnStopState() {
		if (stateBegin) {
			if (_taxiStop == null) {
				_taxiStop = TaxiCentral.getTaxiStopAt(_passengerAgent.getPosition());
			}

			/* Check if are already in the queue */
			if (!_taxiStop.isPassengerInQueue(_passengerAgent)) {
				_taxiStop.addPassengerToQueue(_passengerAgent);
			}

			stateBegin = false;
		}

		_passengerAgent.increaseWaitingTick();

		/* Check if we have a taxi available and is our turn */
		if (_taxiStop.hasTaxiAvailable() && _taxiStop.isMyTurn(_passengerAgent)) {

			if (!_waitingForReply) {
				System.out.println("MSG: send message asktaxifortravel");
				/* Send request to taxi at head of queue */
				AskTaxiForTravel askTaxiForTravelMessage = new AskTaxiForTravel(_passengerAgent.getAID(),
						_passengerAgent.getDestination(), _taxiStop.getTaxiAtHeadOfQueue());
				_passengerAgent.send(askTaxiForTravelMessage);
				_waitingForReply = true;
			}

			return;
		}

		if (_waitingForReply) {
			System.out.println("XX Passenger Waiting for response..");
			ACLMessage msg = _passengerAgent.receive();
			if (msg != null) {
				String title = msg.getContent();
				System.out.println("MSG: Passenger got message:" + title);

				switch (msg.getPerformative()) {
				case ACLMessage.ACCEPT_PROPOSAL:
					_passengerAgent.setOnTaxi();
					_taxiStop.removePassengerFromQueue(_passengerAgent);
					changeStateTo(STATE_IN_TAXI);
					return;

				case ACLMessage.REJECT_PROPOSAL:

					break;

				default:
					System.out.println("MSG: Passenger received unkown message.");
					break;
				}
			}
		}

		if (_taxiStop.hasTaxiAvailable()) {

			return;
		} else {
			/* Send a message to taxi central asking for taxis */
			// System.out.println(_passengerAgent.toString());

			return;
		}

		/* Read messages since another taxi might go to the same destination */
	}

	private void travelCompleteState() {
		if (stateBegin) {
			stateBegin = false;
		}

		if (!_passengerAgent.hasReachedDestination()) {
			System.out.println("BUG: Passenger not on destination but on TravelComplete behavior.");
		}

		_passengerAgent.printStats();
		_passengerAgent.doDelete();
	}

	private void changeStateTo(int newState) {
		System.out.println(_passengerAgent.getLocalName() + " state: " + newState);
		stateBegin = true;
		_currentState = newState;
	}

	@Override
	public boolean done() {
		return (_currentState == STATE_TRAVEL_COMPLETE && !stateBegin);
	}
}