package taxitycoon;

import org.javatuples.Pair;

public class TaxiStop extends StaticMapObject {
	
	TaxiStop(Pair<Integer, Integer> pos){
		_pos = pos;
	}
}
