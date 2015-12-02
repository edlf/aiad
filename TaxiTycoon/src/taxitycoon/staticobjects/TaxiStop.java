package taxitycoon.staticobjects;

import java.util.Queue;

import org.javatuples.Pair;

public class TaxiStop extends StaticMapObject {
	
	private Queue<taxitycoon.agents.PassengerAgent> passengersInQueue;
	private Queue<taxitycoon.agents.TaxiAgent> taxisInQueue;
	
	public TaxiStop(Pair<Integer, Integer> pos){
		_pos = pos;
	}
}
