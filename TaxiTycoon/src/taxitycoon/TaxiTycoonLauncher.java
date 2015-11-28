package taxitycoon;

import jade.wrapper.StaleProxyException;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.continuous.StrictBorders;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.agents.TaxiAgent;

public class TaxiTycoonLauncher extends RepastSLauncher implements ContextBuilder<Object> {
	/* Variables */
	private MapLoader mapLoader;
		
	/* Repast */
	private final String contextID = "TaxiTycoon";
	
	/* Graphical representation */
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	/* SaJas Stuff */
	private ContainerController mainContainer;

	public TaxiTycoonLauncher() {
		mapLoader = new MapLoader("map.whatever");
	}

	@Override
	public String getName() {
		return contextID;
	}

	@Override
	protected void launchJADE() {
		createRepresentation();
  
		mainContainer = Runtime.instance().createMainContainer(null);
		launchAgents();
	}
	
	private void createRepresentation(){
		/* Create continuous space object */
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		space = spaceFactory.createContinuousSpace("space", mainContext,
				new SimpleCartesianAdder<Object>(),
				new StrictBorders(), mapLoader.getMapSizeX(), mapLoader.getMapSizeY());

		/* Create grid */
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		grid = gridFactory.createGrid("grid", mainContext,
				new GridBuilderParameters<Object>(new repast.simphony.space.grid.WrapAroundBorders(),
				new SimpleGridAdder<Object>(), true, mapLoader.getMapSizeX(), mapLoader.getMapSizeY()));
	}
	
	private void launchAgents(){
		try {
			/* Create taxis */
			for (int i = 0; i < mapLoader.getTaxisCount(); i++) {
				mainContainer.acceptNewAgent("Taxi[" + i + "]", new TaxiAgent(space, grid, mapLoader.getTaxiPosition(i), mapLoader.getMapSize())).start();
			}
			/* Create passengers */
			for (int i = 0; i < mapLoader.getPassengerCount(); i++) {
				mainContainer.acceptNewAgent("Passenger[" + i+ "]", new PassengerAgent(space, grid, mapLoader.getPassengerPosition(i), mapLoader.getMapSize())).start();
			}
			
		} catch (StaleProxyException e) {
			System.err.println("Connection with Jade lost.");
		}
	}
}
