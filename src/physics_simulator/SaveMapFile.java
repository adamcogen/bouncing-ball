package physics_simulator;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Save a map as a file, in the correct format to be read by the ReadMapFile class.
 * Map files are saved with built-in annotations so that they are very easy
 * to read and modify.
 * 
 * @author Adam Cogen
 *
 */
public class SaveMapFile {
	private Map map; //the Map instance that we are creating a file from
	private String filename; //a String containing the file path of the file to save
	
	public SaveMapFile(Map initMap, String initFilename){
		filename = initFilename;
		map = initMap;
		saveFile();
	}
	
	private void saveFile(){
		PrintWriter pw;
		try {
			pw = new PrintWriter(filename);
			saveMapProperties(pw);
			savePlayers(pw);
			saveShapes(pw);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println("There was a problem with saving the map file.");
		}
	}
	
	private void saveMapProperties(PrintWriter pw){
		pw.write("height: " + map.getHeight() + "\n");
		pw.write("width: " + map.getWidth() + "\n");
		pw.write("draw_border: " + map.getDrawBorder() + "\n");
		pw.write("\n");
	}
	
	private void savePlayers(PrintWriter pw){
		int numberOfPlayers = map.getPlayerList().size();
		Point initialPosition;
		Point initialVelocity;
		Color color;
		pw.write("players: " + numberOfPlayers + "\n");
		for(int i = 0; i < numberOfPlayers; i++){
			initialPosition = map.getPlayerList().get(i).getInitialPosition();
			initialVelocity = map.getPlayerList().get(i).getInitialVelocity();
			color = map.getPlayerList().get(i).getColor();
			pw.write("\n");
			pw.write("start_x_coordinate: " + initialPosition.getX() + "\n");
			pw.write("start_y_coordinate: " + initialPosition.getY() + "\n");
			pw.write("start_x_velocity: " + initialVelocity.getX() + "\n");
			pw.write("start_y_velocity: " + initialVelocity.getY() + "\n");
			pw.write("color_red_value: " + color.getRed() + "\n");
			pw.write("color_green_value: " + color.getGreen() + "\n");
			pw.write("color_blue_value: " + color.getBlue() + "\n");
		}
	}
	
	private void saveShapes(PrintWriter pw){
		int numberOfShapes = map.getShapeList().size();
		int numberOfVertices;
		Point vertex;
		boolean drawBorder = map.getDrawBorder();
		int i = 0;
		if(drawBorder){
			numberOfShapes--;
			i++;
		}
		pw.write("\n" + "shapes: " + numberOfShapes + "\n");
		for( ; i < map.getShapeList().size(); i++){
			numberOfVertices = map.getShapeList().get(i).getNumberOfVertices();
			pw.write("\n");
			pw.write("number_of_vertices: " + numberOfVertices + "\n");
			for(int j = 0; j < numberOfVertices; j++){
				vertex = map.getShapeList().get(i).getVertex(j);
				pw.write("vertex: " + vertex.getX() + " " + vertex.getY() + "\n");
			}
		}
	}
	
	public String getFileName(){
		return filename;
	}
}
