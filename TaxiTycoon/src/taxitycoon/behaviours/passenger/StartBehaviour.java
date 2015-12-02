package taxitycoon.behaviours.passenger;

import org.javatuples.Pair;
import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;

/**
 * Initial passenger agent behaviour
 * 
 * Does initial checks, and changes to appropriate behaviours.
 */

public class StartBehaviour extends Behaviour {
	private static final long serialVersionUID = 6833600371259913934L;

	@Override
	public void action() {
		PassengerAgent passengerAgent = (PassengerAgent) myAgent;

		/* If on destination (do nothing) */
		if (passengerAgent.hasReachedDestination()) {
			passengerAgent.replaceBehaviour(new TravelComplete());
			return;
		}

		/* If on stop, change behaviour to waiting */
		if (passengerAgent.isOnStop()) {
			passengerAgent.replaceBehaviour(new Waiting());
			return;
		}

		/* Looks like we are walking, check were we are going */

		/* Get nearest stop */
		Pair<Integer, Integer> nearestStop = passengerAgent.getNearestStop();

		/* Check if we should walk or take a cab */
		if (passengerAgent.getCostToPoint(nearestStop) > passengerAgent.getCostToDestination()) {
			passengerAgent.replaceBehaviour(new Walking(passengerAgent.getDestination()));
		} else {
			passengerAgent.replaceBehaviour(new Walking(nearestStop));
		}
	}

	@Override
	public boolean done() {
		return false;
	}
}
