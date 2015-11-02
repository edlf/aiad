package taxitycoon.agents;

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
	
	@Override
	protected void setup(){
		System.out.println(getLocalName() + " setup()");
		
		/* Move to specified position */
		space.moveTo(this, this.posX, this.posY);
		
		/* Add initial behavior */
		addBehaviour(new taxitycoon.behaviours.taxi.Waiting());
	}
	
	@Override
	protected void takeDown(){
		System.out.println(getLocalName() + " takeDown()");
	}
}
