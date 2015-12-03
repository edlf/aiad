package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class PickingPassengers extends Behaviour {
	private static final long serialVersionUID = 2595075077727643900L;
	private TaxiAgent _taxiAgent;
	
	public PickingPassengers() {
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
		
		_taxiAgent.increasePickingPassengerTick();

	}

	@Override
	public boolean done() {
		return false;
	}

}
