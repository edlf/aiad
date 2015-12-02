package taxitycoon.messages.taxi;

import jade.lang.acl.ACLMessage;

public class RequestDestination extends ACLMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4480571352898732608L;

	public RequestDestination(){
		super(ACLMessage.REQUEST);
	}
}
