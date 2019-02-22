package physics_simulator;
import java.util.ArrayList;

/**
 * 
 * The Axis class, which stores and manipulates data representing an axis.
 * In this context, an Axis is like a Line, except that it doesn't
 * contain any specific points. Instead, it only has a slope, and its
 * actual position in the coordinate plane is not determined. 
 * For ease of implementation, an Axis is represented here as a Line
 * through an arbitrarily selected point. The coordinates of that point
 * don't affect the outcome of desired uses of an Axis. 
 * An axis keeps track of various projections that are made onto it. 
 * In other words, an axis is a line with a specified slope, which
 * be projected onto by Points, Segments, and Shapes.
 * The Axis class stores these projections, and can perform operations
 * to analyze the set of projections being made onto it.
 * 
 * @author Adam Cogen
 */
public class Axis extends Line {

	/*
	 * The ArrayList<Segment> projections is a list of the projections that have been 
	 * made onto this axis, represented as Segments. An axis doesn't have an actual 
	 * position in the coordinate plane, only a slope, so an arbitrary point is
	 * selected, and the Axis is represented as a Line made through that point.
	 */
	private ArrayList<Segment> projections;

	/**
	 * Construct an Axis object which has a slope, an indicator
	 * of whether the slope is 0, undefined, or standard.
	 * @param initSlopeType The slopeType of the axis. 0 is zero slope, 1 is standard slope, 2 is undefined.
	 * @param initSlope The slope of the axis. This will be disregarded if slopeType is 2
	 */
	public Axis(double initSlopeType, double initSlope){
		//Call the superclass constructor to construct a Line starting at an arbitrary 
		//point, with the specified slope for this axis. 
		//The location of the point does not matter, (100, 100) is essentially random.
		super(new Point(100,100), initSlopeType, initSlope);
		projections = new ArrayList<Segment>();
	}

	/**
	 * Add a projection to the projection list. 
	 * @param addedProjection The projection to be added to the projections list
	 */
	public void addProjection(Segment addedProjection){
		projections.add(addedProjection);
	}

	/**
	 * Return the list of projections that have been made onto this axis
	 * @return An ArrayList<Segment> containing all of the projections that have been made onto this axis.
	 */
	public ArrayList<Segment> getProjections(){
		return projections;
	}

	/**
	 * Check whether all projections on the Axis overlap each other. This is necessary in order to implement 
	 * the Separating Axis collision detection algorithm, which determines if the projections of two shapes'
	 * axes overlap each other.
	 * @return boolean: true if all projections overlap each other, false if any do not, or if there are 0 or 1 projections
	 */
	public boolean projectionsOverlap(){
		if(projections.size() <= 1){ //there are zero  or one projections, so no overlap is possible
			return false;
		} else { //iterate through all projections and check that each projection overlaps each other projection
			boolean result = true;
			outerloop: for(int i = 0; i < projections.size(); i++){
				for(int j = i + 1; j < projections.size(); j++){ //start at j = i + 1 to avoid redundancy and to avoid checking for overlaps with self
					if(!projections.get(i).isOverlapping(projections.get(j))){
						//stop checking immediately if you find any projections that do not overlap
						result = false;
						break outerloop;
					}
				}
			}
			return result;
		}
	}
}
