import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

import ij.io.OpenDialog;
import ij.io.Opener;
import ij.ImagePlus;

public class VirtualStack_SymCycler implements PlugIn {

	public void run(String arg) {
		//String path = getDirPath(arg);
		//IJ.showMessage("CGSImageCycler_","Hello world!");

		// Open file from dialog box and store directory path
		Opener op = new Opener();
		ImagePlus imp = op.openImage(null);

		IJ.showMessage("CGSImageCycler_", dir);

		
		// Create 2 symlinks
		//java.lang.Runtime.exec("/bin/ln -s " + dir + " sym1");

		// Load image from dialog box
		//ImagePlus image = openFile(arg);
		imp.show();

		// Associate loaded image with 1st symlink

		// Upon closing, delete symlinks



	}

	/*
	Opens a file in ImageJ
	*/
	private ImagePlus openFile(String arg) {
		Opener op = new Opener();
		ImagePlus imp = op.openImage(null);
		return imp;
	}

	private String getDirPath(String arg) {
		OpenDialog od = new OpenDialog("Choose an image directory", null);
		String dir = od.getDirectory();
		return dir;
	}

	private void getFilePath(String arg) {
		return;
	}

}
