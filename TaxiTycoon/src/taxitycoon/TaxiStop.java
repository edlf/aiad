package taxitycoon;

import org.javatuples.Pair;

public class TaxiStop {
	private Pair<Integer, Integer> _pos;
	
	TaxiStop(Pair<Integer, Integer> pos){
		_pos = pos;
	}
	
	int getX(){
		return _pos.getValue0();
	}
	
	int getY(){
		return _pos.getValue1();
	}
}
