package taxitycoon.messages.passenger;

import jade.lang.acl.ACLMessage;

public class AskTaxiForTravel extends ACLMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7947125494408586405L;

	public AskTaxiForTravel(){
		super(ACLMessage.QUERY_IF);
		
	}
}
