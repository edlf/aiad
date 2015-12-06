package taxitycoon.agents;

import java.util.ArrayList;

import org.javatuples.Pair;
import taxitycoon.behaviours.passenger.*;

/**
 * Passenger agent
 **/
public class PassengerAgent extends SimAgent {
	
	/* Common variables to all passenger agents */
	private static boolean _passengerMapCalculated = false;
	private static char[][] _map = null; /* col x line */
	private static ArrayList<Pair<Integer, Integer>> _stopsPositions;
	
	/* Individual variables */
	private Pair<Integer, Integer> _destination;
	private boolean _inTaxi = false;
	private long _ticksWalking = 0;
	private long _ticksInTaxi = 0;
	private long _ticksWaiting = 0;
	
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
				_map[i][j] = _mapGrass;
			}
		}
		
		/* Fill roads */
		for (Pair<Integer, Integer> road : roads){
			_map[road.getValue0()][road.getValue1()] = _mapRoad;
		}
		
		/* Fill stops */
		_stopsPositions = stops;
		for(Pair<Integer, Integer> stop : stops){
			_map[stop.getValue0()][stop.getValue1()] = _mapStop;
		}
		
		_passengerMapCalculated = true;
	}
	
	/* Non static methods */
	public PassengerAgent(Pair<Integer, Integer> initialPos) {
		if(_isPointWithinBonds(initialPos)) {
			this._startPosition = initialPos;
		} else {
			System.out.println("BUG: Invalid initial position for passenger agent");
			this._startPosition = new Pair<Integer, Integer>(0, 0);
		}	
		_currentPosition = _startPosition;
		
		_currentBehaviour = null;
		_destination = new Pair<Integer, Integer>(28, 28);
		
	}

	public boolean relativeMove(Pair<Integer,Integer> delta){
		Pair<Integer, Integer> newPosition = new Pair<Integer, Integer>(_currentPosition.getValue0() + delta.getValue0(), _currentPosition.getValue1() + delta.getValue1());
		
		/* Check if its out of bonds */
		if (!_isPointWithinBonds(newPosition)){
			return false;
		}
		
		/* Check if it is road */
		if (_map[newPosition.getValue0()][newPosition.getValue1()] == _mapRoad){
			return _move(new Pair<Integer, Integer>(newPosition.getValue0(), newPosition.getValue1()));
		}
		
		/* Check if it is stop */
		if (_map[newPosition.getValue0()][newPosition.getValue1()] == _mapStop){
			return _move(new Pair<Integer, Integer>(newPosition.getValue0(), newPosition.getValue1()));
		}
		
		/* Check if it is refuel station */
		if (_map[newPosition.getValue0()][newPosition.getValue1()] == _mapRefuel){
			return _move(new Pair<Integer, Integer>(newPosition.getValue0(), newPosition.getValue1()));
		}

		/* Check if it is grass */
		if (_map[newPosition.getValue0()][newPosition.getValue1()] == _mapGrass){
			return _move(new Pair<Integer, Integer>(newPosition.getValue0(), newPosition.getValue1()));
		}
		
		/* Nope */
		return false;
	}
	
	@Override
	protected void _addInitialBehaviour() {
		replaceBehaviour(new StartBehaviour());
	}
	
	protected void increaseTick(){
		_totalTicks++;
	}
	
	public void increaseWaitingTick(){
		increaseTick();
		_ticksWaiting++;
	}
	
	public void increaseWalkingTick(){
		increaseTick();
		_ticksWalking++;
	}
	
	public void increaseInTaxiTick(){
		increaseTick();
		_ticksInTaxi++;
	}
	
	void changeToWalkingToNearestStopBehaviour(){
		replaceBehaviour(new StartBehaviour());
	}
	
	public void setDestination(Pair<Integer, Integer> destination){
		_destination = destination;
	}
	
	public Pair<Integer, Integer> getNearestStop(){
		Pair<Integer, Integer> nearestStop = null;
		int currentBest = 0;
		
		for(Pair<Integer, Integer> stop : _stopsPositions){
			if (nearestStop == null) {
				nearestStop = stop;
				currentBest = SimAgent.getCostBetweenTwoPoints(_currentPosition, stop);
			} else {
				int cost = SimAgent.getCostBetweenTwoPoints(_currentPosition, stop);
				if (cost < currentBest) {
					nearestStop = stop;
					currentBest = cost;
				}
			}
		}
		
		return nearestStop;		
	}
	
	public Pair<Integer, Integer> getDestination(){
		return _destination;
	}
	
	/* Passenger status queries */
	public boolean isOnGrass(){
		return (_map[_currentPosition.getValue0()][_currentPosition.getValue1()] == _mapGrass);
	}
	
	public boolean isOnStop(){
		return (_map[_currentPosition.getValue0()][_currentPosition.getValue1()] == _mapStop);
	}
	
	public void setOnTaxi(){
		_inTaxi = true;
	}
	
	public boolean isOnTaxi(){
		return _inTaxi;
	}
	
	public boolean hasReachedDestination(){
		return (_currentPosition.equals(_destination));
	}
	
	public long getTicksWaiting(){
		return _ticksWaiting;
	}
	
	public double getTicksWaitingPercent(){
		return getTickPercent(_ticksWaiting);
	}
	
	public long getTicksWalking(){
		return _ticksWalking;
	}
	
	public double getTicksWalkingPercent(){
		return getTickPercent(_ticksWalking);
	}
	
	public long getTicksInTaxi(){
		return _ticksInTaxi;
	}
	
	public double getTicksInTaxiPercent(){
		return getTickPercent(_ticksInTaxi);
	}
	
	public void printStats(){
		System.out.println(getLocalName() + " walking: " + getTicksWalkingPercent() + "% waiting: " + getTicksWaitingPercent() + "% in taxi: " + getTicksInTaxiPercent() + "%");
	}
	
	/* Travel costs */
	public int getCostToPoint(Pair<Integer, Integer> point){
		return SimAgent.getCostBetweenTwoPoints(_currentPosition, point);
	}
	
	public int getCostToDestination(){
		return getCostToPoint(_destination);
	}
}
