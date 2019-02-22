package physics_simulator;
import java.util.ArrayList;

/**
 * The Shape class.
 * Store and manipulate data representing a shape, which is made of vertices (Points) and edges (Segments).
 * 
 * @author Adam Cogen
 *
 */
public class Shape {

	private int numberOfVertices; //the number of vertices in the shape
	private Point[] vertices; //a list of all the vertices of the shape, stored as Points
	private Segment[] edges; //a list of all of the edges of the shape, stored as Segments
	private ArrayList<Axis> perpendicularAxes; //a list containing the perpendicular axes to each edge in this shape. read the Axis class for more information
	private double xMax; //largest vertex x value
	private double xMin; //smallest vertex x value
	private double yMax; //largest vertex y value
	private double yMin; //smallest vertex y value



	/**
	 * Initialize a Shape by passing in an array of Points that represent each vertex. 
	 * @param initVertices Point[]: an array containing Points, representing the vertices of the Shape
	 */
	public Shape(Point[] initVertices){
		numberOfVertices = initVertices.length;
		vertices = initVertices;
		initializeEdges();
		initializeAxes();
		initializeHitBox();
	}

	/**
	 * Initialize a Shape by passing in an ArrayList<Point>, each Point representing a vertex of the Shape.
	 * @param initVertices: an ArrayList<Point>, with each Point representing a vertex of the Shape
	 */
	public Shape(ArrayList<Point> initVertices){
		numberOfVertices = initVertices.size();
		vertices = new Point[numberOfVertices];
		for(int i = 0; i < numberOfVertices; i++){
			vertices[i] = initVertices.get(i);
		}
		initializeEdges();
		initializeAxes();
		initializeHitBox();
	}

	public Segment[] getEdges() {
		return edges;
	}

	/**
	 * Modify this Shape by moving the specified 
	 * vertex to the specified new Point.
	 * Update the Shape's edges and axes to match the new vertex position.
	 * @param vertexIndex the index of the vertex in the vertices[] array to move
	 * @param newPosition a Point representing the new position of that vertex
	 */
	public void moveVertex(int vertexIndex, Point newPosition){
		vertices[vertexIndex] = newPosition;
		initializeEdges();
		initializeAxes();
		initializeHitBox();
	}

	/**
	 * Move this Shape. The distance to move it is determined by specifying a vertex index in the Shape,
	 * and specifying the new location of that vertex, as a Point. The rest of the vertices are then 
	 * moved to be consistent with the difference between the specified vertex's original position and 
	 * its new location. The edges and axes are then reinitialized to be consistent with the Shape's new
	 * location. 
	 * This seemingly strange implementation makes sense in the context of clicking and dragging a single
	 * vertex to move a Shape, as is done in edit --> move shape mode.
	 * @param selectedVertexIndex the index  in the vertices[] array of the selected vertex
	 * @param newPosition a Point representing the new position for the vertex
	 */
	public void moveShape(int selectedVertexIndex, Point newPosition){
		double xChange = newPosition.getX() - vertices[selectedVertexIndex].getX();
		double yChange = newPosition.getY() - vertices[selectedVertexIndex].getY();
		for(int i = 0; i < vertices.length; i++){
			vertices[i].setX(vertices[i].getX() + xChange);
			vertices[i].setY(vertices[i].getY() + yChange);
		}
		initializeEdges();
		initializeAxes();
		initializeHitBox();
	}

	/**
	 * Define the edges representing this Shape.
	 * Add them to the edges[] array.
	 */
	public void initializeEdges(){
		edges = new Segment[numberOfVertices];
		for(int i = 0; i < numberOfVertices; i++){
			if(i < numberOfVertices - 1){
				edges[i] = new Segment(vertices[i], vertices[i + 1]);
			} else {
				edges[numberOfVertices - 1] = new Segment(vertices[numberOfVertices - 1], vertices[0]);
			}
		}
	}

	/**
	 * Find perpendicular axes for each edge in this Shape,
	 * and add them to the axes ArrayList<Axis> field.
	 */
	public void initializeAxes(){
		perpendicularAxes = new ArrayList<Axis>();
		for(int i = 0; i < edges.length; i++){
			perpendicularAxes.add(edges[i].constructPerpendicularAxis());
		}
	}

	/**
	 * Return an ArrayList<Axis> containing all of the perpendicular axes of this Shape.
	 * Used for collision detection with Separating Axis Theorem. See the Axis class for
	 * more information.
	 * @return Return an ArrayList<Axis> containing all of the perpendicular axes of this Shape
	 */
	public ArrayList<Axis> getAxes(){
		return perpendicularAxes;
	}

	/**
	 * return the number of vertices in the Shape
	 * @return
	 */
	public int getNumberOfVertices(){
		return numberOfVertices;
	}

	/**
	 * return an array of ints which contains all of the x coordinates for the vertices of the Shape
	 * @return
	 */
	public int[] getXCoordinates(){
		int[] x = new int[numberOfVertices];
		for (int i = 0; i < numberOfVertices; i++){
			x[i] = (int) vertices[i].getX();
		}
		return x;
	}

	/**
	 * return an array of ints which contains all of the y coordinates for the vertices of the Shape
	 * @return
	 */
	public int[] getYCoordinates(){
		int[] y = new int[numberOfVertices];
		for (int i = 0; i < numberOfVertices; i++){
			y[i] = (int) vertices[i].getY();
		}
		return y;
	}

	/**
	 * Return a list of all of the edges in this Shape that intersect with the 
	 * Segment that is passed in as an argument.
	 * In other words, imagine that this shape is an obstacle, and the Segment seg0 is a ball path.
	 * Check if that path intersects with an of the edges in this shape.
	 * @param seg0 the Segment to check for intersections with
	 * @return An ArrayList<Segment> containing all of the edges of this shape that intersect with line seg0
	 */
	public ArrayList<Segment> edgeIntersects(Segment seg0){
		Point intersection; //initialize a Point that will temporarily hold the values of points of intersection while iterating through each edge
		ArrayList<Segment> list = new ArrayList<Segment>(); //initialize a list that will contain all of the intersections that are deemed valid
		for(int i = 0; i < edges.length; i++){ //iterate through all of the edges in order to check for intersections
			intersection = edges[i].getIntersectionWith(seg0); //set the intersection local variable as the intersection between seg0 and the edge at edges[i]
			if(edges[i].isIntersecting(seg0, intersection)){ //check if the intersection found above is valid (within the ranges of each segment. read the segment class for more info)
				list.add(edges[i]); //add any valid intersections to the intersections list
			}
		}
		return list;
	}

	/**
	 * Project this entire shape onto a specified Line
	 * @param Line axis: the Line to reflect the Shape onto
	 * @return Segment: the projection, represented by a Segment, which falls on the specified axis
	 */
	public Segment projectOntoLine(Line axis){
		/*
		 * ************************************************
		 * to do: improve the comments for this method
		 * ************************************************
		 */
		ArrayList<Point> projectionList = new ArrayList<Point>();
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		for(int i = 0; i < vertices.length; i++){
			projectionList.add(vertices[i].projectOntoLine(axis));
			if(projectionList.get(i).getX() < projectionList.get(minX).getX()){
				minX = i;
			} else if (projectionList.get(i).getX() > projectionList.get(maxX).getX()) {
				maxX = i;
			}
			if(projectionList.get(i).getY() < projectionList.get(minY).getY()){
				minY = i;
			} else if (projectionList.get(i).getY() > projectionList.get(maxY).getY()){
				maxY = i;
			}
		}
		Point startPoint;
		Point endPoint;
		if(axis.getSlopeType() == 0){
			startPoint = projectionList.get(minX);
			endPoint = projectionList.get(maxX);
		} else if (axis.getSlopeType() == 2){
			startPoint = projectionList.get(minY);
			endPoint = projectionList.get(maxY);
		} else { //axis is a standard-slope line, slopeType is 1
			if((projectionList.get(maxX).getX() - projectionList.get(minX).getX()) >= (projectionList.get(maxY).getY() - projectionList.get(minY).getY())){	
				startPoint = projectionList.get(minX);
				endPoint = projectionList.get(maxX);
			} else {
				startPoint = projectionList.get(minY);
				endPoint = projectionList.get(maxY);
			}
		}
		return new Segment(startPoint, endPoint);
	}

	/**
	 * Return a Point representing the vertex at a specified index in the vertices[] array
	 * @param index The index of the Point to return, from the vertices[] array
	 * @return a Point representing the vertex at a specified index in the vertices[] array
	 */
	public Point getVertex(int index){
		return vertices[index];
	}

	private void initializeHitBox() {
		double tempX;
		double tempY;
		for(int i = 0; i < vertices.length; i++) {
			if(i == 0) {
				xMin = vertices[i].getX();
				yMin = vertices[i].getY();
				xMax = vertices[i].getX();
				yMax = vertices[i].getY();
			} else {
				tempX = vertices[i].getX();
				tempY = vertices[i].getY();
				if(tempX < xMin) {
					xMin = tempX;
				} else if(tempX > xMax) {
					xMax = tempX;
				}

				if(tempY < yMin) {
					yMin = tempY;
				} else if(tempY > yMax) {
					yMax = tempY;
				}
			}
		}
	}
	
	public double getXMin() {
		return xMin;
	}
	
	public double getYMin() {
		return yMin;
	}
	
	public double getXMax() {
		return xMax;
	}
	
	public double getYMax() {
		return yMax;
	}

}
