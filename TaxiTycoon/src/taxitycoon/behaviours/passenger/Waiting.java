package taxitycoon.behaviours.passenger;

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
		PassengerAgent passengerAgent = (PassengerAgent) myAgent;
		
		/* Check if we have reached the destination */
		if (passengerAgent.hasReachedDestination()){
			hasArrivedDestination = true;
			
			/*  */ 
			return;
		}
		
		/* Check if we have a taxi available */

		
		/* Send a message to taxi central asking for taxis */
		
		
	}

	@Override
	public boolean done() {
		return hasArrivedDestination;
	}

}
