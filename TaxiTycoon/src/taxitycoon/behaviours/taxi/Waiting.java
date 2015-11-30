package taxitycoon.behaviours.taxi;

import org.javatuples.*;

import sajas.core.behaviours.CyclicBehaviour;
import taxitycoon.agents.TaxiAgent;

public class Waiting extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2163115385831517999L;

	@Override
	public void action() {
		TaxiAgent myTaxiAgent = (TaxiAgent) myAgent;
		myTaxiAgent.relativeMove(new Pair<Integer, Integer>(1, 1));
	}

}
