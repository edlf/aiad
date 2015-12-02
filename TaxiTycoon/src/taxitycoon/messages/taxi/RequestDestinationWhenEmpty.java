package taxitycoon.messages.taxi;

import jade.lang.acl.ACLMessage;

public class RequestDestinationWhenEmpty extends ACLMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7291068820748541905L;

	public RequestDestinationWhenEmpty(){
		super(ACLMessage.REQUEST);
	}
}
