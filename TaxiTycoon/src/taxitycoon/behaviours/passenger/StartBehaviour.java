package taxitycoon.behaviours.passenger;

import org.javatuples.Pair;
import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;

public class StartBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6833600371259913934L;
	private boolean behaviourDone = false;
	private Pair<Integer, Integer> destination = null;
	private int tic = 0;

	@Override
	public void action() {
		PassengerAgent passengerAgent = (PassengerAgent) myAgent;
		
		/* If on destination */
		if(passengerAgent.hasReachedDestination()){
			behaviourDone = true;
			passengerAgent.replaceBehaviour(new TravelComplete());
			return;
		}
		
		/* If on stop, change behaviour to waiting */
		if(passengerAgent.isOnStop()){
			behaviourDone = true;
			passengerAgent.increaseWaitingTick();
			passengerAgent.replaceBehaviour(new Waiting());
			return;
		}
		
		/* Check were we are going */
		if(destination == null){
			/* Get nearest stop */
			Pair<Integer, Integer> nearestStop = passengerAgent.getNearestStop();
			
			/* Get nearest stop and destination cost */
			int costToDestination = passengerAgent.getCostToDestination();
			int costToNearestStop = passengerAgent.getCostToPoint(nearestStop);
			
			/* Check if we should walk or take a cab */
			if (costToNearestStop > costToDestination){
				destination = passengerAgent.getDestination();
			} else {
				destination = nearestStop;
			}
		}
		
		/* Walk to destination (every 8 tics) */
		tic++;
		if (tic % 8 == 0){
			tic = 0;
			
			int deltaX = passengerAgent.getPosX() - destination.getValue0();
			int deltaY = passengerAgent.getPosY() - destination.getValue1();
			
			if (deltaX != 0){
				if (deltaX < 0){
					passengerAgent.relativeMove(new Pair<Integer, Integer>(+1,0));
				} else {
					passengerAgent.relativeMove(new Pair<Integer, Integer>(-1,0));
				}
			} else if (deltaY != 0){
				if (deltaY < 0){
					passengerAgent.relativeMove(new Pair<Integer, Integer>(0,+1));
				} else {
					passengerAgent.relativeMove(new Pair<Integer, Integer>(0,-1));
				}
			}
		}
		passengerAgent.increaseWalkingTick();
	}

	@Override
	public boolean done() {
		return behaviourDone;
	}

}
