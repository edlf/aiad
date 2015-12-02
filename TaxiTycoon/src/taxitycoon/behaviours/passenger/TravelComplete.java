package taxitycoon.behaviours.passenger;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;

/**
 * Travel Complete behaviour
 * 
 * Removes passenger agent from the container.
 */

public class TravelComplete extends Behaviour {
	private static final long serialVersionUID = 5553375322108245922L;

	@Override
	public void action() {
		PassengerAgent passengerAgent = (PassengerAgent) myAgent;
		
		if(!passengerAgent.hasReachedDestination()){
			System.out.println("BUG: Passenger not on destination but on TravelComplete behavior.");
		}
		
		passengerAgent.printStats();
		passengerAgent.doDelete();
	}

	@Override
	public boolean done() {
		return true;
	}
}
