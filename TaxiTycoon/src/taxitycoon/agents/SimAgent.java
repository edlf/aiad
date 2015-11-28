package taxitycoon.agents;

import org.javatuples.Pair;

import jade.domain.FIPAException;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;
import sajas.domain.DFService;

/**
 * Shared agent logic
 **/
public abstract class SimAgent extends Agent {
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	
	protected Pair<Double,Double> currentPosition;
	protected Pair<Double,Double> startPosition;
	protected Pair<Integer, Integer> mapSize;
	
	/* Direct map position methods */
	public boolean move(Pair<Double,Double> newPosition){
		if (newPosition.getValue0() < 0 || newPosition.getValue1() < 0 || newPosition.getValue0() > mapSize.getValue0() || newPosition.getValue1() > mapSize.getValue1()){
			return false;
		}
		
		if (space.moveTo(this, newPosition.getValue0(), newPosition.getValue1())){
			currentPosition = newPosition;
			return true;
		} else {
			return false;
		}
	}
	
	/* Getters for position */
	public Pair<Double, Double> getPosition(){
		return currentPosition;
	}
	
	public double getPosX(){
		return currentPosition.getValue0();
	}
	
	public double getPosY(){
		return currentPosition.getValue1();
	}
	
	/* Relative move */
	public boolean relativeMove(Pair<Double,Double> delta){
		return move(new Pair<Double, Double>(currentPosition.getValue0() + delta.getValue0(), currentPosition.getValue1() + delta.getValue1()));
	}
	
	/* Setup and takedown methods */
	@Override
	protected void setup(){
		System.out.println(getLocalName() + " setup()");
		
		/* Move to initial position */
		move(startPosition);
		
		addInitialBehaviour();
	}
	
	@Override
	protected void takeDown(){
		System.out.println(getLocalName() + " takeDown()");
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	/* Add behaviour abstract method */
	abstract void addInitialBehaviour();
}
