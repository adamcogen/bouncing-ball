package physics_simulator;
import java.awt.Color;
import java.util.ArrayList;
/**
 * The Ball class. 
 * Stores information about a ball, such as its position, velocity, and color.
 * The simulation keeps track of a list of balls. 
 * @author Adam Cogen
 *
 */
public class Ball {

	private double yVelocity; //the x component of the ball's velocity vector
	private double xVelocity; //the y component of the ball's velocity vector
	private double yPosition; //the y position of the ball
	private double xPosition; //the x position of the ball
	/*
	 * is the ball currently frozen in place? 
	 * When the player clicks to draw a velocity vector
	 * while the simulation is running, the ball freezes in place 
	 * until the mouse is released, at which point the ball unfreezes,
	 * and the drawn velocity vector is given to the ball, replacing its 
	 * old velocity.
	 */
	private boolean frozen;
	/*
	 * double yReflectionConstant:
	 * each time the ball bounces, the y velocity of the ball is multiplied by 
	 * this constant, to immitate loss of velocity due to friction. the value 
	 * declared using the setReflectionConstant() method, called in the Physics class
	 */
	private double yReflectionConstant;
	private double xReflectionConstant; //same as yReflectionConstant, but for x velocity
	private Color color; //the color of the ball
	/*
	 * ArrayList<Segment> intersectingEdges:
	 * a list of the each edge that is intersecting with the path of the ball within the 
	 * current clock tick. this list is populated at each clock tick, by the Physics class.
	 */
	private ArrayList<Segment> intersectingEdges = new ArrayList<Segment>();
	/*
	 * ArrayList<Point> intersections:
	 * Similar to the intersectingEdges ArrayList, but instead of actual edges, this list
	 * contains the point of intersection between this ball's path in the current clock tick,
	 * and each edge in the intersectingEdges list. corresponding edges and points of
	 * intersection are stored in each list at the same indices.
	 */
	private ArrayList<Point> pointsOfIntersection = new ArrayList<Point>();
	private double initialXPosition; //this ball's starting x position in the simulation (used whenever the "reset balls" button is pressed)
	private double initialYPosition; //this ball's starting y position in the simulation (used whenever the "reset balls" button is pressed)
	private double initialXVelocity; //this ball's starting x velocity in the simulation (used whenever the "reset balls" button is pressed)
	private double initialYVelocity; //this ball's starting y velocity in the simulation (used whenever the "reset balls" button is pressed)
	private int radius = 5;
	
	/**
	 * Constructor that initializes a ball in the simulation.
	 * @param initXPos the starting x position of the ball
	 * @param initYPos the starting y position of the ball
	 * @param initXVel the starting x velocity of the ball
	 * @param initYVel the starting y velocity of the ball
	 * @param initColor the color of the ball
	 */
	public Ball(double initXPos, double initYPos, double initXVel, double initYVel, Color initColor){
		frozen = false;
		xPosition = initialXPosition = initXPos;
		yPosition = initialYPosition = initYPos;
		xVelocity = initialXVelocity = initXVel;
		yVelocity = initialYVelocity = initYVel;
		color = initColor;
	}

	/**
	 * Reset the velocity and position of this ball to the initial velocity and position.
	 * Initial velocity and position are the velocity and position that are stored in the
	 * map file, where the ball starts when the map is first opened. 
	 */
	public void reset(){
		resetIntersectionLists();
		xPosition = initialXPosition;
		yPosition = initialYPosition;
		xVelocity = initialXVelocity;
		yVelocity = initialYVelocity;
	}

	/**
	 * Return the ArrayList<Segment> that contains all
	 * of the edges that the ball's path intersects
	 * with within the current clock tick.
	 * @return a list of the edges that the ball's path intersects with within the current clock tick
	 */
	public ArrayList<Segment> getIntersectingEdges(){
		return intersectingEdges;
	}

	/**
	 * Return the ArrayList<Point> that contains all of
	 * points of intersection between the ball's path and
	 * each edge that intersects it within the current clock tick.
	 * @return a list of the points of intersection between the ball's path and the shape edges it 
	 * 		   intersects with within the current clock tick.
	 */
	public ArrayList<Point> getPointsOfIntersection(){
		return pointsOfIntersection;
	}
	
	/**
	 * Add a Segment to the ArrayList<Segment> intersectingEdges,  
	 * which contains all of the edges that the ball's path 
	 * intersects with within the current clock tick.
	 * @param edge the shape edge that the ball's path intersects with within the current clock tick
	 */
	public void addIntersectingEdgeToList(Segment edge){
		intersectingEdges.add(edge);
	}
	
	/**
	 * Add a point of intersection to the pointsOfIntersection ArrayList<Point>, 
	 * which contains all of points of intersection between the ball's 
	 * path and each edge that intersects it within the current clock tick.
	 * @param inter the point of intersection to add to the pointsOfIntersection list
	 */
	public void addPointOfIntersectionToList(Point inter){
		pointsOfIntersection.add(inter);
	}

	/**
	 * Add a multiple Segments to the ArrayList<Segment> intersectingEdges,  
	 * which contains all of the edges that the ball's path 
	 * intersects with within the current clock tick.
	 * @param edges an ArrayList<Segment> containing the shape edges that the 
	 * 		  ball's path intersects with within the current clock tick
	 */
	public void addMultipleIntersectingEdgesToList(ArrayList<Segment> edges){
		intersectingEdges.addAll(edges);
	}

	/**
	 * Clear the pointsOfIntersection and intersectingEdges list.
	 * This method is called at the end of every clock tick, so 
	 * that these lists can be filled with new intersection data 
	 * on the next clock tick. This method is also cleared 
	 * whenever the balls are reset using the "reset balls"
	 * button, just to ensure that no invalid intersection 
	 * data remains in the lists.
	 */
	public void resetIntersectionLists(){
		pointsOfIntersection.clear();
		intersectingEdges.clear();
	}

	/**
	 * Set the value for the yReflectionConstant. 
	 * See the comments for the yReflectionConstant
	 * field for information about what this means.
	 * This method is called, for each ball, by the 
	 * Physics class at the start of the simulation.
	 * @param ref the value for the y reflection constant
	 */
	public void setYReflectionConstant(double ref){
		yReflectionConstant = ref;
	}

	/**
	 * Set the value for the xReflectionConstant. 
	 * See the comments for the xReflectionConstant
	 * field for information about what this means.
	 * This method is called, for each ball, by the 
	 * Physics class at the start of the simulation.
	 * @param ref the value for the x reflection constant
	 */
	public void setXReflectionConstant(double ref){
		xReflectionConstant = ref;
	}

	/**
	 * Return the ball's y velocity
	 * @return the ball's y velocity
	 */
	public double getYVelocity(){
		return yVelocity;
	}

	/**
	 * Return the ball's x velocity
	 * @return the ball's x velocity
	 */
	public double getXVelocity(){
		return xVelocity;
	}

	/**
	 * Set the ball's x velocity to the specified value
	 * @param newVel the new x velocity for the ball to have
	 */
	public void setXVelocity(double newVel){
		xVelocity = newVel;
	}

	/**
	 * Set the ball's y velocity to the specified value
	 * @param newVel the new y velocity for the ball to have
	 */
	public void setYVelocity(double newVel){
		yVelocity = newVel;
	}

	/**
	 * Set the ball's x position to the specified value
	 * @param newPos the new x position for the ball
	 */
	public void setXPosition(double newPos){
		xPosition = newPos;
	}

	/**
	 * Set the ball's y position to the specified value
	 * @param newPos the new y position for the ball
	 */
	public void setYPosition(double newPos){
		yPosition = newPos;
	}

	/**
	 * Return the ball's x position
	 * @return the ball's x position
	 */
	public double getXPosition(){
		return xPosition;
	}

	/**
	 * Return the ball's y position
	 * @return the ball's y position
	 */
	public double getYPosition(){
		return yPosition;
	}

	/**
	 * Freeze the ball in place.
	 * This is done when the player clicks to draw a velocity vector
	 * while the simulation is running. The ball freezes in place 
	 * until the mouse is released, at which point the drawn velocity
	 * vector is applied to the ball, replacing its old velocity.
	 */
	public void freeze(){
		frozen = true;
	}

	/**
	 * Unfreeze the ball.
	 * When the player clicks to draw a velocity vector
	 * while the simulation is running, the ball freezes in place 
	 * until the mouse is released, at which point the ball unfreezes,
	 * and the drawn velocity vector is given to the ball, replacing its 
	 * old velocity.
	 */
	public void unfreeze(){
		frozen = false;
	}

	/**
	 * Set the ball's position, by specifying both an x and a y 
	 * coordinate for it.
	 * @param newX the new x position for the ball
	 * @param newY the new y position for the ball
	 */
	public void setPosition(double newX, double newY){
		xPosition = newX;
		yPosition = newY;
	}

	/**
	 * Set the ball's position, by specifying a Point that 
	 * describes the ball's new position.
	 * @param newPos a Point describing the new position for the ball
	 */
	public void setPosition(Point newPos){
		xPosition = newPos.getX();
		yPosition = newPos.getY();
	}

	/**
	 * Return the color of this ball
	 * @return a java.awt.Color instance describing the color of this ball
	 */
	public Color getColor(){
		return color;
	}

	/**
	 * Reflect the ball off of an angled surface by changing its velocity accordingly.
	 * @param angleBetween the angle between the ball's path and the wall the ball has intersected with
	 */
	public void angledReflection(double angleBetween){
		Point currentVelocityRespresentedAsPoint = new Point(xVelocity, yVelocity);
		Point velocityAfterReflection = currentVelocityRespresentedAsPoint.calculateVectorReflection(angleBetween);
		xVelocity = velocityAfterReflection.getX() * xReflectionConstant;
		yVelocity = velocityAfterReflection.getY() * yReflectionConstant;
	}
	
	/**
	 * Reflect the ball off of an angled surface by changing its velocity accordingly.
	 * @param angleBetween the angle between the ball's path and the wall the ball has intersected with
	 */
	public void cornerReflection(){
		xVelocity *= -xReflectionConstant;
		yVelocity *= -yReflectionConstant;
	}

	/**
	 * Obsolete method for finding the ball's new velocity after 
	 * a reflection off of a flat (zero slope) ceiling.
	 * This method still works, but it is not needed, since the
	 * angledReflection() method handles this case perfectly well.
	 */
	public void ceilingReflection(){
		xVelocity *= xReflectionConstant;
		yVelocity = ((yReflectionConstant) * yVelocity);
	}

	/**
	 * Obsolete method for finding the ball's new velocity after 
	 * a reflection off of a flat (undefined slope) wall.
	 * This method still works, but it is not needed, since the
	 * angledReflection() method handles this case perfectly well.
	 */
	public void wallReflection(){
		xVelocity = ((xReflectionConstant) * xVelocity);
		yVelocity *= yReflectionConstant;
	}

	/**
	 * Return the current position of the ball, as a Point.
	 * @return a Point describing the current position of the ball.
	 */
	public Point getPositionAsPoint(){
		return new Point(xPosition, yPosition);
	}

	/**
	 * Add the x and y components of the ball's velocity to its 
	 * x and y position, respectively. This happens during each 
	 * clock tick while the simulation is running, as long as 
	 * no collision occurs (and ball is not frozen). See the 
	 * Physics class for information about how collisions are 
	 * handled, how exactly position is updated, etc. 
	 */
	public void updatePosition(){
		if(!frozen){
			xPosition += xVelocity;
			yPosition += yVelocity;
		}
	}

	/**
	 * Return a Point describing where the ball WOULD be IF its 
	 * position were updated. The path between the ball's current
	 * position and its potential new position can then be tested
	 * for intersections with walls, etc.
	 * @return a Point describing where the ball would be if its position were updated
	 */
	public Point returnPotentialUpdatedPositionAsPoint(){
		if(!frozen){
			return new Point(xPosition + xVelocity, yPosition + yVelocity);
		} else {
			return getPositionAsPoint();
		}
	}

	/**
	 * Return the initial position of the ball as a Point.
	 * This is used whenever the "reset balls" button is pressed;
	 * the ball's position is reset to the Point returned by this
	 * method.
	 * @return the position that the balls should have when they are reset
	 */
	public Point getInitialPosition(){
		return new Point(initialXPosition, initialYPosition);
	}

	/**
	 * Return the initial velocity of the ball as a Point,
	 * with the x component of velocity as the x value of 
	 * the point, and the y component of velocity as the y
	 * value of point.
	 * This is used whenever the "reset balls" button is pressed;
	 * the ball's velocity is reset to the values returned by this
	 * method.
	 * @return the velocity that the balls should have when they are reset
	 */
	public Point getInitialVelocity(){
		return new Point(initialXVelocity, initialYVelocity);
	}
	
	public Point getVelocityVector() {
		return new Point(xVelocity, yVelocity);
	}
	
	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int initRadius) {
		radius = initRadius;
	}
	
	public void setVelocity(Point velocityVector) {
		xVelocity = velocityVector.getX();
		yVelocity = velocityVector.getY();
	}

}