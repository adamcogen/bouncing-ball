package launcher;
import physics_simulator.Simulator;

/**
 * Launch the simulation, loading the initial map file
 * specified in the Simulator.loadInitialMap() method.
 * 
 * @author Adam Cogen
 *
 */
public class Launcher {
	
	public static void main(String [] args){
		Simulator sim = new Simulator();
		sim.loadInitialMap();
	}
}
