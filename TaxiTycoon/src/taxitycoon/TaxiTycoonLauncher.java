package taxitycoon;

import java.util.ArrayList;
import java.util.List;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.BootProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import sajas.core.Runtime;
import sajas.wrapper.ContainerController;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.agents.TaxiAgent;
import sajas.sim.repasts.RepastSLauncher;

public class TaxiTycoonLauncher extends RepastSLauncher implements ContextBuilder<Object> {
	/* Variables */
	private int mapSizeX = 100, mapSizeY = 100;
	private int taxisCount = 5, passengerCount = 20;
		
	/* Repast Simphony */
	private final String contextID = "TaxiTycoon";
	private Context<Object> context;
	
	/* Graphical representation */
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	/* SaJas Stuff */
	private ContainerController mainContainer;
	private final boolean JADE_GUI = true;

	public TaxiTycoonLauncher() {

	}

	@Override
	public String getName() {
		return contextID;
	}

	@Override
	protected void launchJADE() {
		context = getContext();
		createRepresentation();
		
		Runtime runtime = Runtime.instance();

/*        if (JADE_GUI) {
            List<String> params = new ArrayList<String>();
            params.add("-gui");
            profile = new BootProfileImpl(params.toArray(new String[params.size()]));
        } else {
            profile = new ProfileImpl();
        }
*/        
		mainContainer = runtime.createMainContainer(null);
		launchAgents();
	}
	
	private void createRepresentation(){
		/* Create continuous space object */
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(),
				new WrapAroundBorders(), mapSizeX, mapSizeY);

		/* Create grid */
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new repast.simphony.space.grid.WrapAroundBorders(),
				new SimpleGridAdder<Object>(), true, mapSizeX, mapSizeY));
	}
	
	private void launchAgents(){
		try {
						
			//AID resultsCollectorAID = null;

			// create results collector
			//ResultsCollector resultsCollector = new ResultsCollector(N_CONSUMERS + N_CONSUMERS_FILTERING_PROVIDERS);
			//mainContainer.acceptNewAgent("ResultsCollector", resultsCollector).start();
			//resultsCollectorAID = resultsCollector.getAID();	
			
			/* Create taxis */
			for (int i = 0; i < taxisCount; i++) {
				mainContainer.acceptNewAgent("Taxi[" + i + "]", new TaxiAgent(space, grid, 1, 1)).start();
			}
			/* Create passengers */
			for (int i = 0; i < passengerCount; i++) {
				mainContainer.acceptNewAgent("Passenger[" + i+ "]", new PassengerAgent(space, grid, 1, 1)).start();
			}
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}