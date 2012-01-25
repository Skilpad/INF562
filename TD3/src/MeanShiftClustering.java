

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

    static boolean verbose = true;

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
    	Point_D    center  = seed;
    	PointCloud cluster = Rs.kdNN(center, sqInflRad);
    	double     delta2  = Double.POSITIVE_INFINITY;
    	while (delta2 > sqCvgRad) {
    		Point_D center_ = new Point_D(Rs.kdNN(center, sqAvgRad).toArray());
    		delta2 = center.squareDistance(center_);
    		center = center_;
    		cluster.add(Rs.kdNN(center, sqInflRad));
    	}
    	cluster.add(center);
    	for (PointCloud pc = cluster; pc != null; pc = pc.next) pc.p.cluster = clusterIndex;
    	return cluster;
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
    	int i = -1; 
    	double dist2 = sqMergeRad;
    	Point_D c = cluster.p;
    	for (int j = 0; j < clusterCenters.length; j++) {
    		if (clusterCenters[j] == null) break;
    		double d2 = c.squareDistance(clusterCenters[j]);
    		if (d2 < dist2) { i = j; dist2 = d2; } 
    	}
    	if (i < 0) return -1;
    	for (PointCloud pc = cluster.next; pc != null; pc = pc.next) pc.p.cluster = i;
    	return i;
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
    	int todo_n = N.size();
    	Point_D[] clusterCenters = new Point_D[todo_n];
    	int clusterIndex = 0;
    	while (todo_n > 0) {
    		if (verbose) System.out.println("TODO:    "+todo_n);
    		int rdm = (int) (todo_n*Math.random());
    		Point_D seed = null;
    		for (PointCloud pc = N; pc != null; pc = pc.next) {
    			if (pc.p.cluster < 0) {
    				if (rdm == 0) { seed = pc.p; break; }
    				rdm--;
    			}
    		}
    		PointCloud cl = detectCluster(seed, clusterIndex);
    		if (verbose) System.out.print("NEIGH.:  "+cl.size());
    		if (mergeCluster(cl, clusterCenters) < 0) {
    			if (verbose) System.out.print("  >> New cluster!");
    			clusterCenters[clusterIndex] = cl.p;
    			clusterIndex++;
    		}
    		if (verbose) System.out.println();
    		todo_n = 0;
    		for (PointCloud pc = N; pc != null; pc = pc.next) if (pc.p.cluster < 0) todo_n++;
    	}
    	return clusterCenters;
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
//    	testDetectCluster(N, bandWidth); // ex 2.1
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


