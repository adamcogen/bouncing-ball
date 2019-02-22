package physics_simulator;
import java.awt.Color;
import java.util.ArrayList;

/**
 * 
 * The Physics class. 
 * In physics mode, all balls in the simulation move and reflect
 * off of shapes in the simulation. They can be given new velocity
 * by clicking and dragging to draw velocity "vectors".
 * Handle clock ticks in physics mode.
 * This class also contains some useful static methods for tasks 
 * like conversion from radians to degrees and vice versa, as well
 * as a few variations of distance formulas, which may be useful in 
 * many places. 
 * 
 * @author Adam Cogen
 *
 */
public class Physics9 {

	private static final double GRAVITY_CONSTANT = -10; //constant that is proportional to the magnitude of acceleration due to gravity. recommended value is -10. 
	private static final double ACC_GRAVITY = GRAVITY_CONSTANT * -(0.005); //constant that will be used to represent acceleration due to gravity. it is recommended that instead of adjusting this parameter, you adjust the GRAVITY_CONSTANT to modify gravity in the simulation. default value for this is GRAVITY_CONSTANT * -(0.005)
	private static final double Y_REFLECTION_CONSTANT = .75; //whenever a ball collides with a wall, its velocity is multiplied by this constant in order to to slow the ball down after collisions, representing friction. also known as coefficient of restitution.
	private static final double X_REFLECTION_CONSTANT = .97;//.97; coefficient of restitution along x axis.
	private Point oldPosition; //the position of a ball at the beginning of a clock tick
	private Point testPosition; //a potential position for the ball after moving during a clock tick
	private ArrayList<Ball> players; //a list of each ball in the simulation
	private ArrayList<Shape> shapes; //a list of each shape (obstacle) in the simulation
	private Map map; //the Map instance that is currently running in the simulation
	//public static final int BALL_RADIUS = 5;
	boolean stuck = false;

	/**
	 * Constructor for the physics class.
	 * Retrieves the player and shape lists
	 * from the map class, and initializes
	 * necessary values in each player in
	 * the simulation.
	 */
	public Physics9(Map initMap){

		map = initMap;

		players = map.getPlayerList();
		shapes = map.getShapeList();

		//set the reflection constant of each ball in the simulation to that defined in the fields of the Physics class
		for(int i = 0; i < players.size(); i++){
			players.get(i).setYReflectionConstant(Y_REFLECTION_CONSTANT);
			players.get(i).setXReflectionConstant(X_REFLECTION_CONSTANT);
		}

	}

	/**
	 * On each clock tick in physics mode, check for collisions between
	 * each ball and each shape, and handle them appropriately.
	 */
	public void bounceModeClockTick(){
		//update positions for each ball in the players ArrayList one at a time by iterating through the list
		for (int i = 0; i < players.size(); i++){
			int currentPlayerRadius = players.get(i).getRadius();
			Segment path; //the potential path of the ball during this clock tick if no collision occurs
			Segment intersectingEdge; //the edge that has collided with the ball
			Point intersection; //the point of intersection between the ball and the intersectingEdge

			oldPosition = new Point(players.get(i).getXPosition(), (players.get(i).getYPosition()));
			players.get(i).setYVelocity(players.get(i).getYVelocity() + ACC_GRAVITY);
			testPosition = players.get(i).returnPotentialUpdatedPositionAsPoint();
			//oldPosition = findPointAlongLine(players.get(i).getVelocityVector(), oldPosition, (double) 5);
			//System.out.println(oldPosition);
			//start at start point of ball's path
			//draw segment perpendicular to each shape edge through center point of ball.
			//check if segment length is less than circle radius
			//if it is, a collision has occurred.
			//if it is not, move distance .01 along the line and check again.

			//start at start point of ball's path
			Point oldPositionIncrementing = oldPosition;
			path = new Segment(oldPosition, testPosition);
			Point previousPosition = oldPosition;

			ArrayList<Segment> allShapeEdges = new ArrayList<Segment>();
			Segment[] currentShapeEdges;
			ArrayList<Line> perpendiculars = new ArrayList<Line>();
			ArrayList<Double> edgeDistances = new ArrayList<Double>();
			ArrayList<Boolean> isCorner = new ArrayList<Boolean>();
			ArrayList<Double> ballDistances = new ArrayList<Double>();
			int closestEdgeDistanceIndex = -1;
			int closestBallDistanceIndex = -1;
			int count = 0;
			ArrayList<Shape> withinHitBox = new ArrayList<Shape>();
			while(shapes.size() != 0 && !oldPositionIncrementing.equals(testPosition, .01)) {
				int hb = 0;
				//instead of checking carefully for collision with all shapes,
				//only check carefully when the ball is within a shape's 
				//approximate area. determine whether this is the case by 
				//making a rectangle around
				//each shape (like this []) and checking if the ball is within that rectangle.
				//this saves a huge amount of time -- checking whether the ball is within
				//the rectangle only involves checking if it is within a certain x and y 
				//range, since the sides of the rectangle are parallel with the x and y axes.
				withinHitBox = new ArrayList<Shape>();
				while(shapes.size() != 0 && hb < shapes.size()) {
					double hbx = oldPositionIncrementing.getX();
					double hby = oldPositionIncrementing.getY();
					Shape tempshp = shapes.get(hb);
					//increase the size of the hit box by the ball's radius,
					//which will be faster than using the distance formula
					//but will have the same effect
					if(hbx > tempshp.getXMin() - currentPlayerRadius && hbx < tempshp.getXMax() + currentPlayerRadius && hby > tempshp.getYMin() - currentPlayerRadius && hby < tempshp.getYMax() + currentPlayerRadius) {
						withinHitBox.add(tempshp);
					}
					hb++;
				}
				//System.out.println(withinHitBox.size());
				allShapeEdges = new ArrayList<Segment>();
				perpendiculars = new ArrayList<Line>();
				edgeDistances = new ArrayList<Double>();
				closestEdgeDistanceIndex = -1;
				//check each shape in the shapes ArrayList for intersections with the proposed path of the ball (in other words, check for collisions with the ball)
				for(int j = 0; j < withinHitBox.size(); j++){
					//draw segment perpendicular to each shape edge through center point of ball.
					currentShapeEdges = withinHitBox.get(j).getEdges();
					//add current shape edges to list of all shape edges
					for(int k = 0; k < currentShapeEdges.length; k++) {
						allShapeEdges.add(currentShapeEdges[k]);
						//System.out.println(currentShapeEdges.length);
					}
					//make a list of all perpendicular lines between current shape edges and incrementing position
					for(int k = 0; k < currentShapeEdges.length; k++) {
						perpendiculars.add(currentShapeEdges[k].constructPerpendicularLine(oldPositionIncrementing));
					}
				}
				//check if segment length is less than circle (ball/player) radius
				//if it is, a collision has occurred.
				for(int l = 0; l < perpendiculars.size(); l++) {
					Point perpendicularIntersection = perpendiculars.get(l).getIntersectionWith(allShapeEdges.get(l));
					if(allShapeEdges.get(l).isIntersecting(perpendiculars.get(l), perpendicularIntersection)) {
						edgeDistances.add(distanceNoSqrt(oldPositionIncrementing, perpendicularIntersection));
					} else {
						edgeDistances.add(Double.MAX_VALUE);
					}
					isCorner.add(false);
				}

				//do the same type of thing again, but this time we will check for collisions with corners
				//start by making a list of lines perpendicular to the internal bisectors of all corners. 
				//corners exist between adjacent shape edges in the shapes' edge lists, as well as between the first and last edges in the lists
				//check if the ball is hitting the start/end points of any shape edges.
				//if the ball is hitting the start point, the corner is between this edge and the previous one.
				//if the ball is hitting the end point, the corner is between this edge and the next one.
				//find a line perpendicular to the interior bisector of this corner
				//refect the ball off of that line
				int cornerCollidingShape = -1;
				int firstCorner = -1;
				int secondCorner = -1;
				boolean firstCornerIsEnd = false; 
				int allShapesIndex = 0;
				for(int k = 0; k < withinHitBox.size(); k++) {
					for(int ka = 0; ka < withinHitBox.get(k).getNumberOfVertices(); ka++) {
						Segment currentEdge = withinHitBox.get(k).getEdges()[ka];
						double distanceFromEdgeStart = distanceNoSqrt(oldPositionIncrementing, currentEdge.getStartPoint());
						double distanceFromEdgeEnd = distanceNoSqrt(oldPositionIncrementing, currentEdge.getEndPoint());
						if(distanceFromEdgeStart <= currentPlayerRadius * currentPlayerRadius) {
							//we have a corner collision with the start point
							//take note of which two edges of which shape we are colliding with
							firstCornerIsEnd = false;
							cornerCollidingShape = k;
							firstCorner = ka;
							if(ka == 0) {
								secondCorner = withinHitBox.get(k).getNumberOfVertices() - 1;
							} else {
								secondCorner = ka - 1;
							}
							break; 
						} else if (distanceFromEdgeEnd <= currentPlayerRadius * currentPlayerRadius) {
							//we have a corner collision with the end point
							//take note of which two edges of which shape we are colliding with
							firstCornerIsEnd = true;
							cornerCollidingShape = k;
							firstCorner = ka;
							if(withinHitBox.get(k).getNumberOfVertices() == ka) {
								secondCorner = 0;
							} else {
								secondCorner = ka + 1;
							}
							break;
						}
						allShapesIndex++;
					}
					//find the line perpendicular to the corner's internal bisector. \|/ in this little ASCII picture, the line in the middle is the internal bisector to the the other two lines
					//start by finding the internal bisector
					if(cornerCollidingShape != -1) {
						Segment firstEdge = withinHitBox.get(cornerCollidingShape).getEdges()[firstCorner];
						Segment secondEdge = withinHitBox.get(cornerCollidingShape).getEdges()[secondCorner];
						Point corner;
						if(firstCornerIsEnd) {
							corner = firstEdge.getEndPoint();
						} else {
							corner = firstEdge.getStartPoint();
						}
						edgeDistances.set(allShapesIndex, distanceNoSqrt(oldPositionIncrementing, corner));
						isCorner.set(allShapesIndex, true);

					}
				}
				closestEdgeDistanceIndex = findIndexOfMinElement(edgeDistances);
				if(closestEdgeDistanceIndex != -1 && edgeDistances.get(closestEdgeDistanceIndex) <= currentPlayerRadius * currentPlayerRadius) {
					//collision. stop the ball from moving to this spot or further. 
					if(count == 0) { 
						//a value of count == 0 indicates that the ball's starting position was already colliding with an edge. 
						//this means we are at risk of getting stuck inside of a wall if not handled correctly, 
						//due to lack of precision in the double data type.
						//if the ball is stuck in a wall due to double precision errors (or at risk of having this happen), get the ball out of (away from) the wall
						//figure out the direction in which the ball needs to move to get further away from the closest edge
						//move the ball a tiny bit along that vector
						Point perpendicularIntersection = perpendiculars.get(closestEdgeDistanceIndex).getIntersectionWith(allShapeEdges.get(closestEdgeDistanceIndex));
						Point unstickVector = new Point(oldPositionIncrementing.getX() - perpendicularIntersection.getX(), oldPositionIncrementing.getY() - perpendicularIntersection.getY());
						previousPosition = findPointAlongLine(unstickVector, oldPositionIncrementing, .1);
					}
					break;
				}
				//if segment length is not less than radius, move distance .01 along the line and check again.
				previousPosition = oldPositionIncrementing;
				oldPositionIncrementing = findPointAlongLine(players.get(i).getVelocityVector(), oldPositionIncrementing, .01);
				count++;
			}

			if (closestEdgeDistanceIndex != -1 && edgeDistances.get(closestEdgeDistanceIndex) <= currentPlayerRadius * currentPlayerRadius ){
				//handle collision with a wall here. allShapeEdges.get(closestEdgeDistanceIndex()) is the closest wall to the ball, which we know is colliding with the ball
				intersection = previousPosition;
				intersectingEdge = allShapeEdges.get(closestEdgeDistanceIndex);
				double anglebtwn;
				if(isCorner.get(closestEdgeDistanceIndex)) {
					//handle corner collision here
					oldPositionIncrementing = previousPosition;
					players.get(i).setPosition(previousPosition);
					players.get(i).cornerReflection();
				} else {
					anglebtwn = path.angleBetween(intersectingEdge);
					oldPositionIncrementing = previousPosition;
					players.get(i).setPosition(previousPosition);
					players.get(i).angledReflection(anglebtwn);
				}

			} else {
				//no collision takes place along our potential path 
				players.get(i).updatePosition();
			}
		}
	}

	/**
	 * find the index of the smallest element in an ArrayList<Double>, the first time it appears
	 * @param list
	 * @return
	 */
	public int findIndexOfMinElement(ArrayList<Double> list) {
		int minIndex = -1;
		double minimum = Double.MAX_VALUE;
		double currentElement = 0;
		for(int i = 0; i < list.size(); i++) {
			currentElement = list.get(i);
			if(currentElement < minimum) {
				minimum = currentElement;
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * find the indices of the smallest element in an ArrayList<Double>, every time it appears
	 * @param list
	 * @return
	 */
	public ArrayList<Integer> findIndicesOfMinElement(ArrayList<Double> list) {
		ArrayList<Integer> minIndices = new ArrayList<Integer>();
		double minimum = Double.MAX_VALUE;
		double currentElement = 0;
		for(int i = 0; i < list.size(); i++) {
			currentElement = list.get(i);
			if(currentElement < minimum) {
				minimum = currentElement;
				minIndices.clear();
				minIndices.add(i);
			} else if(currentElement == minimum) {
				minIndices.add(i);
			}
		}
		return minIndices;
	}

	/**
	 * find the indices of the smallest element in an ArrayList<Double>, every time it appears
	 * @param list
	 * @return
	 */
	public ArrayList<Integer> findIndicesOfElementsLessThan(ArrayList<Double> list, double value) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		//double minimum = Double.MAX_VALUE;
		double currentElement = 0;
		for(int i = 0; i < list.size(); i++) {
			currentElement = list.get(i);
			//System.out.println(currentElement + " " + value);
			if(currentElement <= value) {
				//System.out.println("ya");
				indices.add(i);
			}
		}
		return indices;
	}

	/**
	 * Find the distance between two Points, but don't perform the square root operation involved in finding
	 * exact distance. The square root operation takes a long time. Exact distances can be compared by 
	 * squaring the other side of the equation (which is much faster than finding square root). 
	 */
	public static double distanceNoSqrt(Point point0, Point point1){
		double xDifference = point0.getX() - point1.getX();
		double yDifference = point0.getY() - point1.getY();
		return (xDifference * xDifference) + (yDifference * yDifference);
	}

	/**
	 * Return a Point with x and y values containing two indices specifying the closest pair out of 
	 * any two points in the specified ArrayList
	 */
	public Point closestPairBruteForce(ArrayList<Point> list){
		double closestDistance = Double.MAX_VALUE;
		Point closestPair = new Point(-1, -1);
		double tempDistance;
		for(int i = 0; i < list.size() - 1; i++){
			for(int j = i  + 1; j < list.size(); j++){
				tempDistance = distanceNoSqrt(list.get(i), list.get(j));
				if(tempDistance < closestDistance){
					closestDistance = tempDistance;
					closestPair = new Point(i, j);
				}
			}
		}
		return closestPair;
	}

	/**
	 * Find the closest pair from a specified point, to any point in a specified ArrayList of points
	 * @param list: An ArrayList containing Points 
	 * @param pointOfReference: A Point
	 * @return the index in the list, of the Point in the list that is closest to the Point pointOfReference
	 */
	public static ArrayList<Integer> indicesOfClosestPointBruteForce(ArrayList<Point> list, Point pointOfReference){
		double closestDistance = Double.MAX_VALUE;
		int indexOfClosestPoint = 0;
		double tempDistance;
		ArrayList<Double> distances = new ArrayList<Double>();
		ArrayList<Integer> results = new ArrayList<Integer>();
		for(int i = 0; i < list.size(); i++){
			tempDistance = distanceNoSqrt(list.get(i), pointOfReference);
			distances.add(tempDistance);
			if(tempDistance < closestDistance){
				closestDistance = tempDistance;
			}
		}
		for(int i = 0; i < list.size(); i++){
			if(distances.get(i) + 0.1 >= closestDistance && distances.get(i) - 0.1 <= closestDistance){
				results.add(i);
			}
		}
		return results;
	}

	/**
	 * Static method to convert an angle measurement from radians to degrees
	 * @param radians: a double of the value in radians to be converted to degrees
	 * @return the angle, converted from radians to degrees
	 */
	public static double radiansToDegrees(double radians){
		return (radians * 180) / Math.PI;
	}

	/**
	 * Static method to convert an angle measurement from degrees to radians
	 * @param degrees: a double of the value in degrees to be converted to radians
	 * @return the angle, converted from degrees to radians
	 */
	public static double degreesToRadians(double degrees){
		return (degrees * Math.PI) / 180;
	}

	/**
	 * Find the distance between two balls in the simulation.
	 * This method calls the distanceNoSqrt() method,
	 * and calculates the square root itself before
	 * returning the result. Thus, this method returns
	 * the true distance value between two balls.
	 * @param player0: 
	 * @param player1: 
	 * @return
	 */
	public static double distance(Ball player0, Ball player1){
		double distX = player1.getXPosition() - player0.getXPosition();
		distX *= distX;
		double distY = player1.getYPosition() - player0.getYPosition();
		distY *= distY;
		return Math.sqrt(distX + distY);
	}

	public static double distance(Point player0, Point player1){
		double distX = player1.getX() - player0.getX();
		distX *= distX;
		double distY = player1.getY() - player0.getY();
		distY *= distY;
		return Math.sqrt(distX + distY);
	}

	/**
	 * find a point a specified distance along a vector.
	 */
	public static Point findPointAlongLine(Point velocityVector, Point startingPoint, double distance) {
		Point vectorNorm = velocityVector.normalizeVector();
		return new Point(startingPoint.getX() + (distance * vectorNorm.getX()), startingPoint.getY() + (distance * vectorNorm.getY()));
	}

	/**
	 * find a point a specified distance along a line.
	 * this method assumes that the start point is on the line.
	 * if the start point is not on the line, then the resulting
	 * point will be the specified distance from the start point 
	 * at the same slope than the line, but not on the line.
	 */
	public static Point findPointAlongLine(Line line, Point startingPoint, double distance) {
		Point velocityVector;
		if(line.getSlopeType() == 2) { //undefined slope
			velocityVector = new Point(0, 1);
		} else {
			velocityVector = new Point(line.getSlope(), 1);
		}
		Point vectorNorm = velocityVector.normalizeVector();
		return new Point(startingPoint.getX() + (distance * vectorNorm.getX()), startingPoint.getY() + (distance * vectorNorm.getY()));
	}

	/**
	 * Reset the position and velocities of all balls in the simulation 
	 * to their initial positions and velocities.
	 */
	public void resetBalls(){
		for(int i = 0; i < players.size(); i++){
			players.get(i).reset();

		}
	}

	public static void main(String args []) {
		Point velocityVector = new Point(0, 10);
		Point intersection = new Point(0, 0);
		double distance = 10;
		System.out.println(findPointAlongLine(velocityVector, intersection, distance));
	}
}
