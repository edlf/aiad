package taxitycoon;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.util.ContextUtils;
import sajas.core.Runtime;
import sajas.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import sajas.sim.repasts.RepastSLauncher;

public class TaxiTycoonLauncher extends RepastSLauncher implements ContextBuilder<Object> {
	private static final String contextID = "TaxiTycoon";
	private int mapSizeX = 100, mapSizeY = 100;
	private int taxisCount = 5, passengerCount = 20;
	private ContainerController mainContainer;
	Context<Object> context;

	public TaxiTycoonLauncher() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return contextID;
	}

	@Override
	protected void launchJADE() {
		context = getContext();
		
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);

		launchAgents();
	}
	
	private void launchAgents(){
		try {
						
			ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
			ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
					new RandomCartesianAdder<Object>(),
					new WrapAroundBorders(), mapSizeX, mapSizeY);

			/* Create grid */
			GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
			
			Grid<Object> grid = gridFactory.createGrid("grid", context,
					new GridBuilderParameters<Object>(new repast.simphony.space.grid.WrapAroundBorders(),
							new SimpleGridAdder<Object>(), true, mapSizeX, mapSizeY));
			
			//AID resultsCollectorAID = null;
	
			// create results collector
			//ResultsCollector resultsCollector = new ResultsCollector(N_CONSUMERS + N_CONSUMERS_FILTERING_PROVIDERS);
			//mainContainer.acceptNewAgent("ResultsCollector", resultsCollector).start();
			//resultsCollectorAID = resultsCollector.getAID();
			
			// create taxis
			for (int i = 0; i < taxisCount; i++) {
				mainContainer.acceptNewAgent("Taxi[" + i + "]", new TaxiAgent(space, grid, 1, 1)).start();
			}
			// bad providers
			for (int i = 0; i < passengerCount; i++) {
				mainContainer.acceptNewAgent("Passenger[" + i+ "]", new PassengerAgent(space, grid, 1, 1)).start();
			}
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}
