package Jcg.image;

import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.viewer.*;

import java.util.*;

/**
 * A class representing a point cloud (as a linked list)
 */
public class PointCloud {

    public Point_D p;
    public PointCloud next;

    /**
     * Constructor: add a new point to the cloud (copying or not)
     */
    PointCloud (Point_D p, PointCloud n, boolean copy) {
    	if (copy)
    		this.p = new Point_D (p);
    	else
    		this.p = p;
    	next = n;
    }

    public String toString () {
	String s = "";
	for (PointCloud n = this; n != null; n = n.next)
	    s += n.p.toString() + "\n";
	return s;
    }
    
    /**
     * return the size (number of points)
     */
    public static int size(PointCloud N) {
    	int size = 0;
    	for (PointCloud n = N; n != null; n = n.next) size++;
    	return size;
    }
    
    /**
     * return a point cloud of n random points (in the unit hyper-square in dimension dim)
     */
    public static PointCloud randomPoints(int n, int dim) {
    	PointCloud result=null;
    	for (int i=0; i<n; i++) {
    	    double[] c = new double[dim];
    	    for (int j=0; j<dim; ++j)
    	    	c[j] = 1.*Math.random();
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
