package taxitycoon.behaviours.taxi;

import java.util.ArrayList;

import org.javatuples.Pair;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class InTransit extends Behaviour {
	private static final long serialVersionUID = 6454337518008277717L;

	private Pair<Integer, Integer> _destination;
	private ArrayList<Pair<Integer, Integer>> _pathToDestination;
	
	public InTransit(Pair<Integer, Integer> destination) {
		_destination = destination;
		_pathToDestination = null;
	}
	
	@Override
	public void action() {
		TaxiAgent taxiAgent = (TaxiAgent) myAgent;
		
		if (_pathToDestination == null){
			_pathToDestination = taxiAgent.getShortestPathTo(_destination);
		}
		
		/* Check if we ran out of gas */
		if(taxiAgent.getGasInTank() == 0){
			taxiAgent.replaceBehaviour(new NoGas());
			return;
		}
		
		taxiAgent.increaseInTransitTick();
		
		/* Move towards destination */
		

	}

	@Override
	public boolean done() {
		return false;
	}

}
