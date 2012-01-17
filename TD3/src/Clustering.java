

import java.io.*;
import java.util.Calendar;
import java.math.*;
import java.awt.Color;

import Jcg.geometry.*;

class Clustering extends MeanShiftClustering {

    // Constructors

    Clustering (PointCloud n, PointCloud s, 
		double cr, double ar, double ir, double mr) {
	super (n,s,cr,ar,ir, mr);
    }

    Clustering (PointCloud n, double bandWidth) {
	super (n, bandWidth);
    } 

    static void assignColors (int n, Point_D[] clusterCenters, Color[] cols) {
	// In this basic version, assign random colors
	for (int i=0; i<n; i++)
	    cols[i] = new Color ((float)Math.random(), 
				 (float)Math.random(), 
				 (float)Math.random());
    }

    // Input point cloud

    public static PointCloud readFile (String fileName) {
    	System.out.print("reading point cloud from file "+fileName+" ...");
	PointCloud N = null;
    try{ 
	BufferedReader in = new BufferedReader(new FileReader(fileName));
	String s = null;

	int dim = -1;

	// Get dimension of point cloud
	s = in.readLine();
	if (s == null)
	    return N;

	// pass comments in data file
	while (s.charAt(0) == '/')
	    s = in.readLine();

	int i = 0;
	while ( s.charAt(i) == ' ' )
	    ++i;
	// i points to first digit of number
	dim = (int)Double.parseDouble (s.substring (i));
	
	// Get the points, one after the other
	double[] coords = new double [dim]; 
	while ( (s = in.readLine()) != null ) {
	    i=0;
	    for (int d=0; d<dim-1; d++) {
		while ( s.charAt(i) == ' ' )
		    ++i;
		// i points to first digit of coordinate
		int j = i+1;
		while ( s.charAt(j) != ' ')
		    ++j;
		// j points to space after coordinate
		coords[d] = Double.parseDouble(s.substring(i, j));
		i = j+1;
	    }
	    // Get last coordinate
	    while ( s.charAt(i) == ' ' )
		++i;
	    // i points to first digit of last coordinate
	    coords[dim-1] = Double.parseDouble(s.substring(i));
	    
	    // Append new point to point cloud
	    N = new PointCloud (new Point_D (coords), N, true);  // copy point
	}} catch (Exception e) {  
	    e.printStackTrace();  
	}  
	System.out.println(" done");
	return N;
    }

}


