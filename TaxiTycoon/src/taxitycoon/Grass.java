package taxitycoon;

import org.javatuples.Pair;

public class Grass {
	private Pair<Integer, Integer> _pos;
	
	Grass(Pair<Integer, Integer> pos){
		_pos = pos;
	}
	
	int getX(){
		return _pos.getValue0();
	}
	
	int getY(){
		return _pos.getValue1();
	}
}
