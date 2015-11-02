package taxitycoon.agents;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;

public class PassengerAgent extends Agent {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private int posX, posY;
	
	public PassengerAgent(ContinuousSpace<Object> space, Grid<Object> grid, int posX, int posY) {
		this.space = space;
		this.grid = grid;
		
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	protected void setup(){
		System.out.println(getLocalName() + " setup()");
		
		/* Move to initial position */
		move(this.posX, this.posY);

		/* Add initial behavior */
		addBehaviour(new taxitycoon.behaviours.passenger.Waiting());
	}
	
	@Override
	protected void takeDown(){
		System.out.println(getLocalName() + " takeDown()");
	}
	
	public boolean move(int x, int y){
		return space.moveTo(this, x, y);
	}
}
