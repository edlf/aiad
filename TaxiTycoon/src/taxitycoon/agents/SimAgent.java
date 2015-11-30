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
	protected Grid<Object> _grid;
	protected Pair<Integer, Integer> _currentPosition;
	protected Pair<Integer, Integer> _startPosition;
	protected Pair<Integer, Integer> _mapSize;
	protected Behaviour _currentBehaviour = null;
	
	/* Direct map position methods */
	public boolean move(Pair<Integer, Integer> newPosition){
		// System.out.println(getLocalName() + " attempting to move to: " + newPosition.getValue0() + "," + newPosition.getValue1());
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
	public boolean relativeMove(Pair<Integer,Integer> delta){
		return move(new Pair<Integer, Integer>(_currentPosition.getValue0() + delta.getValue0(), _currentPosition.getValue1() + delta.getValue1()));
	}
	
	/* Setup and takedown methods */
	@Override
	protected void setup(){
		System.out.println(getLocalName() + " setup()");
		
		/* Move to initial position */
		move(_startPosition);
		
		addInitialBehaviour();
	}
	
	@Override
	protected void takeDown(){
		System.out.println(getLocalName() + " takeDown()");
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			System.out.println(getLocalName() + " error no takeDown()");
		}
	}
	
	/* Add behaviour abstract method */
	abstract void addInitialBehaviour();
	
	/* Behaviour replacement method */
	void replaceBehaviour(Behaviour newBehaviour){
		if (_currentBehaviour != null) {
			removeBehaviour(_currentBehaviour);
		}
		
		this._currentBehaviour = newBehaviour;
		addBehaviour(this._currentBehaviour);
	}
}
