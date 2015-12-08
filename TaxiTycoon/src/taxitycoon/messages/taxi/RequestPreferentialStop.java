package taxitycoon.messages.taxi;

import jade.lang.acl.ACLMessage;

public class RequestPreferentialStop extends ACLMessage  {
	private static final long serialVersionUID = -7867599059107269271L;

	public RequestPreferentialStop() {
		super(ACLMessage.REQUEST_WHENEVER);
		
	}

}
