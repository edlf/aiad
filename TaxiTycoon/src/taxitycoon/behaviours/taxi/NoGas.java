package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class NoGas extends Behaviour {
	private static final long serialVersionUID = 149533442279510777L;
	private TaxiAgent _taxiAgent;
	
	public NoGas(){
		super();
		_taxiAgent = null;
		
	}

	@Override
	public void action() {
		if (_taxiAgent == null){
			_taxiAgent = (TaxiAgent) myAgent;
		}
		
		System.out.println(_taxiAgent.getClass().getSimpleName() + " ran out of gas.");
	}

	@Override
	public boolean done() {
		return false;
	}

}
