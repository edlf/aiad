package taxitycoon.agents;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;

/**
 * Shared agent logic
 **/
public abstract class SimAgent extends Agent {
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	
	protected double posX, posY;
	
	/* Direct map position methods */
	public boolean move(double x, double y){
		return space.moveTo(this, x, y);
	}
	
	public double getPosX(){
		return posX;
	}
	
	public double getPosY(){
		return posY;
	}
	
	/* Setup and teardown methods */
	@Override
	protected void setup(){
		System.out.println(getLocalName() + " setup()");
		
		/* Move to initial position */
		move(this.posX, this.posY);
		
		addInitialBehaviour();
	}
	
	@Override
	protected void takeDown(){
		System.out.println(getLocalName() + " takeDown()");
	}
	
	/* Add behaviour abstract method */
	abstract void addInitialBehaviour();
}
