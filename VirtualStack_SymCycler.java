import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

import ij.io.FileInfo;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.ImagePlus;

import java.util.*;
import java.io.*;
import java.awt.event.*;

public class VirtualStack_SymCycler implements PlugIn, KeyListener {

	public void run(String arg) {
		//IJ.showMessage("CGSImageCycler_","Hello world!");

		new VirtualStack_SymCycler();

		// Open file from dialog box and store directory path
		Opener op = new Opener();
		ImagePlus imp = op.openImage(null);

		// store directory path
		FileInfo fi = imp.getOriginalFileInfo();
		String dir = fi.directory;	// directory with images
		String name = fi.fileName;	// file name
		String fpath = dir + name;	// path to file

		String lnk1 = dir+"sym1";
		String lnk2 = dir+"sym2";

		//IJ.showMessage("directory", dir);
		//IJ.showMessage("name", name);
		//IJ.showMessage("path", path);
		
		// Create 2 symlinks
		// Associate loaded image with 1st symlink

		try {

			java.lang.Runtime rt = java.lang.Runtime.getRuntime();
			java.lang.Process p = rt.exec(new String[]{
				"bash","-c", 
				"ln -s "+fpath+" "+lnk1+"; ln -s "+fpath+" "+lnk2+"; ls "+dir+"> testlog.txt"});
			p.waitFor();
		}
		catch (Exception e) {
			System.err.println("Caught Exception: " + e.getMessage());
			IJ.showMessage("exception", e.getMessage());
		}

		// Load image from dialog box
		//ImagePlus image = openFile(arg);
		imp.show();

		// Handle image loading here
		// Mouse-click or left/right arrow keys --> load prev/next image
		// Swap symlinks

		// Upon closing, delete symlinks
		// why aren't sym1 and sym2 showing up in testlog?
		//	--> need to make sure process completes in order, use Process.waitFor()

		try {
			java.lang.Runtime rt2 = java.lang.Runtime.getRuntime();
			java.lang.Process q = rt2.exec(new String[]{
				"bash", "-c",
				"rm "+lnk1+"; rm "+lnk2});
			q.waitFor();
			
		}
		catch (Exception e) {
			System.err.println("Caught Exception: " + e.getMessage());
			IJ.showMessage("exception", e.getMessage());
		}
	}

	/*
	Opens a file in ImageJ
	*/
/*
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
*/

	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();

		switch(keyCode) {
			case KeyEvent.VK_LEFT:
				// left arrow
				// go to previous image in folder
				//IJ.showMessage("CGSImageCycler_","Left!");
				System.out.println("left!");
				break;
			case KeyEvent.VK_RIGHT:
				// right arrow:
				// go to next image in folder
				//IJ.showMessage("CGSImageCycler_","Right!");
				System.out.println("right!");
				break;
		}

	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

}
