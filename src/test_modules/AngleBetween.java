package test_modules;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import physics_simulator.DrawPanel;
import physics_simulator.Point;
import physics_simulator.Segment;

/**
 * Test module for exemplifying the reflection of a Segment 
 * off of an angled surface (represented by another Segment),
 * using methods in the Segment class. 
 * Something very similar is done in order to calculate the
 * new velocity of a ball after colliding with a wall.
 * Click and drag to set the start point of the red segment,
 * and the blue segment will indicate its appropriate reflection
 * off of the angled surface. The start point of the red 
 * segment is circled, and the end point of the blue segment is 
 * circled, to indicate the direction of the paths before
 * and after reflection off of the wall. 
 * The angle between the red segment and the angled surface
 * is also printed to the console with each change of the
 * red segment's start point.
 * 
 * @author Adam Cogen
 *
 */
public class AngleBetween extends JFrame{

	private AngleDrawPanel panel;
	private static final int FRAME_HEIGHT = 150;
	private static final int FRAME_WIDTH = 150;
	private Segment s0, s1;
	private Segment reflection;
	private Segment floor;
	private double anglebtwn;
	private Point inters;

	public AngleBetween(Segment initS1){
		s1 = initS1;
		floor = new Segment (new Point(s1.getEndPoint().getX() - 100, 75), new Point(s1.getEndPoint().getX() + 100, 105));
		panel = new AngleDrawPanel();
		this.add(panel);
		this.setVisible(true);
		this.setSize(FRAME_HEIGHT, FRAME_WIDTH);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("angle");

		anglebtwn = s1.angleBetween(floor);
		//System.out.println(anglebtwn);

		reflection = s1.returnReflectionOffOf(floor);

		class GeneralMouseListener implements MouseListener {

			@Override
			public void mouseClicked(MouseEvent e) {
				handleClicks(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				//Do nothing
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				//Do nothing
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//Do nothing
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//Do nothing
			}
		}

		panel.addMouseListener(new GeneralMouseListener());

		class GeneralMouseMotionListener implements MouseMotionListener{

			@Override
			public void mouseDragged(MouseEvent e) {
				handleClicks(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				//Do nothing	
			}	
		}
		panel.addMouseMotionListener(new GeneralMouseMotionListener());
	}

	class AngleDrawPanel extends DrawPanel{
		public void paintComponent(Graphics g){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 510, 510);
			g.setColor(Color.BLACK);
			drawSegment(g, floor.getStartPoint(), floor.getEndPoint());
			g.setColor(Color.BLUE);
			drawSegment(g, s1.getStartPoint(), s1.getEndPoint());
			g.setColor(Color.BLACK);
			drawPoint(g, s1.getStartPoint(), 3);
			g.setColor(Color.RED);
			drawSegment(g, reflection.getStartPoint(), reflection.getEndPoint());
			g.setColor(Color.BLACK);
			drawPoint(g, reflection.getEndPoint(), 3);
			int round = (int) (anglebtwn * 100);
			anglebtwn = (double) (round / 100.0);
			g.drawString("" + anglebtwn + " degrees", 60, 120);
		}

	}
	
	public void handleClicks(MouseEvent e){
		s1 = new Segment(new Point(e.getX(), e.getY()), s1.getEndPoint());
		inters = s1.getIntersectionWith(floor);
		anglebtwn = s1.angleBetween(floor);
		//System.out.println(anglebtwn);
		reflection = s1.returnReflectionOffOf(floor);
		panel.repaint();
	}

	/**
	 * Main method
	 */
	public static void main(String [] args){
		Point pointMWall = new Point(65, 50);
		Point pointNWall = new Point(60, 90);
		Segment seg6Wall = new Segment(pointMWall, pointNWall);
		AngleBetween is3 = new AngleBetween(seg6Wall);
	}

}
