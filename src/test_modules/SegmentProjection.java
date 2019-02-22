package test_modules;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import physics_simulator.DrawPanel;
import physics_simulator.Line;
import physics_simulator.Point;
import physics_simulator.Segment;

/**
 * Test module exemplifying the projection of a segment onto an axis.
 * This will be necessary in order to implement separating axis theorem. 
 * @author Adam Cogen
 *
 */
public class SegmentProjection extends JFrame{

	private SegmentProjectionDrawPanel panel;
	private static final int FRAME_HEIGHT = 150;
	private static final int FRAME_WIDTH = 150;
	private Segment s0, s1;

	public SegmentProjection(Segment initS0, Segment initS1){
		s0 = initS0;
		s1 = initS1;
		panel = new SegmentProjectionDrawPanel();
		this.add(panel);
		this.setVisible(true);
		this.setSize(FRAME_HEIGHT, FRAME_WIDTH);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("| projection");
		
	}

	class SegmentProjectionDrawPanel extends DrawPanel{
		public void paintComponent(Graphics g){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 510, 510);
			g.setColor(Color.BLACK);
			drawSegment(g, s0.getStartPoint(), s0.getEndPoint());
			drawSegment(g, s1.getStartPoint(), s1.getEndPoint());
			g.setColor(Color.RED);
			Line axis = new Line(s0.getStartPoint(), s0.getEndPoint());
			Segment projection = s1.projectOntoLine(axis);
			drawSegment(g, projection.getStartPoint(), projection.getEndPoint());
		}

	}

	public static void main(String [] args){
		//an undefined-slope line and a standard-slope line
		Point pointE = new Point(60, 20);
		Point pointF = new Point(60, 100);
		Point pointG = new Point(120, 60);
		Point pointH = new Point(80, 90);
		Segment seg2 = new Segment(pointE, pointF);
		Segment seg3 = new Segment(pointG, pointH);
		SegmentProjection is0 = new SegmentProjection(seg2, seg3);

		//2 standard-slope lines
		Point pointA = new Point(50, 30);
		Point pointB = new Point(110, 50);
		Point pointC = new Point(20, 70);
		Point pointD = new Point(120, 65);
		Segment seg0Wall = new Segment(pointA, pointB);
		Segment seg1Path = new Segment(pointC, pointD);
		SegmentProjection is1 = new SegmentProjection(seg1Path, seg0Wall);
		
		//standard-slope projected onto zero-slope
		Point pointMWall = new Point(50, 30);
		Point pointNWall = new Point(110, 50);
		Point pointOPath = new Point(20, 70);
		Point pointPPath = new Point(120, 70);
		Segment seg6Wall = new Segment(pointMWall, pointNWall);
		Segment seg7Path = new Segment(pointOPath, pointPPath);
		SegmentProjection is3 = new SegmentProjection(seg7Path, seg6Wall);
	}

}
