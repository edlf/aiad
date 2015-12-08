package taxitycoon.behaviours;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.staticobjects.TaxiStop;

public class TaxiCentralBehaviour extends Behaviour {
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
		ACLMessage message = taxiCentral.receive();
		if (message != null) {
			String title = message.getContent();
			jade.core.AID senderAID = message.getSender();
			System.out.println("MSG: Taxi got message:" + title);
			
			switch (message.getPerformative()) {
			/* TODO: check stops for passengers */
			case ACLMessage.REQUEST_WHENEVER:
				
				break;
				
			case ACLMessage.QUERY_IF:
				
				break;
			
			default:
					
				break;
			}
		}

		
		//TaxiRequest taxiRequestMessage = new TaxiRequest();
		//taxiRequestMessage = null;
	}

	@Override
	public boolean done() {
		return false;
	}

}
