package taxitycoon.behaviours.taxi;

import org.javatuples.Pair;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class StartBehaviour extends Behaviour {
	private static final long serialVersionUID = 6972973475342866016L;
	private TaxiAgent _taxiAgent;
	private static boolean test = false;

	public StartBehaviour(){
		super();
		_taxiAgent = null;
		
	}
	
	@Override
	public void action() {
		if (_taxiAgent == null){
			_taxiAgent = (TaxiAgent) myAgent;
		}
		
		/* Check if we ran out of gas */
		if(_taxiAgent.getGasInTank() == 0){
			_taxiAgent.replaceBehaviour(new NoGas());
			return;
		}
		
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
