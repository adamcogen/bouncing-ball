package test_modules;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import physics_simulator.Axis;
import physics_simulator.DrawPanel;
import physics_simulator.Physics9;
import physics_simulator.Point;
import physics_simulator.Shape;
/**
 * Test module exemplifying collision detection between two convex polygons
 * using separating axis theorem.
 * @author Adam Cogen
 *
 */
public class SeparatingAxisTheorem extends JFrame{

	private SATDrawPanel panel;
	private static final int FRAME_HEIGHT = 320;
	private static final int FRAME_WIDTH = 320;
	private Shape s0;
	private Shape s1;
	private ArrayList<Axis> perpendicularAxes;
	private boolean colliding;
	private ArrayList<Shape> shapes;
	private boolean showAxes = true;

	/*
	 * When a vertex is selected, a circle is drawn around it. vertexCircleRadius defines what the radius of that circle
	 * should be. Also, when selecting a vertex in move vertex or move shape mode, putting your mouse within the inside
	 * of this circle allows that that vertex to be selected. Larger radius means that you can select the vertex from 
	 * further away.
	 */
	private int vertexCircleRadius = 3;
	/*
	 * select a vertex and shape by mousing over a vertex within that shape.
	 * a value of -1 means that nothing has been selected.
	 */
	private int selectedShapeIndex = -1; //the index of the shape that is selected, in the shapes ArrayList within the Map class. 
	private int selectedVertexIndex = -1; //the index of the vertex that is selected, within the vertices[] array of the selected shape.
	private Point vertexDragStartPoint;

	public SeparatingAxisTheorem(Shape initS0, Shape initS1){
		s0 = initS0;
		s1 = initS1;
		shapes = new ArrayList<Shape>();
		shapes.add(s0);
		shapes.add(s1);
		panel = new SATDrawPanel();
		this.add(panel);
		this.setVisible(true);
		this.setSize(FRAME_HEIGHT, FRAME_WIDTH);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("/\\ collision");

		panel.addMouseMotionListener(getMouseMotionListener());

		initializeSAT();
		colliding = runSAT();

	}

	public void initializeSAT(){
		perpendicularAxes = new ArrayList<Axis>();
		perpendicularAxes.addAll(s0.getAxes());
		perpendicularAxes.addAll(s1.getAxes());
	}

	public boolean runSAT(){
		for(int i = 0; i < perpendicularAxes.size(); i++){
			perpendicularAxes.get(i).addProjection(s0.projectOntoLine(perpendicularAxes.get(i)));
			perpendicularAxes.get(i).addProjection(s1.projectOntoLine(perpendicularAxes.get(i)));
		}
		for(int i = 0; i < perpendicularAxes.size(); i++){
			if(!perpendicularAxes.get(i).projectionsOverlap()){
				return false;
			}
		}
		return true;
	}

	class SATDrawPanel extends DrawPanel{
		public void paintComponent(Graphics g){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 510, 510);
			g.setColor(Color.BLACK);
			drawShape(g, 0, s0);
			drawShape(g, 0, s1);
			g.drawString("" + colliding, 115, 123);
			if(showAxes){
				for(int i = 0; i < perpendicularAxes.size(); i++){
					drawSegment(g, perpendicularAxes.get(i).getProjections().get(0).getStartPoint(), perpendicularAxes.get(i).getProjections().get(0).getEndPoint());
					g.setColor(Color.RED);
					drawSegment(g, perpendicularAxes.get(i).getProjections().get(1).getStartPoint(), perpendicularAxes.get(i).getProjections().get(1).getEndPoint());
					g.setColor(Color.BLACK);
				}
			}
			if(selectedShapeIndex != -1){
				circleSelectedVertex(g);
			}
		}

		private void circleSelectedVertex(Graphics g){
			drawPoint(g, shapes.get(selectedShapeIndex).getVertex(selectedVertexIndex), vertexCircleRadius);
		}

	}

	public void updateVertexSelection(MouseEvent e){
		Point mousepoint = new Point(e.getX(), e.getY());
		selectedShapeIndex = -1;
		selectedVertexIndex = -1;
		Point potentialSelectedVertex;
		int index = 0;
		for( /* we will use the variable 'index', which is already initialized above */ ; index < shapes.size(); index++){
			for(int j = 0; j < shapes.get(index).getNumberOfVertices(); j++){
				potentialSelectedVertex = shapes.get(index).getVertex(j);
				if(Physics9.distanceNoSqrt(potentialSelectedVertex, mousepoint) <= (vertexCircleRadius * vertexCircleRadius)){
					/*
					 * note that we didn't calculate the square root in the distance formula, but we instead squared
					 * the value that we are comparing this distance to.
					 */
					selectedShapeIndex = index;
					selectedVertexIndex = j;
				}
			}
		}
	}

	public MouseMotionListener getMouseMotionListener(){
		class GeneralMouseMotionListener implements MouseMotionListener{

			@Override
			public void mouseDragged(MouseEvent e) {
				if(selectedShapeIndex != -1){
					shapes.get(selectedShapeIndex).moveVertex(selectedVertexIndex, new Point(e.getX(), e.getY()));
					for(int j = 0; j < shapes.size(); j++){
						shapes.get(j).initializeEdges();
						shapes.get(j).initializeAxes();
					}
				}
				initializeSAT();
				colliding = runSAT();
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

	public static void main(String [] args){

		//wall at 45 degrees, path straight line
		Point vertex0 = new Point(150, 130);
		Point vertex1 = new Point(210, 150);
		Point vertex2 = new Point(170, 160);
		Point[] vertexList0 = new Point[3];
		vertexList0[0] = vertex0;
		vertexList0[1] = vertex1;
		vertexList0[2] = vertex2;
		Shape shape0 = new Shape(vertexList0);

		Point vertex3 = new Point(150, 120);
		Point vertex4 = new Point(210, 187);
		Point vertex5 = new Point(170, 200);
		Point[] vertexList1 = new Point[3];
		vertexList1[0] = vertex3;
		vertexList1[1] = vertex4;
		vertexList1[2] = vertex5;
		Shape shape1 = new Shape(vertexList1);

		SeparatingAxisTheorem is3 = new SeparatingAxisTheorem(shape0, shape1);
	}

}
