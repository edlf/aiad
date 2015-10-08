package taxitycoon;

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import groovy.lang.Closure;
import repast.simphony.relogo.*;
import repast.simphony.relogo.ast.Diffusible;
import repast.simphony.relogo.builder.GeneratedByReLogoBuilder;
import repast.simphony.relogo.builder.ReLogoBuilderGeneratedFor;
import repast.simphony.space.SpatialException;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

@GeneratedByReLogoBuilder
@SuppressWarnings({"unused","rawtypes","unchecked"})
public class ReLogoPatch extends BasePatch{

	/**
	 * Sprouts (makes) a number of new userTurtles and then executes a set of commands on the
	 * created userTurtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created userTurtles
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public AgentSet<taxitycoon.relogo.UserTurtle> sproutUserTurtles(int number, Closure closure) {
		AgentSet<taxitycoon.relogo.UserTurtle> result = new AgentSet<>();
		AgentSet<Turtle> createResult = this.sprout(number,closure,"UserTurtle");
		for (Turtle t : createResult){
			if (t instanceof taxitycoon.relogo.UserTurtle){
				result.add((taxitycoon.relogo.UserTurtle)t);
			}
		} 
		return result;
	}

	/**
	 * Sprouts (makes) a number of new userTurtles and then executes a set of commands on the
	 * created userTurtles.
	 * 
	 * @param number
	 *            a number
	 * @param closure
	 *            a set of commands
	 * @return created userTurtles
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public AgentSet<taxitycoon.relogo.UserTurtle> sproutUserTurtles(int number) {
		return sproutUserTurtles(number,null);
	}

	/**
	 * Returns an agentset of userTurtles from the patch of the caller.
	 * 
	 * @return agentset of userTurtles from the caller's patch
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public AgentSet<taxitycoon.relogo.UserTurtle> userTurtlesHere(){
	  Grid grid = getMyObserver().getGrid();
	  GridPoint gridPoint = grid.getLocation(this);
	  AgentSet<taxitycoon.relogo.UserTurtle> result = new AgentSet<taxitycoon.relogo.UserTurtle>();
	  for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"userTurtle")){
			if (t instanceof taxitycoon.relogo.UserTurtle)
			result.add((taxitycoon.relogo.UserTurtle)t);
		}
		return result;
	}

	/**
	 * Returns the agentset of userTurtles on the patch at the direction (ndx, ndy) from the
	 * caller.
	 * 
	 * @param nX
	 *            a number
	 * @param nY
	 *            a number
	 * @returns agentset of userTurtles at the direction (nX, nY) from the caller
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public AgentSet<taxitycoon.relogo.UserTurtle> userTurtlesAt(Number nX, Number nY){
		double dx = nX.doubleValue();
		double dy = nY.doubleValue();
		double[] displacement = {dx,dy};

		try{
		GridPoint gridPoint = Utility.getGridPointAtDisplacement(getGridLocationAsNdPoint(),displacement,getMyObserver());
		AgentSet<taxitycoon.relogo.UserTurtle> result = new AgentSet<taxitycoon.relogo.UserTurtle>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),"userTurtle")){
			if (t instanceof taxitycoon.relogo.UserTurtle)
			result.add((taxitycoon.relogo.UserTurtle)t);
		}
		return result;
		}
		catch(SpatialException e){
			return new AgentSet<taxitycoon.relogo.UserTurtle>();
		}
	}

	/**
	 * Returns an agentset of userTurtles on a given patch.
	 * 
	 * @param p
	 *            a patch
	 * @return agentset of userTurtles on patch p
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public AgentSet<taxitycoon.relogo.UserTurtle> userTurtlesOn(Patch p){
		AgentSet<taxitycoon.relogo.UserTurtle> result = new AgentSet<taxitycoon.relogo.UserTurtle>();						
		for (Turtle t : Utility.getTurtlesOnGridPoint(p.getGridLocation(),getMyObserver(),"userTurtle")){
			if (t instanceof taxitycoon.relogo.UserTurtle)
			result.add((taxitycoon.relogo.UserTurtle)t);
		}
		return result;
	}

	/**
	 * Returns an agentset of userTurtles on the same patch as a turtle.
	 * 
	 * @param t
	 *            a turtle
	 * @return agentset of userTurtles on the same patch as turtle t
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public AgentSet<taxitycoon.relogo.UserTurtle> userTurtlesOn(Turtle t){
		AgentSet<taxitycoon.relogo.UserTurtle> result = new AgentSet<taxitycoon.relogo.UserTurtle>();						
		for (Turtle tt : Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(t.getTurtleLocation()),getMyObserver(),"userTurtle")){
			if (tt instanceof taxitycoon.relogo.UserTurtle)
			result.add((taxitycoon.relogo.UserTurtle)tt);
		}
		return result;
	}

	/**
	 * Returns an agentset of userTurtles on the patches in a collection or on the patches
	 * that a collection of turtles are.
	 * 
	 * @param a
	 *            a collection
	 * @return agentset of userTurtles on the patches in collection a or on the patches
	 *         that collection a turtles are
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public AgentSet<taxitycoon.relogo.UserTurtle> userTurtlesOn(Collection c){

		if (c == null || c.isEmpty()){
			return new AgentSet<taxitycoon.relogo.UserTurtle>();
		}

		Set<taxitycoon.relogo.UserTurtle> total = new HashSet<taxitycoon.relogo.UserTurtle>();
		if (c.iterator().next() instanceof Turtle){
			for (Object o : c){
				if (o instanceof Turtle){
					Turtle t = (Turtle) o;
					total.addAll(userTurtlesOn(t));
				}
			}
		}
		else {
			for (Object o : c){
				if (o instanceof Patch){
					Patch p = (Patch) o;
					total.addAll(userTurtlesOn(p));
				}
			}
		}
		return new AgentSet<taxitycoon.relogo.UserTurtle>(total);
	}

	/**
	 * Queries if object is a userTurtle.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a userTurtle
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public boolean isUserTurtleQ(Object o){
		return (o instanceof taxitycoon.relogo.UserTurtle);
	}

	/**
	 * Returns an agentset containing all userTurtles.
	 * 
	 * @return agentset of all userTurtles
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public AgentSet<taxitycoon.relogo.UserTurtle> userTurtles(){
		AgentSet<taxitycoon.relogo.UserTurtle> a = new AgentSet<taxitycoon.relogo.UserTurtle>();
		for (Object e : this.getMyObserver().getContext().getObjects(taxitycoon.relogo.UserTurtle.class)) {
			if (e instanceof taxitycoon.relogo.UserTurtle){
				a.add((taxitycoon.relogo.UserTurtle)e);
			}
		}
		return a;
	}

	/**
	 * Returns the userTurtle with the given who number.
	 * 
	 * @param number
	 *            a number
	 * @return turtle number
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserTurtle")
	public taxitycoon.relogo.UserTurtle userTurtle(Number number){
		Turtle turtle = Utility.turtleU(number.intValue(), getMyObserver());
		if (turtle instanceof taxitycoon.relogo.UserTurtle)
			return (taxitycoon.relogo.UserTurtle) turtle;
		return null;
	}

	/**
	 * Queries if object is a userLink.
	 * 
	 * @param o
	 *            an object
	 * @return true or false based on whether the object is a userLink
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserLink")
	public boolean isUserLinkQ(Object o){
		return (o instanceof taxitycoon.relogo.UserLink);
	}

	/**
	 * Returns an agentset containing all userLinks.
	 * 
	 * @return agentset of all userLinks
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserLink")
	public AgentSet<taxitycoon.relogo.UserLink> userLinks(){
		AgentSet<taxitycoon.relogo.UserLink> a = new AgentSet<taxitycoon.relogo.UserLink>();
		for (Object e : this.getMyObserver().getContext().getObjects(taxitycoon.relogo.UserLink.class)) {
			if (e instanceof taxitycoon.relogo.UserLink){
				a.add((taxitycoon.relogo.UserLink)e);
			}
		}
		return a;
	}

	/**
	 * Returns the userLink between two turtles.
	 * 
	 * @param oneEnd
	 *            an integer
	 * @param otherEnd
	 *            an integer
	 * @return userLink between two turtles
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserLink")
	public taxitycoon.relogo.UserLink userLink(Number oneEnd, Number otherEnd) {
		return (taxitycoon.relogo.UserLink)(this.getMyObserver().getNetwork("UserLink").getEdge(turtle(oneEnd),turtle(otherEnd)));
	}

	/**
	 * Returns the userLink between two turtles.
	 * 
	 * @param oneEnd
	 *            a turtle
	 * @param otherEnd
	 *            a turtle
	 * @return userLink between two turtles
	 */
	@ReLogoBuilderGeneratedFor("taxitycoon.relogo.UserLink")
	public taxitycoon.relogo.UserLink userLink(Turtle oneEnd, Turtle otherEnd) {
		return userLink(oneEnd.getWho(), otherEnd.getWho());
	}


}