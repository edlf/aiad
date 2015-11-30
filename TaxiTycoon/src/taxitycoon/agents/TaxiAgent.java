package taxitycoon.agents;

import org.javatuples.Pair;
import repast.simphony.space.grid.Grid;

/**
 * Taxi agent
 **/
public class TaxiAgent extends SimAgent {
	private int maximumCapacity = 4;
	private int numberOfPassengers = 0;
	
	public TaxiAgent(Grid<Object> grid, Pair<Integer, Integer> initialPos, Pair<Integer, Integer> mapSize) {
		this._grid = grid;
		this._mapSize = mapSize;
		
		this._startPosition = initialPos;
		this._currentPosition = initialPos;
		
		this._currentBehaviour = new taxitycoon.behaviours.taxi.Waiting();
	}
	
	@Override
	void addInitialBehaviour(){
		replaceBehaviour(_currentBehaviour);
	}
	
	public boolean isTaxiFull(){
		return (numberOfPassengers == maximumCapacity);
	}

}
