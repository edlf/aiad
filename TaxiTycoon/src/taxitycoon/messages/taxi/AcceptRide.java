package taxitycoon.messages.taxi;

import jade.lang.acl.ACLMessage;

public class AcceptRide extends ACLMessage {
	private static final long serialVersionUID = -8561968757257431416L;

	public AcceptRide(jade.core.AID aid) {
		super(ACLMessage.ACCEPT_PROPOSAL);
		
		addReceiver(aid);
		setLanguage("English");
		setOntology("Taxitycoon-Passenger-ontology");
		//setContent(destinationPosition.toString());
	}

}
