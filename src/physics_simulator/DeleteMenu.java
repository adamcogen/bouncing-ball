package physics_simulator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * 
 * The DeleteMenu class, which manages the non-modal dialog box that
 * appears when in edit --> select / delete shape mode. When a shape
 * is selected in the simulation window, the "Delete Shape" button in
 * this dialog box becomes enabled, and can be clicked to delete the
 * selected shape.
 * 
 * @author Adam Cogen
 *
 */
public class DeleteMenu {
	private JOptionPane playerMenu; //the JOptionPane which will be used as the delete JDialog's ContentPane
	private JDialog dialog; //the delete JDialog (this is the window that appears onscreen)
	private JPanel panel; //the JPanel which will be added to the delete dialog, and will contain the delete JButton
	private JButton deleteButton; //the "Delete Shape" button
	private Window window; //the main simulation display window, which is passed in as a parameter so that it can be referenced from this class
	private Simulator sim; //the main simulation controller, which is passed in as a parameter so that it can be referenced from this class
	/**
	 * 
	 * Construct the DeleteMenu, passing in the main
	 * Simulator and Window to which the DeleteMenu belongs.
	 * 
	 * @param initSim the Simulator to which this DeleteMenu belongs
	 * @param initWindow the Window to which this DeleteMenu belongs
	 */
	public DeleteMenu(Simulator initSim, Window initWindow){
		sim = initSim;
		window = initWindow;
		//initialize the panel and its content
		panel = new JPanel();
		deleteButton = new JButton("Delete Shape");
		deleteButton.setEnabled(false); //should not be enabled until a shape is selected
		deleteButton.addActionListener(createDeleteButtonListener());
		panel.add(deleteButton);
		//initialize the JOptionPane which will become the ContentPane for the the delete menu JDialog
		playerMenu = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		dialog = new JDialog(window);
		dialog.setTitle("Delete Shapes");
		dialog.setModal(false);
		dialog.setContentPane(playerMenu);
		dialog.setDefaultCloseOperation((JDialog.DO_NOTHING_ON_CLOSE)); //the delete dialog can only be closed by switching out of delete mode
		playerMenu.setVisible(true);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setVisible(false); //should not be visible until the user is in edit --> select/delete shape mode
	}

	/**
	 * Create and return the ActionListener that will be added to the "Delete Shape" JButton
	 * @return an instance of the DeleteButtonListener class
	 */
	private ActionListener createDeleteButtonListener(){
		/**
		 * Inner class: action listener which will be called
		 * when the "Delete Shape" button is pressed.
		 * It should call the deleteShape() method in the
		 * Simulator. This method is written in the simulator 
		 * because the simulator has access to all of the data
		 * in the simulation, so it can perform the necessary
		 * tasks to delete a shape.
		 * 
		 * @author Adam Cogen
		 *
		 */
		class DeleteButtonListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				sim.deleteShape();
			}
		}
		return new DeleteButtonListener();
	}

	/**
	 * Set the "Delete Shape" button to enabled or disabled.
	 * Will be set to enabled when a shape is selected in
	 * edit --> select/delete shape mode. Will be disabled
	 * at all other times.
	 * @param boolean - enabled: true if the button should be enabled
	 */
	public void setDeleteButtonEnabled(boolean enabled){
		deleteButton.setEnabled(enabled);
	}

	/**
	 * Set the delete menu to visible or invisible.
	 * Will be set to visible when the user is in
	 * edit --> select/delete shape mode. Will be 
	 * disabled at all other times.
	 * @param boolean - visible: true if the menu should be visible
	 */
	public void setDeleteMenuVisible(boolean visible){
		resetLocation();
		dialog.setVisible(visible);
	}

	/**
	 * Move the delete shape dialog to where it should appear on the screen.
	 * It should appear just off of the top right side of the simulation 
	 * window, no matter where the simulation window is. This location
	 * is calculated each time the delete dialog reappears.
	 */
	public void resetLocation(){
		java.awt.Point windowLocation = window.getLocationOnScreen();
		dialog.setLocation((int) windowLocation.getX() + window.getWidth(), (int) windowLocation.getY());
	}
}
