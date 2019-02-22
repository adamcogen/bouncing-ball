package physics_simulator;

import java.util.ArrayList;

/**
 * Line class. Store and manipulate data representing a line, 
 * which has an equation and continues in both directions indefinitely.
 * 
 * @author Adam Cogen
 *
 */
public class Line {

	protected double slope; //slope of line. 'm' in the equation y = mx + b. irrelevant if slope is undefined (see slopeType field)
	protected double slopeType; //0 is zero slope, 1 is a normal number, 2 is undefined
	protected double constant; //'b' in the equation y = mx + b. for undefined slope lines, slopeType = 2, and the equation for the line is x = constant.

	/**
	 * Construct a Line from 2 points. Initialize the fields defining the line's equation.
	 * 
	 * @param p0 a Point along the line
	 * @param p1 another Point along the line.
	 */
	public Line(Point p0, Point p1){

		if(p1.getX() == p0.getX()){ //line has undefined slope
			slopeType = 2;
			slope = 0;
			constant = p1.getX();
		} else if (p1.getY() == p0.getY()){ //line has zero slope
			slopeType = 0;
			slope = 0;
			constant = (p0.getY());
		} else { //line has nonzero, defined slope
			slope = (p1.getY()-p0.getY()) / (p1.getX()-p0.getX());
			slopeType = 1;
			constant = calculateConstantForLineWithDefinedSlope(p0);
		}
	}

	/**
	 * Construct a Line from 1 point and a slope.
	 * 
	 * @param p0 a Point along the line
	 * @param initSlopeType the slopeType of the line. 0 is slope of zero, 
	 * 		  1 is non-zero defined slope, 2 is undefined slope.
	 * @param initSlope the slope of the line. this value is ignored if slopeType is 0 or 2.
	 */
	public Line(Point p0, double initSlopeType, double initSlope){
		if(initSlopeType == 0){ //slope is zero
			slope = 0;
			constant = p0.getY();
			slopeType = 0;
		} else if(initSlopeType == 2){ //slope is undefined
			slope = 0;
			constant = p0.getX();
			slopeType = 2;
		} else { //slopeType 1, slope is neither zero nor undefined
			slope = initSlope;
			slopeType = 1;
			constant = calculateConstantForLineWithDefinedSlope(p0);
		}
	}

	/**
	 * Calculate the constant in the equation for a Line with slopeType 1. 
	 * This method is called within Line constructors to improve readability.
	 * Note that this method works for Lines with slopeType 0 as well, but
	 * it is not useful in these cases, since we already know that the slope
	 * of lines with slopeType 0 is zero.
	 * 
	 * Finds 'b' in y = mx + b 
	 * for slopeType == 1 Lines
	 * 
	 * Note that we pass in a single point because as the algebra below shows,
	 * we can represent the line in point-slope form (y - y1 = m ( x - x1)), so 
	 * that only one point along the line is needed to calculate the value of 
	 * the constant in the line's equation in y-intercept form (y = mx + b).
	 * 
	 * @param p0 a point along the line
	 * @return the constant b, in slope-intercept form y = mx + b, assuming that the line is defined
	 */
	private double calculateConstantForLineWithDefinedSlope(Point p0){
		/*
		 * 		algebra for finding the constant, b, in the equation y = mx + b
		 * 		for a line that has a defined slope. 
		 * 		y - y1 = m ( x - x1)
		 * 		y - y1 = m(x) - m(x1)
		 * 		y = m(x) - m(x1) + y1
		 *		y = m(x) + (-1 * (m(x1) + y1))
		 * 		b = (-1 * (m(x1) + y1))
		 * 
		 */
		return -1 * (((slope) * (p0.getX())) - (p0.getY()));
	}

	/**
	 * Return the slope type of this Line.
	 * Slope type 0 is slope zero.
	 * Slope type 1 is nonzero, defined slope.
	 * Slope type 2 is undefined slope.
	 * @return the slope type of this Line.
	 */
	public double getSlopeType(){
		return slopeType;
	}

	/**
	 * Return the slope of this Line, assuming that the slope of this Line is not undefined.
	 * @return the slope of this Line, assuming that the slope of the Line is defined.
	 */
	public double getSlope(){
		if(slopeType == 0 || slopeType == 1){ //if slope is defined, return it
			return slope;
		} else { //if slope is undefined, this method doesn't return a useful result. return 0 instead.
			return 0;
		}
	}

	/**
	 * Return the constant, b, in the equation y= mx + b for this Line.
	 * Also known as the y-intercept of that Line.
	 * @return the constant, b, in the equation y = mx + b for this Line
	 */
	public double getConstant(){
		return constant;
	}

	/**
	 * Return a slope value which is perpendicular to the slope of
	 * this Line.
	 * this method is private because it doesn't work for 0 slope 
	 * (slopeType 0), since perpendicular to slope 0 is slope 
	 * undefined. You need to check for that case elsewhere. 
	 * The constructPerpendicularLine() method in this class creates 
	 * perpendicular lines properly, checking for and handling lines 
	 * with zero slope.
	 * For this reason, the getPerpendicularSlope() method
	 * should not be called be anyone, it only exists to be
	 * used by the constructPerpendicularLine() method.
	 * @return the slope of a Line that would be perpendicular to this Line, assuming that this Line has a defined, non-zero slope.
	 */
	private double getPerpendicularSlope(){
		if(slopeType != 2){ //if the slope of this line is not undefined,
			//return the opposite reciprocal of the slope of this line
			return -1 / (slope);
		} else { //the slope is undefined,
			//so a perpendicular Line would have a slope of zero
			return 0;
		}
	}

	/**
	 * Construct a Line that is perpendicular to this Line
	 * and passes through a specified Point.
	 * @param point the Point that the perpendicular Line should pass through
	 * @return the perpendicular Line, passing through the specified Point
	 */
	public Line constructPerpendicularLine(Point point){
		Line perpLine;
		if(slopeType == 1){ //the original line has a nonzero, defined slope
			double perpSlope = getPerpendicularSlope();
			perpLine = new Line(point, 1, perpSlope);
		} else if(slopeType == 0){ //the original line has a zero slope
			perpLine = new Line(point, 2, 0);
		} else { //the original Line has an undefined slope
			perpLine = new Line(point, 0, 0);
		}
		return perpLine;
	}

	/**
	 * Construct an Axis that is perpendicular to this Line
	 * and does not pass through any particular Point, but has
	 * perpendicular slope to this line
	 * @return the perpendicular Axis
	 */
	public Axis constructPerpendicularAxis(){
		/*
		 * This Line passes through an arbitrary Point, since the actual position in the 
		 * coordinate plane of an Axis does not matter, only its slope. See the Axis class
		 * for more information.
		 */
		Line line = constructPerpendicularLine(new Point(0, 0));
		return new Axis(line.getSlopeType(), line.getSlope());
	}

	/**
	 * Calculate and return the Point of intersection between this Line and a specified
	 * Line. 
	 * When this method is called from the Physics class, Segment line0 is the path of a
	 * ball, and the invoking Segment is a shape edge.
	 * @param line0 the Line to calculate this Line's intersection with
	 * @return
	 */
	public Point getIntersectionWith(Line line0){
		double intersectionX = 0;
		double intersectionY = 0;
		if((slopeType == 0 || slopeType == 1) && (line0.getSlopeType() == 0 || line0.getSlopeType() == 1)){ //both lines have defined slopes
			//		algebra: Finding an intersection between two lines with defined slopes
			//		m(x) + b = m1(x) + b1;
			//		m(x) = m1(x) + b1 - b;
			//		m(x) - m1(x) = b1 - b
			//		(m-m1)(x) = b1 - b;
			//		(x) = (b1 - b) / (m - m1)
			intersectionX = (line0.getConstant() - constant) / (slope - line0.getSlope());
			intersectionY = ((slope) * (intersectionX) + constant);
			return new Point(intersectionX, intersectionY);
		} else if (slopeType == 2 && line0.getSlopeType() != 2){ //this line is undefined slope, other line is defined slope
			intersectionX = constant;
			intersectionY = (line0.getSlope() * (intersectionX) + line0.getConstant());
			return new Point(intersectionX, intersectionY);
		} else if (line0.getSlopeType() == 2 && slopeType != 2){  //this line is defined slope, other line is undefined slope
			intersectionX = (line0.getConstant());
			intersectionY = ((slope) * (intersectionX) + constant);
			return new Point(intersectionX, intersectionY);
		} else{ //both lines have undefined slopes
			//this case is irrelevant for a ball hitting a wall, but it will needed to be implemented for ball-to-ball collisions
			return null;
		}
	}

	/**
	 * Given an X value, calculate the corresponding y value using this Line's equation.
	 * @param yValue the given y value to calculate an x value for
	 * @return the x value, based on the given y value and this Line's equation
	 */
	public double calculateXValue(double yValue){
		if(slopeType == 1){
			//		algebra: solving for x when you know y and the equation of a line
			//		y = m(x) + b
			//		y - b = m(x)
			//		(y - b) / m = x
			return (yValue - constant) / slope;
		} else { //slopeType == 0 or slopeType == 2
			return constant;
		}
	}

	/**
	 * Given a y value, calculate the corresponding x value using this Line's equation.
	 * Note that this method throws an exception if it is called on a Line with an 
	 * undefined slope, since undefined slope lines have infinitely many y values
	 * for the same x value, but no y values for all other x values.
	 * @param xValue the given x value to calculate a y value for
	 * @return the y value, based on the given x value and this Line's equation, assuming that this Line's slope is not undefined
	 */
	public double calculateYValue(double xValue){
		if(slopeType != 2){
			//		y = m(x) + b
			return (slope * xValue) + constant;
		} else {
			throw new ArithmeticException("Can't solve for a y value on an undefined line");
		}
	}

	/**
	 * Main method.
	 * Currently tests the calculateXValue and calculateYValue methods
	 */
	public static void main(String[] args){
		Line l0 = new Line(new Point(0,0), new Point(1,1));
		System.out.println("const " + l0.getConstant() + ", slope " + l0.getSlope());
		System.out.println(l0.calculateXValue(2.34));
		System.out.println(l0.calculateYValue(1.999));
	}
}
