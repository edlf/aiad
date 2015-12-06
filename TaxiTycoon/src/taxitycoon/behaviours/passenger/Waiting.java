package taxitycoon.behaviours.passenger;

import sajas.core.behaviours.CyclicBehaviour;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.passenger.AskTaxiForTravel;
import taxitycoon.staticobjects.TaxiStop;

/**
 * Waiting behaviour
 * 
 * 
 */

public class Waiting extends CyclicBehaviour {
	private static final long serialVersionUID = 5553375322108245921L;
	private PassengerAgent _passengerAgent;
	private TaxiStop _taxiStop;
	private boolean _waitingForReply = false;

	public Waiting() {
		super();
		_passengerAgent = null;
		_taxiStop = null;
	}

	@Override
	public void action() {
		if (_passengerAgent == null) {
			_passengerAgent = (PassengerAgent) myAgent;
		}

		/* Should not happen, check if we have reached the destination */
		if (_passengerAgent.hasReachedDestination()) {
			System.out.println("BUG: PassengerAgent reached destination while on waiting behaviour");
			_passengerAgent.replaceBehaviour(new TravelComplete());
			return;
		}

		/* Check if we are in a stop */
		if (_passengerAgent.isOnStop()) {
			onStopLogic();
		} else {
			System.out.println("BUG: PassengerAgent with waiting behaviour and not on a stop or destination");
		}

		/* Guess we are still waiting */
		_passengerAgent.increaseWaitingTick();
	}
	
	private void onStopLogic(){
		/*
		 * Get in what taxi stop were on and add ourself to the passenger
		 * queue
		 */
		if (_taxiStop == null){
			_taxiStop = TaxiCentral.getTaxiStopAt(_passengerAgent.getPosition());
		} 

		/* Check if are already in the queue */
		if(!_taxiStop.isPassengerInQueue(_passengerAgent)){
			_taxiStop.addPassengerToQueue(_passengerAgent);
		}		

		/* Check if we have a taxi available and is our turn */
		if (_taxiStop.hasTaxiAvailable() && _taxiStop.isMyTurn(_passengerAgent)) {

			if(_waitingForReply) {

			} else {
				/* Send request to taxi at head of queue */
				System.out.println("Passenger sent message");
				AskTaxiForTravel askTaxiForTravelMessage = new AskTaxiForTravel(_taxiStop.getTaxiAtHeadOfQueue());
				askTaxiForTravelMessage.sendMessage();
				_waitingForReply = true;
			}


			return;
		}

		if (_taxiStop.hasTaxiAvailable()) {

			return;
		} else {
			/* Send a message to taxi central asking for taxis */
			// System.out.println(_passengerAgent.toString());

			return;
		}
		
		/* Read messages since another taxi might go to the same destination */
	}
}
