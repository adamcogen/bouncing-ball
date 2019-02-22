package test_modules;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import physics_simulator.DrawPanel;
import physics_simulator.Physics9;
import physics_simulator.Point;
import physics_simulator.Segment;

/**
 * A test module that exemplifies determining whether 
 * two Segments are intersecting, using methods
 * from the Segment class.
 * Basically checks where the equations of each 
 * Segment would intersect, then checks if that 
 * intersection is within the bounds of each Segment.
 * If it is, the Segments are intersecting,
 * and the intersection is circled in green.
 * A true/false indication of whether the 
 * Segments intersect is also written in the
 * bottom right corner of the module.
 * 
 * @author Adam Cogen
 *
 */
public class IntersectingSegments extends JFrame{

	private IntersectingSegmentsDrawPanel panel;
	private static final int FRAME_HEIGHT = 150;
	private static final int FRAME_WIDTH = 150;
	private Segment s0, s1;
	private int vertexCircleRadius = 3;
	ArrayList<Point> points = new ArrayList<Point>();
	private int selectedPointIndex = -1;

	public IntersectingSegments(Segment initS0, Segment initS1){
		s0 = initS0;
		s1 = initS1;
		points.add(s0.getStartPoint());
		points.add(s0.getEndPoint());
		points.add(s1.getStartPoint());
		points.add(s1.getEndPoint());
		panel = new IntersectingSegmentsDrawPanel();
		this.add(panel);
		this.setVisible(true);
		this.setSize(FRAME_HEIGHT, FRAME_WIDTH);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//this.setTitle("Do the segments intersect?");
		
		panel.addMouseMotionListener(getMouseMotionListener());
		
	}

	class IntersectingSegmentsDrawPanel extends DrawPanel{
		public void paintComponent(Graphics g){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 510, 510);
			Point intersection = s0.getIntersectionWith(s1);
			boolean isIntersect = s0.isIntersecting(s1, intersection);

			//drawPointBeforeIntersection(g,intersection);
			drawIntersection(g, intersection, isIntersect);

			g.setColor(Color.BLACK);
			//g.drawString("" + isIntersect, 464, 469);
			g.drawString("" + isIntersect, 114, 119);
		}

		public void drawIntersection(Graphics g, Point intersection, boolean isIntersecting){
			if(isIntersecting){
				g.setColor(Color.GREEN);
				drawPoint(g, new Point(intersection.getX(), intersection.getY()), 3, 1);
				//g.fillOval((int)intersection.getX() - 3, (int)intersection.getY() - 3, 6, 6);
			}
			g.setColor(Color.BLACK);
			drawSegment(g, s0.getStartPoint(), s0.getEndPoint());
			//g.setColor(Color.BLUE);
			drawSegment(g, s1.getStartPoint(), s1.getEndPoint());
			if(isIntersecting){
				g.setColor(Color.BLACK);
				drawPoint(g, new Point(intersection.getX(), intersection.getY()), 3);
			}
			if(selectedPointIndex != -1) {
				circleSelectedPoint(g);
			}
		}
		
		private void circleSelectedPoint(Graphics g) {
			drawPoint(g,points.get(selectedPointIndex), 3);
		}

	}
	
	public MouseMotionListener getMouseMotionListener(){
		class GeneralMouseMotionListener implements MouseMotionListener{

			@Override
			public void mouseDragged(MouseEvent e) {
				if(selectedPointIndex != -1) {
					points.get(selectedPointIndex).setX(e.getX());
					points.get(selectedPointIndex).setY(e.getY());
					s0 = new Segment(points.get(0), points.get(1));
					s1 = new Segment(points.get(2), points.get(3));
				}
				panel.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				updateVertexSelection(e);
				panel.repaint();
			}
		}
		return new GeneralMouseMotionListener();
	}
	
	public void updateVertexSelection(MouseEvent e){
		Point mousepoint = new Point(e.getX(), e.getY());
		selectedPointIndex = -1;
		Point potentialSelectedBall;
		int index = 0;
		index = 0;
		for( /*index */ ; index < points.size(); index++) {
			if(Physics9.distanceNoSqrt(points.get(index), mousepoint) <= (vertexCircleRadius * vertexCircleRadius)) {
				selectedPointIndex = index;
			}
		}
	}

	public static void main(String [] args){

		Point pointA = new Point(60, 20);
		Point pointB = new Point(90, 100);
		Point pointC = new Point(120, 60);
		Point pointD = new Point(30, 90);
		Segment seg0 = new Segment(pointA, pointB);
		Segment seg1 = new Segment(pointC, pointD);
		IntersectingSegments is = new IntersectingSegments(seg0, seg1);
	}

}
