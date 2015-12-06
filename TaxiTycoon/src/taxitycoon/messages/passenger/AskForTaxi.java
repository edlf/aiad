package taxitycoon.messages.passenger;

import jade.lang.acl.ACLMessage;
import sajas.core.AID;
import taxitycoon.agents.TaxiCentral;

public class AskForTaxi extends ACLMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7043106136193323422L;

	public AskForTaxi(){
		super(ACLMessage.REQUEST);
		
		addReceiver(new AID(TaxiCentral.class.getSimpleName(), AID.ISLOCALNAME));
		setLanguage("English");
		setOntology("Taxitycoon-Passenger-AskForTaxi");
		setContent("Taxi Request"); 
	}
	
	public void sendMessage(){
		// sendMessage();
	}
}
