package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class Refueling extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9159828344904437717L;

	@Override
	public void action() {
		TaxiAgent taxiAgent = (TaxiAgent) myAgent;
		
		/* Check if we ran out of gas */
		if(taxiAgent.getGasInTank() == 0){
			taxiAgent.replaceBehaviour(new taxitycoon.behaviours.taxi.NoGas());
			return;
		}
		
		/* Check if we are on a refueling station */
		if(taxiAgent.isOnRefuelStation()){
			taxiAgent.gasRefuel();
			taxiAgent.replaceBehaviour(new taxitycoon.behaviours.taxi.Waiting());
			return;
		}

		/* Keep moving towards gas station */
	}

	@Override
	public boolean done() {
		return false;
	}

}
