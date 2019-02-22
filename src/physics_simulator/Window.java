package physics_simulator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
/**
 * The main window in which the simulation is run.
 * Keeps track of mouse clicks and mouse motion, draws all shapes and players, 
 * and performs different functions depending on which mode the simulation
 * is running in. 
 * @author Adam Cogen
 *
 */
public class Window extends JFrame{

	private WindowDrawPanel panel;
	private int frame_height = 500;
	private int frame_width = 500;
	private int clickMouseX = 0;
	private int clickMouseY = 0;
	private int unclickMouseX = 0;
	private int unclickMouseY = 0;
	private int dragMouseX = 0;
	private int dragMouseY = 0;
	private boolean drawVector = false;
	private boolean drawPath = false;
	private static final int MOUSE_DRAW_X_OFFSET = -1;
	private static final int MOUSE_DRAW_Y_OFFSET = -23;
	private boolean colliding;
	private Point path0 = new Point(0, 0);
	private Point path1 = new Point(0,0);
	private Point closestPlayers = new Point(-1, -1);
	private boolean showClosestPair;
	private Color closestPairColor;
	private boolean showMouseCoordinates = false;
	private int mouseX = 0;
	private int mouseY = 0;
	private ArrayList<Ball> players; //a list of each ball in the simulation
	private ArrayList<Shape> shapes; //a list of each shape (obstacle) in the simulation
	private int mode = 0;
	private int submode = 0;
	private Map map;
	private Draw drawmode;
	private Edit editmode;
	private int editModeSelectedShapeIndex;
	private int editModeSelectedVertexIndex;
	private boolean DRAW_HIT_BOXES = false;
	//private int permanentSelectedShapeIndex;

	public Window(Map initMap, Draw initDrawmode, Edit initEditmode, JMenuBar menu){
		map = initMap;
		drawmode = initDrawmode;
		editmode = initEditmode;
		colliding = false;
		panel = new WindowDrawPanel();
		this.add(panel);
		this.setJMenuBar(menu);
		//this.setSize(map.getHeight(), map.getWidth());
		//Dimension size = new Dimension(FRAME_HEIGHT, FRAME_WIDTH);
		//this.setPreferredSize(size);
		//this.setMinimumSize(size);
		//this.setMaximumSize(size);
		//this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		shapes = map.getShapeList();
		players = map.getPlayerList();

		panel.addMouseListener(createMouseListener());

		panel.addMouseMotionListener(createMouseMotionListener());

		panel.setPreferredSize(new Dimension(map.getHeight(), map.getWidth()));
		this.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width / 2) - (this.getWidth() / 2), 0);
		this.setVisible(true);
	}

	private MouseListener createMouseListener(){
		class VectorClickListener implements MouseListener {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mode == 0){

				} else if (mode == 1){
					drawModeMouseClicked(e);
				} else if (mode == 2){
					editModeMouseClicked(e);
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(mode == 0){
					bounceModeMousePressed(e);
				} else if (mode == 1){

				} else if (mode == 2){

				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(mode == 0){
					bounceModeMouseReleased(e);
				} else if (mode == 1){

				} else if (mode == 2){

				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				//do nothing
			}
			@Override
			public void mouseExited(MouseEvent e) {
				//do nothing
			}
		}
		return new VectorClickListener();
	}

	private MouseMotionListener createMouseMotionListener(){
		class VectorDragListener implements MouseMotionListener {
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = getMouseX(e);
				mouseY = getMouseY(e);
				if(mode == 0){
					bounceModeMouseDragged(e);
				} else if (mode == 1){

				} else if (mode == 2){
					editModeMouseDragged(e);
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = getMouseX(e);
				mouseY = getMouseY(e);
				if(mode == 0){

				} else if (mode == 1){

				} else if (mode == 2){
					editModeMouseMoved(e);
				}
			}
		}
		return new VectorDragListener();
	}

	private void editModeMouseClicked(MouseEvent e){
		if(editModeSelectedShapeIndex != -1 && editModeSelectedVertexIndex != -1 && submode == 2){
			editmode.setPermanentlySelectedShapeIndex(editModeSelectedShapeIndex);
		} else {
			editmode.setPermanentlySelectedShapeIndex(-1);
		}
	}

	private void editModeMouseDragged(MouseEvent e){
		if(editModeSelectedShapeIndex != -1 && editModeSelectedVertexIndex != -1){
			if(submode == 0){
				editmode.moveShapeVertex(editModeSelectedShapeIndex, editModeSelectedVertexIndex, new Point(getMouseX(e), getMouseY(e)));
			} else if(submode == 1){
				editmode.moveShape(editModeSelectedShapeIndex, editModeSelectedVertexIndex, new Point(getMouseX(e), getMouseY(e)));
			}
		}
	}

	private void editModeMouseMoved(MouseEvent e){
		editmode.setMousePosition(new Point(mouseX, mouseY));
		editModeSelectedShapeIndex = editmode.getSelectedShapeIndex();
		editModeSelectedVertexIndex = editmode.getSelectedVertexIndex();
	}

	private void drawModeMouseClicked(MouseEvent e){
		drawmode.incrementShapeDrawStep();
		Point vertex = new Point(getMouseX(e), getMouseY(e));
		drawmode.addVertexToCurrentShape(vertex);
	}

	private void bounceModeMouseDragged(MouseEvent e){
		dragMouseX = getMouseX(e);
		dragMouseY = getMouseY(e);
		drawVector = true;
	}

	private void bounceModeMousePressed(MouseEvent e){
		clickMouseX = getMouseX(e);
		clickMouseY = getMouseY(e);
		for(int i = 0; i < players.size(); i++){
			players.get(i).setXVelocity(0);
			players.get(i).setYVelocity(0);
			players.get(i).freeze();
		}
	}

	private void bounceModeMouseReleased(MouseEvent e){
		unclickMouseX = getMouseX(e);
		unclickMouseY = getMouseY(e);
		double xChange = clickMouseX - unclickMouseX;
		double yChange = clickMouseY - unclickMouseY;
		//xChange /= 2;
		//yChange /= 2;
		for(int i = 0; i < players.size(); i++){
			if(yChange == 0){
				yChange = 0.1;
			}
			players.get(i).setXVelocity(xChange / 2);
			players.get(i).setYVelocity(yChange / 2);
			players.get(i).unfreeze();
		}
		drawVector = false;
	}

	public void showMouseCoordinates(boolean show){
		showMouseCoordinates = show;
	}

	public int plrXCoord(int index){
		return (int) players.get(index).getXPosition();
	}

	public int plrYCoord(int index){
		//return (int) (players.get(index).getYPosition());
		return (int) (players.get(index).getYPosition());
	}

	public int getMouseX(MouseEvent e){
		return e.getX();// + MOUSE_DRAW_X_OFFSET;
	}

	public int getMouseY(MouseEvent e){
		return e.getY();// + MOUSE_DRAW_Y_OFFSET;
	}

	public void setColliding(int tf){
		if (tf <= 0){
			colliding = false;
		} else {
			colliding = true;
		}
	}

	public void setPath(Point initPath0, Point initPath1){
		path0 = initPath0;
		path1 = initPath1;
	}

	public void setClosestPlayers(Point initClosestPlayers){
		closestPlayers = initClosestPlayers;
	}

	public void setShowClosestPair(boolean show) {
		showClosestPair = show;
	}

	public void setClosestPairColor(Color clr) {
		closestPairColor = clr;
	}

	public void setMode(int initMode, int initSubmode){
		mode = initMode;
		submode = initSubmode;
	}

	public void setSelectedShape(int shapeIndex){
		editModeSelectedShapeIndex = shapeIndex;
	}

	public void setSelectedVertex(int vertexIndex){
		editModeSelectedVertexIndex = vertexIndex;
	}

	class WindowDrawPanel extends DrawPanel{
		public void paintComponent(Graphics g){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 800, 800);
			if(mode == 0 || mode == 1 || mode == 2){
				for(int i = 0; i < players.size(); i++){
					drawPlayer(g, i);
				}
			}
			for(int i = 0; i < shapes.size(); i++){
				g.setColor(Color.BLACK);
				drawShape(g, 0, shapes.get(i));
				if(DRAW_HIT_BOXES) {
					Point a = new Point(shapes.get(i).getXMin(), shapes.get(i).getYMin());
					Point b = new Point(shapes.get(i).getXMax(), shapes.get(i).getYMin());
					Point c = new Point(shapes.get(i).getXMax(), shapes.get(i).getYMax());
					Point d = new Point(shapes.get(i).getXMin(), shapes.get(i).getYMax());
					Point[] tmp = {a, b, c, d};
					g.setColor(Color.RED);
					drawShape(g, 0, new Shape(tmp));
					g.setColor(Color.BLACK);
				}
			}
			if(mode == 1){
				int shapeDrawStep = drawmode.getShapeDrawStep();
				//shapeDraw step 0: don't draw anything
				//shapeDraw step 1: draw segment from first vertex to mousepoint
				//shapeDraw step 2: draw segment from first to second point, and second point to mousepoint
				//shapeDraw last step: draw segment from first point to mousepoint, and second to last point to mousepoint
				if(shapeDrawStep > 0){
					int i = 0;
					while(shapeDrawStep > 1 && i < shapeDrawStep - 1){
						drawSegment(g, drawmode.getCurrentShapeVertex(i), drawmode.getCurrentShapeVertex(i + 1));
						i++;
					}
					if(shapeDrawStep == drawmode.getMaxShapeDrawStep()){
						drawSegment(g, drawmode.getCurrentShapeVertex(0), new Point(mouseX, mouseY));
					}
					drawSegment(g, drawmode.getCurrentShapeVertex(shapeDrawStep - 1), new Point(mouseX, mouseY));
				}
			}

			if(mode == 2 && editModeSelectedShapeIndex != -1 && editModeSelectedVertexIndex != -1){
				//shapes.get(editModeSelectedShapeIndex).getVertex(editModeSelectedVertexIndex)
				if(submode == 0){
					drawPoint(g, shapes.get(editModeSelectedShapeIndex).getVertex(editModeSelectedVertexIndex), editmode.getVertexCircleRadius());
				} else if(submode == 1 || submode == 2){
					for(int i = 0; i < shapes.get(editModeSelectedShapeIndex).getNumberOfVertices(); i++){
						drawPoint(g, shapes.get(editModeSelectedShapeIndex).getVertex(i), editmode.getVertexCircleRadius());
					}
				}
			} if (mode == 2){
				if(submode == 2){
					int permanentSelectedShapeIndex = editmode.getPermanentlySelectedShapeIndex();
					if(permanentSelectedShapeIndex != -1){
						for(int i = 0; i < shapes.get(permanentSelectedShapeIndex).getNumberOfVertices(); i++){
							drawPoint(g, shapes.get(permanentSelectedShapeIndex).getVertex(i), editmode.getVertexCircleRadius());
						}
					}
				}
			}

			if(mode == 0 && drawPath){
				g.setColor(Color.BLACK);
				g.drawLine((int) path0.getX(), (int) path0.getY(), (int) path1.getX(), (int) path1.getY());
			}
			if(mode == 1 || mode == 2){
				drawMouseCoordinates(g);
			}
		}

		public void drawMouseCoordinates(Graphics g){
			g.drawString(mouseX + ", " + mouseY, 5, 15);
		}

		public void drawPlayer(Graphics g, int index){
			Color ballColor = players.get(index).getColor();
			int radius = players.get(index).getRadius();
			if(showClosestPair && (index == closestPlayers.getX() || index == closestPlayers.getY())){
				g.setColor(closestPairColor);
			} else {
				g.setColor(ballColor);
			}
			g.fillOval(plrXCoord(index) - radius, plrYCoord(index) - radius, radius * 2, radius * 2);
			g.setColor(Color.BLACK);
			g.drawOval(plrXCoord(index) - radius, plrYCoord(index) - radius, radius * 2, radius * 2);
			if(drawVector){
				//g.setColor(Color.BLACK);
				g.drawLine(clickMouseX, clickMouseY, dragMouseX, dragMouseY);
			}
		}
	}
}

