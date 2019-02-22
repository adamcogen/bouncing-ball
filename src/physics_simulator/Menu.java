package physics_simulator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
/**
 * The Menu class, which initializes the JMenuBar at the top of the simulation,
 * manages all action listeners within it, and makes the necessary method calls
 * when buttons are pressed.
 * The getMenuBar() method returns the JMenuBar instance that is to appear at the
 * top of the simulation window.
 * 
 * @author Adam Cogen
 *
 */
public class Menu {
	private Simulator sim; //the simulation that this Menu has been instantiated within
	private JMenuItem fileSave; //the "file-->save" button
	private JMenuItem fileLoad; //the "file-->load" button
	private JMenuItem fileNew; //the "file-->new" button
	private JMenuItem fileLoadInitial; //the "file-->load sample file" button
	private JMenuItem physicsReset; //the "physics-->reset balls" button
	private JMenuItem physicsRunPause; //the "physics-->run / pause simulation" button
	private JMenuItem drawTriangle; //the "draw-->triangle" button
	private JMenuItem drawRectangle; //the "draw-->quadrilateral" button
	private JMenuItem editMoveVertex; //the "edit-->move vertex" button
	private JMenuItem editMoveShape; //the "edit-->move shape" button
	private JMenuItem editSelectDelete; //the "edit-->select / delete shape" button
	/**
	 * Constructor for the Menu class, which stores as a field 
	 * the Simulator instance that has invoked this constructor.
	 * It is useful to pass this Simulator as a parameter to the
	 * Menu class so that the Menu can make method calls to methods
	 * within the Simulator class, which will access instances of 
	 * other classes that are kept track of by the Simulator class.
	 * @param initSim the Simulator instance that has invoked this constructor
	 */
	public Menu(Simulator initSim){
		sim = initSim;
	}

	/**
	 * Initialize and return the JMenuBar that will be appear at the top of 
	 * simulation window.
	 * @return the JMenuBar that will appear at the top of the simulation window
	 */
	public JMenuBar getMenuBar(){
		//create the JMenuBar that will be returned
		JMenuBar menubar = new JMenuBar();
		//create the submenus that will be on the JMenuBar
		JMenu fileMenu = new JMenu("File");
		JMenu physicsMenu = new JMenu("Physics");
		JMenu drawMenu = new JMenu("Draw");
		JMenu editMenu = new JMenu("Edit");
		//initiailize the buttons that will be in the JMenuBar submenus
		fileSave = new JMenuItem("Save File...");
		fileLoad = new JMenuItem("Load File...");
		fileNew = new JMenuItem("New File...");
		fileLoadInitial = new JMenuItem("Reload Sample File...");
		physicsReset = new JMenuItem("Reset Balls...");
		physicsRunPause = new JMenuItem("Pause Simulation...");
		drawTriangle = new JMenuItem("Draw Triangle...");
		drawRectangle = new JMenuItem("Draw Quadrilateral...");
		editMoveVertex = new JMenuItem("Move Vertex...");
		editMoveShape = new JMenuItem("Move Shape...");
		editSelectDelete = new JMenuItem("Select / Delete Shape...");
		//add the ActionListener to the JMenuBar's buttons
		addActionListenerToMenuButtons(getActionListener());
		//add each button to each JMenuBar submenu
		fileMenu.add(fileSave);
		fileMenu.add(fileLoad);
		fileMenu.add(fileNew);
		fileMenu.add(fileLoadInitial);
		physicsMenu.add(physicsReset);
		physicsMenu.add(physicsRunPause);
		drawMenu.add(drawTriangle);
		drawMenu.add(drawRectangle);
		editMenu.add(editMoveVertex);
		editMenu.add(editMoveShape);
		editMenu.add(editSelectDelete);
		//add each submenu to the JMenuBar
		menubar.add(fileMenu);
		menubar.add(physicsMenu);
		menubar.add(drawMenu);
		menubar.add(editMenu);
		//return the JMenuBar
		return menubar;
	}

	/**
	 * Return the ActionListener that will be added to all buttons in the JMenuBar,
	 * and will be called each time a button is clicked.
	 * @return the ActionListener that will be added to all buttons in the JMenuBar
	 */
	private ActionListener getActionListener(){
		/**
		 * 
		 * This inner class is the ActionListener that will be added to all of the buttons
		 * in the simulation menu. The ActionListener will determine which button was 
		 * pressed, and make the appropriate method calls, etc.
		 * Some buttons call methods that are implemented in the Simulator class, while 
		 * others call methods that are implemented within the Menu class. The simulator
		 * has access to all of the other parts of the simulation, such as the Map class,
		 * the shapes list, the list of balls, etc., while all of this information is not
		 * necessary to properly handle some buttons. There are many methods in the 
		 * Simulator class that are written to handle button presses in the menu; those
		 * are called from within this ActionListener. 
		 * 
		 * 
		 * @author Adam Cogen
		 *
		 */
		class MenuButtonListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == fileSave){
					/*
					 * The "file-->save" button was pressed. call the Simulator.saveMap() method.
					 */
					sim.saveMap();
				} else if(e.getSource() == fileLoad){
					/*
					 * The "file-->load" button was pressed. call the Simulator.loadMapFromFileChooser() method.
					 */
					sim.loadMapFromFileChooser();
				} else if(e.getSource() == fileNew){
					/*
					 * The "file-->new" button was pressed. call the Simulator.newMap() method.
					 */
					sim.newMap();
				} else if(e.getSource() == fileLoadInitial){
					/*
					 * The "file-->load sample file" button was pressed. call the Simulator.loadInitialMap() method.
					 */
					sim.loadInitialMap();
				} else if(e.getSource() == physicsReset){
					/*
					 * The "physics-->reset balls" button was pressed. call the Simulator.resetBalls() method. 
					 */
					sim.resetBalls();
				} else if(e.getSource() == physicsRunPause){
					//the "physics-->run/pause simulation" button was pressed, which toggles whether the simulation is running or paused.
					if(sim.getMode() != 0 || sim.getSubmode() != 0){ 
						//if the simulation is not running, and the physicsRunPause button was pressed, run the simulation
						runSimulation(); //this method is implemented within the Menu class
					} else { 
						//otherwise, the simulation is already running, and the physicsRunPause button was pressed, so pause the simulation
						pauseSimulation(); //this method is implemented within the Menu class
					}
				} else if(e.getSource() == drawTriangle){
					/*
					 * The "draw-->triangle" button was pressed. Pause the simulation, set the Simulator's 
					 * mode and submode accordingly, and set the number of vertices to draw to three 
					 * (by calling a method in the Simulator).
					 */
					pauseSimulation();
					sim.setNumberOfVerticesToDraw(3);
					sim.setMode(1);
					sim.setSubmode(0);
				} else if(e.getSource() == drawRectangle){
					/*
					 * The "draw-->rectangle" button was pressed. Pause the simulation, set the Simulator's 
					 * mode and submode accordingly, and set the number of vertices to draw to four 
					 * (by calling a method in the Simulator).
					 */
					pauseSimulation();
					sim.setNumberOfVerticesToDraw(4);
					sim.setMode(1);
					sim.setSubmode(1);
				} else if(e.getSource() == editMoveVertex){
					/*
					 * The "edit-->move vertex" button was pressed. Pause the simulation, 
					 * and set the Simulator's mode and submode accordingly.
					 */
					pauseSimulation();
					sim.setMode(2);
					sim.setSubmode(0);
				} else if(e.getSource() == editMoveShape){
					/*
					 * The "edit-->move shape" button was pressed. Pause the simulation, 
					 * and set the Simulator's mode and submode accordingly.
					 */
					pauseSimulation();
					sim.setMode(2);
					sim.setSubmode(1);
				} else if(e.getSource() == editSelectDelete){
					/*
					 * The "edit-->move select / delete shape" button was pressed. Pause
					 * the simulation, and set the Simulator's mode and submode accordingly.
					 */
					pauseSimulation();
					sim.setMode(2);
					sim.setSubmode(2);
				} else {
					//none of the buttons that were just checked for were clicked
					System.out.println("An unimplemented button was clicked");
				}
				/*
				 * Whenever a button is pressed, show the delete menu if it 
				 * is the delete mode button, or else hide the delete menu.
				 * For more information about the delete menu, see the 
				 * DeleteMenu class.
				 */
				if(e.getSource() == editSelectDelete){
					sim.enableDeleteMenu(true);
				} else {
					sim.enableDeleteMenu(false);
				}
			}
		}
		return new MenuButtonListener();
	}
	
	/**
	 * Adjust the simulation as if the "Run Simulation" button was just pressed.
	 * This includes adjusting the mode and submode fields in the Simulator class,
	 * and adjusting the text of the physicsRunPause button.
	 */
	public void runSimulation(){
		sim.setMode(0);
		sim.setSubmode(0);
		physicsRunPause.setText("Pause Simulation...");
	}
	
	/**
	 * Adjust the simulation as if the "Pause Simulation" button was just pressed.
	 * This includes adjusting the mode and submode fields in the Simulator class,
	 * and adjusting the text of the physicsRunPause button.
	 */
	public void pauseSimulation(){
		sim.setMode(0);
		sim.setSubmode(1);
		physicsRunPause.setText("Run Simulation...");
	}
	
	/**
	 * Add the appropriate ActionListener to each button in the menu.
	 * This is made it into its own method for the sake of readability.
	 * @param listener the ActionListener to add to each button in the menu
	 */
	private void addActionListenerToMenuButtons(ActionListener listener){
		fileSave.addActionListener(listener);
		fileLoad.addActionListener(listener);
		fileNew.addActionListener(listener);
		fileLoadInitial.addActionListener(listener);
		physicsReset.addActionListener(listener);
		physicsRunPause.addActionListener(listener);
		drawTriangle.addActionListener(listener);
		drawRectangle.addActionListener(listener);
		editMoveVertex.addActionListener(listener);
		editMoveShape.addActionListener(listener);
		editSelectDelete.addActionListener(listener);
	}
	
	/*
	 * some notes:
	 * 
	 * physics submenu functions:
	 * 		reset ball positions
	 * 		pause / unpause
	 * 
	 * edit submenu functions: 
	 * 		move vertices
	 * 		move shapes
	 * 		select shapes
	 * 			delete shapes
	 * 		rotate shapes
	 * 		edit players
	 * 			color, position, x and y velocity
	 * 
	 * draw submenu functions:
	 * 		draw shapes
	 * 			rectangles
	 * 			triangles
	 * 		draw player
	 * 			color, position, x and y velocity
	 * 
	 */
}
