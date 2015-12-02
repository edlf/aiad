package taxitycoon.messages.taxicentral;

import jade.lang.acl.ACLMessage;

public class EmptyTaxiDestinationReply extends ACLMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3869574976910017369L;

	public EmptyTaxiDestinationReply() {
		super(ACLMessage.AGREE);

	}

}
