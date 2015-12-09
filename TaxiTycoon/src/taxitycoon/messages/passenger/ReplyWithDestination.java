package taxitycoon.messages.passenger;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;

public class ReplyWithDestination extends ACLMessage {
	private static final long serialVersionUID = 2619257808985890760L;

	public ReplyWithDestination(jade.core.AID aid, Pair<Integer,Integer> destinationPosition) {
		super(ACLMessage.CONFIRM);
		addReceiver(aid);
		setContent(destinationPosition.getValue0()+","+destinationPosition.getValue1());
	}
}
