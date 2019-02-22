package physics_simulator;
import java.util.ArrayList;
/**
 * The Draw class.
 * Draw mode keeps track of various settings used while drawing shapes,
 * including the number of vertices to give to shapes being drawn, and the
 * vertices of shapes that are currently in the process of being drawn.
 * 
 * @author Adam Cogen
 *
 */
public class Draw {
	//Shape draw settings:
	private int shapeDrawStep = 0; //what step of the drawing process is the user on? (how many times has the user clicked, which adds a vertex)
	private int numberOfVertices = 3; //how many vertices should shapes being drawn have? set to 3 in triangle draw mode, and 4 in quadrilateral draw mode
	private Map map; //the map that is currently being used
	private ArrayList<Shape> shapes; //the shape list, taken from the map
	private ArrayList<Point> currentShape; //a list of vertices that have been added to the shape that is currently in the process of being drawn

	/**
	 * Construct the Draw class, passing in as a parameter the map that is being used
	 * @param initMap the Map that the simulation is running
	 */
	public Draw(Map initMap){
		map = initMap;
		shapes = map.getShapeList();
		currentShape = new ArrayList<Point>();
	}

	public void drawModeClockTick(){
		/* 
		 * Do nothing. Most of the functionality used in Draw mode is invoked within the
		 * Window class, using data and methods stored within the Draw class.
		 * 
		 */
	}

	/**
	 * Add a vertex to the shape that is currenlty in the process of being drawn.
	 * This method is called when the user clicks while in draw mode. The point
	 * where the user's mouse was located when they clicked is added as a vertex,
	 * and immediately drawn.
	 * @param vertex The new Point to be added to the shape as a vertex (position is where the user clicked)
	 */
	public void addVertexToCurrentShape(Point vertex){
		currentShape.add(vertex);
		//keep allowing vertices to be added to the current shape, until the required number of vertices is reached
		if(currentShape.size() == numberOfVertices){
			//add the completed shape to the shapes list
			shapes.add(new Shape(currentShape));
			//clear the currentShape ArrayList<Point>, so that it can be used again for a new shape
			currentShape.clear();
		}
	}

	/**
	 * Increment the shapeDrawStep variable. This will be incremented whenever a 
	 * vertex is added to the shape that is currently being drawn, in order to keep
	 * track of how many vertices have been drawn. This method takes 
	 * mod(numberOfVertices) of the shapeDrawStep as it increments,  so that once the
	 * shapeDrawStep reaches the highest possible value, it wraps back around to zero,
	 * preparing for the start of a new shape.
	 */
	public void incrementShapeDrawStep(){
		shapeDrawStep = (shapeDrawStep + 1) % numberOfVertices;
	}

	/**
	 * Get a vertex from the shape that is currently being drawn, 
	 * at a specified index in the currentShape ArrayList<Point>.
	 * Used when displaying the partially drawn shape, in the
	 * Window class.
	 * @param index The index of the currently-being-drawn shape to get
	 * @return a point representing the vertex at the specified index in the currentShape ArrayList<Point>
	 */
	public Point getCurrentShapeVertex(int index){
		return currentShape.get(index);
	}

	/**
	 * Get the value that shapeDrawStep needs to reach for the shape to be complete.
	 * This is only to avoid confusion when trying to figure out when to stop 
	 * the shape drawing process.
	 * @return the maximum value that the shapeDrawStep can reach before resetting to zero
	 */
	public int getMaxShapeDrawStep(){
		return numberOfVertices - 1;
	}

	/**
	 * Return the current value of the shapeDrawStep variable.
	 * What step of the drawing process is the user currently 
	 * on? How many vertices have the drawn for the shape that
	 * is currently being draw?
	 * @return shapeDrawStep: the current step of the shape drawing process that the user is on
	 */
	public int getShapeDrawStep(){
		return shapeDrawStep;
	}

	/**
	 * Change the number of vertices to draw in new shapes.
	 * Will be set to 3 for triangle draw mode, and 4 for
	 * quadrilateral draw mode. Any other positive integers
	 * would also work correctly, but are not currently 
	 * available from within the simulator. 
	 * @param numVertices the number of vertices that new shapes should be drawn by the user should have
	 */
	public void setNumberOfVerticesToDraw(int numVertices){
		numberOfVertices = numVertices;
	}

	/*
	 * some notes:
	 * 
	 * edit panel button ideas:
	 * 		map settings
	 * 			keep ball within window boundaries
	 * 		draw shape (opens shape draw panel, which has colors, number of sides, etc)
	 * 		edit shapes (allows for click and drag movement of vertices and whole shapes)
	 * 		grid settings
	 * 			show grid?
	 * 			snap shapes to grid?
	 * 			grid resolution (how wide should each cell be? 2x2 or 3x3 minimum)
	 * 		help
	 * 		file
	 * 			open
	 * 			save
	 * 			load
	 * 			new
	 */

}
