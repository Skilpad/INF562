package Jcg.image;



import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.viewer.MeshViewer;

import java.util.*;;

/**
 * A class implementing a Kd-tree data structure
 * Exercice 1
 */
public class KdTree implements NearestNeighborSearch {

    private int pointDimension, cutDimension;
    private double cutValue;
    private int numPoints;
    private PointCloud points;
    private KdTree lowerHalf, upperHalf;
    

    
    //--------------------------------------------
    //--- Computing the cut value ----------------
    //--- Ex 1.1a: median in linear time
    //--- Ex 1.1b: median with sorting (nlog n time)
    //--- Ex 1.1c: use the mean (instead of median)
    //--------------------------------------------

    
    
    /**
     * Select the i-th element in time O(nlog n) (after sorting)
     */    
    private static Point_D selectWithSorting (Point_D[] buffer, int index, int cutDim){
    	//throw new Error("Exercice 1.1b: a' completer");
    	
    	// sort the array
    	CompareCoordinate comparator=new CompareCoordinate(cutDim);    	
    	Arrays.sort(buffer, comparator);
    	return buffer[index];
    }

    /**
     * Compute the median of a set of point cloud in time O(nlog n) (after sorting)
     */    
    private static Point_D findMedianWithSorting (PointCloud N, int cutDim){
    	//throw new Error("Exercice 1.1b: a' completer");
    	
    	int size=PointCloud.size(N);
    	Point_D[] arrayToSort=new Point_D[size];
    	
    	int i=0;
    	for(PointCloud t=N; t!=null; t=t.next) {
    		arrayToSort[i]=t.p;
    		i++;
    	}
    	
    	return selectWithSorting(arrayToSort, size/2, cutDim);
    }

    
    /**
     * Compute the median of a set of points (just for small arrays)
     */    
    private static Point_D findMedianArray (Point_D[] buffer, int max, int cutDim) {
    	//throw new Error("Exercice 1.1a: a' completer");
    	
	// sort array
	for (int i=0; i<max-1; i++)
	    for (int j=i+1; j<max; j++)
		if (buffer[i].Cartesian(cutDim)> buffer[j].Cartesian(cutDim)) {
		    Point_D pTemp = buffer[i];
		    buffer[i] = buffer[j];
		    buffer[j] = pTemp;
		}

	// sort check
	for (int i=0; i<max-1; i++)
	    if (buffer[i].Cartesian(cutDim) > buffer[i+1].Cartesian(cutDim))
		throw new RuntimeException ("Array not sorted!");

	return buffer[max/2];
    }


    /**
     * Compute the median of a set of points (linear median)
     */    
    private static Point_D findMedianLinear (PointCloud N, int cutDim) {
    	//throw new Error("Exercice 1.1a: a' completer");
	
    	if (N == null)
    		throw new RuntimeException ("Empty cloud for median finding!");
    	
    	if (N.next == null)
    		return N.p;

    	// build sub-cloud from arrays of at most 5 points
    	PointCloud subN = null;
    	PointCloud n = N;
    	while (n != null) {
    		// construct arrays of size 5 from N
    		Point_D[] buffer = new Point_D [5];
    		int i=0;
    		while (i<5 && n != null) { 
    			buffer[i] = new Point_D (n.p);
    			i++;
    			n = n.next;
    		}
    		if (i > 0) { // if the array is not empty
    			// add to the sub-cloud the median of the array buffer
    			subN = new PointCloud (findMedianArray (buffer, i, cutDim),  subN, false);
    		}	 
    	}
    	// recursive call
    	return findMedianLinear (subN, cutDim);
    }

    /**
     * Split a point cloud into two sub-point clouds (lower an upper point clouds)
     */    
    public static PointCloud[] split(PointCloud N, double cutValue, int cutDim) {
    	PointCloud[] subN=new PointCloud[2]; 
    	subN[0] = null; subN[1] = null;
    	
    	for (PointCloud n = N; n != null; n = n.next) {
    	    if (n.p.Cartesian(cutDim) < cutValue) {
    		// assign to lower half
    		subN[0] = new PointCloud (n.p, subN[0], false);
    	    }
    	    else
    		subN[1] = new PointCloud (n.p, subN[1], false);
    	}
    	return subN;
    }

    
    //-------------------------------    
    //--- Constructing the Kd-Tree --
    //-------------------------------

    /**
     * Construct a Kd-Tree from a point cloud (in dimension pDim)
     */    
    public static KdTree constructDataStructure(PointCloud N, int pDim) {
    	return new KdTree(N, pDim, 0);
    }

    /**
     * Constructor -- builds entire kd-tree at once recursively
     */    
    public KdTree (PointCloud N, int pDim, int cutDim) {
    	//throw new Error("Exercice 1.2: a' completer");
	this.pointDimension = pDim;
	this.cutDimension = cutDim;
	points = null;
	numPoints = 0;

	
	Point_D p;
	p= findMedianLinear (N, cutDim); // find median (linear time)
	//p= findMedianWithSorting(N, cutDim); // find median in O(nlog n) time
	//p = Point_D.mean(N); // use the mean to compute the cut value
	this.cutValue=p.Cartesian(cutDim);


	// split point cloud
	PointCloud[] subN=split(N, this.cutValue, cutDim);	
	PointCloud lowerN = subN[0], upperN = subN[1];
	int numPointsLower = PointCloud.size(lowerN);

	for (PointCloud n = N; n != null; n = n.next) {
	    numPoints++;
	    points = new PointCloud (n.p, points, false);  // do not copy points
	}

 	// balance check
 	if (numPoints >= 200 &&
 	    ((double)numPointsLower/(double)numPoints < 0.25 ||
 	     (double)numPointsLower/(double)numPoints > 0.75))
 		System.out.println("Bad balance in kd-tree! (" + 
 		 numPointsLower + "/" + numPoints +
 		 "=" + (double)numPointsLower/(double)numPoints + ")");

	// recursive calls
	if (numPointsLower > 0 && numPointsLower < numPoints) {
	    lowerHalf = new KdTree (lowerN, pDim, (cutDim+1)%pDim);
	    upperHalf = new KdTree (upperN, pDim, (cutDim+1)%pDim);
	}
	else { // if median does not split N well, then |N|=O(1)
	    lowerHalf = null;
	    upperHalf = null;
	}
    } 
    
    

    //-----------------------------------
    //--- Implementation of NN search ---
    //-----------------------------------

    
    
    /**
     * Range search: return the list of nearest point to a given query point q.
     * The output is the set of points at distance at most sqRad from q.
     */    
    public PointCloud NearestNeighbor (Point_D q, double sqRad) {
    	//throw new Error("Exercice 1.3: a' completer");

    	// termination case
	if (lowerHalf == null && upperHalf == null) {
	    // exhaustive search among current point cloud
	    PointCloud res = null;
	    for (PointCloud n = points; n != null; n = n.next)
		if (q.squareDistance(n.p).doubleValue() < sqRad)
		    res = new PointCloud (n.p, res, false);  // do not copy point
	    return res;
	}

	// Note: neither lowerHalf nor upperHalf is null
	// test median's curDim'th coordinate -- necessary for bounded l^2 dist.
	double d = q.Cartesian(this.cutDimension)-cutValue;
	double dd = d*d;
	if (dd > sqRad) {  // median hyperplane does not intersect box 
	    if (d < 0)
		return lowerHalf.NearestNeighbor (q, sqRad);
	    else
		return upperHalf.NearestNeighbor (q, sqRad);
	}
	else {  // median hyperplane intersects box => check both sides
	    PointCloud lowerN = lowerHalf.NearestNeighbor (q, sqRad);
	    PointCloud upperN = upperHalf.NearestNeighbor (q, sqRad);
	    // merge results -- point clouds have already been copied
	    if (lowerN == null)
		return upperN;
	    else {
		PointCloud n = lowerN;
		while (n.next != null) n = n.next;
		n.next = upperN;
		return lowerN;
	    }
	}
    }
    
    
    
    //-------------------------------    
    //------ Testing  Kd-Trees ------
    //-------------------------------

    public static void testMedianComputing(PointCloud N) {
    	System.out.println("exercice 1.1: computing the median (or the mean)");
    	if(N==null) return;
    	int cutDim=1;
    	
    	Point_D p;
    	p=findMedianLinear(N, cutDim);
    	//p=findMedianWithSorting(N, cutDim);
    	//p=Point_D.mean(N);
    	double value=p.Cartesian(cutDim);
    	PointCloud[] subN=split(N, value, cutDim);
    	
    	System.out.println("size original point cloud: "+PointCloud.size(N));
    	System.out.println("size lower half: "+PointCloud.size(subN[0]));
    	
    	Draw.draw2D(subN[0], "lower points");
    	Draw.draw2D(subN[1], "upper points");
    }

    public static void testConstructionKdTree(PointCloud N) {
    	System.out.println("exercice 1.2: constructing Kd-Tree");
    	if(N==null) return;
    	
    	KdTree kdtree=KdTree.constructDataStructure(N, N.p.dimension());

    	System.out.println("size original point cloud: "+PointCloud.size(N));
    	System.out.println("size Kd-Tree (total number of nodes): "+KdTree.size(kdtree));
    	System.out.println("number of leaves in the tree: "+leavesNumber(kdtree));
    	KdTree.checkBalance(kdtree);

    }

    public static void main(String[ ] args) throws Exception {
    	System.out.println("Exercice 1: testing Kd-trees");
    	PointCloud N;
    	
    	if (args.length < 1) {
    		N=PointCloud.randomPoints(3000, 3);
    	}
    	else { 
    		N=Clustering.readFile(args[0]);
    	}
    	Draw.draw2D(N, "original point cloud");
    	Draw.draw3D(N);
    	
    	testMedianComputing(N);
    	//testConstructionKdTree(N);

    	System.out.println("end");
    }
    
    
    //-----------------------------------
    //--- Methods on binary trees ---
    //-----------------------------------

    
    
    static public int size(KdTree t) {
    	int result=0;
    	if(t==null) return 0;
    	else {
    		result=size(t.lowerHalf);
    		result=result+size(t.upperHalf);
    	}
    	return result+1;
    }

    static public boolean checkBalance(KdTree t) {
     	if (t.lowerHalf==null && t.upperHalf==null)
     		return true;
    	if (t.numPoints < 200) 
     		return true;
    	
    	boolean result=true;
        if ((double)t.lowerHalf.numPoints/(double)t.numPoints < 0.25) {
        	System.out.println("Bad balance in kd-tree! (" + 
     	 		 t.lowerHalf.numPoints + "/" + t.numPoints +
     	 		 "=" + (double)t.lowerHalf.numPoints/(double)t.numPoints + ")");
        	result=false;
        }
        if ((double)t.lowerHalf.numPoints/(double)t.numPoints > 0.75) {
        	System.out.println("Bad balance in kd-tree! (" + 
     	 		 t.lowerHalf.numPoints + "/" + t.numPoints +
     	 		 "=" + (double)t.lowerHalf.numPoints/(double)t.numPoints + ")");
        	result=false;
        }
    	result=result && checkBalance(t.lowerHalf);
    	result=result && checkBalance(t.upperHalf);
    	
        return result;
    }

    static public int leavesNumber(KdTree t) {
    	int result=0;
    	if(t==null) return 0;
    	if(t.upperHalf==null && t.lowerHalf==null)
    		return 1;
    	else {
    		result=leavesNumber(t.lowerHalf);
    		result=result+leavesNumber(t.upperHalf);
    	}
    	return result;
    }

    static public String toString(KdTree t) {
    	String result="";
    	if(t==null) return result;
    	if(t.upperHalf==null && t.lowerHalf==null)
    		return ""+t.points+"\n";
    	else {
    		result=toString(t.lowerHalf);
    		result=result+toString(t.upperHalf);
    	}
    	return result;
    }
    
    static public ArrayList<Point_D> toList(KdTree t, ArrayList<Point_D> list) {
    	if(t==null) return list;
    	if(t.upperHalf==null && t.lowerHalf==null)
    		list.add(t.points.p);
    	else {
    		toList(t.lowerHalf, list);
    		toList(t.upperHalf, list);
    	}
    	return list;
    }

}
