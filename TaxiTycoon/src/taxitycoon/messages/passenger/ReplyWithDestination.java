package taxitycoon.messages.passenger;

import jade.lang.acl.ACLMessage;

public class ReplyWithDestination extends ACLMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2619257808985890760L;

	public ReplyWithDestination() {
		super(ACLMessage.CONFIRM);
	}

}
