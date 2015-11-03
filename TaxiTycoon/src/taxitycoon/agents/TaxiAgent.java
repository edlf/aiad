package taxitycoon.agents;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Taxi agent
 **/
public class TaxiAgent extends SimAgent {
	private int capacity;
	
	public TaxiAgent(ContinuousSpace<Object> space, Grid<Object> grid, double posX, double posY) {
		this.space = space;
		this.grid = grid;
		
		this.posX = posX;
		this.posY = posY;
		
		this.capacity = 5;
	}
	
	@Override
	void addInitialBehaviour(){
		addBehaviour(new taxitycoon.behaviours.taxi.Waiting());
	}
	

}
