package taxitycoon.behaviours.passenger;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;

public class TravelComplete extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5553375322108245922L;

	@Override
	public void action() {
		((PassengerAgent) myAgent).doDelete();
	}

	@Override
	public boolean done() {
		return true;
	}

}
