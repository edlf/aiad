package taxitycoon;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;

public class TaxiTycoonBuilder implements ContextBuilder<Object> {
	private final String contextID = "TaxiTycoon";
	private int mapSizeX = 100, mapSizeY = 100;
	private int taxisCount = 5, passengerCount = 20;
	
	
	public TaxiTycoonBuilder() {
		
	}

	@Override
	public Context build(Context<Object> context) {
		context.setId(this.contextID);
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(),
				new WrapAroundBorders(), mapSizeX, mapSizeY);

		/* Create grid */
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new repast.simphony.space.grid.WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, mapSizeX, mapSizeY));
		
		/* Create Agents */
		for (int i=0; i < taxisCount; i++){
			context.add(new TaxiAgent(space, grid, 0, 0));
		}
		
		for (int i=0; i < passengerCount; i++){
			context.add(new PassengerAgent(space, grid,0 ,0));
		}
		
		/* ? */
		for (Object object : context){
			NdPoint ndPoint = space.getLocation(object);
			grid.moveTo(object, (int)ndPoint.getX(), (int)ndPoint.getY());
		}
		
		return context;
	}

}
