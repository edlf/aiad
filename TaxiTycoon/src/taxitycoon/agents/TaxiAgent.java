package taxitycoon.agents;

import java.util.ArrayList;

import org.javatuples.Pair;

/**
 * Taxi agent
 **/
public class TaxiAgent extends SimAgent {
	private int maximumCapacity = 4;
	private int numberOfPassengers = 0;

	static protected boolean taxiMapCalculated = false;
	
	public static void createAgentMap(ArrayList<Pair<Integer, Integer>> roads, ArrayList<Pair<Integer, Integer>> stops){
		if (taxiMapCalculated) {
			System.out.println("BUG: Taxi map already calculated.");
			return;
		}

		/* Calc map */
		
		taxiMapCalculated = true;
	}
	
	public TaxiAgent(Pair<Integer, Integer> initialPos) {
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
