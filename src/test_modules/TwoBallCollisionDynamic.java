package test_modules;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import physics_simulator.Axis;
import physics_simulator.Ball;
import physics_simulator.DrawPanel;
import physics_simulator.Physics9;
import physics_simulator.Point;
import physics_simulator.Segment;
import physics_simulator.Shape;
import javax.swing.Timer;
/**
 * 
 * Test module exemplifying a collision between two balls.
 * @author Adam Cogen
 *
 */
public class TwoBallCollisionDynamic extends JFrame{

	private TwoBallCollisionDrawPanel panel;
	private Ball ball0;
	private Ball ball1;
	private ArrayList<Ball> balls;
	private static final int FRAME_HEIGHT = 320;
	private static final int FRAME_WIDTH = 320;;
	private boolean colliding;
	private Timer timer;

	/*
	 * When a vertex is selected, a circle is drawn around it. vertexCircleRadius defines what the radius of that circle
	 * should be. Also, when selecting a vertex in move vertex or move shape mode, putting your mouse within the inside
	 * of this circle allows that that vertex to be selected. Larger radius means that you can select the vertex from 
	 * further away.
	 */
	private int vertexCircleRadius = 10;
	/*
	 * select a vertex and shape by mousing over a vertex within that shape.
	 * a value of -1 means that nothing has been selected.
	 */
	private int selectedBallIndex = -1; //the index of the shape that is selected, in the shapes ArrayList within the Map class. 
	private int selectedVertexIndex = -1; //the index of the vertex that is selected, within the vertices[] array of the selected shape.
	private Point vertexDragStartPoint;
	private static final int TIMER_FREQUENCY = 500;

	public TwoBallCollisionDynamic(Ball initB0, Ball initB1){
		//s0 = initS0;
		//s1 = initS1;
		//shapes = new ArrayList<Shape>();
		//shapes.add(s0);
		//shapes.add(s1);
		ball0 = initB0;
		ball1 = initB1;
		balls = new ArrayList<Ball>();
		balls.add(ball0);
		balls.add(ball1);
		panel = new TwoBallCollisionDrawPanel();
		this.add(panel);
		this.setVisible(true);
		this.setSize(FRAME_HEIGHT, FRAME_WIDTH);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("O-><-O collision");

		panel.addMouseListener(getMouseListener());
		panel.addMouseMotionListener(getMouseMotionListener());

		timer = new Timer(TIMER_FREQUENCY, createTimer());
		timer.start();
		//initializeSAT();
		//colliding = runSAT();

	}

	private ActionListener createTimer() {
		class TimerListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(Physics9.distance(ball0, ball1) > ball1.getRadius() + ball0.getRadius()) {
					ball0.updatePosition();
					ball1.updatePosition();
				} else {
					
				}
				panel.repaint();
			}

		}
		return new TimerListener();
	}

	public void initializeSAT(){
		//perpendicularAxes = new ArrayList<Axis>();
		//perpendicularAxes.addAll(s0.getAxes());
		//perpendicularAxes.addAll(s1.getAxes());
	}

	public boolean runSAT(){
		//		for(int i = 0; i < perpendicularAxes.size(); i++){
		//			perpendicularAxes.get(i).addProjection(s0.projectOntoLine(perpendicularAxes.get(i)));
		//			perpendicularAxes.get(i).addProjection(s1.projectOntoLine(perpendicularAxes.get(i)));
		//		}
		//		for(int i = 0; i < perpendicularAxes.size(); i++){
		//			if(!perpendicularAxes.get(i).projectionsOverlap()){
		//				return false;
		//			}
		//		}
		return true;
	}

	class TwoBallCollisionDrawPanel extends DrawPanel{
		public void paintComponent(Graphics g){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 510, 510);
			g.setColor(Color.BLACK);
			drawPoint(g, ball0.getPositionAsPoint(), ball0.getRadius(), 0);
			drawPoint(g, ball0.getPositionAsPoint(), 1, 0);
			drawPoint(g, ball0.getPositionAsPoint(), 1, 1);
			drawPoint(g, ball1.getPositionAsPoint(), ball1.getRadius(), 0);
			drawPoint(g, ball1.getPositionAsPoint(), 1, 0);
			drawPoint(g, ball1.getPositionAsPoint(), 1, 1);
			colliding = checkCollision();
			g.drawString("" + colliding, 280, 290);
			//			if(showAxes){
			//				for(int i = 0; i < perpendicularAxes.size(); i++){
			//					drawSegment(g, perpendicularAxes.get(i).getProjections().get(0).getStartPoint(), perpendicularAxes.get(i).getProjections().get(0).getEndPoint());
			//					g.setColor(Color.RED);
			//					drawSegment(g, perpendicularAxes.get(i).getProjections().get(1).getStartPoint(), perpendicularAxes.get(i).getProjections().get(1).getEndPoint());
			//					g.setColor(Color.BLACK);
			//				}
			//			}
			if(selectedBallIndex != -1){
				circleSelectedVertex(g);
			}

			/*
			 * draw a line from each ball's center point to its outer edge
			 * in the direction of the other ball's center point
			 */
			Point ball0CollisionVector = Point.constructNormalizedVectorFromTwoPoints(ball0.getPositionAsPoint(), ball1.getPositionAsPoint());
			Point ball1CollisionVector = Point.constructNormalizedVectorFromTwoPoints(ball1.getPositionAsPoint(), ball0.getPositionAsPoint());
			Point ball0OuterPoint = Physics9.findPointAlongLine(ball0CollisionVector, ball0.getPositionAsPoint(), ball0.getRadius());
			Point ball1OuterPoint = Physics9.findPointAlongLine(ball1CollisionVector, ball1.getPositionAsPoint(), ball1.getRadius());
			drawSegment(g, ball0.getPositionAsPoint(), ball0OuterPoint);
			drawSegment(g, ball1.getPositionAsPoint(), ball1OuterPoint);

			/*
			 * draw a line representing each ball's velocity vector from 
			 * its outer edge to where the ball would end up
			 */
			//Point ball0VelocityStart = Physics9.findPointAlongLine(ball0.getVelocityVector(), ball0.getPositionAsPoint(), ball0.getRadius());
			Point ball0VelocityStart = ball0.getPositionAsPoint();
			Point ball0VelocityEnd = new Point(ball0VelocityStart.getX() + ball0.getXVelocity(), ball0VelocityStart.getY() + ball0.getYVelocity());
			//Point ball1VelocityStart = Physics9.findPointAlongLine(ball1.getVelocityVector(), ball1.getPositionAsPoint(), ball1.getRadius());
			Point ball1VelocityStart = ball1.getPositionAsPoint();
			Point ball1VelocityEnd = new Point(ball1VelocityStart.getX() + ball1.getXVelocity(), ball1VelocityStart.getY() + ball1.getYVelocity());
			g.setColor(Color.RED);
			drawSegment(g, ball0VelocityStart, ball0VelocityEnd);
			drawSegment(g, ball1VelocityStart, ball1VelocityEnd);
			g.setColor(Color.BLACK);
			/*
			 * if a collision is taking place, draw a vector representing
			 * each ball's velocity after the collision 
			 * from its outer edge to where the ball would end up
			 */
			Segment ball0CollisionSegment = new Segment(ball0.getPositionAsPoint(), ball0OuterPoint);
			Segment ball0VelocitySegment = new Segment(ball0VelocityStart, ball0VelocityEnd);
			Segment ball1CollisionSegment = new Segment(ball1.getPositionAsPoint(), ball1OuterPoint);
			Segment ball1VelocitySegment = new Segment(ball1VelocityStart, ball1VelocityEnd);
			double ball0AngleBetweenVelocityAndCollision = ball0CollisionSegment.angleBetween(ball0VelocitySegment);
			double ball1AngleBetweenVelocityAndCollision = ball1CollisionSegment.angleBetween(ball1VelocitySegment);
			//System.out.println("0: " + ball0AngleBetweenVelocityAndCollision);
			//System.out.println("1: "+ ball1AngleBetweenVelocityAndCollision);
			double ball0VelocityAmountTransfered = 1 - Math.abs(ball0AngleBetweenVelocityAndCollision) / 90;
			double ball1VelocityAmountTransfered = 1 - Math.abs(ball1AngleBetweenVelocityAndCollision) / 90;
			//System.out.println(ball0VelocityAmountTransfered);
			Point ball0VelocityTransferred = new Point(ball0.getXVelocity() * ball0VelocityAmountTransfered, ball0.getYVelocity() * ball0VelocityAmountTransfered);
			Point ball1VelocityTransferred = new Point(ball1.getXVelocity() * ball1VelocityAmountTransfered, ball1.getYVelocity() * ball1VelocityAmountTransfered);
			Point ball0NewVelocity = new Point(ball0.getXVelocity() - ball0VelocityTransferred.getX() + ball1VelocityTransferred.getX(), ball0.getYVelocity() - ball0VelocityTransferred.getY() + ball1VelocityTransferred.getY());
			Point ball1NewVelocity = new Point(ball1.getXVelocity() - ball1VelocityTransferred.getX() + ball0VelocityTransferred.getX(), ball1.getYVelocity() - ball1VelocityTransferred.getY() + ball0VelocityTransferred.getY());
			Point ball0NewVelocityEnd = new Point(ball0VelocityStart.getX() + ball0NewVelocity.getX(), ball0VelocityStart.getY() + ball0NewVelocity.getY());
			Point ball1NewVelocityEnd = new Point(ball1VelocityStart.getX() + ball1NewVelocity.getX(), ball1VelocityStart.getY() + ball1NewVelocity.getY());
			g.setColor(Color.BLUE);
			drawSegment(g, ball0VelocityStart, ball0NewVelocityEnd.rotate(ball0AngleBetweenVelocityAndCollision, ball0VelocityStart));
			drawSegment(g, ball1VelocityStart, ball1NewVelocityEnd.rotate(ball1AngleBetweenVelocityAndCollision, ball1VelocityStart));
		}

		public boolean checkCollision() {
			return Physics9.distance(ball0.getPositionAsPoint(), ball1.getPositionAsPoint()) <= ball0.getRadius() + ball1.getRadius();
		}

		private void circleSelectedVertex(Graphics g){
			drawPoint(g, balls.get(selectedBallIndex).getPositionAsPoint(), 3);
		}

	}

	public void updateVertexSelection(MouseEvent e){
		Point mousepoint = new Point(e.getX(), e.getY());
		selectedBallIndex = -1;
		//		selectedVertexIndex = -1;
		Point potentialSelectedBall;
		int index = 0;
		for( /* we will use the variable 'index', which is already initialized above */ ; index < balls.size(); index++){
			//			for(int j = 0; j < shapes.get(index).getNumberOfVertices(); j++){
			potentialSelectedBall = balls.get(index).getPositionAsPoint();
			if(Physics9.distanceNoSqrt(potentialSelectedBall, mousepoint) <= (vertexCircleRadius * vertexCircleRadius)){
				//					/*
				//					 * note that we didn't calculate the square root in the distance formula, but we instead squared
				//					 * the value that we are comparing this distance to.
				//					 */
				selectedBallIndex = index;
				//					selectedVertexIndex = j;
			}
			//			}
		}
	}

	public MouseMotionListener getMouseMotionListener(){
		class GeneralMouseMotionListener implements MouseMotionListener{

			@Override
			public void mouseDragged(MouseEvent e) {
				if(selectedBallIndex != -1){
					//int ballRadius = balls.get(selectedBallIndex).getRadius();
					balls.get(selectedBallIndex).setPosition(new Point(e.getX(), e.getY()));
					//					for(int j = 0; j < shapes.size(); j++){
					//						shapes.get(j).initializeEdges();
					//						shapes.get(j).initializeAxes();
					//					}
				}
				//				initializeSAT();
				//				colliding = runSAT();
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

	private MouseListener getMouseListener(){
		class GeneralMouseListener implements MouseListener{

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				//				if(selectedBallIndex != -1){
				//					vertexDragStartPoint = new Point(e.getX(), e.getY());
				//				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		}
		return new GeneralMouseListener();

	}

	public static void main(String [] args){

		//wall at 45 degrees, path straight line
		//		Point vertex0 = new Point(150, 130);
		//		Point vertex1 = new Point(210, 150);
		//		Point vertex2 = new Point(170, 160);
		//		Point[] vertexList0 = new Point[3];
		//		vertexList0[0] = vertex0;
		//		vertexList0[1] = vertex1;
		//		vertexList0[2] = vertex2;
		//		Shape shape0 = new Shape(vertexList0);
		//
		//		Point vertex3 = new Point(150, 120);
		//		Point vertex4 = new Point(210, 187);
		//		Point vertex5 = new Point(170, 200);
		//		Point[] vertexList1 = new Point[3];
		//		vertexList1[0] = vertex3;
		//		vertexList1[1] = vertex4;
		//		vertexList1[2] = vertex5;
		//		Shape shape1 = new Shape(vertexList1);
		Ball ball0 = new Ball(100, 100, 20, 0, Color.BLUE);
		ball0.setRadius(10);
		Ball ball1 = new Ball(110, 110, 0, 20, Color.RED);
		ball1.setRadius(10);
		TwoBallCollisionDynamic is3 = new TwoBallCollisionDynamic(ball0, ball1);
	}

}
