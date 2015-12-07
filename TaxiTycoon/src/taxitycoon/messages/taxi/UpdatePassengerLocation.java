package taxitycoon.messages.taxi;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;

public class UpdatePassengerLocation extends ACLMessage {
	private static final long serialVersionUID = 1134711802065231981L;

	public UpdatePassengerLocation(jade.core.AID aid, Pair<Integer,Integer> newPos) {
		super(ACLMessage.INFORM);
		addReceiver(aid);
		setContent(newPos.getValue0()+","+newPos.getValue1());
	}

}
