package taxitycoon.messages.taxicentral;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;

public class RequestPreferentialStopReply extends ACLMessage {
	private static final long serialVersionUID = -3869574976910017369L;

	public RequestPreferentialStopReply(Pair<Integer, Integer> destination) {
		super(ACLMessage.CONFIRM);

		
	}
	
	public RequestPreferentialStopReply() {
		super(ACLMessage.DISCONFIRM);
	}

}
