package taxitycoon.agents;

import java.util.ArrayList;

import org.javatuples.Pair;

/**
 * Taxi agent
 **/
public class TaxiAgent extends SimAgent {
	/* Common variables to all taxi agents */
	static private boolean taxiMapCalculated = false;
	static private char[][] map = null; /* col x line */
	static final private char mapEmpty = ' ';
	static final private char mapRoad = '_';
	static final private char mapStop = 'S';
	static final private char mapRefuel = 'R';
	
	/* Individual variables */
	private int maximumCapacity = 4;
	private int numberOfPassengers = 0;

	public static void createAgentMap(ArrayList<Pair<Integer, Integer>> roads, ArrayList<Pair<Integer, Integer>> stops, ArrayList<Pair<Integer, Integer>> refuelStations){
		if (taxiMapCalculated) {
			System.out.println("BUG: Taxi map already calculated.");
			return;
		}

		/* Create map and fill */
		map = new char[_mapSize.getValue0()][_mapSize.getValue1()];
		for (int i = 0; i < _mapSize.getValue0(); i++){
			for (int j = 0; j < _mapSize.getValue1(); j++){
				map[i][j] = mapEmpty;
			}
		}
		
		/* Fill roads */
		for (Pair<Integer, Integer> road : roads){
			map[road.getValue0()][road.getValue1()] = mapRoad;
		}
		
		/* Fill stops */
		for(Pair<Integer, Integer> stop : stops){
			map[stop.getValue0()][stop.getValue1()] = mapStop;
		}
		
		/* Fill refuel stations */
		for(Pair<Integer, Integer> refuelStation : refuelStations){
			map[refuelStation.getValue0()][refuelStation.getValue1()] = mapRefuel;
		}
		
		for (int j = 0; j < _mapSize.getValue1(); j++){
			for (int i = 0; i < _mapSize.getValue0(); i++){
				System.out.print(map[i][j]);
			}
			System.out.println("");
		}
		
		taxiMapCalculated = true;
	}
	
	public TaxiAgent(Pair<Integer, Integer> initialPos) {
		this._startPosition = initialPos;
		this._currentPosition = initialPos;
		
		this._currentBehaviour = new taxitycoon.behaviours.taxi.Waiting();
	}
	
	public void move(Pair<Integer, Integer> newPosition){
		
	}
	
	@Override
	void addInitialBehaviour(){
		replaceBehaviour(_currentBehaviour);
	}
	
	public boolean isTaxiFull(){
		return (numberOfPassengers == maximumCapacity);
	}

}
