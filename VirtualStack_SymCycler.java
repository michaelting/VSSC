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
import java.lang.*;

public class VirtualStack_SymCycler implements PlugIn {

	public void run(String arg) {
		//IJ.showMessage("CGSImageCycler_","Hello world!");

		// Open file from dialog box and store directory path
		Opener op = new Opener();
		ImagePlus imp = op.openImage(null);

		// store directory path
		FileInfo fi = imp.getOriginalFileInfo();
		String dir = fi.directory;
		String name = fi.fileName;
		String path = dir + name;

		//IJ.showMessage("directory", dir);
		//IJ.showMessage("name", name);
		//IJ.showMessage("path", path);
		
		// Create 2 symlinks
		// Associate loaded image with 1st symlink

		try {

			java.lang.Runtime rt = java.lang.Runtime.getRuntime();
			java.lang.Process p = rt.exec(new String[]{
				"bash","-c", 
				"ln -s " + path + " sym1; ln -s " + path + " sym2; ls > testlog.txt"});
		}
		catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
			IJ.showMessage("exception", e.getMessage());
		}

		// Load image from dialog box
		//ImagePlus image = openFile(arg);
		imp.show();

		// Upon closing, delete symlinks

		// why aren't sym1 and sym2 showing up in testlog?
		// files appear on Desktop, need to re-route them to image directory

		try {
			java.lang.Runtime rt2 = java.lang.Runtime.getRuntime();
			java.lang.Process q = rt2.exec(new String[]{
				"bash", "-c",
				"rm sym1; rm sym2"});
			
		}
		catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
			IJ.showMessage("exception", e.getMessage());
		}

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
