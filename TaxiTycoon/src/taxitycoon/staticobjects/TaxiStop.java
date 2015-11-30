package taxitycoon.staticobjects;

import org.javatuples.Pair;

public class TaxiStop extends StaticMapObject {
	
	public TaxiStop(Pair<Integer, Integer> pos){
		_pos = pos;
	}
}
