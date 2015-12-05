package taxitycoon.behaviours.taxicentral;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.taxicentral.TaxiRequest;
import taxitycoon.staticobjects.TaxiStop;

public class MainBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6376348164782963415L;

	@Override
	public void action() {
		TaxiCentral taxiCentral = (TaxiCentral) myAgent;
		
		for (TaxiStop taxiStop : taxiCentral.getTaxiStops()){
			if(taxiStop.getPassengerAtHeadOfQueue() != null){
				//System.out.println(taxiStop.getPassengerAtHeadOfQueue().toString());
			}
			
		}
		
		/* Get message from queue */
		ACLMessage msg = taxiCentral.receive();
		if (msg != null) {
			// Process the message
		}

		
		TaxiRequest taxiRequestMessage = new TaxiRequest();
		taxiRequestMessage = null;
	}

	@Override
	public boolean done() {
		return false;
	}

}
