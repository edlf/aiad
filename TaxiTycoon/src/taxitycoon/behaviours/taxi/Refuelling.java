package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class Refuelling extends Behaviour {
	private static final long serialVersionUID = 9159828344904437717L;
	private TaxiAgent _taxiAgent;
	
	public Refuelling() {
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
		
		_taxiAgent.increaseRefuelingTick();
		
		/* Check if we are on a refuelling station */
		if(_taxiAgent.isOnRefuelStation()){
			_taxiAgent.gasRefuel();
			_taxiAgent.replaceBehaviour(new Waiting());
			return;
		}

		/* Keep moving towards gas station */
	}

	@Override
	public boolean done() {
		return false;
	}

}
