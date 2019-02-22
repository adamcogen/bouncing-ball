package test_modules;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import physics_simulator.DrawPanel;
import physics_simulator.Line;
import physics_simulator.Point;
import physics_simulator.Segment;
import physics_simulator.Shape;

/**
 * Test module exemplifying the projection of a shape onto an axis.
 * This will be necessary in order to implement separating axis theorem. 
 * @author Adam Cogen
 *
 */
public class ShapeProjection extends JFrame{

	private ShapeProjectionDrawPanel panel;
	private static final int FRAME_HEIGHT = 150;
	private static final int FRAME_WIDTH = 150;
	private Segment s0;
	private Shape s1;

	public ShapeProjection(Segment initS0, Shape initS1){
		s0 = initS0;
		s1 = initS1;
		panel = new ShapeProjectionDrawPanel();
		this.add(panel);
		this.setVisible(true);
		this.setSize(FRAME_HEIGHT, FRAME_WIDTH);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("/\\ projection");
		
	}

	class ShapeProjectionDrawPanel extends DrawPanel{
		public void paintComponent(Graphics g){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 510, 510);
			g.setColor(Color.BLACK);
			drawSegment(g, s0.getStartPoint(), s0.getEndPoint());
			drawShape(g, 0, s1);
			g.setColor(Color.RED);
			Line axis = new Line(s0.getStartPoint(), s0.getEndPoint());
			Segment projection = s1.projectOntoLine(axis);
			drawSegment(g, projection.getStartPoint(), projection.getEndPoint());
		}
	}

	public static void main(String [] args){
		
		//wall at 45 degrees, path straight line
		Point vertex0 = new Point(50, 30);
		Point vertex1 = new Point(110, 50);
		Point vertex2 = new Point(70, 60);
		Point[] vertexList = new Point[3];
		vertexList[0] = vertex0;
		vertexList[1] = vertex1;
		vertexList[2] = vertex2;
		Point axisStart = new Point(10, 70);
		Point axisEnd = new Point(130, 80);
		Shape shape0 = new Shape(vertexList);
		Segment axis = new Segment(axisStart, axisEnd);
		ShapeProjection is3 = new ShapeProjection(axis, shape0);
	}

}
