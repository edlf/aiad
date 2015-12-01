package taxitycoon.behaviours.passenger;

import org.javatuples.Pair;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;

public class WalkingToNearestStop extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6833600371259913934L;
	private boolean isOnStop = false;
	private Pair<Integer, Integer> nearestStop = null;
	private int tic = 0;

	@Override
	public void action() {
		PassengerAgent myPassengerAgent = (PassengerAgent) myAgent;
		
		/* If on stop, change behaviour to waiting */
		if(myPassengerAgent.isOnStop()){
			isOnStop = true;
			myPassengerAgent.replaceBehaviour(new taxitycoon.behaviours.passenger.Waiting());
			return;
		}
		
		/* Get nearest stop */
		if(nearestStop == null){
			nearestStop = myPassengerAgent.getNearestStop();
		}
		
		/* Walk to destination (every 10 tics) */
		tic++;
		if (tic % 10 == 0){
			tic = 0;
			
			
		}
	}

	@Override
	public boolean done() {
		return isOnStop;
	}

}
