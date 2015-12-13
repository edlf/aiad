package taxitycoon;

import java.util.ArrayList;
import java.util.Random;

import org.javatuples.Pair;

import jade.wrapper.StaleProxyException;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;
import taxitycoon.agents.PassengerAgent;
import taxitycoon.agents.SimAgent;
import taxitycoon.agents.TaxiAgent;
import taxitycoon.agents.TaxiCentral;
import taxitycoon.staticobjects.Grass;
import taxitycoon.staticobjects.RefuelStation;
import taxitycoon.staticobjects.Road;
import taxitycoon.staticobjects.TaxiStop;

public class TaxiTycoonLauncher extends RepastSLauncher implements
		ContextBuilder<Object> {
	/* Variables */
	private MapLoader _mapLoader;
	private final String _contextID = "TaxiTycoon";
	private int currentTaxiID = 0;
	private int currentPassengerID = 0;
	private ArrayList<TaxiStop> _taxiStops = new ArrayList<>();
	private ArrayList<RefuelStation> _refuelStations = new ArrayList<>();

	/* Graphical representation */
	private Grid<Object> _grid;

	/* SaJas Stuff */
	private ContainerController _mainContainer;

	public TaxiTycoonLauncher() {
		System.out.println("###### TaxiTycoon logging started. ######");
		_mapLoader = new MapLoader("map.csv");
	}

	@Override
	public String getName() {
		return _contextID;
	}

	@Override
	protected void launchJADE() {
		RunEnvironment runEnvironment = RunEnvironment.getInstance();
		runEnvironment.setScheduleTickDelay(10);

		_createRepresentation();
		_mainContainer = Runtime.instance().createMainContainer(null);
		launchAgents();
	}

	private void _createRepresentation() {
		/* Create grid */
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		_grid = gridFactory.createGrid(
				"Grid",
				mainContext,
				new GridBuilderParameters<Object>(
						new repast.simphony.space.grid.StrictBorders(),
						new SimpleGridAdder<Object>(), true, _mapLoader
								.getMapSizeX(), _mapLoader.getMapSizeY()));

		/* Fill map with grass */
		for (int i = 0; i < _mapLoader.getMapSizeX(); i++) {
			for (int j = 0; j < _mapLoader.getMapSizeX(); j++) {
				Grass grass = new Grass(new Pair<Integer, Integer>(i, j));
				_grid.getAdder().add(_grid, grass);
				mainContext.add(grass);
				_grid.moveTo(grass, grass.getX(), grass.getY());
			}
		}

		/* Draw roads */
		for (Pair<Integer, Integer> roadPos : _mapLoader.getRoadPositions()) {
			Road road = new Road(roadPos);
			_grid.getAdder().add(_grid, road);
			mainContext.add(road);
			_grid.moveTo(road, road.getX(), road.getY());
		}

		/* Draw refuel stations */
		for (Pair<Integer, Integer> refuelPos : _mapLoader.getRefuelPositions()) {
			RefuelStation refuelStation = new RefuelStation(refuelPos);
			_grid.getAdder().add(_grid, refuelStation);
			_refuelStations.add(refuelStation);
			mainContext.add(refuelStation);
			_grid.moveTo(refuelStation, refuelStation.getX(),
					refuelStation.getY());
		}

		/* Draw taxi stops */
		for (Pair<Integer, Integer> taxiStopPos : _mapLoader
				.getTaxiStopPositions()) {
			TaxiStop taxiStop = new TaxiStop(taxiStopPos);
			_grid.getAdder().add(_grid, taxiStop);
			_taxiStops.add(taxiStop);
			mainContext.add(taxiStop);
			_grid.moveTo(taxiStop, taxiStop.getX(), taxiStop.getY());
		}

		/* Store grid and map size on all moving agents */
		SimAgent.setupMap(_grid, _mapLoader.getMapSize(), _taxiStops,
				_refuelStations);

		/* Create static map for each agent type */
		TaxiAgent.createAgentMap(_mapLoader.getRoadPositions(),
				_mapLoader.getTaxiStopPositions(),
				_mapLoader.getRefuelPositions());
		PassengerAgent.createAgentMap(_mapLoader.getRoadPositions(),
				_mapLoader.getTaxiStopPositions());
		TaxiCentral.createAgentMap(SimAgent.getTaxiStopsArray());
	}

	private void launchAgents() {
		try {
			/* Create taxi central */
			addTaxiCentralAgent();

			/* Create taxis */
			for (Pair<Integer, Integer> taxiLocation : _mapLoader
					.getTaxiPositions()) {
				addTaxiAgent(taxiLocation);
			}

			/* Create passengers */
			for (Pair<Integer, Integer> passengerLocation : _mapLoader
					.getPassengerPositions()) {
				addPassengerAgent(passengerLocation);
			}

		} catch (StaleProxyException e) {
			System.err.println("Connection with Jade lost.");
		}
	}

	private void addTaxiAgent(Pair<Integer, Integer> taxiLocation)
			throws StaleProxyException {
		_mainContainer.acceptNewAgent("Taxi[" + currentTaxiID + "]",
				new TaxiAgent(taxiLocation)).start();
		currentTaxiID++;
	}

	private void addPassengerAgent(Pair<Integer, Integer> passengerLocation)
			throws StaleProxyException {
		_mainContainer.acceptNewAgent("Passenger[" + currentPassengerID + "]",
				new PassengerAgent(passengerLocation, getRandomCorner()))
				.start();
		currentPassengerID++;
	}
	
	/* Return the coordinates of one of the 4 corner of the loaded map randomly */
	private Pair<Integer, Integer> getRandomCorner() {
		int num = new Random().nextInt(4) + 1;

		switch (num) {
		case 1:
			return new Pair<Integer, Integer>(1, 1);
		case 2:
			return new Pair<Integer, Integer>(1, _mapLoader.getMapSizeY() - 2);
		case 3:
			return new Pair<Integer, Integer>(_mapLoader.getMapSizeX() - 2, 1);
		case 4:
			return new Pair<Integer, Integer>(_mapLoader.getMapSizeX() - 2,
					_mapLoader.getMapSizeY() - 2);
		default:
			return new Pair<Integer, Integer>(1, 1);
		}
	}

	private void addTaxiCentralAgent() throws StaleProxyException {
		_mainContainer.acceptNewAgent(TaxiCentral.class.getSimpleName(),
				new TaxiCentral()).start();
	}

	public static String roundDouble(double input) {
		return String.format("%,.2f", input);
	}
}
