package taxitycoon.behaviours.passenger;

import org.javatuples.Pair;
import sajas.core.behaviours.OneShotBehaviour;
import taxitycoon.agents.PassengerAgent;

/**
 * Initial passenger agent behaviour
 * 
 * Does initial checks, and changes to appropriate behaviours.
 */

public class StartBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 6833600371259913934L;
	private PassengerAgent _passengerAgent;
	
	public StartBehaviour() {
		super();
		_passengerAgent = null;
	}

	@Override
	public void action() {
		if (_passengerAgent == null){
			_passengerAgent = (PassengerAgent) myAgent;
		}
		
		/* If on destination (do nothing) */
		if (_passengerAgent.hasReachedDestination()) {
			_passengerAgent.replaceBehaviour(new TravelComplete());
			return;
		}

		/* If on stop, change behaviour to waiting */
		if (_passengerAgent.isOnStop()) {
			_passengerAgent.replaceBehaviour(new Waiting());
			return;
		}

		/* Looks like we are walking, check were we are going */

		/* Get nearest stop */
		Pair<Integer, Integer> nearestStop = _passengerAgent.getNearestStop();

		/* Check if we should walk or take a cab */
		if (_passengerAgent.getCostToPoint(nearestStop) > _passengerAgent.getCostToDestination()) {
			_passengerAgent.replaceBehaviour(new Walking(_passengerAgent.getDestination()));
		} else {
			_passengerAgent.replaceBehaviour(new Walking(nearestStop));
		}
	}
}
