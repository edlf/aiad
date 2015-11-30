package taxitycoon.agents;

import org.javatuples.Pair;
import repast.simphony.space.grid.Grid;

/**
 * Passenger agent
 **/
public class PassengerAgent extends SimAgent {
	
	public PassengerAgent(Grid<Object> grid, Pair<Integer, Integer> initialPos, Pair<Integer, Integer> mapSize) {
		this._grid = grid;
		this._mapSize = mapSize;
		
		this._startPosition = initialPos;
		this._currentPosition = initialPos;
		
		replaceBehaviour(new taxitycoon.behaviours.passenger.Waiting());
	}

	@Override
	void addInitialBehaviour() {
		replaceBehaviour(_currentBehaviour);
	}
	
	void changeToWalkingToNearestStopBehaviour(){
		replaceBehaviour(new taxitycoon.behaviours.passenger.WalkingToNearestStop());
	}
	
	public boolean isOnGrass(){
		
		return false;
	}

}
