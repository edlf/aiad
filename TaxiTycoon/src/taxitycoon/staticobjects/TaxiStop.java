package taxitycoon.staticobjects;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.javatuples.Pair;
import taxitycoon.agents.TaxiAgent;
import taxitycoon.agents.PassengerAgent;

public class TaxiStop extends StaticMapObject {
	
	private ConcurrentLinkedQueue<PassengerAgent> passengersInQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<TaxiAgent> taxisInQueue = new ConcurrentLinkedQueue<>();
	
	public TaxiStop(Pair<Integer, Integer> pos){
		_pos = pos;
	}
	
	public void addTaxiToQueue(TaxiAgent taxiAgent){
		System.out.println("TaxiStop" + _pos.toString() + " new taxi in queue");
		taxisInQueue.add(taxiAgent);
	}
	
	public void removeTaxiFromQueue(TaxiAgent taxiAgent){
		if (taxisInQueue.contains(taxiAgent)){
			taxisInQueue.remove(taxiAgent);
		}
	}
	
	public boolean isMyTurn(TaxiAgent taxiAgent){
		return (taxisInQueue.poll() == taxiAgent);
	}
	
	public TaxiAgent getTaxiAtHeadOfQueue(){
		return taxisInQueue.poll();
	}
	
	public boolean hasTaxiAvailable(){
		return !taxisInQueue.isEmpty();
	}
	
	public void addPassengerToQueue(PassengerAgent passengerAgent){
		System.out.println("TaxiStop" + _pos.toString() + " new passenger in queue");
		passengersInQueue.add(passengerAgent);
	}
	
	public void removePassengerFromQueue(PassengerAgent passengerAgent){
		if (passengersInQueue.contains(passengerAgent)){
			System.out.println("TaxiStop" + _pos.toString() + " passenger has left the queue");
			passengersInQueue.remove(passengerAgent);
		}
	}
	
	public boolean isMyTurn(PassengerAgent passengerAgent){
		return (passengersInQueue.poll() == passengerAgent);
	}
}
