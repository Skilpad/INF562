

import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.viewer.*;

import java.util.*;

/**
 * A class representing a point cloud (as a linked list)
 */
public class PointCloud {

    public Point_D 		p;
    public PointCloud 	next;
    
    private int s;

    /** Constructor: add a new point to the cloud (copying or not) **/
    PointCloud (Point_D p, PointCloud n, boolean copy) {
    	if (p == null) return;
    	if (copy)
    		this.p = new Point_D (p);
    	else
    		this.p = p;
    	this.next = n;
    	this.s = ((n == null) ? 0 : n.s) + ((p == null) ? 0 : 1);
    }

    /** Creates PointCloud only containing only p **/
    PointCloud (Point_D p) {
    	this.p = p;
    	this.next = null;
    	this.s = (p == null) ? 0 : 1;
    }
    
    
    /** Creates a copy (same next, same p) **/
    private PointCloud(PointCloud c) {
    	this.next = c.next;
    	this.p = c.p;
    	this.s = c.s;
	}
    
    
    public String toString () {
	String s = "";
	for (PointCloud n = this; n != null; n = n.next)
	    s += n.p.toString() + "\n";
	return s;
    }
    
    /**
     * Return the size (number of points) of PointCloud N
     */
    public static int size(PointCloud N) {
    	return N.s;
    }

    /**
     * Return the size
     */
    public int size() {
    	return this.s;
    }
    
    /** Add point (copied or not) **/
    public void add(Point_D p, boolean copy) {
    	if (p == null) return;
    	this.next = (this.p == null && this.next == null) ? null : new PointCloud(this);
    	this.p = (copy) ? new Point_D (p) : p;
    	s++;
    }

    /** Add point (not copied) **/
    public void add(Point_D p) {
    	if (p == null) return;
    	this.next = (this.p == null && this.next == null) ? null : new PointCloud(this);
    	this.p = p;
    	s++;
    }
        
    /** Adds all points of pc (not copied) **/
    public void add(PointCloud pc) {
    	for (PointCloud pc_ = pc; pc_ != null; pc_ = pc_.next) this.add(pc_.p);
    }
        
    
    /** Return an array representation **/
    public Point_D[] toArray() {
    	Point_D[] r = new Point_D[s];
    	int i = 0;
		for (PointCloud pc = this; pc != null; pc = pc.next) 
			if (pc.p != null) { r[i] = pc.p; i++; }
		return r;
    }
    
    /**
     * return a point cloud of n random points (in the unit hyper-square in dimension dim)
     */
    public static PointCloud randomPoints(int n, int dim) {
    	PointCloud result=null;
    	for (int i=0; i<n; i++) {
    	    double[] c = new double[dim];
    	    for (int j=0; j<dim; ++j)
    	    	c[j] = Math.random();
    	    result= new PointCloud (new Point_D (c), result, true);
    	}
    	System.out.println("Generated point cloud from random points in dimension "+dim);
    	return result;
    }

    /**
     * return a point cloud of n random points sampled on a circle 
     * (according to normal distribution)
     */
    public static PointCloud randomPointsOnCircle(int n, int dim) {
    	PointCloud result=null;
    	double R=1.;
    	for (int i=0; i<n; i++) {
    	    double[] c = new double[dim];
    	    double radius=0.2;
    	    double angle=Math.random()*2*Math.PI;
    	    for (int j=0; j<dim; ++j) {    	
    	    	c[j] = -radius+radius*2.*Math.random();
    	    }
    	    c[0]=c[0]+R*Math.cos(angle);
    	    c[1]=c[1]+R*Math.sin(angle);
    	    result= new PointCloud (new Point_D (c), result, true);
    	}
    	System.out.println("Generated point cloud from random points in dimension "+dim);
    	return result;
    }

    /**
     * Return a random value according to the normal distribution
     */
    public double randNorm(double mean, double sigma) {
        double u = Math.sqrt(-2.0*Math.log(Math.random()));
        return (u+mean)*sigma;
    }
    
    public static void main(String[ ] args) throws Exception {
    	PointCloud N = null;
    	N=randomPoints(40,5);
    	
    	Draw.draw2D(N, "2D point cloud test");
    	Draw.draw3D(N);
    	//System.out.println(N);
    }
}
