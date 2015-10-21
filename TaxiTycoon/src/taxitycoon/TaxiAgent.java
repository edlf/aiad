package taxitycoon;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;

public class TaxiAgent extends Agent {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private int posX, posY;
	private int capacity;
	
	public TaxiAgent(ContinuousSpace<Object> space, Grid<Object> grid, int posX, int posY) {
		this.space = space;
		this.grid = grid;
		
		this.posX = posX;
		this.posY = posY;
		
		
	}
	
}
