package taxitycoon.behaviours.passenger;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.staticobjects.TaxiStop;
import taxitycoon.messages.passenger.*;

public class Waiting extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5553375322108245921L;
	private boolean hasArrivedDestination = false;

	@Override
	public void action() {
		PassengerAgent passengerAgent = (PassengerAgent) myAgent;
		
		/* Check if we have reached the destination */
		if (passengerAgent.hasReachedDestination()){
			hasArrivedDestination = true;
			
			/*  */ 
			return;
		}
		
		passengerAgent.increaseWaitingTick();
		
		/* Check if we have a taxi available */
		if(passengerAgent.isOnStop()){
			/* Get on what taxi stop were on and add ourself to the queue */
			for (TaxiStop taxiStop : passengerAgent.getTaxiStopsArray()){
				if (taxiStop.getPosition().equals(passengerAgent.getPosition())){
					
					/* Check if we have a taxi available and is our turn */
					if(taxiStop.hasTaxiAvailable() && taxiStop.isMyTurn(passengerAgent)){
						
						/* Send request to taxi at head of queue */
						AskTaxiForTravel askTaxiForTravelMessage = new AskTaxiForTravel(taxiStop.getTaxiAtHeadOfQueue());
						askTaxiForTravelMessage.sendMessage();	
					}
					
					break;
				}
			}
			
			// passengerAgent.getTaxiStopsArray()passengerAgent;
			passengerAgent.increaseWaitingTick();
			passengerAgent.replaceBehaviour(new Waiting());
			return;
		}
		
		/* Send a message to taxi central asking for taxis */
		
		
	}

	@Override
	public boolean done() {
		return hasArrivedDestination;
	}

}
