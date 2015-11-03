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
		// TODO Auto-generated method stub
		TaxiAgent myTaxiAgent = (TaxiAgent) myAgent;
		myTaxiAgent.relativeMove(new Pair<Double, Double>(0.1, 0.1));
	}

}
