package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class InTransit extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6454337518008277717L;

	@Override
	public void action() {
		TaxiAgent taxiAgent = (TaxiAgent) myAgent;
		
		/* Check if we ran out of gas */
		if(taxiAgent.getGasInTank() == 0){
			taxiAgent.replaceBehaviour(new NoGas());
			return;
		}
		
		taxiAgent.increaseInTransitTick();

	}

	@Override
	public boolean done() {
		return false;
	}

}
