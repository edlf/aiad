package taxitycoon.messages.taxicentral;

import org.javatuples.Pair;

import jade.lang.acl.ACLMessage;

public class TaxiRequest extends ACLMessage {
	private static final long serialVersionUID = -964091040179615733L;

	public TaxiRequest(jade.core.AID aidDest, Pair<Integer,Integer> passengerPos){
		super(ACLMessage.INFORM);
		addReceiver(aidDest);
		setContent(passengerPos.getValue0()+","+passengerPos.getValue1());
	}
}
