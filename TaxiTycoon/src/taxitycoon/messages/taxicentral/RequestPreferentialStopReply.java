package taxitycoon.messages.taxicentral;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;

public class RequestPreferentialStopReply extends ACLMessage {
	private static final long serialVersionUID = -3869574976910017369L;

	public RequestPreferentialStopReply(jade.core.AID receiver, Pair<Integer, Integer> destination) {
		super(ACLMessage.CONFIRM);
		addReceiver(receiver);
		setContent(destination.getValue0()+","+destination.getValue1());
		
	}
	
	public RequestPreferentialStopReply(jade.core.AID receiver) {
		super(ACLMessage.DISCONFIRM);
		addReceiver(receiver);
	}

}
