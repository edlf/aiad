package taxitycoon.behaviours.passenger;

import sajas.core.behaviours.OneShotBehaviour;
import taxitycoon.agents.PassengerAgent;

/**
 * Travel Complete behaviour
 * 
 * Removes passenger agent from the container.
 */

public class TravelComplete extends OneShotBehaviour {
	private static final long serialVersionUID = 5553375322108245922L;
	private PassengerAgent _passengerAgent;

	public TravelComplete() {
		super();
		_passengerAgent = null;
	}
	
	@Override
	public void action() {
		if (_passengerAgent == null){
			_passengerAgent = (PassengerAgent) myAgent;
		}
		
		if(!_passengerAgent.hasReachedDestination()){
			System.out.println("BUG: Passenger not on destination but on TravelComplete behavior.");
		}
		
		_passengerAgent.printStats();
		_passengerAgent.doDelete();
	}
}
