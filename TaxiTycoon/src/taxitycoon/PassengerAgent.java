package taxiTycoon;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;

public class PassengerAgent extends Agent {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	public PassengerAgent(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}


	
}
