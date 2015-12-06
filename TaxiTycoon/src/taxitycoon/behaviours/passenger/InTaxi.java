package taxitycoon.behaviours.passenger;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.agents.TaxiAgent;

/**
 * Passenger agent behaviour while in taxi
 * 
 * 
 */

public class InTaxi extends Behaviour {
	private static final long serialVersionUID = 5109545139429133391L;
	private PassengerAgent _passengerAgent;
	private boolean hasArrivedDestination = false;

	public InTaxi() {
		super();
		_passengerAgent = null;
	}
	
	@Override
	public void action() {
		if (_passengerAgent == null){
			_passengerAgent = (PassengerAgent) myAgent;
		}
		
		/* Should never happen (unless taxi is out of gas). In case it does go to the initial behaviour */
		if(!_passengerAgent.isOnTaxi()){
			_passengerAgent.replaceBehaviour(new StartBehaviour());
			return;
		}
		
		_passengerAgent.increaseInTaxiTick();
		
		/* Check if we are currently inside a taxi and not on the destination*/
		if (_passengerAgent.isOnTaxi() && !_passengerAgent.hasReachedDestination()){
			return;
		}
		
		/* Check if we have reached the destination */
		if (_passengerAgent.hasReachedDestination()){
			hasArrivedDestination = true;
			
			/* Inform taxi that we exited the taxi */ 
			
			/* Change behaviour to travel complete */
			_passengerAgent.replaceBehaviour(new TravelComplete());
			return;
		}

	}

	@Override
	public boolean done() {
		return hasArrivedDestination;
	}

}
