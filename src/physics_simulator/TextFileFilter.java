package physics_simulator;
import java.io.File;
import javax.swing.filechooser.FileFilter;
/**
 * A file filter that only allows text files (*.txt) to be loaded and saved. 
 * Used by the file chooser to limit the types of files that can be opened
 * in the simulator.
 * 
 * @author Adam Cogen
 *
 */
public class TextFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		String fileName = f.getName();
		String extension = "";
		//Check that the file ends in '.txt'
		if (fileName.contains(".")){
			extension = fileName.substring(fileName.lastIndexOf('.'));
		}
		//directories and .txt files are all that should appear in the file chooser
		if(f.isDirectory() || extension.equalsIgnoreCase(".txt")){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getDescription() {
		return ".txt files";
	}
	
}
