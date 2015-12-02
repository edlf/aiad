package taxitycoon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.javatuples.*;

public class MapLoader {
	private static String taxiTile = "T";
	private static String passengerTile = "P";
	private static String refuelTile = "R";
	private static String stopTile = "C";
	private static String roadTile = "_";
	
	private Pair<Integer, Integer> _mapSize;
	private ArrayList<Pair<Integer, Integer>> _taxiLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> _passengerLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> _taxiStopLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> _taxiRefuelLocations = new ArrayList<>();
	private ArrayList<Pair<Integer, Integer>> _roadLocations = new ArrayList<>();
	
	MapLoader(String fileName){
			System.out.println("Loading [" + fileName + "].");
	        
	        int mapX = 0, mapY = 0;
	        
	        BufferedReader bufferedReader = null;
	    	String line = "";

	    	try {
	    		bufferedReader = new BufferedReader(new FileReader(fileName));
	    		while ((line = bufferedReader.readLine()) != null) {
	    			String[] lineContent = line.split(",");
	    			mapX = lineContent.length;
	    			
	    			for (int i = 0; i < mapX; i++){
	    				if (lineContent[i].equals(taxiTile)) {
	    					_taxiLocations.add(new Pair<Integer, Integer>(i,mapY));
	    					_roadLocations.add(new Pair<Integer, Integer>(i,mapY));
	    				} else if (lineContent[i].equals(passengerTile)) {
	    					_passengerLocations.add(new Pair<Integer, Integer>(i,mapY));
	    				} else if (lineContent[i].equals(refuelTile)) {
	    					_taxiRefuelLocations.add(new Pair<Integer, Integer>(i,mapY));
	    				} else if (lineContent[i].equals(roadTile)) {
	    					_roadLocations.add(new Pair<Integer, Integer>(i, mapY));
	    				} else if (lineContent[i].equals(stopTile)) {
	    					_taxiStopLocations.add(new Pair<Integer, Integer>(i, mapY));
	    				}
	    				//else if (lineContent[i].equals("X")) {
	    				//}
	    			}
	    			
	    			mapY++;
	    		}

	    	} catch (FileNotFoundException e) {
	    		System.out.println("File: [" + fileName + "] not found.");
	    		_loadHardcodedMap();
	    		return;
	    	} catch (IOException e) {
	    		System.out.println("IO Exception while reading [" + fileName + "].");
	    		_loadHardcodedMap();
	    		return;
	    	} finally {
	    		if (bufferedReader != null) {
	    			try {
	    				bufferedReader.close();
	    			} catch (IOException e) {
	    	    		System.out.println("IO Exception while reading [" + fileName + "].");
	    	    		_loadHardcodedMap();
	    	    		return;
	    			}
	    		}
	    	}
	        	    	
	    	if (_sanityChecks()) {
		    	_mapSize = new Pair<Integer, Integer>(mapX, mapY);
			    System.out.println("Map sucessfuly loaded.");
	    	} else {
	    		_loadHardcodedMap();
	    	}
	}
	
	private boolean _sanityChecks(){
    	if (_taxiLocations.size() == 0) {
    		System.out.println("Map is missing taxis.");
    		return false;
    	}
    	
    	if (_passengerLocations.size() == 0) {
    		System.out.println("Map is missing passengers.");
    		return false;
    	}
    	
    	if (_taxiRefuelLocations.size() == 0) {
    		System.out.println("Map is missing taxi refuel stations.");
    		return false;
    	}
    	
    	if (_taxiStopLocations.size() == 0) {
    		System.out.println("Map is missing taxi stops.");
    		return false;
    	}
    	
    	return true;
	}
	
	private void _cleanUpArrays(){
		_taxiLocations = new ArrayList<>();
		_passengerLocations = new ArrayList<>();
		_taxiStopLocations = new ArrayList<>();
		_taxiRefuelLocations = new ArrayList<>();
		_roadLocations = new ArrayList<>();
	}
	
	private void _loadHardcodedMap(){
		System.out.println("Using hard coded map.");
		_cleanUpArrays();
		_mapSize = new Pair<Integer, Integer>(5, 5);
		
		_taxiLocations.add(new Pair<Integer, Integer>(1,1));		
		_passengerLocations.add(new Pair<Integer, Integer>(3,3));

		_roadLocations.add(new Pair<Integer, Integer>(1,1));
		_roadLocations.add(new Pair<Integer, Integer>(2,1));
		_roadLocations.add(new Pair<Integer, Integer>(3,1));
		
		_roadLocations.add(new Pair<Integer, Integer>(1,2));
		_roadLocations.add(new Pair<Integer, Integer>(3,2));
		
		_roadLocations.add(new Pair<Integer, Integer>(1,3));
		_roadLocations.add(new Pair<Integer, Integer>(2,3));
		_roadLocations.add(new Pair<Integer, Integer>(3,3));
		
		_taxiStopLocations.add(new Pair<Integer, Integer>(1, 4));
		_taxiRefuelLocations.add(new Pair<Integer, Integer>(3, 0));
		
		if(!_sanityChecks()){
			System.out.println("Hardcoded map error.");
		}
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
	
	public int getRefuelStationCount(){
		return _taxiRefuelLocations.size();
	}
	
	public int getTaxiStopCount(){
		return _taxiStopLocations.size();
	}
	
	public ArrayList<Pair<Integer, Integer>> getTaxiPositions(){
		return _taxiLocations;
	}
	
	public ArrayList<Pair<Integer, Integer>> getPassengerPositions(){
		return _passengerLocations;
	}
	
	public ArrayList<Pair<Integer, Integer>> getRoadPositions(){
		return _roadLocations;
	}
	
	public ArrayList<Pair<Integer, Integer>> getRefuelPositions(){
		return _taxiRefuelLocations;
	}
	
	public ArrayList<Pair<Integer, Integer>> getTaxiStopPositions(){
		return _taxiStopLocations;
	}
}
