package taxitycoon.behaviours.passenger;

import org.javatuples.Pair;
import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.staticobjects.TaxiStop;

public class StartBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6833600371259913934L;
	private boolean behaviourDone = false;

	@Override
	public void action() {
		PassengerAgent passengerAgent = (PassengerAgent) myAgent;

		/* If on destination (do nothing) */
		if (passengerAgent.hasReachedDestination()) {
			behaviourDone = true;
			passengerAgent.replaceBehaviour(new TravelComplete());
			return;
		}

		/* If on stop, change behaviour to waiting */
		if (passengerAgent.isOnStop()) {
			behaviourDone = true;

			/* Get on what taxi stop were on and add ourself to the queue */
			for (TaxiStop taxiStop : passengerAgent.getTaxiStopsArray()) {
				if (taxiStop.getPosition().equals(passengerAgent.getPosition())) {
					taxiStop.addPassengerToQueue(passengerAgent);
					break;
				}
			}

			passengerAgent.increaseWaitingTick();
			passengerAgent.replaceBehaviour(new Waiting());
			return;
		}

		/* Looks like we are walking, check were we are going */

		/* Get nearest stop */
		Pair<Integer, Integer> nearestStop = passengerAgent.getNearestStop();

		/* Get nearest stop and destination cost */
		int costToDestination = passengerAgent.getCostToDestination();
		int costToNearestStop = passengerAgent.getCostToPoint(nearestStop);

		/* Check if we should walk or take a cab */
		if (costToNearestStop > costToDestination) {
			passengerAgent.replaceBehaviour(new Walking(passengerAgent.getDestination()));
		} else {
			passengerAgent.replaceBehaviour(new Walking(nearestStop));
		}
	}

	@Override
	public boolean done() {
		return behaviourDone;
	}

}
