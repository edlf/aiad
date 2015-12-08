package taxitycoon.staticobjects;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.javatuples.Pair;
import taxitycoon.agents.TaxiAgent;
import taxitycoon.agents.PassengerAgent;

public class TaxiStop extends StaticMapObject {
	
	private ConcurrentLinkedQueue<PassengerAgent> _passengersInQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<TaxiAgent> _taxisInQueue = new ConcurrentLinkedQueue<>();
	
	public TaxiStop(Pair<Integer, Integer> pos){
		_pos = pos;
	}
	
	public void addTaxiToQueue(TaxiAgent taxiAgent){
		System.out.println("TaxiStop[" + _pos.getValue0() + "," + _pos.getValue1() + "] new taxi in queue");
		_taxisInQueue.add(taxiAgent);
	}
	
	public void removeTaxiFromQueue(TaxiAgent taxiAgent){
		if (_taxisInQueue.contains(taxiAgent)){
			System.out.println("TaxiStop[" + _pos.getValue0() + "," + _pos.getValue1() + "] taxi leaving the queue");
			_taxisInQueue.remove(taxiAgent);
		} else {
			System.out.println("BUG: Attempting to remove non existing taxi from queue");
		}
	}
	
	public boolean isMyTurn(TaxiAgent taxiAgent){
		return (_taxisInQueue.peek() == taxiAgent);
	}
	
	public TaxiAgent getTaxiAtHeadOfQueue(){
		return _taxisInQueue.peek();
	}
	
	public boolean hasTaxiAvailable(){
		return !_taxisInQueue.isEmpty();
	}
	
	public void addPassengerToQueue(PassengerAgent passengerAgent){
		System.out.println("TaxiStop[" + _pos.getValue0() + "," + _pos.getValue1() + "] new passenger in queue");
		_passengersInQueue.add(passengerAgent);
	}
	
	public void removePassengerFromQueue(PassengerAgent passengerAgent){
		if (_passengersInQueue.contains(passengerAgent)){
			System.out.println("TaxiStop[" + _pos.getValue0() + "," + _pos.getValue1() + "] passenger leaving the queue");
			_passengersInQueue.remove(passengerAgent);
		} else {
			System.out.println("BUG: Attempting to remove non existing passenger from queue");
		}
	}
	
	public boolean isPassengerInQueue(PassengerAgent passengerAgent){
		return _passengersInQueue.contains(passengerAgent);
	}
	
	public PassengerAgent getPassengerAtHeadOfQueue(){
		return _passengersInQueue.peek();
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
		
		if (passengers == 0){
			return 0;
		}
		
		if(taxis > passengers){
			return 0;
		}
		
		return passengers - taxis;
	}
	
	public boolean hasMoreTaxisThanPassengers(){
		return _taxisInQueue.size() > _passengersInQueue.size();
	}
}
