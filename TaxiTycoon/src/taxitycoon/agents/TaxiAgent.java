package taxitycoon.agents;

import org.javatuples.Pair;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Taxi agent
 **/
public class TaxiAgent extends SimAgent {
	private int maximumCapacity;
	private int numberOfPassengers = 0;
	
	public TaxiAgent(ContinuousSpace<Object> space, Grid<Object> grid, Pair<Double, Double> initialPos) {
		this.space = space;
		this.grid = grid;
		
		this.startPosition = initialPos;
		this.currentPosition = initialPos;
		
		this.maximumCapacity = 4;
	}
	
	@Override
	void addInitialBehaviour(){
		addBehaviour(new taxitycoon.behaviours.taxi.Waiting());
	}
	
	public boolean isTaxiFull(){
		return (numberOfPassengers == maximumCapacity);
	}

}
