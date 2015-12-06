package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class StartBehaviour extends Behaviour {
	private static final long serialVersionUID = 6972973475342866016L;
	private TaxiAgent _taxiAgent;

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
		
		/* Check if taxi central wants us to go to a specific stop */
		
		
		/* If not go to the nearest stop */
		_taxiAgent.replaceBehaviour(new InTransit(_taxiAgent.getNearestTaxiStop().getPosition()));
	}

	@Override
	public boolean done() {
		return false;
	}

}
