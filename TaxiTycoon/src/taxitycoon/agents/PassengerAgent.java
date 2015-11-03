package taxitycoon.agents;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Passenger agent
 **/
public class PassengerAgent extends SimAgent {
	
	public PassengerAgent(ContinuousSpace<Object> space, Grid<Object> grid, double posX, double posY) {
		this.space = space;
		this.grid = grid;
		
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	void addInitialBehaviour() {
		addBehaviour(new taxitycoon.behaviours.passenger.Waiting());
	}

}
