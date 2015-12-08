package taxitycoon.messages.passenger;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;
import sajas.core.AID;
import taxitycoon.agents.TaxiCentral;

public class AskForTaxi extends ACLMessage {
	private static final long serialVersionUID = -7043106136193323422L;

	public AskForTaxi(Pair<Integer,Integer> passengerPos){
		super(ACLMessage.REQUEST);
		
		addReceiver(new AID(TaxiCentral.class.getSimpleName(), AID.ISLOCALNAME));
		setContent(passengerPos.getValue0()+","+passengerPos.getValue1()); 
	}
	
	public void sendMessage(){
		// sendMessage();
	}
}
