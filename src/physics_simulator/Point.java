package physics_simulator;

/**
 * Point class, which stores and manipulates data specifying a Point
 * in the Cartesian coordinate plane, with an x and a y value.
 * 
 * @author Adam Cogen
 */
public class Point implements Comparable<Object>{

	private double x; //x coordinate of the point
	private double y; //y coordinate of the point

	/**
	 * Constructor. Initialize the x and y coordinate values
	 * @param initX double: value for x coordinate
	 * @param initY double: value for y coordinate
	 */
	public Point(double initX, double initY){
		x = initX;
		y = initY;
	}

	/**
	 * Set the x value of the point to a specified value
	 * @param newX the new x value for the point
	 */
	public void setX(double newX){
		x = newX;
	}

	/**
	 * Set the y value of the point to a specified value
	 * @param newY the new y value for the point
	 */
	public void setY(double newY){
		y = newY;
	}

	/**
	 * Default constructor. 
	 * Set the coordinates of the point to the origin (0,0).
	 */
	public Point(){
		x = 0;
		y = 0;
	}

	/**
	 * return value of x coordinate
	 * @return double: x value
	 */
	public double getX(){
		return x;
	}

	/**
	 * return value of y coordinate
	 * @return double: y value
	 */
	public double getY(){
		return y;
	}

	/**
	 * Switch the x and y coordinate values. 
	 * This can be used in a sort method 
	 * so that points can be sorted by y value,
	 * despite the fact that the compareTo() method
	 * only compares x values.
	 * To sort by y value, switch the x and y values, 
	 * sort using the normal compareTo() method which
	 * compares by x value, then switch x and y 
	 * values again. 
	 */
	public void switchXY(){
		double temp = x;
		x = y;
		y = temp;
	}

	/**
	 * 
	 * This method uses a rotational matrix to rotate a Point a specified number of degrees
	 * about a specified origin. 
	 * Returns a Point with the new rotated coordinates.
	 * 
	 * Thanks to Physics 131 TA Hieu Le for telling me about rotational matrices.
	 * 
	 * @param degrees double: the number of degrees to rotate the point about the origin
	 * @param origin Point: the center point about which to rotate.
	 * @return Point: a point with rotated coordinates
	 */
	public Point rotate(double degrees, Point origin){
		/*
		 * offset point so that it is at (0,0), since
		 * rotational matrix only works about the origin (0,0).
		 * the point will be put back where it belongs later.
		 */
		double tempResultX = x - origin.getX();
		double tempResultY = y - origin.getY();
		/*
		 * convert degrees to radians, since Math library trigonometric functions
		 * take angles in radians as parameters.
		 */
		double radians = Physics9.degreesToRadians(degrees); 
		/*
		 * *************************************************************************************
		 * trigonometry for rotating a Point about the origin (0,0), using a rotational matrix:
		 * *************************************************************************************
		 */
		double resultX = (tempResultX * Math.cos(radians) - tempResultY * Math.sin(radians));
		double resultY = (tempResultX * Math.sin(radians) + tempResultY * Math.cos(radians));
		/*
		 * move rotated point back from (0,0) to where it belongs
		 */
		resultX += origin.getX();
		resultY += origin.getY();
		return new Point(resultX, resultY);
	}

	/**
	 * Compare Points by x value.
	 * To compare by y value, use the switchXY() method,
	 * then compare the points using this compareTo(), 
	 * then use switchXY() again.
	 */
	@Override
	public int compareTo(Object o) {
		//Returns a negative integer, zero, or a positive integer if this 
		//object's x value is less than, equal to, or greater than the specified object's, 
		//respectively
		if( x < ((Point) o).getX()){
			return - 1;
		} else if (x > ((Point) o).getX()){
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Project this Point onto a Line
	 * @param axis: the Line to project the Point onto
	 * @return Point: the projection of this Point onto the axis Line
	 */
	public Point projectOntoLine(Line axis){
		return axis.getIntersectionWith(axis.constructPerpendicularLine(this));
	}

	/**
	 * This method is used when calculating the new velocity for a ball
	 * after it reflects off of a wall. This is done by treating the 
	 * x and y components of the ball's velocity as a vector. X velocity
	 * is used as the x coordinate of a point, and y velocity is used as
	 * the y coordinate of that point. Then, the point is rotated the
	 * correct number of degrees about the origin (0,0). This has the
	 * same effect as rotating a vector to determine the right velocity
	 * for a ball after reflecting off of a wall at a certain angle.
	 * @param double anglebtwn: the angle between the balls path and the 
	 * 							wall it hit. will be used in calculations.
	 * @return Point: 	a Point with x representing x component of velocity, 
	 * 					and y representing y component, for an appropriately 
	 * 					rotated velocity "vector"
	 */
	public Point calculateVectorReflection(double anglebtwn){
		Point origin = new Point(0,0);
		Point ref;
		/*
		 * The next few lines of code prevent a reflection of less than 10 degrees
		 * (but reflections of exactly 0 degrees are still allowed).
		 * Any reflection with a magnitude smaller than 10 degrees has its sign
		 * preserved, but its magnitude is bumped up to 10. This is to account for
		 * slight imprecision that can allow some reflections to be miscalculated,
		 * causing a ball moving at a very slight angle to be accidentally reflected 
		 * through an edge.
		 */
		if (anglebtwn < 10 && anglebtwn > 0){
			anglebtwn = 10;
		}
		if (anglebtwn > -10 && anglebtwn < 0){
			anglebtwn = -10;
		}
		//perform the appropriate rotation of the vector
		if (anglebtwn != 0){ //case 1: angle between is not zero, rotate about origin.
			ref = rotate(anglebtwn * -2, origin);
		} else { //case 2: angle between is 0, don't rotate
			ref = this;
		}
		return ref;
	}
	
	/**
	 * Check whether two Points are exactly equal to each other.
	 * This method won't always return the right result when expected,
	 * due to lack of precision in the double data-type. To account for this,
	 * use the method equals(Object o, double tolerance) instead. 
	 */
	public boolean equals(Object o) {
		if(x == ((Point) o).getX() && y == ((Point) o).getY()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * An equals method for Points that takes lack of double data-type precision
	 * into account by including a tolerance parameter. the points will be considered
	 * equal if the difference between each of their components is less than or equal
	 * to the tolerance. e.g. if the tolerance is 0.1, the x components 1, 1.084, 1.09, 
	 * and 1.1 will all be considered equal to each other, but the x components 1 and 
	 * 1.11 will not be considered equal. 
	 * @param o
	 * @param tolerance
	 * @return
	 */
	public boolean equals(Object o, double tolerance) {
		if(Math.abs(x - ((Point) o).getX()) <= tolerance && Math.abs(y - ((Point) o).getY()) <= tolerance) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Treat this point as a vector, and normalize it
	 * by dividing each of the vector's components by the vector's magnitude. 
	 * @return a Point representing the normalized vector
	 */
	public Point normalizeVector() {
		double magnitude = getVectorMagnitude();
		return new Point(x / magnitude, y / magnitude);
	}
	
	public double getVectorMagnitude() {
		return Math.sqrt((x * x) + (y * y));
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	/**
	 * Construct a normalized vector from two points,
	 * i.e. it will have a length of one but it will point in the direction
	 * from the first point to the second point
	 * @param p0
	 * @param p1
	 * @return
	 */
	public static Point constructNormalizedVectorFromTwoPoints(Point p0, Point p1) {
		Point tempVector = new Point(p1.getX() - p0.getX(), p1.getY() - p0.getY());
		return tempVector.normalizeVector();
	}

	/**
	 * The main method.
	 * Currently runs some tests on the calculateVectorReflection() method.
	 */
	public static void main(String [] args){
		Point p0 = new Point(1, 2);
		Point p1 = p0.calculateVectorReflection(360);
		System.out.println(p1.getX() + " " + p1.getY());
	}
}
