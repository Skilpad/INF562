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
public class Line_2 {

	  public Double a, b, c;

	  public Line_2() {}
	  
	  public Line_2(Number a,Number b, Number c) { 
	  	this.a=a.doubleValue(); 
	  	this.b=b.doubleValue();
	  	this.c=c.doubleValue();
	  }

	  public boolean hasOn(Point_2 p) {
	  	if(a*p.getX().doubleValue() + b*p.getY().doubleValue() + c == 0.) return true;
	  	else return false;  
	  } 

	  public boolean hasOnPositiveSide(Point_2 p) {
	  	if(a*p.getX().doubleValue() + b*p.getY().doubleValue() + c > 0.) return true;
	  	else return false;  
	} 

	  public boolean hasOnNegativeSide(Point_2 p) {
	  	if(a*p.getX().doubleValue() + b*p.getY().doubleValue() + c < 0) return true;
	  	else return false;  
	  } 
	 
	  public boolean equals(Line_2 l) { 
	  	throw new Error("A completer");  
	  }

	  public String toString() {return ""+a+"x +"+b+"y +"+c; }
	  public int dimension() { return 2;}
}
