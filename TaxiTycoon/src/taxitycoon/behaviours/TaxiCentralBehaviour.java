package taxitycoon.behaviours;

import java.util.HashMap;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.taxicentral.RequestPreferentialStopReply;
import taxitycoon.messages.taxicentral.TaxiRequest;
import taxitycoon.staticobjects.TaxiStop;

public class TaxiCentralBehaviour extends Behaviour {
	private static final long serialVersionUID = 6376348164782963415L;

	@Override
	public void action() {
		TaxiCentral _taxiCentral = (TaxiCentral) myAgent;
		
		int currentMax = 0;
		TaxiStop highestPriorityTaxiStop = null;
		for (TaxiStop taxiStop : _taxiCentral.getTaxiStops()){
			if(taxiStop.getTaxiStopPriority() > currentMax){
				highestPriorityTaxiStop = taxiStop;
				currentMax = taxiStop.getTaxiStopPriority();
			}
		}
		
		/* Get message from queue */
		ACLMessage message = _taxiCentral.receive();
		while (message != null) {
			String title = message.getContent();
			jade.core.AID senderAID = message.getSender();
			// System.out.println("Taxicentral:" + title);
			
			switch (message.getPerformative()) {
			
			/* Taxi Request: Reply with highest priority taxi stop or reject the request if there is no preferential stop */
			case ACLMessage.REQUEST_WHENEVER:
				RequestPreferentialStopReply requestPreferentialStopReply;
				if (currentMax > 0 || highestPriorityTaxiStop != null){
					requestPreferentialStopReply = new RequestPreferentialStopReply(senderAID, highestPriorityTaxiStop.getPosition());	
				} else {
					requestPreferentialStopReply = new RequestPreferentialStopReply(senderAID);	
				}
				
				_taxiCentral.send(requestPreferentialStopReply);
				break;
				
			case ACLMessage.QUERY_IF:
				String[] pos = title.split(",");
				int x = Integer.parseInt(pos[0]);
				int y = Integer.parseInt(pos[1]);
				TaxiRequest taxiRequest = new TaxiRequest(null, new Pair<Integer, Integer> (x,y));
				_taxiCentral.send(taxiRequest);
				break;
			
				/* Passenger request */
			case ACLMessage.REQUEST:
				
				break;
				
			default:
					
				break;
			}
			
			message = _taxiCentral.receive();
		}

		
		//TaxiRequest taxiRequestMessage = new TaxiRequest();
		//taxiRequestMessage = null;
	}

	@Override
	public boolean done() {
		return false;
	}

}
