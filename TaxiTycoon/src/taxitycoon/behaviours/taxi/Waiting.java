package taxitycoon.behaviours.taxi;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.passenger.AskTaxiForTravel;
import taxitycoon.staticobjects.TaxiStop;

public class Waiting extends Behaviour {
	private static final long serialVersionUID = 2163115385831517999L;
	private TaxiAgent _taxiAgent;

	public Waiting() {
		super();
		_taxiAgent = null;
		
	}
	
	@Override
	public void action(){
		if (_taxiAgent == null){
			_taxiAgent = (TaxiAgent) myAgent;
		}
		
		/* Check if we ran out of gas */
		if(_taxiAgent.getGasInTank() == 0){
			_taxiAgent.replaceBehaviour(new NoGas());
			return;
		}
		
		_taxiAgent.increaseWaitingTick();
		
		/* Check if we are on reserve */
		if(_taxiAgent.isGasOnReserve()){
			_taxiAgent.replaceBehaviour(new Refuelling());
			return;
		}
		
		/* Check if we are in a stop */
		if (_taxiAgent.isOnStop()) {

			/*
			 * Get in what taxi stop were on and add ourself to the taxi
			 * queue
			 */
			TaxiStop taxiStop = TaxiCentral.getTaxiStopAt(_taxiAgent.getPosition());

			/* Check if are already in the queue */
			if(taxiStop.isTaxiInQueue(_taxiAgent)){

				
			} else {
				/* Add to queue */
				taxiStop.addTaxiToQueue(_taxiAgent);
			}		

		} else {
			System.out.println("BUG: TaxiAgent with waiting behaviour and not on a stop");
		}
		
	}

	@Override
	public boolean done() {
		return false;
	}

}
