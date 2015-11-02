package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class Waiting extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2163115385831517999L;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		TaxiAgent myTaxiAgent = (TaxiAgent) myAgent;
		myTaxiAgent.move(10, 10);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
