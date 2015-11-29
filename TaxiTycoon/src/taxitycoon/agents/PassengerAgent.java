package taxitycoon.agents;

import org.javatuples.Pair;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Passenger agent
 **/
public class PassengerAgent extends SimAgent {
	
	public PassengerAgent(Grid<Object> grid, Pair<Integer, Integer> initialPos, Pair<Integer, Integer> mapSize) {
		this.grid = grid;
		this.mapSize = mapSize;
		
		this.startPosition = initialPos;
		this.currentPosition = initialPos;
	}

	@Override
	void addInitialBehaviour() {
		addBehaviour(new taxitycoon.behaviours.passenger.Waiting());
	}

}
