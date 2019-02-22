package physics_simulator;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
/**
 * The DrawPanel class.
 * Extends the javax.swing.JPanel, adding functionality that is useful for displaying the 
 * simulator's test modules, the Window class, and the toolbox and menus. All of these 
 * classes extend the JFrame class, and contain an inner class that extends the DrawPanel. 
 * The class that extends DrawPanel is then added as the JFrame's JPanel. 
 * 
 * @author Adam Cogen
 *
 */
public abstract class DrawPanel extends JPanel{
	/**
	 * The paintComponent() method tells the DrawPanel what to do whenever it is displayed. 
	 * This method is left abstract, since it will be very different in each class that 
	 * extends the DrawPanel.
	 * The parameter, Graphics g, does not actually need to be passed in to the paintComponent
	 * method by the programmer, it is automatically taken care of.  
	 */
	public abstract void paintComponent(Graphics g);

	/**
	 * Draw a Segment, defined by its start Point and end Point. 
	 * @param g the Graphics object, which can be passed in from the paintComponent method
	 * @param point0 the start point of the Segment
	 * @param point1 the end point of the Segment
	 */
	public void drawSegment(Graphics g, Point point0, Point point1){
		g.drawLine((int) point0.getX(), (int) point0.getY(), (int) point1.getX(), (int) point1.getY());
	}
	
	/**
	 * Draw a Segment
	 * @param g the Graphics object, which can be passed in from the paintComponent method
	 * @param seg the Segment to draw
	 */
	public void drawSegment(Graphics g, Segment seg){
		drawSegment(g, seg.getStartPoint(), seg.getEndPoint());
	}

	/**
	 * Draw a Point, represented as a circle surrounding the specified point
	 * To draw a point with no circle surrounding it, use radius 1.
	 * @param g the Graphics object, which can be passed in from the paintComponent method
	 * @param point The Point to center the circle around
	 * @param radius The radius of the circle surrounding the Point
	 */
	public void drawPoint(Graphics g, Point point, int radius){
		g.drawOval((int)point.getX() - radius, (int)point.getY() - radius, radius * 2, radius * 2);
	}

	/**
	 * Draw a Point, represented as a circle surrounding the specified point.
	 * The circle can either be filled in, or an outline.
	 * To draw a point with no circle surrounding it, use radius 1.
	 * @param g the Graphics object, which can be passed in from the paintComponent method
	 * @param point The Point to center the circle around
	 * @param radius The radius of the circle surrounding the Point
	 * @param type Should the circle be just an outline (type = 0) or filled in? (type != 0)
	 */
	public void drawPoint(Graphics g, Point initPoint, int radius, int type){
		if(type == 0){ //if type is 0, draw the outline of the cirlce only
			drawPoint(g, initPoint, radius);
		} else { //if type is not equal to zero, fill the circle in
			g.fillOval((int)initPoint.getX() - radius, (int)initPoint.getY() - radius, radius * 2, radius * 2);
		}
	}

	/**
	 * Draw a Shape. 
	 * Can be either filled in, or just an outline.
	 * @param g the Graphics object, which can be passed in from the paintComponent method
	 * @param type Should the shape be just an outline (type = 0) or filled in? (type != 0)
	 * @param shape0 the Shape to draw
	 */
	public void drawShape(Graphics g, int type, Shape shape0){
		if(type == 0){ //type 0 means draw outline only
			g.drawPolygon(shape0.getXCoordinates(), shape0.getYCoordinates(), shape0.getNumberOfVertices());
		} else { //type of any other number means fill the shape
			g.fillPolygon(shape0.getXCoordinates(), shape0.getYCoordinates(), shape0.getNumberOfVertices());
		}
	}
}
