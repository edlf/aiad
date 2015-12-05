package taxitycoon.behaviours.passenger;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.passenger.AskTaxiForTravel;
import taxitycoon.staticobjects.TaxiStop;

/**
 * Waiting behaviour
 * 
 * 
 */

public class Waiting extends Behaviour {
	private static final long serialVersionUID = 5553375322108245921L;
	private PassengerAgent _passengerAgent;

	public Waiting() {
		super();
		_passengerAgent = null;
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

			/*
			 * Get in what taxi stop were on and add ourself to the passenger
			 * queue
			 */
			TaxiStop taxiStop = TaxiCentral.getTaxiStopAt(_passengerAgent.getPosition());

			/* Check if are already in the queue */
			if(taxiStop.isPassengerInQueue(_passengerAgent)){
				/* Check if we have a taxi available and is our turn */
				if (taxiStop.hasTaxiAvailable() && taxiStop.isMyTurn(_passengerAgent)) {

					/* Send request to taxi at head of queue */
					AskTaxiForTravel askTaxiForTravelMessage = new AskTaxiForTravel(taxiStop.getTaxiAtHeadOfQueue());
					askTaxiForTravelMessage.sendMessage();

					return;
				}

				if (taxiStop.hasTaxiAvailable()) {

					return;
				} else {
					// System.out.println(_passengerAgent.toString());

					return;
				}
				
			} else {
				/* Add to queue */
				taxiStop.addPassengerToQueue(_passengerAgent);
			}		

		} else {
			System.out.println("BUG: PassengerAgent with waiting behaviour and not on a stop or destination");
		}

		/* Send a message to taxi central asking for taxis */

		/* Guess we are still waiting */
		_passengerAgent.increaseWaitingTick();
	}

	@Override
	public boolean done() {
		return false;
	}
}
