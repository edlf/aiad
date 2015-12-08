package taxitycoon.agents;

import java.util.ArrayList;

import org.javatuples.Pair;

import jade.domain.FIPAException;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import sajas.domain.DFService;
import taxitycoon.behaviours.TaxiCentralBehaviour;
import taxitycoon.staticobjects.TaxiStop;

public class TaxiCentral extends Agent {
	private Behaviour _currentBehaviour =  null;

	private static ArrayList<TaxiStop> _taxiStops;
	
	public static void createAgentMap(ArrayList<TaxiStop> taxiStops){
		_taxiStops = taxiStops;
	}
	
	/* Setup and takedown methods */
	@Override
	protected void setup(){
		System.out.println(getLocalName() + " setup()");
		
		_addBehaviour();
	}
	
	@Override
	protected void takeDown(){
		System.out.println(getLocalName() + " takeDown()");
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			System.out.println(getLocalName() + " error in takeDown()");
		}
	}
	
	private void _addBehaviour(){
		replaceBehaviour(new TaxiCentralBehaviour());
	}
	
	/* Behaviour replacement method */
	public void replaceBehaviour(Behaviour newBehaviour){
		if (_currentBehaviour != null) {
			removeBehaviour(_currentBehaviour);
		}
		
		_currentBehaviour = newBehaviour;
		addBehaviour(_currentBehaviour);
		// System.out.println(getLocalName() + " behaviour change: " + newBehaviour.getClass().getSimpleName());
	}
	
	public static TaxiStop getTaxiStopAt(Pair<Integer, Integer> pos){
		/* Get in what taxi stop were on and add ourself to the passenger queue */
		for (TaxiStop taxiStop : _taxiStops){
			if (taxiStop.getPosition().equals(pos)){
				return taxiStop;
			}
		}
		
		return null;
	}
	
	public ArrayList<TaxiStop> getTaxiStops(){
		return _taxiStops;
	}
	
}
