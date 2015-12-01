package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class NoGas extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 149533442279510777L;

	@Override
	public void action() {
		TaxiAgent taxiAgent = (TaxiAgent) myAgent;

		System.out.println(taxiAgent.getClass().getSimpleName() + " ran out of gas.");
	}

	@Override
	public boolean done() {
		return false;
	}

}
