package taxitycoon.agents;

import org.javatuples.Pair;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Passenger agent
 **/
public class PassengerAgent extends SimAgent {
	
	public PassengerAgent(ContinuousSpace<Object> space, Grid<Object> grid, Pair<Double, Double> initialPos) {
		this.space = space;
		this.grid = grid;
		
		this.startPosition = initialPos;
		this.currentPosition = initialPos;
	}

	@Override
	void addInitialBehaviour() {
		addBehaviour(new taxitycoon.behaviours.passenger.Waiting());
	}

}
