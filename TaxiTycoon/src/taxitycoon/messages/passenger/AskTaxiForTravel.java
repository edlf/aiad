package taxitycoon.messages.passenger;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;
import sajas.core.AID;
import taxitycoon.agents.TaxiAgent;

public class AskTaxiForTravel extends ACLMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7947125494408586405L;

	public AskTaxiForTravel(jade.core.AID aid, Pair<Integer, Integer> destinationPosition, TaxiAgent messageReceiver){
		super(ACLMessage.REQUEST);
		
		addReceiver(new AID(messageReceiver.getLocalName(), AID.ISLOCALNAME));
		setLanguage("English");
		setOntology("Taxitycoon-passenger-ontology");
		setContent(destinationPosition.toString());

		
	}
	
}
