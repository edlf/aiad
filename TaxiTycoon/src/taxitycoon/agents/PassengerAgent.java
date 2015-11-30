package taxitycoon.agents;

import java.util.ArrayList;

import org.javatuples.Pair;

/**
 * Passenger agent
 **/
public class PassengerAgent extends SimAgent {
	/* Common variables to all passenger agents */
	private static boolean _passengerMapCalculated = false;
	private static char[][] _map = null; /* col x line */
	
	/* Individual variables */
	private Pair<Integer, Integer> _destination;
	
	/* Static methods */
	public static void createAgentMap(ArrayList<Pair<Integer, Integer>> roads, ArrayList<Pair<Integer, Integer>> stops){
		if (_passengerMapCalculated) {
			System.out.println("BUG: Passenger map already calculated.");
			return;
		}

		/* Create map and fill */
		_map = new char[_mapSize.getValue0()][_mapSize.getValue1()];
		for (int i = 0; i < _mapSize.getValue0(); i++){
			for (int j = 0; j < _mapSize.getValue1(); j++){
				_map[i][j] = _mapEmpty;
			}
		}
		
		/* Fill roads */
		for (Pair<Integer, Integer> road : roads){
			_map[road.getValue0()][road.getValue1()] = _mapRoad;
		}
		
		/* Fill stops */
		for(Pair<Integer, Integer> stop : stops){
			_map[stop.getValue0()][stop.getValue1()] = _mapStop;
		}
		
		_passengerMapCalculated = true;
	}
	
	/* Non static methods */
	public PassengerAgent(Pair<Integer, Integer> initialPos) {
		this._startPosition = initialPos;
		this._currentPosition = initialPos;
		
		this._currentBehaviour = new taxitycoon.behaviours.passenger.Waiting();
	}

	@Override
	protected void _addInitialBehaviour() {
		replaceBehaviour(_currentBehaviour);
	}
	
	void changeToWalkingToNearestStopBehaviour(){
		replaceBehaviour(new taxitycoon.behaviours.passenger.WalkingToNearestStop());
	}
	
	public void setDestination(Pair<Integer, Integer> destination){
		_destination = destination;
	}
	
	public Pair<Integer, Integer> getDestination(){
		return _destination;
	}
	
	public boolean isOnGrass(){
		
		return false;
	}

}
