package taxitycoon.agents;

import jade.domain.FIPAException;
import sajas.core.Agent;
import sajas.domain.DFService;

public class TaxiCentral extends Agent {

	/* Setup and takedown methods */
	@Override
	protected void setup(){
		System.out.println(getLocalName() + " setup()");
		
		_addBehaviour();
	}
	
	@Override
	protected void takeDown(){
		System.out.println(getLocalName() + " takeDown()");
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			System.out.println(getLocalName() + " error in takeDown()");
		}
	}
	
	private void _addBehaviour(){
		
	}
}
