package taxitycoon.agents;

import java.util.ArrayList;

import org.javatuples.Pair;

/**
 * Taxi agent
 **/
public class TaxiAgent extends SimAgent {
	/* Common variables to all taxi agents */
	static private boolean _taxiMapCalculated = false;
	static private char[][] _map = null; /* col x line */
	
	/* Individual variables */
	private int _maximumCapacity = 4;
	private int _numberOfPassengers = 0;

	/* Static methods */
	public static void createAgentMap(ArrayList<Pair<Integer, Integer>> roads, ArrayList<Pair<Integer, Integer>> stops, ArrayList<Pair<Integer, Integer>> refuelStations){
		if (_taxiMapCalculated) {
			System.out.println("BUG: Taxi map already calculated.");
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
		
		/* Fill refuel stations */
		for(Pair<Integer, Integer> refuelStation : refuelStations){
			_map[refuelStation.getValue0()][refuelStation.getValue1()] = _mapRefuel;
		}
		
		_taxiMapCalculated = true;
	}
	
	/* Non static methods */
	public TaxiAgent(Pair<Integer, Integer> initialPos) {
		this._startPosition = initialPos;
		this._currentPosition = initialPos;
		
		this._currentBehaviour = new taxitycoon.behaviours.taxi.Waiting();
	}
	
	public boolean relativeMove(Pair<Integer,Integer> delta){
		Pair<Integer, Integer> newPosition = new Pair<Integer, Integer>(_currentPosition.getValue0() + delta.getValue0(), _currentPosition.getValue1() + delta.getValue1());
		
		/* Check if its out of fonds */
		if (newPosition.getValue0() < 0 || newPosition.getValue1() < 0 || newPosition.getValue0() >= _mapSize.getValue0() || newPosition.getValue1() >= _mapSize.getValue1()){
			return false;
		}
		
		/* Check if it is road */
		if (_map[newPosition.getValue0()][newPosition.getValue1()] == _mapRoad){
			return _move(new Pair<Integer, Integer>(newPosition.getValue0(), newPosition.getValue1()));
		}
		
		/* Nope */
		return false;
	}
	
	public void move(Pair<Integer, Integer> newPosition){
		
	}
	
	@Override
	protected void _addInitialBehaviour(){
		replaceBehaviour(_currentBehaviour);
	}
	
	public boolean isTaxiFull(){
		return (_numberOfPassengers == _maximumCapacity);
	}

}
