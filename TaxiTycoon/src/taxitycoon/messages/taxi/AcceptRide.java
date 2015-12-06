package taxitycoon.messages.taxi;

import jade.lang.acl.ACLMessage;
import sajas.core.AID;
import taxitycoon.agents.PassengerAgent;

public class AcceptRide extends ACLMessage {
	private static final long serialVersionUID = -8561968757257431416L;

	public AcceptRide(PassengerAgent passengerAgent) {
		super(ACLMessage.ACCEPT_PROPOSAL);
		
		addReceiver(new AID(passengerAgent.getLocalName(), AID.ISLOCALNAME));
		setLanguage("English");
		setOntology("Taxitycoon-Passenger-ontology");
		//setContent(destinationPosition.toString());
	}

}
