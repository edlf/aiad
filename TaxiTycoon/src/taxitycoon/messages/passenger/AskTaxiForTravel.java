package taxitycoon.messages.passenger;

import jade.lang.acl.ACLMessage;
import sajas.core.AID;
import taxitycoon.agents.TaxiAgent;

public class AskTaxiForTravel extends ACLMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7947125494408586405L;

	public AskTaxiForTravel(TaxiAgent taxiAgent){
		super(ACLMessage.QUERY_IF);
		
		addReceiver(new AID(taxiAgent.getLocalName(), AID.ISLOCALNAME));
		setLanguage("English");
		setOntology("Taxitycoon-passenger-ontology");
		setContent("Taxi Request"); 
		
	}
	
}
