package taxitycoon.agents;

import java.util.ArrayList;

import org.javatuples.Pair;
import taxitycoon.behaviours.taxi.*;

/**
 * Taxi agent
 **/
public class TaxiAgent extends SimAgent {
	/* Common variables to all taxi agents */
	static private boolean _taxiMapCalculated = false;
	static private char[][] _map = null; /* col x line */
	static private ArrayList<Pair<Integer, Integer>> _intersectionPositions = new ArrayList<>();
	static private ArrayList<Pair<Integer, Integer>> _deadEndPositions = new ArrayList<>();
	static final private int _gasMax = 600;
	static final private int _gasReserve = 60;
	
	/* Individual variables */
	private int _maximumCapacity = 4;
	private int _numberOfPassengers = 0;
	private int _gas = _gasMax;
	private int _numberOfRefuels = 0;
	
	private long _totalGasSpent = 0;
	private long _totalGasRefuelAmount = 0;
	private long _ticksRefueling = 0;
	private long _ticksPickingPassenger = 0;
	private long _ticksInTransit = 0;
	private long _ticksWaiting = 0;

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
		
		/* Check for intersections and dead ends */
		for (int i = 0; i < _mapSize.getValue0(); i++){
			for (int j = 0; j < _mapSize.getValue1(); j++){
				if(_checkPositionForIntersection(i, j)){
					_intersectionPositions.add(new Pair<Integer, Integer>(i, j));
				} else if(_checkPositionForDeadEnd(i, j)){
					_deadEndPositions.add(new Pair<Integer, Integer>(i, j));
				}
			}
		}
		
		System.out.println("Found " + _intersectionPositions.size() + " intersections.");
		System.out.println("Found " + _deadEndPositions.size() + " dead ends.");
		
		_taxiMapCalculated = true;
	}
	
	/* Check if position has more than 2 connections */
	private static boolean _checkPositionForIntersection(int i, int j) {
		/* Check that we are within bonds */
		if (i < 0 || j < 0 || i >= _mapSize.getValue0() || j >= _mapSize.getValue1()){
			return false;
		}
		
		/* Map corners are never intersections */
		if ((i == 0 && j == 0) ||
			(i == 0 && j == (_mapSize.getValue1() - 1)) ||
			(i == (_mapSize.getValue0() - 1) && j == 0) ||
			(i == (_mapSize.getValue0() - 1)) && j == (_mapSize.getValue1() - 1)){
			return false;
		}
		
		/* Check if it is road */
		if (_map[i][j] != _mapRoad){
			return false;
		}
		
		/* Left bar */
		if(i == 0) {
			return (_map[i][j-1] == _mapRoad && _map[i][j+1] == _mapRoad && _map[i+1][j] == _mapRoad); // |-
		}
		
		/* Right bar */
		if(i == (_mapSize.getValue0() - 1)) {
			return (_map[i][j-1] == _mapRoad && _map[i][j+1] == _mapRoad && _map[i-1][j] == _mapRoad); // -|
		}
		
		/* Top bar */
		if(j == 0) {
			return (_map[i-1][j] == _mapRoad && _map[i+1][j] == _mapRoad && _map[i][j+1] == _mapRoad); // T
		}
		
		/* Down bar */
		if(j == (_mapSize.getValue1() - 1)) {
			return (_map[i-1][j] == _mapRoad && _map[i+1][j] == _mapRoad && _map[i][j-1] == _mapRoad); // inverted T
		}
		
		/* Rest of the map */
		return ((_map[i][j-1] == _mapRoad && _map[i][j+1] == _mapRoad && _map[i+1][j] == _mapRoad) || //
				(_map[i][j-1] == _mapRoad && _map[i][j+1] == _mapRoad && _map[i-1][j] == _mapRoad) || //
				(_map[i-1][j] == _mapRoad && _map[i+1][j] == _mapRoad && _map[i][j+1] == _mapRoad) || // regular T
				(_map[i-1][j] == _mapRoad && _map[i+1][j] == _mapRoad && _map[i][j-1] == _mapRoad));  // upside T
	}
	
	private static boolean _checkPositionForDeadEnd(int i, int j){
		/* Check that we are within bonds */
		if (i < 0 || j < 0 || i >= _mapSize.getValue0() || j >= _mapSize.getValue1()){
			return false;
		}
		
		/* Check if it is road */
		if (_map[i][j] != _mapRoad){
			return false;
		}
		
		int numberOfConnections = 0;
		
		/* Map corners */
		if (i == 0 && j == 0){
			/* Right, Up */
			if(_checkPositionForRoadUp(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadRight(i,j)) {
				numberOfConnections++;
			}
			
			return (numberOfConnections == 1);
		}

		if(i == (_mapSize.getValue0() - 1) && j == 0){
			/* Left, Up */
			if(_checkPositionForRoadUp(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadLeft(i,j)) {
				numberOfConnections++;
			}

			return (numberOfConnections == 1);
		}
		
		if(i == 0 && j == (_mapSize.getValue1() - 1)){
			/* Right, Down */
			if(_checkPositionForRoadDown(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadRight(i,j)) {
				numberOfConnections++;
			}
			
			return (numberOfConnections == 1);
		}
			
		if	((i == (_mapSize.getValue0() - 1)) && j == (_mapSize.getValue1() - 1)){
			/* Left, Down */
			if(_checkPositionForRoadDown(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadLeft(i,j)) {
				numberOfConnections++;
			}

			return (numberOfConnections == 1);
		}
		
		/* Left bar */
		if(i == 0) {
			// |-
			if(_checkPositionForRoadUp(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadDown(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadRight(i,j)) {
				numberOfConnections++;
			}
			
			return (numberOfConnections == 1);
		}
		
		/* Right bar */
		if(i == (_mapSize.getValue0() - 1)) {
			// -|
			if(_checkPositionForRoadUp(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadDown(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadLeft(i,j)) {
				numberOfConnections++;
			}
			
			return (numberOfConnections == 1);
		}
		
		/* Top bar */
		if(j == 0) {
			// T
			if(_checkPositionForRoadDown(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadLeft(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadRight(i,j)) {
				numberOfConnections++;
			}
			
			return (numberOfConnections == 1);
		}
		
		/* Down bar */
		if(j == (_mapSize.getValue1() - 1)) {
			// inverted T
			if(_checkPositionForRoadUp(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadLeft(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadRight(i,j)) {
				numberOfConnections++;
			}
			
			return (numberOfConnections == 1);
		}
		
		/* Regular points */
		
		if(_checkPositionForRoadUp(i,j)) {
			numberOfConnections++;
		}
		
		if(_checkPositionForRoadDown(i,j)) {
			numberOfConnections++;
		}
		
		if(_checkPositionForRoadLeft(i,j)) {
			numberOfConnections++;
		}
		
		if(_checkPositionForRoadRight(i,j)) {
			numberOfConnections++;
		}
		
		return (numberOfConnections == 1);
	}
	
	private static boolean _checkPositionForRoadUp(int i, int j){
		return _map[i][j + 1] == _mapRoad;
	}
	
	private static boolean _checkPositionForRoadDown(int i, int j){
		return _map[i][j - 1] == _mapRoad;
	}
	
	private static boolean _checkPositionForRoadLeft(int i, int j){
		return _map[i - 1][j] == _mapRoad;
	}
	
	private static boolean _checkPositionForRoadRight(int i, int j){
		return _map[i + 1][j] == _mapRoad;
	}
	
	/* Non static methods */
	public TaxiAgent(Pair<Integer, Integer> initialPos) {
		this._startPosition = initialPos;
		this._currentPosition = initialPos;
		
		this._currentBehaviour = null;
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
		replaceBehaviour(new Waiting());
	}
	
	protected void increaseTick(){
		_totalTicks++;
	}
	
	public void increaseWaitingTick(){
		increaseTick();
		_ticksWaiting++;
	}
	
	public void increaseRefuelingTick(){
		increaseTick();
		_ticksRefueling++;
	}
	
	public void increasePickingPassengerTick(){
		increaseTick();
		_ticksPickingPassenger++;
	}
	
	public void increaseInTransitTick(){
		increaseTick();
		_ticksInTransit++;
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
	
	public long getTotalGasSpent(){
		return _totalGasSpent;
	}
	
	public long getTotalGasRefuelAmount(){
		return _totalGasRefuelAmount;
	}
	
	public int getNumberOfRefuels(){
		return _numberOfRefuels;
	}
	
	public boolean isGasOnReserve(){
		return (_gas <= _gasReserve);
	}
	
	public long getWaitingTicks(){
		return _ticksWaiting;
	}
	
	public long getRefuelingTicks(){
		return _ticksRefueling;
	}
	
	public long getPickingPassengerTicks(){
		return _ticksPickingPassenger;
	}
	
	public long getInTransitTicks(){
		return _ticksInTransit;
	}
	
	/* Fuel costs */
	public void gasRefuel(){
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
