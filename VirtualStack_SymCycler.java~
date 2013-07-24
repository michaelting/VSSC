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

	private Opener op;
	private ImagePlus imp;

	private FileInfo fi;
	private String dir;
	private String name;
	private String fpath;

	private File dirobj;
	private File filenames[];
	private int currindex = -1;

	private String lnk1;
	private String lnk2;
	private int symdex = 0;
	private final int SYMFIRST = 0;
	private final int SYMSECOND = 1;

	private ImageWindow win;
	private ImageCanvas canvas;

	public void run(String arg) {
		//IJ.showMessage("CGSImageCycler_","Hello world!");

		// Open file from dialog box and store directory path
		op = new Opener();
		imp = op.openImage(null);

		// store directory path
		fi = imp.getOriginalFileInfo();
		dir = fi.directory;	// directory with images
		name = fi.fileName;	// file name
		fpath = dir + name;	// path to file

		dirobj = new File(dir);
		filenames = dirobj.listFiles();
		Arrays.sort(filenames);
/*
		for (File file : filenames) {
			IJ.log(file.getName());
		}
*/
		
		for (int i = 0; i < filenames.length; i++) {
			if (filenames[i].getName().equals(name)) {
				currindex = i;			
			}
		}
		// error checking
		if (currindex < 0) {
			IJ.log("Filename not detected in filenames[]!");
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

		// Initialize the KeyListener using the open image
		win = imp.getWindow();
		canvas = win.getCanvas();

		win.removeKeyListener(IJ.getInstance());
		canvas.removeKeyListener(IJ.getInstance());
		win.addKeyListener(this);
		canvas.addKeyListener(this);
		ImagePlus.addImageListener(this);
	}

	// Load prev/next image based on arrow key press
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();

		switch(keyCode) {
			case KeyEvent.VK_LEFT:
				// left arrow
				// go to previous image in folder
				IJ.log("left");

				// set new image to load as prev
				currindex -= 1;
				// loop to end of file list
				if (currindex < 0) {
					currindex = filenames.length-1;				
				}
				break;
			case KeyEvent.VK_RIGHT:
				// right arrow:
				// go to next image in folder
				IJ.log("right");

				// set new image to load as next
				currindex += 1;
				// loop to front of list
				if (currindex >= filenames.length) {
					currindex = 0;
				}

				break;
		}

		String newname = filenames[currindex].getName();
		String newfpath = dir + newname;

		// update symlink to new usage
		symdex = (symdex+1) % 2;	// alternate between 0 and 1

		// load image
		switch(symdex) {
			case SYMFIRST:	// symdex == 0
				// update sym1
				try {
					java.lang.Runtime rt1 = java.lang.Runtime.getRuntime();
					java.lang.Process psym1 = rt1.exec(new String[]{
						"bash","-c", 
						"ln -nsf "+newfpath+" "+lnk1});
					psym1.waitFor();
				}
				catch (Exception exc) {
					System.err.println("Caught Exception: " + exc.getMessage());
					IJ.showMessage("exception", exc.getMessage());
				}
			case SYMSECOND:	// symdex == 1
				// update sym2
				try {
					java.lang.Runtime rt2 = java.lang.Runtime.getRuntime();
					java.lang.Process psym2 = rt2.exec(new String[]{
						"bash","-c", 
						"ln -nsf "+newfpath+" "+lnk2});
					psym2.waitFor();
				}
				catch (Exception exc) {
					System.err.println("Caught Exception: " + exc.getMessage());
					IJ.showMessage("exception", exc.getMessage());
				}
		}

		ImagePlus newimp = op.openImage(newfpath);
		imp.setImage(newimp);
		imp.updateImage();

		IJ.log("lnk1: " + lnk1);
		IJ.log("lnk2: " + lnk2);


		try {
			java.lang.Runtime rt3 = java.lang.Runtime.getRuntime();
			java.lang.Process pcheck = rt3.exec(new String[]{
				"bash","-c", 
				"ls -la "+dir+" >> lscheck.txt"});
			pcheck.waitFor();
		}
		catch (Exception exc) {
			System.err.println("Caught Exception: " + exc.getMessage());
			IJ.showMessage("exception", exc.getMessage());
		}

	}

	// Upon closing, delete symlinks
	public void imageClosed(ImagePlus imp) {
		if (win!=null)
		    win.removeKeyListener(this);
		if (canvas!=null)
		    canvas.removeKeyListener(this);
		ImagePlus.removeImageListener(this);

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

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
    	public void imageOpened(ImagePlus imp) {}
    	public void imageUpdated(ImagePlus imp) {}
}
