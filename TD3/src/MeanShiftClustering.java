

import java.io.*;
import java.math.*;
import java.util.Calendar;
import java.awt.Color;

import Jcg.geometry.*;

/**
 * This class contains the main methods implementing the Mean-Shift clustering
 */
public class MeanShiftClustering {

    PointCloud N;
    PointCloud seeds;
    double sqCvgRad;  // convergence radius
    double sqAvgRad;  // averaging radius (definining the window)
    double sqInflRad;  // influence radius
    double sqMergeRad;  // merging radius

    RangeSearch Rs;  // data structure for nearest neighbor search


    // Constructors

    void initMSC (PointCloud n, PointCloud s, double cr, double ar, double ir, double mr){
	N = n;
	seeds = s;
	sqCvgRad = cr*cr;
	sqAvgRad = ar*ar;
	sqInflRad = ir*ir;
	sqMergeRad = mr*mr;
	Rs = new RangeSearch (N);
    }

    MeanShiftClustering (PointCloud n, PointCloud s, 
			 double cr, double ar, double ir, double mr) {
	initMSC (n,s,cr,ar,ir, mr);
    }

    MeanShiftClustering (PointCloud n, double bandWidth) {
	initMSC (n, n, bandWidth*1e-3, bandWidth, bandWidth/4, bandWidth);
    } 

    /**
     * Single cluster detection -- returns approximate peak and cluster points
     * The output is a list of point containing:
     *  - all the points belonging to the detected cluster
     *  - the peak point (at the top of the list)
     */
    public PointCloud detectCluster (Point_D seed, int clusterIndex) {
    	 throw new Error("Exercice 2.1: a' completer");	

    }

    /**
     * Cluster merging -- Note: cluster center is at top of cluster cloud
     * input: 
     *   - the point cloud to be merge (cluster), containing at the top its center
     *   - the set (array) of cluster centers
     * output:
     *   -1: if no merge is performed
     *    i: if the input cluster has been associated with the cluster i (already existing)
     */
    public int mergeCluster (PointCloud cluster, Point_D[] clusterCenters) {
    	throw new Error("Exercice 2.2: a' completer");	
    }


    /**
     * Main algorithm for detecting all clusters
     * Clusters are detected iteratively (until all points are processed)
     * Clusters are merged if required: when the corresponding peaks are close
     * output: an array of points, of size n
     *   - first i elements are cluster centers (non null points)
     *   - remaining n-i elements must be null
     */
    public Point_D[] detectClusters () {
    	throw new Error("Exercice 2.3: a' completer");

    }

    
    
//-------------------------------    
//------ Test (exercice 2)-------
//-------------------------------

    
    
    public static void main(String[ ] args) throws Exception {
    	System.out.println("Exercice 2:");
    	double bandWidth;
    	PointCloud N=null;
    	if (args.length < 1) {
    		System.out.println("Usage: java MeanShiftClustering (datafile.dat) bandwidth");
    		System.out.println("dataFile.dat optionnel");
    		System.exit(0);
    	}    	
    	if (args.length == 1 ) {
    		N=PointCloud.randomPoints(3000, 3);
    		//N=PointCloud.randomPointsOnCircle(3000, 3);
    		bandWidth = Double.parseDouble(args[0]);
    	}
    	else { 
    		N=Clustering.readFile(args[0]);
    		bandWidth = Double.parseDouble(args[1]);
    	}
    	
    	System.out.println("point cloud of size: "+PointCloud.size(N));
    	Draw.draw2D(N, "original point cloud");

    	Calendar rightNow = Calendar.getInstance(); // to compute time performances
    	long time0 = rightNow.getTimeInMillis();

//--------- choose test to perform ---------------
    	//testDetectCluster(N, bandWidth); // ex 2.1
    	testMeanShift(N, bandWidth); // ex 2.3
//------------------------------------------------

    	rightNow = Calendar.getInstance(); // to compute time performances
    	long time = rightNow.getTimeInMillis();
    	System.out.println("Total time to find clusters: " + (time-time0)/1000 + "s " + (time-time0)%1000 + "ms");
      
    }
    
    public static void testMeanShift(PointCloud N, double bandwidth) {
    	System.out.println("Exercice 2.3: testing Mean-Shift clustering");
    	
    	MeanShiftClustering msc = new MeanShiftClustering (N, bandwidth);
    	
    	Point_D[] clusterCenters = msc.detectClusters();
    	int nDetectedClusters=0;
    	for(int i=0;i<clusterCenters.length;i++)
    		if(clusterCenters[i]!=null) 
    			nDetectedClusters++;
    	System.out.println("Number of clusters detected: "+nDetectedClusters);
    	
    	Draw.draw3D(N);
    	msc.Rs.timePerformance();
    }
    
    public static void testDetectCluster(PointCloud N, double bandwidth) {
    	System.out.println("Exercice 2.1: testing detecting a cluster");
    	
    	MeanShiftClustering msc = new MeanShiftClustering (N, bandwidth);
    	
    	int clusterNumber=0;
    	PointCloud cluster=msc.detectCluster(N.next.p, clusterNumber);
    	
    	PointCloud t=cluster;
    	int i=0;
    	while(t!=null) {
    		if(t.p.cluster==clusterNumber)
    			i++;
    		t=t.next;
    	}
    	System.out.println("size of the detected cluster: "+PointCloud.size(cluster));
    	System.out.println("size of the cluster: "+i);
    	
    	Draw.draw3D(N);
    	//Draw.draw3D(cluster);
    	msc.Rs.timePerformance();
    }

}


