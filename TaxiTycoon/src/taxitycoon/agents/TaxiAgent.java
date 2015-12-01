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
	static final private int _gasMax = 600;
	
	/* Individual variables */
	private int _maximumCapacity = 4;
	private int _numberOfPassengers = 0;
	
	private int _gas = _gasMax;
	private int _totalGasSpent = 0;
	private int _numberOfRefuels = 0;
	private int _totalGasRefuelAmount = 0;

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
				_map[i][j] = _mapGrass;
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
		
		/* Check if its out of bonds */
		if (newPosition.getValue0() < 0 || newPosition.getValue1() < 0 || newPosition.getValue0() >= _mapSize.getValue0() || newPosition.getValue1() >= _mapSize.getValue1()){
			return false;
		}
		
		/* Check if we have gas */
		if (_gas <= 0) {
			return false;
		}
		
		/* Check if it is road */
		if (_map[newPosition.getValue0()][newPosition.getValue1()] == _mapRoad){
			gasReduce();
			return _move(new Pair<Integer, Integer>(newPosition.getValue0(), newPosition.getValue1()));
		}
		
		/* Check if it is stop */
		if (_map[newPosition.getValue0()][newPosition.getValue1()] == _mapStop){
			gasReduce();
			return _move(new Pair<Integer, Integer>(newPosition.getValue0(), newPosition.getValue1()));
		}
		
		/* Check if it is refuel station */
		if (_map[newPosition.getValue0()][newPosition.getValue1()] == _mapRefuel){
			gasRefuel();
			return _move(new Pair<Integer, Integer>(newPosition.getValue0(), newPosition.getValue1()));
		}
		
		/* Nope */
		return false;
	}
	
	@Override
	protected void _addInitialBehaviour(){
		replaceBehaviour(_currentBehaviour);
	}
	
	/* Taxi status queries */
	public boolean isTaxiFull(){
		return (_numberOfPassengers == _maximumCapacity);
	}
	
	public boolean isOnRoad(){
		return (_map[_currentPosition.getValue0()][_currentPosition.getValue1()] == _mapRoad);
	}

	public boolean isOnRefuelStation(){
		return (_map[_currentPosition.getValue0()][_currentPosition.getValue1()] == _mapRefuel);
	}
	
	public boolean isOnStop(){
		return (_map[_currentPosition.getValue0()][_currentPosition.getValue1()] == _mapStop);
	}
	
	public int getGasInTank(){
		return _gas;
	}
	
	public int getTotalGasSpent(){
		return _totalGasSpent;
	}
	
	public int getTotalGasRefuelAmount(){
		return _totalGasRefuelAmount;
	}
	
	public int getNumberOfRefuels(){
		return _numberOfRefuels;
	}
	
	/* Fuel costs */
	private void gasRefuel(){
		_numberOfRefuels++;
		_totalGasRefuelAmount += _gasMax - _gas;
		_gas = _gasMax;
	}
	
	private void gasReduce(){
		if (_gas > 0){
			_gas--;
			_totalGasSpent++;
		} else {
			System.out.println("BUG: Attempting to reduce gas when there is none");
		}
	}
	
	/* Travel costs */
	public int getCostToPoint(Pair<Integer, Integer> point){
		// TODO: Check if possible to get to point
		return SimAgent.getCostBetweenTwoPoints(_currentPosition, point);
	}
	
	
}
