package Jcg.image;

import java.io.*;
import java.math.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.viewer.MeshViewer;

/**
 * A class for dealing with range search queries
 */
public class RangeSearch {

    private NearestNeighborSearch kdTree;
    
    static long queryTime=0;
    static long constructionTime=0;
    static long basicQueryTime=0;    

    public static void timePerformance() {
    	System.out.println("Range Search performaces:\nTotal construction time: " + constructionTime/1000 + "s " + 
    				   constructionTime%1000 + "ms");   	
    	System.out.println("Total NN query time: " + queryTime/1000 + "s " + 
				   queryTime%1000 + "ms\n");   	
    }

    /**
     * Construct a data structure for range search (from a point cloud)
     */
    RangeSearch (PointCloud N) {
    	if (N == null)
    		kdTree = null;
    	else {
    		Calendar rightNow = Calendar.getInstance();
    		long time0 = rightNow.getTimeInMillis();

    		kdTree=KdTree.constructDataStructure(N, N.p.dimension());

    		rightNow = Calendar.getInstance();
    		constructionTime = constructionTime+(rightNow.getTimeInMillis()-time0);
    	}
    }


    /**
     * Main search procedure (for Nearest neighbor search)
     */
    public PointCloud NN (Point_D q, PointCloud N, double sqRad) {
		Calendar rightNow = Calendar.getInstance(); // to compute query time
		long time0 = rightNow.getTimeInMillis();
		
		PointCloud result;
		// choose your favorite Nearest neighbor search method
		//result=basicNN (q, N, sqRad);
		result=kdNN (q, sqRad); // efficient NN search

		rightNow = Calendar.getInstance(); // to compute query time
		queryTime = queryTime+(rightNow.getTimeInMillis()-time0);
		
		return result;
    }


    /**
     * Linear time search (exhaustive search)
     * Useful for point clouds of small size
     */
    public PointCloud basicNN (Point_D q, PointCloud N, double sqRad) {
	PointCloud res = null;
	for (PointCloud n = N; n != null; n = n.next)
	    if (q.squareDistance(n.p).doubleValue() < sqRad)
		res = new PointCloud (n.p, res, false);  // do not duplicate point
	return res;
    }
    
    /**
     * Nearest neighbor search using KdTrees
     * Useful for point clouds of big size
     */
    public PointCloud kdNN (Point_D q, double sqRad) {
	if (kdTree == null)
	    return null;
	else {
		PointCloud result;
	    result=kdTree.NearestNeighbor (q, sqRad);
		return result;
	}
    }
 
    
    
    //-------------------------------    
    //-- Test Range Search(ex 1.3) --
    //-------------------------------

    
    
    public static void main(String args[]) {
    	System.out.println("Exo 1.3b: testing Range Search");
    	PointCloud N;
    	int dimension=300;
    	System.out.println(""+args.length);
    	if (args.length < 1) {
    		int n=100000;
    		N=PointCloud.randomPoints(n, dimension);
    		System.out.println("Test "+n);
    	}
    	else { 
    		N=Clustering.readFile(args[0]);
    	}

    	//Draw.draw2D(N, "original point cloud");
    	//Draw.draw3D(N);
    	double radius=4.;

    	System.out.print("Computing Kd-Tree data structure... ");
    	RangeSearch search=new RangeSearch(N);
    	System.out.println(" done");

    	double[] originCoord2D={0.5, 0.5};
    	double[] originCoord3D={0.5, 0.5, 0.5};
    	
    	double[] originCoordnD=new double[dimension];
    	for(int i=0;i<dimension;i++) originCoordnD[i]=0.5;
    	
    	Point_D origin;
    	if(N.p.dimension()==3)
    		origin=new Point_D(originCoord3D);
    	else
    		origin=new Point_D(originCoordnD);

    	PointCloud neighbors1=search.basicNN(origin, N, radius);
    	//Draw.draw2D(neighbors1, "basic (linear) search");    	
    	//Draw.draw3D(neighbors1);
    	System.out.println("basic search: "+PointCloud.size(neighbors1)+" neighbors");
    	
    	PointCloud neighbors2=null;
    	for(int j=0;j<1;j++) {
    		neighbors2=search.kdNN(origin, radius);
    	}
    	//Draw.draw2D(neighbors2, "NN search with Kd-Trees");
    	//Draw.draw3D(neighbors2);
    	System.out.println("Kd-Trees search: "+PointCloud.size(neighbors2)+" neighbors");
    	
    	RangeSearch.timePerformance();
    	
    	System.out.println("Testing Range Search: end");
    }
}


