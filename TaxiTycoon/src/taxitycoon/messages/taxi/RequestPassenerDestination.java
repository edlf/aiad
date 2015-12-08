package taxitycoon.messages.taxi;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class RequestPassenerDestination extends ACLMessage {
	private static final long serialVersionUID = -4480571352898732608L;

	public RequestPassenerDestination(AID destination){
		super(ACLMessage.REQUEST);
		setContent("GET_DESTINATION");
		addReceiver(destination);
		
	}
}
