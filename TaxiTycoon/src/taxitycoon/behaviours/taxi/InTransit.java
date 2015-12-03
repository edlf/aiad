package taxitycoon.behaviours.taxi;

import java.util.LinkedList;

import org.javatuples.Pair;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class InTransit extends Behaviour {
	private static final long serialVersionUID = 6454337518008277717L;
	private TaxiAgent _taxiAgent;
	
	private Pair<Integer, Integer> _destination;
	private LinkedList<Pair<Integer, Integer>> _pathToDestination;
	
	public InTransit(Pair<Integer, Integer> destination) {
		super();
		_taxiAgent = null;

		_destination = destination;		
	}
	
	@Override
	public void action() {
		if (_taxiAgent == null){
			_taxiAgent = (TaxiAgent) myAgent;
			_pathToDestination = _taxiAgent.getShortestPathTo(_destination);
		}
		
		/* No path obtained */
		if (_pathToDestination.isEmpty()){
			_taxiAgent.replaceBehaviour(new Waiting());
		}
		
		/* Check if we ran out of gas */
		if(_taxiAgent.getGasInTank() == 0){
			_taxiAgent.replaceBehaviour(new NoGas());
			return;
		}
		
		_taxiAgent.increaseInTransitTick();
		
		/* Move towards destination */
		

	}

	@Override
	public boolean done() {
		return false;
	}

}
