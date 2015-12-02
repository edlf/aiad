package taxitycoon.agents;

import jade.domain.FIPAException;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import sajas.domain.DFService;
import taxitycoon.behaviours.taxicentral.*;

public class TaxiCentral extends Agent {
	private Behaviour _currentBehaviour =  null;

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
		replaceBehaviour(new MainBehaviour());
	}
	
	/* Behaviour replacement method */
	public void replaceBehaviour(Behaviour newBehaviour){
		if (_currentBehaviour != null) {
			removeBehaviour(_currentBehaviour);
		}
		
		_currentBehaviour = newBehaviour;
		addBehaviour(_currentBehaviour);
		System.out.println(getLocalName() + " behaviour change: " + newBehaviour.getClass().getSimpleName());
	}
}
