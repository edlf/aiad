package taxitycoon.agents;

import org.javatuples.Pair;

import jade.domain.FIPAException;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;
import sajas.domain.DFService;
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
	
	/* Static sets */
	static public void setupMap(Grid<Object> grid, Pair<Integer, Integer> mapSize){
		if (_baseMapSetupDone) {
			System.out.println("BUG: Sim Agent map already calculated.");
			return;
		}
		
		_grid = grid;
		_mapSize = mapSize;
		_baseMapSetupDone = true;
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
	}

	public static int getCostBetweenTwoPoints(Pair<Integer, Integer> pointA, Pair<Integer, Integer> pointB){
		return Math.abs(pointA.getValue0() - pointB.getValue0()) + Math.abs(pointA.getValue1() - pointB.getValue1());
	}
}
