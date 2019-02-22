package physics_simulator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.Timer;
/**
 * The Simulator class.
 * This is the central engine that manages data for the simulation.
 * This is where all other classes used by the simulator (the Window, 
 * Players, Segments, toolbox, etc.) are instantiated. 
 * Runs a timer, and manages which tasks are called during different
 * simulation modes. 
 * Also contains methods that are called by the buttons in simulation menus.
 * 
 * @author Adam Cogen
 *
 */
public class Simulator {

	private Timer simulationTimer; //the timer that allows ball positions to change over time
	private static final int TIMER_FREQUENCY = 10; //how often the physicsTimer ticks, in milliseconds. normally has the value 10.
	private Window window; //the window in which the simulation is displayed
	private Draw drawmode; //0 is bouncing ball, 1 is edit shapes, 2 is draw triangles
	private Edit editmode;
	private Physics9 physics;
	private Map map;
	final JFileChooser fc;
	private String filename;
	private Menu menu;
	private DeleteMenu deleteMenu;
	private boolean drawBorder = true;
	private int mode = 0;
	/*
	 * mode 0 is physics mode. mode 1 is draw mode. mode 2 is edit mode.
	 * in physics mode, submode 0 is running, submode 1 is paused
	 * in edit mode, submode 0 is move vertices, submode 1 is move shapes, submode 2 is select shapes,
	 * 				 to be implemented: submode 3 is edit players, submode 4 is rotate shapes
	 * in draw mode, submode 0 is draw triangles, submode 1 is draw rectangles, 
	 * 				 to be implemented: submode 2 is draw players
	 */
	private int submode = 0;
	//private ToolBox toolbox;

	public Simulator(){
		fc = new JFileChooser();
		fc.setFileFilter(new TextFileFilter());
		fc.setAcceptAllFileFilterUsed(false);
		//loadMapFromFileChooser();
		//loadInitialMap();
	}

	public void saveMap(){
		int returnVal = fc.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			filename = fc.getSelectedFile().getPath();
			if(!filename.endsWith(".txt")){
				filename += ".txt";
			}
			map.save(filename);
		} else {
			//save dialog cancelled
		}

	}

	public void loadMapFromFileChooser(){
		int returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			filename = fc.getSelectedFile().getPath();
			loadMapFromFilePath(filename);
		} else {
			//file chooser cancelled
		}
	}
	
	public void loadMapFromFilePath(String initFilename){
		filename = initFilename;
		
		mode = 0;
		submode = 0;
		if(window != null){
			window.setVisible(false);
		}
		map = new Map(filename);
		physics = new Physics9(map);
		drawmode = new Draw(map);
		editmode = new Edit(map);
		menu = new Menu(this);
		window = new Window(map, drawmode, editmode, menu.getMenuBar());
		window.setMode(mode, submode);
		deleteMenu = new DeleteMenu(this, window);
		//instantiate and start the physicsTimer
		if(simulationTimer != null){
			simulationTimer.stop();
		}
		simulationTimer = new Timer(TIMER_FREQUENCY, createSimulationTimer());
		simulationTimer.start();
		menu.runSimulation();
		refresh();
	}

	public void setMode(int initMode){
		mode = initMode;
	}

	/**
	 * Create and return the physicsTimer. 
	 * This method will be called once, inside of the Physics class constructor.
	 * @return
	 */
	private ActionListener createSimulationTimer(){
		/**
		 * This TimerListener inner class is where all of the physics and movement in the game are updated.
		 * The actionPerformed(ActionEvent e) method is called every time the in-game clock ticks,
		 * so that updates happen over time.
		 * This is where collision detecting method calls are made, velocities are changed after reflections
		 * off of walls, etc.
		 * 
		 * @author Adam Cogen
		 *
		 */
		class TimerListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.setMode(mode, submode);
				if(mode == 0 && submode == 0){
					physics.bounceModeClockTick();
				} else if (mode == 1){
					drawmode.drawModeClockTick();
				} else if (mode == 2){
					editmode.editModeClockTick();
					if(submode == 2 && editmode.getPermanentlySelectedShapeIndex() != -1){
						deleteMenu.setDeleteButtonEnabled(true);
					} else{
						deleteMenu.setDeleteButtonEnabled(false);
					}
				}
				//redraw the display of simulation window
				refresh();
			}
		}

		return new TimerListener();
	}

	/**
	 * A simple method that can be called to repaint the simulation window. 
	 * In other words, update the display to show current positions of balls, etc.
	 */
	private void refresh(){
		window.repaint();
	}
	
	public void loadInitialMap(){
		loadMapFromFilePath("maps/simulator_initial_file.txt");
	}
	
	public void newMap(){
		loadMapFromFilePath("maps/simulator_new_file.txt");
	}

	public int getMode(){
		return mode;
	}

	public int getSubmode(){
		return submode;
	}

	public void setSubmode(int initSubmode){
		submode = initSubmode;
	}

	public void resetBalls(){
		physics.resetBalls();
	}
	
	public void enableDeleteMenu(boolean enabled){
		deleteMenu.setDeleteMenuVisible(enabled);
	}
	
	public void setNumberOfVerticesToDraw(int number){
		drawmode.setNumberOfVerticesToDraw(number);
	}

	public void deleteShape(){
		if(editmode.getPermanentlySelectedShapeIndex() != -1){
			editmode.deleteShape(editmode.getPermanentlySelectedShapeIndex());
			editmode.setPermanentlySelectedShapeIndex(-1);
		}
	}

	public static void main(String [] args){
		Simulator sim = new Simulator();
	}

}
