package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.OneShotBehaviour;
import taxitycoon.agents.TaxiAgent;

public class NoGas extends OneShotBehaviour {
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
		
		/* Release passengers */
		
		System.out.println(_taxiAgent.getClass().getSimpleName() + " ran out of gas.");
		
		/* Remove agent from system */
		// _taxiAgent.printStats();
		_taxiAgent.doDelete();
	}
}
