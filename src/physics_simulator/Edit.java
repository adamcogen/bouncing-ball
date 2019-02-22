package physics_simulator;
import java.util.ArrayList;

/**
 * The Edit class. 
 * Edit mode keeps track of various settings used while editing shapes that
 * are already drawn. Also contains the necessary method to call on each 
 * clock tick in edit mode.
 * 
 * @author Adam Cogen
 *
 */
public class Edit {
	private boolean drawBorder; //should a non-editable rectangle border be drawn 20 pixels in from the edge of the map?
	private Map map; //the Map object that is currently open in the simulator
	/*
	 * When a vertex is selected, a circle is drawn around it. vertexCircleRadius defines what the radius of that circle
	 * should be. Also, when selecting a vertex in move vertex or move shape mode, putting your mouse within the inside
	 * of this circle allows that that vertex to be selected. Larger radius means that you can select the vertex from 
	 * further away.
	 */
	private int vertexCircleRadius = 3;
	private Point mousepoint;  //a Point representing the current position of the mouse. updated from the Window class whenever the mouse moves
	/*
	 * select a vertex and shape by mousing over a vertex within that shape.
	 * a value of -1 means that nothing has been selected.
	 */
	private int selectedShapeIndex = -1; //the index of the shape that is selected, in the shapes ArrayList within the Map class. 
	private int selectedVertexIndex = -1; //the index of the vertex that is selected, within the vertices[] array of the selected shape.
	/*
	 * The permanentlySelectedShapeIndex is the index of the shape within the map's shape ArrayList that is permanently selected.
	 * In edit-->select / delete shape mode, clicking causes a shape to remain selected, even if you move the mouse away. This is
	 * so that you can delete the selected shape by clicking the "Delete Shape" button. To deselect a shape without deleting it,
	 * you can click within the simulation somewhere besides that shape. When the value of this variable is -1, no shape is 
	 * permanently selected.
	 */
	private int permanentlySelectedShapeIndex = -1;

	/**
	 * Construct an instance of the Edit class, by passing in the Map instance that is currently open in the simulation.
	 * @param initMap the Map instance that is currently open in the simulation
	 */
	public Edit(Map initMap){
		map = initMap;
		drawBorder = map.getDrawBorder();
		//initialize the mouse position offscreen, so that if a vertex is present at the default initial location (0,0), it won't be selected
		mousepoint = new Point(-100, -100);
	}

	/**
	 * On each edit mode clock tick, check whether the mouse is over 
	 * the vertex of any shape. If it is, these vertices will be considered
	 * "selected". 
	 */
	public void editModeClockTick(){
		selectedShapeIndex = -1;
		selectedVertexIndex = -1;
		ArrayList<Shape> shapes = map.getShapeList();
		Point potentialSelectedVertex;
		int index;
		if(drawBorder){
			/*
			 * if there is an automatic border drawn on the Map, start iterating through the Map's shape list
			 * at the index = 1, so that the border rectangle can't be edited.
			 */
			index = 1;
		} else {
			//if there is no automatic border, start iterating through the Map's shapes list starting at index = 0.
			index = 0; 
		}
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

	/**
	 * Update the mousepoint variable, which keeps track of the current position of 
	 * mouse in the simulation.
	 * This method will be called by the Window class each time the mouse is moved.
	 * @param initMousePoint
	 */
	public void setMousePosition(Point initMousePoint){
		mousepoint = initMousePoint;
	}

	/**
	 * A shape is selected when the mouse is over a vertex of that shape.
	 * Return the index of that shape within the Map's shapes ArrayList.
	 * @return the index of the selected shape within the Map's shapes ArrayList.
	 */
	public int getSelectedShapeIndex(){
		return selectedShapeIndex;
	}

	/**
	 * A vertex is selected when the mouse is over that vertex. 
	 * Return the index of that vertex within the shape's vertices[] array.
	 * @return the index of the selected vertex within the shape's vertices[] array.
	 */
	public int getSelectedVertexIndex(){
		return selectedVertexIndex;
	}

	/**
	 * Set the index of the permanently selected shape. See the comments written
	 * above the the permanentlySelectedShapeIndex field for more information.
	 * @param initPermanentShapeSelection the new index of the permanently selected shape
	 */
	public void setPermanentlySelectedShapeIndex(int initPermanentShapeSelection){
		permanentlySelectedShapeIndex = initPermanentShapeSelection;
	}

	/**
	 * Get the index of the permanently selected shape. See the comments written
	 * above the the permanentlySelectedShapeIndex field for more information.
	 * @return the index of the current permanently selected shape
	 */
	public int getPermanentlySelectedShapeIndex(){
		return permanentlySelectedShapeIndex;
	}

	/**
	 * Move the vertex at the specified index in the vertices[] array of the shape
	 * at the specified index in the Map's shapes ArrayList. Move it to the specified
	 * new position. See the comments for they physics_simulator.Shape.moveVertex 
	 * method for more information.
	 * @param shapeIndex the index of the shape in the Map's shapes ArrayList
	 * @param vertexIndex the index of the vertex in the shape's vertices[] array
	 * @param newPosition the new position of the vertex
	 */
	public void moveShapeVertex(int shapeIndex, int vertexIndex, Point newPosition){
		ArrayList<Shape> shapes = map.getShapeList();
		shapes.get(shapeIndex).moveVertex(vertexIndex, newPosition);
	}

	/**
	 * Move the shape at the specified index in the Map's shapes ArrayList.
	 * Determine how much to move the shape by finding the difference 
	 * between the original position of the selected vertex, and the new
	 * position of the mouse after the mouse has been released. See
	 * the comments for the physics_simulator.Shape.moveShape method for
	 * more information.
	 * @param shapeIndex the index shape to move in the Map's shapes ArrayList
	 * @param selectedVertexIndex the index of the vertex that was selected
	 * @param newPosition the new position for the selected vertex
	 */
	public void moveShape(int shapeIndex, int selectedVertexIndex, Point newPosition){
		ArrayList<Shape> shapes = map.getShapeList();
		shapes.get(shapeIndex).moveShape(selectedVertexIndex, newPosition);
	}

	/**
	 * Return the value of the vertexCircleRadius field. See the comments written above
	 * the field for a description of what this value means.
	 * @return the value of the vertexCircleRadius field.
	 */
	public int getVertexCircleRadius(){
		return vertexCircleRadius;
	}

	/**
	 * Delete the shape at the specified index in the Map's shapes ArrayList.
	 * @param index the index of the shape to delete in the Map's shapes ArrayList.
	 */
	public void deleteShape(int index){
		ArrayList<Shape> shapes = map.getShapeList();
		shapes.remove(index);
	}
}
