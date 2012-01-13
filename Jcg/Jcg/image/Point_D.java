package Jcg.image;

import Jcg.geometry.*;
import Jcg.graph.GraphNode;

import java.util.*;

/*
 * Class for representing a point in d-dimension space 
 */
public class Point_D extends Point_d{
	
	  public int cluster=-1;
	
	  public Point_D(int d) {
		  super(d);
	  }

	  public Point_D(double[] coord) { 
		  super(coord);
	  }

	  public Point_D(Point_ p) { 
		  super(p);
	  }

	  /*
	   * Return the i-th coordinate of the point 
	   */
	  public double Cartesian(int i) {
		  return this.getCartesian(i).doubleValue();
	  }

	   /*
	   * Compute the mean of a point cloud 
	   */
	  public static Point_D mean (PointCloud N) {
		int dim = N.p.dimension();
		double[] coords = new double [dim];
		int totalWeight = 0;
		
		for (PointCloud n = N; n != null; n=n.next) {
		    totalWeight++;
		    for (int i=0; i<dim; i++)
			coords[i] += n.p.Cartesian(i);
		}
		for (int i=0; i<dim; i++)
			coords[i] /= totalWeight;

		return new Point_D (coords);
	  }

	   /*
	   * Compute the bounding box of a point cloud 
	   */
	  public static double[] boundingBox(PointCloud N) {
		  if(N==null) {
			  System.out.println("point cloud empty");
			  return null;
		  }
		  int dim=N.p.dimension();
		  double[] box=new double[2*dim];
		  
		  Point_D point=N.p;
		  for(int i=0;i<dim;i++) {
			  box[i]=point.Cartesian(i);
			  box[dim+i]=point.Cartesian(i);
		  }
	      	
		  PointCloud t=N;
		  while(t!=null) {
			  point=t.p;
			  for(int i=0;i<dim;i++) {
				  // updating minimum values
				  if(point.Cartesian(i)<box[i]) box[i]=point.Cartesian(i);
				  // updating maximum values
				  if(point.Cartesian(i)>box[i+dim]) box[i+dim]=point.Cartesian(i);
			  }
			  t=t.next;
		  }
	      return box;
	  }

}

/*
 * Class for comparing the i-th coordinate of two points 
 */
class CompareCoordinate implements Comparator<Point_D> {
	
	int dim;
	
	public CompareCoordinate(int dim) {
		this.dim=dim;
	}
	
	public int compare(Point_D p, Point_D q) {
		if(p.Cartesian(dim)<q.Cartesian(dim))
			return -1;
		else if(p.Cartesian(dim)>q.Cartesian(dim)) 
			return 1;
		return 0;
	}
	
}
