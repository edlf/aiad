package taxitycoon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Scanner;

import org.javatuples.*;

public class MapLoader {
	private Pair<Integer, Integer> _mapSize;
	private ArrayList<Pair<Integer, Integer>> _taxiLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> _passengerLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> _taxiPickupLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> _taxiRefuelLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> _roadLocations = new ArrayList<>();
	
	MapLoader(String fileName){
			System.out.println("Attempting to load " + fileName);
	        
	        int mapX = 0, mapY = 0;
	        
	        BufferedReader br = null;
	    	String line = "";
	    	String cvsSplitBy = ",";

	    	try {
	    		br = new BufferedReader(new FileReader(fileName));
	    		while ((line = br.readLine()) != null) {
	    		    // use comma as separator
	    			String[] lineContent = line.split(cvsSplitBy);
	    			mapX = lineContent.length;
	    			
	    			for (int i = 0; i < mapX; i++){
	    				if (lineContent[i].equals("T")) {
	    					System.out.println("Taxi at " + i + "," + mapY);
	    					_taxiLocations.add(new Pair<Integer, Integer>(i,mapY));
	    				} else if (lineContent[i].equals("P")) {
	    					System.out.println("Passenger at " + i + "," + mapY);
	    					_passengerLocations.add(new Pair<Integer, Integer>(i,mapY));
	    				} else if (lineContent[i].equals("R")) {
	    					System.out.println("Refuel at " + i + "," + mapY);
	    					_taxiRefuelLocations.add(new Pair<Integer, Integer>(i,mapY));
	    				} else if (lineContent[i].equals("_")) {
	    					_roadLocations.add(new Pair<Integer, Integer>(i, mapY));
	    				}
	    			}
	    			
	    			mapY++;
	    		}

	    	} catch (FileNotFoundException e) {
	    		System.out.println(fileName + " not found.");
	    		System.out.println("Using hard coded map.");
	    		_loadHardcodedMap();
	    	} catch (IOException e) {
	    		System.out.println("IO Exception while reading " + fileName + ".");
	    		System.out.println("Using hard coded map.");
	    		_loadHardcodedMap();
	    	} finally {
	    		if (br != null) {
	    			try {
	    				br.close();
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}
	        
	        _mapSize = new Pair<Integer, Integer>(mapX, mapY);

	}
	
	private void _loadHardcodedMap(){
		_mapSize = new Pair<Integer, Integer>(30, 30);
		
		_taxiLocations.add(new Pair<Integer, Integer>(1,1));
		_taxiLocations.add(new Pair<Integer, Integer>(29,29));
		
		_passengerLocations.add(new Pair<Integer, Integer>(4,1));
		_passengerLocations.add(new Pair<Integer, Integer>(4,4));
		_passengerLocations.add(new Pair<Integer, Integer>(4,8));
		_passengerLocations.add(new Pair<Integer, Integer>(4,20));
		
		_taxiPickupLocations.add(new Pair<Integer, Integer>(15, 15));
		
		_taxiRefuelLocations.add(new Pair<Integer, Integer>(15, 20));
	}
		
	public Pair<Integer, Integer> getMapSize(){
		return _mapSize;
	}
	
	public int getMapSizeX(){
		return _mapSize.getValue0();
	}
	
	public int getMapSizeY(){
		return _mapSize.getValue1();
	}
	
	public int getTaxisCount(){
		return _taxiLocations.size();
	}
	
	public int getPassengerCount(){
		return _passengerLocations.size();
	}
	
	public Pair<Integer, Integer> getTaxiPosition(int taxiNumber){
		if (taxiNumber > getTaxisCount() || taxiNumber < 0) {
			return null;
		} else {
			return _taxiLocations.get(taxiNumber);
		}
	}
	
	public Pair<Integer, Integer> getPassengerPosition(int passengerNumber){
		if (passengerNumber > getPassengerCount() || passengerNumber < 0) {
			return null;
		} else {
			return _passengerLocations.get(passengerNumber);
		}
	}
	
	public ArrayList<Pair<Integer, Integer>> getRoadPositions(){
		return _roadLocations;
	}
}
