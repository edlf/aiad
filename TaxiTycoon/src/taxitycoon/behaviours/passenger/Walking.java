package taxitycoon.behaviours.passenger;

import org.javatuples.Pair;

import sajas.core.behaviours.Behaviour;
import taxitycoon.agents.PassengerAgent;

/**
 * Walking behaviour
 * 
 * Receives destination as a constructor parameter.
 * Destination can be either the taxi stop or the agent final destination.
 * 
 * Reaching the final destination changes the behaviour to the TravelComplete
 * Behaviour. If it reaches a Taxi Stop it changes to the Waiting behaviour.
 */

public class Walking extends Behaviour {
	private static final long serialVersionUID = -7548278065214083296L;
	private int tic = 0;
	private Pair<Integer, Integer> _destination;
	
	public Walking(Pair<Integer, Integer> destination) {
		_destination = destination;
	}

	@Override
	public void action() {
		PassengerAgent passengerAgent = (PassengerAgent) myAgent;
		
		/* If on destination */
		if(passengerAgent.hasReachedDestination()){
			passengerAgent.replaceBehaviour(new TravelComplete());
			return;
		}
		
		/* If on taxi stop */
		if(passengerAgent.hasReachedDestination()){
			passengerAgent.replaceBehaviour(new TravelComplete());
			return;
		}
		
		/* Walk to destination (every 8 tics) */
		tic++;
		if (tic % 8 == 0){
			tic = 0;
			
			int deltaX = passengerAgent.getPosX() - _destination.getValue0();
			int deltaY = passengerAgent.getPosY() - _destination.getValue1();
			
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
		return false;
	}
}
