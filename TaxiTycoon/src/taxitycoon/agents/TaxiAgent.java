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
		this.grid = grid;
		this.mapSize = mapSize;
		
		this.startPosition = initialPos;
		this.currentPosition = initialPos;
	}
	
	@Override
	void addInitialBehaviour(){
		addBehaviour(new taxitycoon.behaviours.taxi.Waiting());
	}
	
	public boolean isTaxiFull(){
		return (numberOfPassengers == maximumCapacity);
	}

}
