package taxitycoon.behaviours.taxicentral;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiCentral;

public class MainBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6376348164782963415L;

	@Override
	public void action() {
		TaxiCentral taxiCentral = (TaxiCentral) myAgent;
		
		/* Get message from queue */
		ACLMessage msg = taxiCentral.receive();
		if (msg != null) {
			// Process the message
		}

	}

	@Override
	public boolean done() {
		return false;
	}

}
