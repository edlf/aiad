package taxitycoon;

import org.javatuples.Pair;

public abstract class StaticMapObject {
	protected Pair<Integer, Integer> _pos;

	int getX(){
		return _pos.getValue0();
	}
	
	int getY(){
		return _pos.getValue1();
	}
}
