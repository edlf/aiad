package taxitycoon.behaviours.taxi;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.CyclicBehaviour;
import taxitycoon.agents.TaxiAgent;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.messages.passenger.AskTaxiForTravel;
import taxitycoon.messages.taxi.AcceptRide;
import taxitycoon.staticobjects.TaxiStop;

public class Waiting extends CyclicBehaviour {
	private static final long serialVersionUID = 2163115385831517999L;
	private TaxiAgent _taxiAgent;
	private TaxiStop _taxiStop;

	public Waiting() {
		super();
		_taxiAgent = null;
		_taxiStop = null;
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
			if (_taxiStop == null){
				_taxiStop = TaxiCentral.getTaxiStopAt(_taxiAgent.getPosition());
			}

			if(_taxiStop.isTaxiInQueue(_taxiAgent)){
				onStopLogic();
			} else {
				_taxiStop.addTaxiToQueue(_taxiAgent);
			}		

		} else {
			System.out.println("BUG: TaxiAgent with waiting behaviour and not on a stop");
		}
		
	}
	
	private void onStopLogic(){
		/* Check for messages */
		ACLMessage msg = _taxiAgent.receive();
		if (msg != null) {
			String title = msg.getContent();
			msg.getOntology();
			
			System.out.println("MSG: Taxi got message:" +  title);
			
			AcceptRide acceptRideMessage = new AcceptRide(_taxiStop.getPassengerAtHeadOfQueue());
			_taxiAgent.send(acceptRideMessage);
		}
	}
}
