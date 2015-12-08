package taxitycoon.messages.taxi;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class RequestPreferentialStop extends ACLMessage  {
	private static final long serialVersionUID = -7867599059107269271L;

	public RequestPreferentialStop(AID destination) {
		super(ACLMessage.REQUEST_WHENEVER);
		addReceiver(destination);
	}

}
