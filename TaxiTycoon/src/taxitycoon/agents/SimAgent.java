package taxitycoon.agents;

import java.util.ArrayList;

import org.javatuples.Pair;

import jade.domain.FIPAException;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;
import sajas.domain.DFService;
import taxitycoon.staticobjects.RefuelStation;
import taxitycoon.staticobjects.TaxiStop;
import sajas.core.behaviours.Behaviour;

/**
 * Shared agent logic
 **/
public abstract class SimAgent extends Agent {
	/* Repast */
	protected static Grid<Object> _grid;
	
	/* Common Map */
	protected static Pair<Integer, Integer> _mapSize;
	protected static boolean _baseMapSetupDone = false;
	protected static ArrayList<TaxiStop> _taxiStops = new ArrayList<>();
	protected static ArrayList<RefuelStation> _refuelStations = new ArrayList<>();
	
	/* Map Types */
	protected static final char _mapRoad = '_';
	protected static final char _mapGrass = 'G';
	protected static final char _mapStop = 'S';
	protected static final char _mapRefuel = 'R';
	
	/* Position */
	protected Pair<Integer, Integer> _currentPosition;
	protected Pair<Integer, Integer> _startPosition;
	
	/* Behaviour */
	protected Behaviour _currentBehaviour = null;
	
	protected long _totalTicks = 0;
	
	/* Static sets */
	static public void setupMap(Grid<Object> grid, Pair<Integer, Integer> mapSize, ArrayList<TaxiStop> taxiStops, ArrayList<RefuelStation> refuelStations){
		if (_baseMapSetupDone) {
			System.out.println("BUG: Sim Agent map already calculated.");
			return;
		}
		
		_grid = grid;
		_mapSize = mapSize;
		_taxiStops = taxiStops;
		_refuelStations = refuelStations;
		_baseMapSetupDone = true;
	}
	
	/* Check if point is within map bounds */
	protected static boolean _isPointWithinBonds(int i, int j){
		/* Check that we are within bonds */
		return (i >= 0 || j >= 0 || i < _mapSize.getValue0() || j < _mapSize.getValue1());
	}
	
	protected static boolean _isPointWithinBonds(Pair<Integer, Integer> point){
		return _isPointWithinBonds(point.getValue0(), point.getValue1());
	}
	
	/* Direct map position methods */
	protected boolean _move(Pair<Integer, Integer> newPosition){
		/* Check if it is out of bonds */
		if (newPosition.getValue0() < 0 || newPosition.getValue1() < 0 || newPosition.getValue0() >= _mapSize.getValue0() || newPosition.getValue1() >= _mapSize.getValue1()){
			return false;
		}
		
		if (_grid.moveTo(this, newPosition.getValue0(), newPosition.getValue1())){
			_currentPosition = newPosition;
			return true;
		} else {
			return false;
		}
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
	
	/* Getters for position */
	public Pair<Integer, Integer> getPosition(){
		return _currentPosition;
	}
	
	public int getPosX(){
		return _currentPosition.getValue0();
	}
	
	public int getPosY(){
		return _currentPosition.getValue1();
	}
	
	public static ArrayList<TaxiStop> getTaxiStopsArray(){
		return _taxiStops;
	}
	
	/* Relative move */
	abstract public boolean relativeMove(Pair<Integer,Integer> delta);
	
	/* Setup and takedown methods */
	@Override
	protected void setup(){
		System.out.println(getLocalName() + " setup()");
		
		/* Move to initial position */
		_move(_startPosition);
		
		_addInitialBehaviour();
	}
	
	@Override
	protected void takeDown(){
		System.out.println(getLocalName() + " takeDown()");
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			System.out.println(getLocalName() + " error in takeDown()");
		}
	}
	
	/* Add behaviour abstract method */
	abstract protected void _addInitialBehaviour();
	
	/* Behaviour replacement method */
	public void replaceBehaviour(Behaviour newBehaviour){
		if (_currentBehaviour != null) {
			removeBehaviour(_currentBehaviour);
		}
		
		_currentBehaviour = newBehaviour;
		addBehaviour(_currentBehaviour);
		System.out.println(getLocalName() + " behaviour change: " + newBehaviour.getClass().getSimpleName());
	}

	/* Cost methods */
	public static int getCostBetweenTwoPoints(Pair<Integer, Integer> pointA, Pair<Integer, Integer> pointB){
		return Math.abs(pointA.getValue0() - pointB.getValue0()) + Math.abs(pointA.getValue1() - pointB.getValue1());
	}
	
	abstract public int getCostToPoint(Pair<Integer, Integer> point);
	
	/* Ticks */
	public long getTotalTicks(){
		return _totalTicks;
	}
	
	protected double getTickPercent(long parcialTick){
		if (_totalTicks == 0){
			return 0.0;
		}
		
		return ((double) parcialTick) / ((double) _totalTicks) * 100.0;
	}
	
	abstract protected void increaseTick();
}
