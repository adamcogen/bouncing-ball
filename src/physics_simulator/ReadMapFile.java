package physics_simulator;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Read a simulator map file and store the map data as fields.
 * 
 * @author Adam Cogen
 *
 */
public class ReadMapFile {
	private int height; //the map's height
	private int width; //the map's width
	private ArrayList<Ball> players; //a list of each ball in the simulation
	private ArrayList<Shape> shapes; //a list of each shape (obstacle) in the simulation
	private boolean drawBorder; //should an unpassable, uneditable rectangular border be drawn 20 pixels in from the edge of the map?

	/**
	 * Read the Map data from a file.
	 * A String describing the file path is passed in as a parameter.
	 * The shape list and players list are passed as parameters as well
	 * because they are initialized within the Map class, not here.
	 * @param initPlayers a list of all of the balls in the simulation
	 * @param initShapes a list of all of the shapes in the simulation. 
	 * 		  if drawBorder is specified as true in the file, the border is already
	 * 		  added to this list at index 0, by the Map class before the list is 
	 * 		  passed to this constructor.
	 * @param filename a String describing the file path of this map file
	 */
	public ReadMapFile(ArrayList<Ball> initPlayers, ArrayList<Shape> initShapes, String filename){
		players = initPlayers;
		shapes = initShapes;
		readFile(filename);
	}

	/**
	 * Read all of the necessary Map data stored in the file. 
	 * @param filename a String describing the file path of the Map file
	 */
	private void readFile(String filename){
		Scanner scan;
		try {
			scan = new Scanner(new FileReader(filename));
			readHeight(scan);
			readWidth(scan);
			readDrawBorder(scan);
			readPlayers(scan);
			readShapes(scan);
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (InputMismatchException e){
			System.out.println("The contents of this file do not match the map file format.");
		}
	}

	/**
	 * Read the height of the map from the file.
	 * @param scan
	 */
	private void readHeight(Scanner scan){
		scan.next(); //read "height:"
		height = scan.nextInt(); //read height value
		scan.nextLine(); //skip any additional text at the end of the line
	}

	/**
	 * Read the width of the map from the file.
	 * @param scan
	 */
	private void readWidth(Scanner scan){
		scan.next(); //read "width: "
		width = scan.nextInt(); //read width value
		scan.nextLine(); //skip any additional text at the end of the line
	}

	/**
	 * Read the draw_border field from the map file.
	 * This is a true or false value indicating whether to draw an automatic uneditable 
	 * rectangular border 20 pixels in from the edge of the map.
	 * @param scan
	 */
	private void readDrawBorder(Scanner scan){
		scan.next(); //read "draw_border: "
		drawBorder = scan.nextBoolean(); //read drawBorder value
		scan.nextLine(); //skip any additional text at the end of the line
	}
	
	/**
	 * Is the drawBorder field in this map file true or false?
	 * Should an uneditable rectangular border automatically be drawn 20 pixels in 
	 * from the edge of the map?
	 * @return Return the boolean value of the drawBorder field for this Map
	 */
	public boolean getDrawBorder(){
		return drawBorder;
	}

	/**
	 * Read the ball data from the map file, including the number
	 * of balls in the simulation and the attributes of each ball.
	 * @param scan
	 */
	private void readPlayers(Scanner scan){

		double start_x_position;
		double start_y_position;
		double start_x_velocity;
		double start_y_velocity;
		int color_red_value; //the red portion of this ball's RGB color
		int color_green_value; //the green portion of this ball's RGB color
		int color_blue_value; //the blue portion of this ball's RGB color

		while(!scan.next().equalsIgnoreCase("players:")){
			scan.next(); //skip everything until you read "players: "
		}
		int numberOfPlayers = scan.nextInt(); //read the number of players
		for(int i = 0; i < numberOfPlayers; i++){
			scan.nextLine(); //skip the blank line separating each player in the file
			start_x_position = readNextDoubleValueFromFile(scan, 0);
			start_y_position = readNextDoubleValueFromFile(scan, 0);
			start_x_velocity = readNextDoubleValueFromFile(scan, 0);
			start_y_velocity = readNextDoubleValueFromFile(scan, 0);
			color_red_value = readNextColorValueFromFile(scan, Color.RED.getRed());
			color_green_value = readNextColorValueFromFile(scan, Color.RED.getGreen());
			color_blue_value = readNextColorValueFromFile(scan, Color.RED.getBlue());
			//add a ball to the simulation with the values that have just been read from the file
			players.add(new Ball(start_x_position, start_y_position, start_x_velocity, start_y_velocity, new Color(color_red_value, color_green_value, color_blue_value)));
		}
	}

	private double readNextDoubleValueFromFile(Scanner scan, double defaultValue){
		double value = defaultValue; //set value to default value
		scan.next(); //read "name_of_value: "
		if(scan.hasNextDouble()){ //check that the double value is next in the file. if not, use the default value
			value = scan.nextDouble(); //read the value
		}
		scan.nextLine(); //skip any additional text at the end of the line
		return value;
	}

	private int readNextColorValueFromFile(Scanner scan, int defaultValue){
		int value = defaultValue;
		scan.next(); //read "name_of_value: "
		if(scan.hasNextInt()){ //check that the int value is next in the file. if not, use the default value
			value = scan.nextInt(); //read the value
		}
		scan.nextLine(); //skip any additional text at the end of the line
		//adjust the value that was read in, to make sure that it is in the correct range to define a color (0 to 255, inclusive)
		if(value > 255){
			value = 255;
		} else if(value < 0){
			value = 0;
		}
		return value;
	}

	private void readShapes(Scanner scan){
		double xValue;
		double yValue;
		ArrayList<Point> currentShape = new ArrayList<Point>();
		while(!scan.next().equalsIgnoreCase("shapes:")){
			scan.next(); //skip everything until you read "shapes: "
		}
		int numberOfShapes = scan.nextInt(); //read the number of shapes
		int numberOfVertices = 0;
		for(int i = 0; i < numberOfShapes; i++){
			scan.nextLine(); //skip the blank line separating each shape in the file
			scan.next(); //read "number_of_vertices: "
			numberOfVertices = scan.nextInt(); //read the value number_of_vertices
			for(int j = 0; j < numberOfVertices; j++){
				currentShape.add(readNextPointFromFile(scan));
			}
			shapes.add(new Shape(currentShape));
			currentShape.clear();
		}
	}

	public Point readNextPointFromFile(Scanner scan){
		double xValue = 0;
		double yValue = 0;
		scan.next(); //read "vertex: "
		xValue = scan.nextDouble();
		yValue = scan.nextDouble();
		return new Point(xValue, yValue);
	}

	/**
	 * Return the height of this Map, as stored in the file.
	 * @return the height of this Map
	 */
	public int getHeight(){
		return height;
	}

	/**
	 * Return the width of this Map, as stored in the file.
	 * @return the width of this Map
	 */
	public int getWidth(){
		return width;
	}
}
