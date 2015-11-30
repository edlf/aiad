package taxitycoon.agents;

import org.javatuples.Pair;

/**
 * Passenger agent
 **/
public class PassengerAgent extends SimAgent {
	
	public PassengerAgent(Pair<Integer, Integer> initialPos) {
		this._startPosition = initialPos;
		this._currentPosition = initialPos;
		
		this._currentBehaviour = new taxitycoon.behaviours.passenger.Waiting();
	}

	@Override
	void addInitialBehaviour() {
		replaceBehaviour(_currentBehaviour);
	}
	
	void changeToWalkingToNearestStopBehaviour(){
		replaceBehaviour(new taxitycoon.behaviours.passenger.WalkingToNearestStop());
	}
	
	public boolean isOnGrass(){
		
		return false;
	}

}
