package taxitycoon.staticobjects;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.javatuples.Pair;

import taxitycoon.agents.TaxiAgent;

public class RefuelStation extends StaticMapObject {
	
	private ConcurrentLinkedQueue<TaxiAgent> _taxisInQueue = new ConcurrentLinkedQueue<>();
	
	public RefuelStation(Pair<Integer, Integer> pos){
		_pos = pos;
	}
	
	public void addTaxiToQueue(TaxiAgent taxiAgent){
		System.out.println("Refuel[" + _pos.getValue0() + "," + _pos.getValue1() + "] new taxi in queue");
		_taxisInQueue.add(taxiAgent);
	}
	
	public void removeTaxiFromQueue(TaxiAgent taxiAgent){
		if (_taxisInQueue.contains(taxiAgent)){
			System.out.println("Refuel[" + _pos.getValue0() + "," + _pos.getValue1() + "] taxi leaving the queue");
			_taxisInQueue.remove(taxiAgent);
		}
	}
	
	public String getStatus(){
		return "T[" + _taxisInQueue.size() + "]";
	}
}
