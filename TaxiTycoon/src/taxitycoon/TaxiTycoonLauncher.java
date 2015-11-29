package taxitycoon;

import java.util.ArrayList;

import org.javatuples.Pair;

import jade.wrapper.StaleProxyException;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
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
	private MapLoader _mapLoader;
	private final String _contextID = "TaxiTycoon";
	
	/* Graphical representation */
	private Grid<Object> _grid;
	
	/* SaJas Stuff */
	private ContainerController _mainContainer;

	public TaxiTycoonLauncher() {
		_mapLoader = new MapLoader("map.csv");
	}

	@Override
	public String getName() {
		return _contextID;
	}

	@Override
	protected void launchJADE() {
		_createRepresentation();
		_mainContainer = Runtime.instance().createMainContainer(null);
		launchAgents();
	}
	
	private void _createRepresentation(){
		/* Create grid */
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		_grid = gridFactory.createGrid("Grid", mainContext,
				new GridBuilderParameters<Object>(new repast.simphony.space.grid.StrictBorders(),
				new SimpleGridAdder<Object>(), true, _mapLoader.getMapSizeX(), _mapLoader.getMapSizeY()));
		
		/* Fill map with grass */
		for (int i=0; i < _mapLoader.getMapSizeX(); i++){
			for (int j=0; j < _mapLoader.getMapSizeX(); j++){
				Grass grass = new Grass(new Pair<Integer, Integer>(i,j));
				_grid.getAdder().add(_grid, grass);
				mainContext.add(grass);
				_grid.moveTo(grass, grass.getX(), grass.getY());
			}
		}
		
		/* Fill with roads */
		ArrayList<Pair<Integer, Integer>> roads = _mapLoader.getRoadPositions();
		for (Pair<Integer, Integer> roadPos : roads){
			Road road = new Road(roadPos);
			_grid.getAdder().add(_grid, road);
			mainContext.add(road);
			_grid.moveTo(road, road.getX(), road.getY());
		}
	}
	
	private void launchAgents(){
		try {
			/* Create taxis */
			for (int i = 0; i < _mapLoader.getTaxisCount(); i++) {
				_mainContainer.acceptNewAgent("Taxi[" + i + "]", new TaxiAgent(_grid, _mapLoader.getTaxiPosition(i), _mapLoader.getMapSize())).start();
			}
			/* Create passengers */
			for (int i = 0; i < _mapLoader.getPassengerCount(); i++) {
				_mainContainer.acceptNewAgent("Passenger[" + i+ "]", new PassengerAgent(_grid, _mapLoader.getPassengerPosition(i), _mapLoader.getMapSize())).start();
			}
			
		} catch (StaleProxyException e) {
			System.err.println("Connection with Jade lost.");
		}
	}
}
