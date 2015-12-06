package taxitycoon.agents;

import java.util.ArrayList;
import java.util.LinkedList;

import org.javatuples.Pair;

import taxitycoon.behaviours.taxi.*;
import taxitycoon.staticobjects.RefuelStation;
import taxitycoon.staticobjects.TaxiStop;

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
				switch (_getPositionConnections(i, j)) {
				case 0:
					System.out.println("Lost point found! [" + i + "," + j+ "]");
					break;
					
				case 1:
					_deadEndPositions.add(new Pair<Integer, Integer>(i, j));
					break;
					
				case 2:
					
					break;
					
				case 3:
					_intersectionPositions.add(new Pair<Integer, Integer>(i, j));
					break;
					
				case 4:
					_intersectionPositions.add(new Pair<Integer, Integer>(i, j));
					break;

				default:
					break;
				}
			}
		}
		
		System.out.println("Found " + _intersectionPositions.size() + " intersections.");
		if (_deadEndPositions.size() !=0){
			System.out.println("Found " + _deadEndPositions.size() + " dead ends.");
		}
			
		_taxiMapCalculated = true;
	}

	/* Get position connections */
	private static int _getPositionConnections(int i, int j){
		/* Check that we are within bonds */
		if (!_isPointWithinBonds(i,j)){
			return -1;
		}
		
		/* Check if it is road */
		if (_map[i][j] != _mapRoad){
			return -1;
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
			
			return numberOfConnections;
		}

		if(i == (_mapSize.getValue0() - 1) && j == 0){
			/* Left, Up */
			if(_checkPositionForRoadUp(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadLeft(i,j)) {
				numberOfConnections++;
			}

			return numberOfConnections;
		}
		
		if(i == 0 && j == (_mapSize.getValue1() - 1)){
			/* Right, Down */
			if(_checkPositionForRoadDown(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadRight(i,j)) {
				numberOfConnections++;
			}
			
			return numberOfConnections;
		}
			
		if	((i == (_mapSize.getValue0() - 1)) && j == (_mapSize.getValue1() - 1)){
			/* Left, Down */
			if(_checkPositionForRoadDown(i,j)) {
				numberOfConnections++;
			}
			
			if(_checkPositionForRoadLeft(i,j)) {
				numberOfConnections++;
			}

			return numberOfConnections;
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
			
			return numberOfConnections;
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
			
			return numberOfConnections;
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
			
			return numberOfConnections;
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
			
			return numberOfConnections;
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
		
		return numberOfConnections;
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
		if(_isPointWithinBonds(initialPos)) {
			this._startPosition = initialPos;
		} else {
			System.out.println("BUG: Invalid initial position for taxi agent");
			this._startPosition = new Pair<Integer, Integer>(0, 0);
		}
		
		this._currentPosition = _startPosition;
		this._currentBehaviour = null;
	}
	
	public boolean relativeMove(Pair<Integer,Integer> delta){
		Pair<Integer, Integer> newPosition = new Pair<Integer, Integer>(_currentPosition.getValue0() + delta.getValue0(), _currentPosition.getValue1() + delta.getValue1());
		
		/* Check if its out of bonds */
		if (!_isPointWithinBonds(newPosition)){
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
	
	public boolean move(Pair<Integer,Integer> dest){
		if (_currentPosition.getValue0() == dest.getValue0() && Math.abs(_currentPosition.getValue1() - dest.getValue1()) == 1){
			if (_currentPosition.getValue1() > dest.getValue1()){
				return relativeMove(new Pair<Integer, Integer>(0, -1));
			} else {
				return relativeMove(new Pair<Integer, Integer>(0, 1));
			}
		}
		
		if (_currentPosition.getValue1() == dest.getValue1() && Math.abs(_currentPosition.getValue0() - dest.getValue0()) == 1){
			if (_currentPosition.getValue0() > dest.getValue0()){
				return relativeMove(new Pair<Integer, Integer>(-1,0));
			} else {
				return relativeMove(new Pair<Integer, Integer>(1, 0));
			}
		}
		
		return false;
	}
	
	@Override
	protected void _addInitialBehaviour(){
		replaceBehaviour(new StartBehaviour());
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
	
	public boolean isOnRoad(Pair<Integer, Integer> point){
		return (_map[point.getValue0()][point.getValue1()] == _mapRoad);
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
	
	public double getWaitingTicksPercent(){
		return getTickPercent(_ticksWaiting);
	}
	
	public long getRefuelingTicks(){
		return _ticksRefueling;
	}
	
	public double getRefuelingTicksPercent(){
		return getTickPercent(_ticksRefueling);
	}
	
	public long getPickingPassengerTicks(){
		return _ticksPickingPassenger;
	}
	
	public double getPickingPassengerTicksPercent(){
		return getTickPercent(_ticksPickingPassenger);
	}
	
	public long getInTransitTicks(){
		return _ticksInTransit;
	}
	
	public double getInTransitTicksPercent(){
		return getTickPercent(_ticksInTransit);
	}
	
	public double getGasTankLevelPercent(){	
		return ((double) _gas) / ((double) _gasMax) * 100.0;
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
		if (!_isPointWithinBonds(point)){
			return -1;
		}

		return getShortestPathTo(point).size();
	}

	public LinkedList<Pair<Integer, Integer>> getShortestPathTo(Pair<Integer, Integer> destination) {
		LinkedList<Pair<Integer, Integer>> path = new LinkedList<>();
		
		/* Check if the destination is within bonds */
		if (!_isPointWithinBonds(destination)){
			return path;
		}
		
		/* Check if we are on road and the destination is also on road */
		if(!isOnRoad() || !(isOnRoad(destination) || isOnTaxiStop(destination))){
			return path;
		}
		
		/* Create cost map and fill with 0 possible locations, -1 impossible locations */
		int costMap[][] = new int[_mapSize.getValue0()][_mapSize.getValue1()];
		for (int i = 0; i < _mapSize.getValue0(); i++){
			for (int j = 0; j < _mapSize.getValue1(); j++){
				if (_map[i][j] == _mapRoad || _map[i][j] == _mapStop || _map[i][j] == _mapRefuel){
					costMap[i][j] = 0;
				} else {
					costMap[i][j] = -1;	
				}
			}
		}
		
		/* Set current position with 1 cost */
		int startI = _currentPosition.getValue0(), startJ = _currentPosition.getValue1();
		costMap[startI][startJ] = 1;

		LinkedList<Pair<Integer, Integer>> queue = new LinkedList<>();
		
		/* Add start pos */
		queue.add(_currentPosition);
		
		/* Calculate costs */
		while(!queue.isEmpty()){
			Pair<Integer, Integer> currentPos = queue.poll();
			
			/* Are we there yet? */
			if (currentPos.equals(destination)){
				break;
			}
			
			int currentCost = costMap[currentPos.getValue0()][currentPos.getValue1()];
			
			ArrayList<Pair<Integer, Integer>> nextPositions = new ArrayList<>();
			nextPositions.add(new Pair<Integer, Integer>(currentPos.getValue0() + 1, currentPos.getValue1()));
			nextPositions.add(new Pair<Integer, Integer>(currentPos.getValue0(), currentPos.getValue1() + 1));
			nextPositions.add(new Pair<Integer, Integer>(currentPos.getValue0() - 1, currentPos.getValue1()));
			nextPositions.add(new Pair<Integer, Integer>(currentPos.getValue0(), currentPos.getValue1() - 1));
			
			for(Pair<Integer, Integer> nextPos : nextPositions){
				/* Skip points out of bonds */
				if (!_isPointWithinBonds(nextPos)){
					continue;
				}
				
				if(costMap[nextPos.getValue0()][nextPos.getValue1()] == 0){
					queue.add(nextPos);
					costMap[nextPos.getValue0()][nextPos.getValue1()] = currentCost + 1;
				}
			}		
		}
		
		/* We got nothing, return an empty path */
		if (costMap[destination.getValue0()][destination.getValue1()] == 0){
			return path;
		}
		
		Pair<Integer, Integer> currentPos = destination;
		
		/* Get the shortest path */
		while(!currentPos.equals(_currentPosition)){
			path.push(currentPos);
			int currentLevel = costMap[currentPos.getValue0()][currentPos.getValue1()];
			
			ArrayList<Pair<Integer, Integer>> nextPositions = new ArrayList<>();
			nextPositions.add(new Pair<Integer, Integer>(currentPos.getValue0() + 1, currentPos.getValue1()));
			nextPositions.add(new Pair<Integer, Integer>(currentPos.getValue0(), currentPos.getValue1() + 1));
			nextPositions.add(new Pair<Integer, Integer>(currentPos.getValue0() - 1, currentPos.getValue1()));
			nextPositions.add(new Pair<Integer, Integer>(currentPos.getValue0(), currentPos.getValue1() - 1));
			
			for(Pair<Integer, Integer> nextPos : nextPositions){
				/* Skip points out of bonds */
				if (!_isPointWithinBonds(nextPos)){
					continue;
				}
				
				if(costMap[nextPos.getValue0()][nextPos.getValue1()] == currentLevel - 1){
					currentPos = nextPos;
					break;
				}
			}
		}
		
		return path;
	}

	private boolean isOnTaxiStop(Pair<Integer, Integer> point) {
		return (_map[point.getValue0()][point.getValue1()] == _mapStop);
	}

	public TaxiStop getNearestTaxiStop() {
		TaxiStop nearest = null;
		int currentCost = Integer.MAX_VALUE;
		
		for(TaxiStop taxiStop: _taxiStops){
			int stopCost = getCostToPoint(taxiStop.getPosition());
			if(stopCost < currentCost){
				nearest = taxiStop;
				currentCost = stopCost;
			}
		}
		
		return nearest;
	}
	
	public RefuelStation getNearestRefuelStation() {
		RefuelStation nearest = null;
		int currentCost = Integer.MAX_VALUE;
		
		for(RefuelStation refuelStation: _refuelStations){
			int stopCost = getCostToPoint(refuelStation.getPosition());
			if(stopCost < currentCost){
				nearest = refuelStation;
				currentCost = stopCost;
			}
		}
		
		return nearest;
	}
	
	public String getStatus(){
		if (_numberOfPassengers > 0){
			return "[" + _numberOfPassengers + " of " + _maximumCapacity + "]";
		}
		
		return "";
	}
}
