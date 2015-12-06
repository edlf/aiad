package taxitycoon.behaviours.taxi;

import java.util.LinkedList;

import org.javatuples.Pair;
import sajas.core.behaviours.CyclicBehaviour;
import taxitycoon.agents.TaxiAgent;

public class Refuelling extends CyclicBehaviour {
	private static final long serialVersionUID = 9159828344904437717L;
	private TaxiAgent _taxiAgent;
	private LinkedList<Pair<Integer, Integer>> _pathToDestination;
	private boolean _inTravel = false;
	
	public Refuelling() {
		super();
		_taxiAgent = null;
		
	}
	
	@Override
	public void action() {
		if (_taxiAgent == null){
			_taxiAgent = (TaxiAgent) myAgent;
			_pathToDestination = _taxiAgent.getShortestPathTo(_taxiAgent.getNearestRefuelStation().getPosition());
		}
		
		/* Check if are on a refuelling station */
		if(_taxiAgent.isOnRefuelStation()){
			_taxiAgent.gasRefuel();
			_taxiAgent.replaceBehaviour(new Waiting());
			return;		
		}
		
		/* Check if we ran out of gas */
		if(_taxiAgent.getGasInTank() == 0){
			_taxiAgent.replaceBehaviour(new NoGas());
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
			
		_taxiAgent.increaseRefuelingTick();

		/* Move towards destination */
		_taxiAgent.move(_pathToDestination.getFirst());
		_pathToDestination.removeFirst();
	}
}
