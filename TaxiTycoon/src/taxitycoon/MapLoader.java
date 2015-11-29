package taxitycoon;

import java.io.File;
import java.io.FileNotFoundException;
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
		try {
			System.out.println("Attempting to load " + fileName);
			
			Scanner scanner = new Scanner(new File(fileName));
	        scanner.useDelimiter(",");
	        
	        int readChars = 0, linePos = 0;
	        int mapX = 0, mapY = 0;
	        
	        while (scanner.hasNext()){
	        	String currentString = scanner.next();
	        	
	        	if (currentString.equals("30") ||
	        		currentString.equals("15") ||
	        		currentString.equals("T") ||
	        		currentString.equals("P") ||
	        		currentString.equals("_")) {
	        		
	        		switch (readChars) {
					case 0:
						mapX = Integer.parseInt(currentString);
						System.out.println("Read X (" + mapX + ")");
						readChars++;
						break;
						
					case 1:
						mapY = Integer.parseInt(currentString);
						System.out.println("Read Y (" + mapY + ")");
						readChars++;
						break;

					default:
						if (linePos < mapX) {
							System.out.print(currentString);
							linePos++;
							
						} else {
							System.out.println("");
							linePos = 0;
						}
						
						readChars++;
						break;
					}
	        	}
	        }
	        
	        _mapSize = new Pair<Integer, Integer>(mapX, mapY);
	        
	        scanner.close();
	        
	        
		} catch (FileNotFoundException e) {
			System.out.println(fileName + " not found.");
			System.out.println("Using hard coded map.");
			
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
