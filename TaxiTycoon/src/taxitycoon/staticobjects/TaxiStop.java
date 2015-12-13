package taxitycoon.staticobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

import org.javatuples.Pair;

import taxitycoon.agents.TaxiAgent;
import taxitycoon.agents.PassengerAgent;

public class TaxiStop extends StaticMapObject {
	
	private BlockingQueue<PassengerAgent> _passengersInQueue = new LinkedBlockingQueue<>();
	private BlockingQueue<TaxiAgent> _taxisInQueue = new LinkedBlockingQueue<>();
	
	public TaxiStop(Pair<Integer, Integer> pos){
		_pos = pos;
	}
	
	public void addTaxiToQueue(TaxiAgent taxiAgent){
		_taxisInQueue.add(taxiAgent);
	}
	
	public void removeTaxiFromQueue(TaxiAgent taxiAgent){
		if (_taxisInQueue.contains(taxiAgent)){
			_taxisInQueue.remove(taxiAgent);
		}
	}
	
	public boolean isMyTurnTaxi(TaxiAgent taxiAgent){
		return (_taxisInQueue.peek() == taxiAgent);
	}
	
	public boolean isMyTurnPassenger(PassengerAgent passengerAgent){
		return (_passengersInQueue.peek() == passengerAgent);
	}
	
	public TaxiAgent getTaxiAtHeadOfQueue(){
		return _taxisInQueue.peek();
	}
	
	public boolean hasTaxiAvailable(){
		return !_taxisInQueue.isEmpty();
	}
	
	public void addPassengerToQueue(PassengerAgent passengerAgent){
		_passengersInQueue.add(passengerAgent);
	}
	
	public void removePassengerFromQueue(PassengerAgent passengerAgent){
		if (_passengersInQueue.contains(passengerAgent)){
			_passengersInQueue.remove(passengerAgent);
		}
	}
	
	public boolean isPassengerInQueue(PassengerAgent passengerAgent){
		return _passengersInQueue.contains(passengerAgent);
	}
	
	public PassengerAgent getPassengerAtHeadOfQueue(){
		return _passengersInQueue.peek();
	}
	
	public ArrayList<PassengerAgent> getPassengers(){
		return new ArrayList<PassengerAgent>(Arrays.asList(_passengersInQueue.toArray(new PassengerAgent[0])));
	}
	
	public boolean isMyTurn(PassengerAgent passengerAgent){
		return (_passengersInQueue.peek() == passengerAgent);
	}
	
	public String getStatus(){
		return "P[" + _passengersInQueue.size() + "] T[" + _taxisInQueue.size() + "]";
	}

	public boolean isTaxiInQueue(TaxiAgent taxiAgent) {
		return _taxisInQueue.contains(taxiAgent);
	}
	
	public int getNumberOfPassengersInQueue(){
		return _passengersInQueue.size();
	}
	
	public int getNumberOfTaxisInQueue(){
		return _taxisInQueue.size();
	}
	
	public int getTaxiStopPriority(){
		int passengers =_passengersInQueue.size(); 
		int taxis = _taxisInQueue.size();
		
		if(passengers > taxis){
			return passengers - taxis;
		} else {
			return 0;
		}
	}
	
	public boolean hasMoreTaxisThanPassengers(){
		return _taxisInQueue.size() > _passengersInQueue.size();
	}
}
