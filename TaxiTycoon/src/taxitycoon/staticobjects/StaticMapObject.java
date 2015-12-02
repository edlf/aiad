package taxitycoon.staticobjects;

import org.javatuples.Pair;

public abstract class StaticMapObject {
	protected Pair<Integer, Integer> _pos;

	public int getX(){
		return _pos.getValue0();
	}
	
	public int getY(){
		return _pos.getValue1();
	}
	
	public Pair<Integer, Integer> getPosition(){
		return _pos;
	}
}
