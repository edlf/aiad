package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class PickingPassengers extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2595075077727643900L;

	@Override
	public void action() {
		TaxiAgent taxiAgent = (TaxiAgent) myAgent;
		
		/* Check if we ran out of gas */
		if(taxiAgent.getGasInTank() == 0){
			taxiAgent.replaceBehaviour(new taxitycoon.behaviours.taxi.NoGas());
			return;
		}

	}

	@Override
	public boolean done() {
		return false;
	}

}
