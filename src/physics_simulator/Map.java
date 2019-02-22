package physics_simulator;
import java.util.ArrayList;

/**
 * Stores data representing a map, to be opened, 
 * edited, saved, and run in the simulation.
 * 
 * @author Adam Cogen
 *
 */
public class Map {
	private boolean drawBorder = false; //should an unpassable, uneditable rectangular border be drawn 20 pixels in from the edge of the map?
	private int height; //the height of the map / simulation window
	private int width; //the width of the map / simulation window
	private ArrayList<Ball> players; //a list of each ball in the simulation
	private ArrayList<Shape> shapes; //a list of each shape (obstacle) in the simulation
	private ReadMapFile mapFileReader; //handles reading the data from an actual map .txt file upon loading
	private String filename; //the file path of the file that is currently open
	private SaveMapFile saveFile; //handles writing the data in this map to a .txt file upon saving

	/**
	 * Construct a new Map instance from a specified file path,
	 * leading to a .txt file with properly formatted map data.
	 * Store all relevant data in the appropriate fields.
	 * @param initFilename the file path of the map's .txt file, as a String
	 */
	public Map(String initFilename){
		filename = initFilename;
		players = new ArrayList<Ball>();
		shapes = new ArrayList<Shape>();
		mapFileReader = new ReadMapFile(players, shapes, filename);
		height = mapFileReader.getHeight();
		width = mapFileReader.getWidth();
		drawBorder = mapFileReader.getDrawBorder();
		if(drawBorder){ 
			/*
			 * If the map should have an automatically drawn uneditable rectangular border, add it as the 
			 * first shape in the shapes ArrayList. Then, when classes such as the Edit class iterate through 
			 * the shapes ArrayList looking for shape selections, they will skip over the first shape in the 
			 * list, the border, so that it cannot be edited. 
			 * Note that this border is drawn 20 pixels in from the edge of the Map on each side.
			 */
			ArrayList<Point> simulationBorder = new ArrayList<Point>();
			simulationBorder.add(new Point(20, 20));
			simulationBorder.add(new Point(width - 20, 20));
			simulationBorder.add(new Point(width - 20, height - 20));
			simulationBorder.add(new Point(20, height - 20));
			shapes.add(0, new Shape(simulationBorder));
		}

	}

	/**
	 * Make a SaveMapFile object for this Map instance, allowing it to be saved to the specified file path.
	 * This method is called from the Simulator class, where a save file dialog is created upon hitting the 
	 * "save file" button in the simulation.
	 * The method call in the Simulator class also makes sure that the file name ends in ".txt"
	 * @param initFilename
	 */
	public void save(String initFilename){
		filename = initFilename;
		saveFile = new SaveMapFile(this, filename); //pass the SaveMapFile instance this Map instance, and the file path for the new file, as a String
	}
	
	/**
	 * Return the ArrayList<Shape> representing the list of all shapes in this Map.
	 * @return the shapes ArrayList<Shape> from this Map
	 */
	public ArrayList<Shape> getShapeList(){
		return shapes;
	}

	/**
	 * Return the ArrayList<Player> representing the list of all balls in this Map.
	 * @return the players ArrayList<Player> from this Map
	 */
	public ArrayList<Ball> getPlayerList(){
		return players;
	}

	/**
	 * Return the height of this Map
	 * @return the height of this Map
	 */
	public int getHeight(){
		return height;
	}

	/**
	 * Return the width of this Map
	 * @return the width of this Map
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Return a boolean specifying whether or not this Map has an automatically drawn uneditable
	 * rectangular border drawn 20 pixels in from its edge.
	 * @return true if this Map has an automatically drawn border, false otherwise
	 */
	public boolean getDrawBorder(){
		return drawBorder;
	}
}
