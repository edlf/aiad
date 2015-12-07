package taxitycoon.messages.taxi;

import java.io.IOException;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;

public class UpdatePassengerLocation extends ACLMessage {
	private static final long serialVersionUID = 1134711802065231981L;

	public UpdatePassengerLocation(jade.core.AID aid, Pair<Integer,Integer> newPos) {
		super(ACLMessage.INFORM);
		addReceiver(aid);
		try {
			setContentObject(newPos);
		} catch (IOException e) {
			System.out.println("Error while serializing.");
			e.printStackTrace();
		}
	}

}
