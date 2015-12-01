package taxitycoon.behaviours.passenger;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;

public class InTaxi extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5109545139429133391L;
	private boolean hasArrivedDestination = false;

	@Override
	public void action() {
		PassengerAgent passengerAgent = (PassengerAgent) myAgent;
		
		/* Should never happen. In case it does go to the initial behaviour */
		if(!passengerAgent.isOnTaxi()){
			passengerAgent.replaceBehaviour(new taxitycoon.behaviours.passenger.StartBehaviour());
			return;
		}
		
		passengerAgent.increaseInTaxiTick();
		
		/* Check if we are currently inside a taxi and not on the destination*/
		if (passengerAgent.isOnTaxi() && !passengerAgent.hasReachedDestination()){
			return;
		}
		
		/* Check if we have reached the destination */
		if (passengerAgent.hasReachedDestination()){
			hasArrivedDestination = true;
			
			/* Inform taxi that we exited the taxi */ 
			
			/* Change behaviour to travel complete */
			passengerAgent.replaceBehaviour(new taxitycoon.behaviours.passenger.TravelComplete());
			return;
		}

	}

	@Override
	public boolean done() {
		return hasArrivedDestination;
	}

}
