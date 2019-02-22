package physics_simulator;
/**
 * The Segment class, which Stores and manipulates information about a segment.
 * A Segment is defined by two points, which make up its start point and end point. 
 * It is represented as a line, with upper and lower boundaries on its valid x and y values. 
 * The line's equation in slope-intercept form ( y = mx + b) and its x and y ranges
 * are calculated and stored when the Segment is constructed.
 *
 *@author Adam Cogen
 *
 */
public class Segment extends Line{

	private double yMin; //the smallest y value that is in the Segment. the lower bound of y values for the Segment.
	private double yMax; //the largest y value that is in the Segment. the upper bound of y values for the Segment.
	private double xMin; //the smallest x value that is in the Segment. the lower bound of x values for the Segment.
	private double xMax; //the largest x value that is in the Segment. the upper bound of x values for the Segment.
	private Point startPoint; //the start point of the Segment
	private Point endPoint; //the end point of the Segment

	/**
	 * Construct a Segment from 2 points.
	 * Call the superclass constructor (the Line constructor), and
	 * initialize all necessary information unique to the Segment
	 * class, such as its start and end point fields, and its
	 * x and y maximum and minimum fields.
	 * @param p0 the start point of the Segment
	 * @param p1 the end point of the Segment
	 */
	public Segment(Point p0, Point p1){
		super(p0, p1); //call the superclass constructor (the Line constructor) on the two Points passed to this constructor as parameters
		startPoint = p0;
		endPoint = p1;
		if(p0.getY() > p1.getY()){ //figure out which values to store as yMin and yMax
			yMin = p1.getY();
			yMax = p0.getY();
		} else {
			yMin = p0.getY();
			yMax = p1.getY();
		}

		if(p0.getX() > p1.getX()){ //figure out which values to store as xMin and xMax
			xMin = p1.getX();
			xMax = p0.getX();
		} else {
			xMin = p0.getX();
			xMax = p1.getX();
		}
	}

	/**
	 * Return the start point of this line (the first Point parameter passed to the constructor)
	 * To return the other point that defines this segment, see the getEndPoint() method.
	 * @return the start point of this line
	 */
	public Point getStartPoint(){
		return startPoint;
	}
	/**
	 * Return the end point of this line (the second Point parameter passed to the constructo)
	 * To return the other point that defines this segment, see the getStartPoint() method.
	 * @return the end point of this line
	 */
	public Point getEndPoint(){
		return endPoint;
	}

	/**
	 * Print all of the information that needed to define this Segment.
	 * This includes the slope, the constant ('b' in y = mx + b),
	 * and the minimum and maximum x and y values.
	 */
	public void printInfo(){
		if(slopeType == 0 || slopeType == 1){
			System.out.println("slope: " + slope);
			System.out.println("constant: " + constant);
			System.out.println("xMin: "+ xMin);
			System.out.println("xMax: " + xMax);
		} else {
			System.out.println("slope: undefined");
			System.out.println("x value:" + xMin);
		}
		System.out.println("yMin: "+ yMin);
		System.out.println("yMax: " + yMax);
	}

	/**
	 * Return a copy of this Segment, which has been rotated about a specified origin.
	 * @param degrees the number of degrees to rotate the Segment. positive is clockwise, negative is counter-clockwise.
	 * @param origin a point specifying the origin around which to rotate the Segment
	 * @return A Segment which is the same as this one, but rotated about the specified origin the specified number of degrees.
	 */
	public Segment rotate(double degrees, Point origin){
		Point resultStartPoint = getStartPoint().rotate(degrees, origin);
		Point resultEndPoint = getEndPoint().rotate(degrees, origin);
		Segment result = new Segment(resultStartPoint, resultEndPoint);
		return result;
	}

	/**
	 * Determine the angle between this Segment and the Segment that has been passed in as a parameter.
	 * Note that this angle will always have an acute magnitude, but it may be positive or negative 
	 * (positive is clockwise, negative is counterclockwise). This method is used when determining
	 * the angle between two Segments for the sake of performing an angled reflection of a ball's 
	 * path off of a wall.
	 * @param line0 the Segment to find the angle of, in relation to the invoking Segment
	 * @return the angled between the invoking Segment and the Segment passed in as a parameter
	 */
	public double angleBetween(Segment line0){
		/*
		 * 		trigonometry for finding angle between two 
		 * 		lines when you know the slope of them both:
		 * 		arctangent((slope0 - slope1) / (1 + (slope0 * slope1)))
		 * 
		 */
		double radians = 0;
		double degrees = 0;
		if(slopeType != 2 && line0.getSlopeType() != 2){ //handle angle between lines with defined slopes
			//trigonometry: see the comment at the beginning of this method
			double temp = ((slope - line0.getSlope()) / (1 + (slope * line0.getSlope())));
			radians = Math.atan(temp);
			degrees = Physics9.radiansToDegrees(radians);
		} else { //handle everything else
			if(slopeType == 2 && line0.getSlopeType() != 2){ 
				/*
				 * handle angle between undefined slope lines and defined slope lines.
				 * to do this, we rotate the undefined slope Segment 90 degrees so 
				 * that it has a defined slope (it will have a slope of 0). 
				 * We then determine the angle between the two Segments, and add 90 
				 * to it, so that we account for having rotated one of the Segments 
				 * 90 degrees. 
				 */
				Segment rotatedCurrentLine = rotate(90, getStartPoint());
				//trigonometry: see the comment at the beginning of this method
				double temp = ((rotatedCurrentLine.getSlope() - line0.getSlope()) / (1 + (rotatedCurrentLine.getSlope() * line0.getSlope())));
				radians = Math.atan(temp);
				degrees = Physics9.radiansToDegrees(radians);
				degrees += 90;
			} else if(slopeType != 2 && line0.getSlopeType() == 2){ 
				/*
				 * handle angle between defined slope lines and undefined slope lines (opposite orientation to previous case).
				 * to do this, we rotate the undefined slope Segment 90 degrees so that it has a defined slope
				 * (it will have a slope of 0 degrees). 
				 * We then determine the angle between the two Segments, and add 90 to it, so that we account for having
				 * rotated one of the Segments 90 degrees.
				 */
				Segment rotatedLine0 = line0.rotate(90, line0.getStartPoint());
				//trigonometry: see the comment at the beginning of this method
				double temp = ((slope - rotatedLine0.getSlope()) / (1 + (slope * rotatedLine0.getSlope())));
				radians = Math.atan(temp);
				degrees = Physics9.radiansToDegrees(radians);
				degrees += 90;
			} else {
				/*
				 * This case can never be reached, but I wanted to use else if statements 
				 * above so that each case was clearly defined for the sake of readability.
				 */
				return 0;
			}
		}
		/*
		 * These next few lines are to convert reflex angles 
		 * (magnitude greater than 180 and less than 360) 
		 * to their more readable (magnitude less than 180) 
		 * values.
		 */
		if(degrees > 90){
			degrees -= 180;
		}
		if(degrees < -90){
			degrees += 180;
		}
		return degrees;
	}

	/**
	 * Find a Segment that represents a reflection of this Segment off of the line 'line0'.
	 * It should be noted that this method is only used within the AngleBetween test module,
	 * as a proof of concept, and actual reflections in the simulator rely on the 
	 * physics_simulator.Point.calculateVectorReflection method.
	 * @param line0 the Segment to reflect this Segment off of
	 * @return Segment: a Segment representing the reflection of this Segment off of line0
	 */
	public Segment returnReflectionOffOf(Segment line0){
		Segment reflection;
		double anglebtwn = angleBetween(line0);
		Point inters = getIntersectionWith(line0);
		/*
		 * We are trying to find the necessary rotation to perform
		 * on a Segment to perform its reflection off of another
		 * Segment.
		 * Let's look at an example to anecdotally show the math 
		 * behind this. Let's say the invoking Segment (this Segment) 
		 * should be reflected off of line0, and the angle between 
		 * this Segment and line0 is 30 degrees. 
		 * 90 - 30 = 60. 60 * 2 = 120. Rotating this Segment by 120 
		 * degrees would mean that the angle between this Segment and 
		 * line0 would now be this Segment's original angle, plus 120, 
		 * which would be 30 + 120 = 160. This would mean that there were
		 * now 180 - 160 = 30 degrees between this Segment and line0, 
		 * but from the opposite side, as if a reflection of this Segment
		 * off line0 had occurred. 
		 * This math is equivalent to rotating this Segment by 
		 * (180 - (2 * anglebtwn), if that is simpler to understand.
		 * Similar math applies when there is a negative angle between
		 * the two Segments, but a minor change is made in this case. 
		 * See the code below.
		 */
		if (anglebtwn > 0){ //case 1: if the angle between this Segment and line0 is positive, rotate this Segment clockwise about intersection
			reflection = rotate((90 - anglebtwn) * 2, inters); //or rotate(180 - (2 * anglebtwn), inters), they are equivalent
		} else if (anglebtwn < 0){ //case 2: if angle between this Segment and line0 is negative, rotate counter-clockwise about intersection
			reflection = rotate((-90 - anglebtwn) * 2, inters); //or rotate(-180 - (2 * anglebtwn), inters), they are equivalent
		} else { //case 3: angle between this Segment and line0 is 0, rotate 180 degrees about the end of Segment 'line0'
			reflection = rotate(180, getEndPoint());
		}
		/*
		 * now we have to flip this reflection's start and end points, so that 
		 * the reflection acts similarly to a ball reflecting off of a wall
		 * (the original Segment ends at the wall, the reflection starts at 
		 * the wall).
		 */
		reflection = new Segment(reflection.getEndPoint(), reflection.getStartPoint());
		return reflection;
	}

	/**
	 * Return the minimum x value of this Segment, which represents 
	 * the lower bound of possible x values on this Segment.
	 * @return the smallest x value on this Segment
	 */
	public double getXMin(){
		return xMin;
	}

	/**
	 * Return the maximum x value of this Segment, which represents 
	 * the upper bound of possible x values on this Segment.
	 * @return the largest x value on this Segment
	 */
	public double getXMax(){
		return xMax;
	}

	/**
	 * Return the minimum y value of this Segment, which represents 
	 * the lower bound of possible y values on this Segment.
	 * @return the smallest y value on this Segment
	 */
	public double getYMin(){
		return yMin;
	}

	/**
	 * Return the maximum y value of this Segment, which represents 
	 * the upper bound of possible y values on this Segment.
	 * @return the largest y value on this Segment
	 */
	public double getYMax(){
		return yMax;
	}

	/**
	 * Project this Segment onto an axis.
	 * @param axis The axis to project this Segment onto.
	 * @return the Segment representing the projection of this Segment onto the specified Axis
	 */
	public Segment projectOntoLine(Line axis){
		return new Segment(startPoint.projectOntoLine(axis), endPoint.projectOntoLine(axis));
	}

	/**
	 * Segments are represented using linear equations, but there is
	 * a limit on the x and y range of a Segment. Given another Segment,
	 * and the point of intersection between the equations representing
	 * the lines defining this Segment and the other, determine whether
	 * that intersection falls within the range of valid x and y values
	 * of each Segment, and is thus a valid intersection between the two
	 * Segments.
	 * It is assumed that the point of intersection given is correct
	 * for the equations of the lines representing the two Segments.
	 * The reason the point of intersection is passed in as a parameter
	 * instead of being calculated is because by the time this method is
	 * called within the Physics class, the point of intersection has 
	 * already been calculated for another purpose, so passing it in
	 * as a parameter prevents redundant calculations from occurring.
	 * @param line0 the Segment to check for an intersection with
	 * @param intersection the point of intersection between the linear equations defining this Segment and line0
	 * @return true if the point of intersection is valid, false otherwise
	 */
	public boolean isIntersecting(Segment line0, Point intersection){
		boolean inXRange0 = false;
		boolean inYRange0 = false;
		boolean inXRange1 = false;
		boolean inYRange1 = false;
		if(intersection != null){
			double intersectionX = intersection.getX();
			double intersectionY = intersection.getY();
			double cushioning = .01; //.01 increase boundary ranges by the cushioning constant, to account for lack of precision in the double data type
			//is the intersection within the boundaries of the invoking Segment?
			inXRange0 = (intersectionX >= xMin - cushioning && intersectionX <= xMax + cushioning);
			inYRange0 = (intersectionY >= yMin - cushioning && intersectionY <= yMax + cushioning);
			//is the intserction within the boundaries of the Segment passed in as a parameter?
			inXRange1 = (intersectionX >= line0.getXMin() - cushioning && intersectionX <= line0.getXMax() + cushioning);
			inYRange1 = (intersectionY >= line0.getYMin() - cushioning && intersectionY <= line0.getYMax() + cushioning);
		}
		return inXRange0 && inYRange0 && inXRange1 && inYRange1;
	}

	public boolean isIntersecting(Line line0, Point intersection) {
		boolean isInXRange0 = false;
		boolean isInYRange0 = false;
		if(intersection != null){
			double intersectionX = intersection.getX();
			double intersectionY = intersection.getY();
			double cushioning = .01; //.01 increase boundary ranges by the cushioning constant, to account for lack of precision in the double data type
			//is the intersection within the boundaries of the invoking Segment?
			isInXRange0 = (intersectionX >= xMin - cushioning && intersectionX <= xMax + cushioning);
			isInYRange0 = (intersectionY >= yMin - cushioning && intersectionY <= yMax + cushioning);
		}
		return isInXRange0 && isInYRange0;
	}

	/**
	 * Determine whether two Segments on the same axis are 
	 * overlapping each other. 
	 * We have assumed the precondition that the segments 
	 * are on the same axis, since the Axis class is the 
	 * only place this method will be called. This method
	 * only tests that Segments are within the correct
	 * range to be overlapping. It will not consistently 
	 * work for Segments that are not on the same Axis.
	 * @param s0 the Segment to check for overlaps with
	 * @return true if the Segments are overlapping, false if otherwise.
	 * 		   correct result is only guaranteed if both Segments are on
	 * 		   the same Axis (see the Axis class).
	 */
	public boolean isOverlapping(Segment s0){
		boolean result = false;
		boolean thisMinIsWithinOther;
		boolean thisMaxIsWithinOther;
		boolean otherMinIsWithinThis;
		boolean otherMaxIsWithinThis;
		if(startPoint.getX() != endPoint.getX()){ 
			//first case: x value of start and end point are not the same
			thisMinIsWithinOther = xMin >= s0.getXMin() && xMin <= s0.getXMax();
			thisMaxIsWithinOther = xMax >= s0.getXMin() && xMax <= s0.getXMax();
			otherMinIsWithinThis = s0.getXMin() >= xMin && s0.getXMin() <= xMax;
			otherMaxIsWithinThis = s0.getXMax() >= xMin && s0.getXMax() <= xMax;
			if(thisMinIsWithinOther || thisMaxIsWithinOther || otherMinIsWithinThis || otherMaxIsWithinThis) {
				result = true;
			}
		} else if(startPoint.getY() != endPoint.getY()){ 
			//second case: x value of start and end point are the same, but y values of start and end point are different 
			thisMinIsWithinOther = yMin >= s0.getYMin() && yMin <= s0.getYMax();
			thisMaxIsWithinOther = yMax >= s0.getYMin() && yMax <= s0.getYMax();
			otherMinIsWithinThis = s0.getYMin() >= yMin && s0.getYMin() <= yMax;
			otherMaxIsWithinThis = s0.getYMax() >= yMin && s0.getYMax() <= yMax;
			if(thisMinIsWithinOther || thisMaxIsWithinOther || otherMinIsWithinThis || otherMaxIsWithinThis) {
				result = true;
			}
		} else { 
			//third case: both Segments we are dealing are really on a single point, as x and y values of start and end points are the same.
			result = true;
		}
		return result;
	}

	/**
	 * return the length of this segment using the distance formula
	 * @return a double representing the length of this segment
	 */
	public double getLength() {
		return Physics9.distance(startPoint, endPoint);
	}

	public Line getLine() {
		return new Line(endPoint, startPoint);
	}

}
