package taxitycoon.behaviours.taxi;

import java.util.LinkedList;

import org.javatuples.Pair;
import org.stringtemplate.v4.compiler.CodeGenerator.includeExpr_return;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.TaxiAgent;

public class InTransit extends Behaviour {
	private static final long serialVersionUID = 6454337518008277717L;
	private TaxiAgent _taxiAgent;
	
	private Pair<Integer, Integer> _destination;
	private LinkedList<Pair<Integer, Integer>> _pathToDestination;
	private boolean _inTravel = false;
	
	public InTransit(Pair<Integer, Integer> destination) {
		super();
		_taxiAgent = null;
		_inTravel = false;

		_destination = destination;		
	}
	
	@Override
	public void action() {
		if (_taxiAgent == null){
			_taxiAgent = (TaxiAgent) myAgent;
			_pathToDestination = _taxiAgent.getShortestPathTo(_destination);
		}
		
		/* Check if travel has ended */
		if(_taxiAgent.getPosition().equals(_destination)){
			_taxiAgent.replaceBehaviour(new Waiting());
			return;		
		}
		
		/* No path obtained */
		if (_pathToDestination.isEmpty() && !_inTravel){
			System.out.println("Taxi cannot find a path to destination!");
			_taxiAgent.replaceBehaviour(new Waiting());
			return;
		} else {
			_inTravel = true;
		}
		
		/* Check if we ran out of gas */
		if(_taxiAgent.getGasInTank() == 0){
			_taxiAgent.replaceBehaviour(new NoGas());
			return;
		}
		
		_taxiAgent.increaseInTransitTick();
		
		/* Move towards destination */
		_taxiAgent.move(_pathToDestination.getFirst());
		_pathToDestination.removeFirst();

	}

	@Override
	public boolean done() {
		return false;
	}

}
