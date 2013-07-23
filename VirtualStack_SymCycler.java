import ij.*;
import ij.process.*;
import ij.gui.*;
//import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

import ij.io.FileInfo;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.ImagePlus;

import java.util.*;
import java.io.*;
import java.awt.event.*;

public class VirtualStack_SymCycler implements PlugIn, KeyListener, ImageListener {

	FileInfo fi;
	String dir;
	String name;
	String fpath;

	File dirobj;
	File filenames[];

	String lnk1;
	String lnk2;

	ImageWindow win;
	ImageCanvas canvas;

	public void run(String arg) {
		//IJ.showMessage("CGSImageCycler_","Hello world!");

		// Open file from dialog box and store directory path
		Opener op = new Opener();
		ImagePlus imp = op.openImage(null);

		// store directory path
		fi = imp.getOriginalFileInfo();
		dir = fi.directory;	// directory with images
		name = fi.fileName;	// file name
		fpath = dir + name;	// path to file

		dirobj = new File(dir);
		filenames = dirobj.listFiles();
/*
		for (int i=0; i < filenames.length; i++) {
			IJ.log(filenames[i]);
		}
*/

		for (File file : filenames) {
			IJ.log(file.getName());
		}

		lnk1 = dir+"sym1";
		lnk2 = dir+"sym2";

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

		// Initialize the KeyListener
		win = imp.getWindow();
		canvas = win.getCanvas();

		win.removeKeyListener(IJ.getInstance());
		canvas.removeKeyListener(IJ.getInstance());
		win.addKeyListener(this);
		canvas.addKeyListener(this);
		ImagePlus.addImageListener(this);

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

	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();

		switch(keyCode) {
			case KeyEvent.VK_LEFT:
				// left arrow
				// go to previous image in folder
				IJ.log("left");
				break;
			case KeyEvent.VK_RIGHT:
				// right arrow:
				// go to next image in folder
				IJ.log("right");
				break;
		}

	}

	public void imageClosed(ImagePlus imp) {
		if (win!=null)
		    win.removeKeyListener(this);
		if (canvas!=null)
		    canvas.removeKeyListener(this);
		ImagePlus.removeImageListener(this);
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
    	public void imageOpened(ImagePlus imp) {}
    	public void imageUpdated(ImagePlus imp) {}
}
