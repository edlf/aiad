package taxitycoon;

import java.util.ArrayList;
import org.javatuples.*;

public class MapLoader {
	private Pair<Integer, Integer> mapSize;

	private ArrayList<Pair<Double, Double>> taxiLocations;
	private ArrayList<Pair<Double, Double>> passengerLocations;
	
	MapLoader(String fileName){
		mapSize = new Pair<Integer, Integer>(100, 100);
		
		taxiLocations = new ArrayList<>();
		passengerLocations = new ArrayList<>();
		
		/* TODO: Hard coded locations */
		taxiLocations.add(new Pair<Double, Double>(10.0,10.0));
		taxiLocations.add(new Pair<Double, Double>(90.0,90.0));
		
		passengerLocations.add(new Pair<Double, Double>(1.0,1.0));
		passengerLocations.add(new Pair<Double, Double>(1.0,4.0));
		passengerLocations.add(new Pair<Double, Double>(1.0,8.0));
		passengerLocations.add(new Pair<Double, Double>(10.0,20.0));
		passengerLocations.add(new Pair<Double, Double>(10.0,40.0));
		passengerLocations.add(new Pair<Double, Double>(50.0,10.0));
		passengerLocations.add(new Pair<Double, Double>(80.0,80.0));
		passengerLocations.add(new Pair<Double, Double>(60.0,30.0));
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
	
	public Pair<Double, Double> getTaxiPosition(int taxiNumber){
		if (taxiNumber > getTaxisCount() || taxiNumber < 0) {
			return null;
		} else {
			return taxiLocations.get(taxiNumber);
		}
	}
	
	public Pair<Double, Double> getPassengerPosition(int passengerNumber){
		if (passengerNumber > getPassengerCount() || passengerNumber < 0) {
			return null;
		} else {
			return passengerLocations.get(passengerNumber);
		}
	}
}
