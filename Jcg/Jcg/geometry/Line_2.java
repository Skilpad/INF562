package Jcg.geometry;

/*
 * Class for representing an oriented line in 2D. 
 * It is defined by the set of points with Cartesian coordinates (x,y) 
 * that satisfy the line equation
 * l : a x + b y + c  = 0  
 *
 * The line splits the 2D plane in a positive and a negative side. 
 * A point p with Cartesian coordinates (px, py) is on the positive side of l, 
 * iff 
 * a px +b py + c > 0. 
 * It is on the negative side, iff a px +b py + c < 0
 */
public class Line_2 extends Hyperplan<Kernel_2>{

	public Line_2() {}

	/** Creates line given by: a*x + b*y + c = 0 **/
	public Line_2(double a, double b, double c) { 
		this.M = new Point_2(0,-c/b);
		this.n = new Vector_2(a,b);
	}

	public int dimension() { return 2;}
}
