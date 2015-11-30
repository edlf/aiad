package taxitycoon.behaviours.passenger;

import org.javatuples.Pair;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;

public class Waiting extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5553375322108245921L;
	private boolean hasArrivedDestination = false;

	@Override
	public void action() {
		PassengerAgent myPassengerAgent = (PassengerAgent) myAgent;

		/* Check if we have reached the destination */
		if (myPassengerAgent.hasReachedDestination()){
			hasArrivedDestination = true;
			return;
		}
		
		// If on grass
		if (myPassengerAgent.isOnGrass()){
			myPassengerAgent.relativeMove(new Pair<Integer, Integer>(0,1));
		}
		
		/* Go to stop */
		
		
		// Try to enter taxi

	}

	@Override
	public boolean done() {
		return hasArrivedDestination;
	}

}
