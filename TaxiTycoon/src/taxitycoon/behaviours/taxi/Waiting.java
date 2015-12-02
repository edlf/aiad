package taxitycoon.behaviours.taxi;

import org.javatuples.*;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class Waiting extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2163115385831517999L;

	@Override
	public void action() {
		TaxiAgent taxiAgent = (TaxiAgent) myAgent;
		
		/* Check if we ran out of gas */
		if(taxiAgent.getGasInTank() == 0){
			taxiAgent.replaceBehaviour(new NoGas());
			return;
		}
		
		taxiAgent.increaseWaitingTick();
		
		/* Check if we are on reserve */
		if(taxiAgent.isGasOnReserve()){
			taxiAgent.replaceBehaviour(new Refueling());
			return;
		}
		
		taxiAgent.relativeMove(new Pair<Integer, Integer>(0,1));

	}

	@Override
	public boolean done() {
		return false;
	}

}
