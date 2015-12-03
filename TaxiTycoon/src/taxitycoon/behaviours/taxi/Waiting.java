package taxitycoon.behaviours.taxi;

import org.javatuples.*;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class Waiting extends Behaviour {
	private static final long serialVersionUID = 2163115385831517999L;
	private TaxiAgent _taxiAgent;
	private static boolean test = false;

	public Waiting() {
		super();
		_taxiAgent = null;
		
	}
	
	@Override
	public void action(){
		if (_taxiAgent == null){
			_taxiAgent = (TaxiAgent) myAgent;
		}
		
		/* Check if we ran out of gas */
		if(_taxiAgent.getGasInTank() == 0){
			_taxiAgent.replaceBehaviour(new NoGas());
			return;
		}
		
		_taxiAgent.increaseWaitingTick();
		
		/* Check if we are on reserve */
		if(_taxiAgent.isGasOnReserve()){
			_taxiAgent.replaceBehaviour(new Refuelling());
			return;
		}
		
		if (!test){
			test = true;
			_taxiAgent.replaceBehaviour(new InTransit(new Pair<Integer, Integer>(28, 28)));
		}
		
	}

	@Override
	public boolean done() {
		return false;
	}

}
