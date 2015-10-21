package taxiTycoon;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;

public class TaxiAgent extends Agent {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
		
	public TaxiAgent(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
		
	}
	
}
