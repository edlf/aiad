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
		System.out.println("Found " + _deadEndPositions.size() + " dead ends.");
		
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
		this._startPosition = initialPos;
		this._currentPosition = initialPos;
		
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
		if (!_isPointWithinBonds(point)){
			return -1;
		}
		
		
		// TODO: Check if possible to get to point
		return SimAgent.getCostBetweenTwoPoints(_currentPosition, point);
	}

	public ArrayList<Pair<Integer, Integer>> getShortestPathTo(Pair<Integer, Integer> destination) {
		ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();
		
		if (!_isPointWithinBonds(destination)){
			return path;
		}
		
		/* Create cost map and fill with -1 */
		int costMap[][] = new int[_mapSize.getValue0()][_mapSize.getValue1()];
		for (int i = 0; i < _mapSize.getValue0(); i++){
			for (int j = 0; j < _mapSize.getValue1(); j++){	
				costMap[i][j] = -1;
			}
		}
		
		// TODO Fill path
		
		
		return path;
	}
	
	
}
