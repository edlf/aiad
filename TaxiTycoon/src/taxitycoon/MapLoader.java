package taxitycoon;

import java.util.ArrayList;
import org.javatuples.*;

public class MapLoader {
	private Pair<Integer, Integer> mapSize;

	private ArrayList<Pair<Integer, Integer>> taxiLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> passengerLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> taxiPickupLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> taxiRefuelLocations = new ArrayList<>();
	
	MapLoader(String fileName){
		mapSize = new Pair<Integer, Integer>(30, 30);
		
		/* TODO: Hard coded locations */
		taxiLocations.add(new Pair<Integer, Integer>(1,1));
		taxiLocations.add(new Pair<Integer, Integer>(29,29));
		
		passengerLocations.add(new Pair<Integer, Integer>(4,1));
		passengerLocations.add(new Pair<Integer, Integer>(4,4));
		passengerLocations.add(new Pair<Integer, Integer>(4,8));
		passengerLocations.add(new Pair<Integer, Integer>(4,20));
		
		taxiPickupLocations.add(new Pair<Integer, Integer>(15, 15));
		
		taxiRefuelLocations.add(new Pair<Integer, Integer>(15, 20));
	}
	
	public Pair<Integer, Integer> getMapSize(){
		return mapSize;
	}
	
	public int getMapSizeX(){
		return mapSize.getValue0();
	}
	
	public int getMapSizeY(){
		return mapSize.getValue1();
	}
	
	public int getTaxisCount(){
		return taxiLocations.size();
	}
	
	public int getPassengerCount(){
		return passengerLocations.size();
	}
	
	public Pair<Integer, Integer> getTaxiPosition(int taxiNumber){
		if (taxiNumber > getTaxisCount() || taxiNumber < 0) {
			return null;
		} else {
			return taxiLocations.get(taxiNumber);
		}
	}
	
	public Pair<Integer, Integer> getPassengerPosition(int passengerNumber){
		if (passengerNumber > getPassengerCount() || passengerNumber < 0) {
			return null;
		} else {
			return passengerLocations.get(passengerNumber);
		}
	}
}
