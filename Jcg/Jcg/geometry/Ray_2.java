package Jcg.geometry;

public class Ray_2 {
  protected Point_2 p;
  protected Vector_2 v;

  public Ray_2() {}
  
  public Ray_2(Point_2 p, Vector_2 v) { 
	  	this.p=p; 
	  	this.v=v;
	  }

  public Ray_2(Segment_2 s) { 
	  	this.p = (Point_2)s.source(); 
	  	this.v = new Vector_2((Point_2)s.source(), (Point_2)s.target());
	  }

  public Point_2 source() {return p; }
  public Vector_2 direction() {return v; }
  
  public boolean equals(Ray_2 r) { 
  	return this.p.equals(r.source()) &&
  		GeometricOperations_2.collinear
  		(new Point_2(0,0), new Point_2(v.getX(), v.getY()),
  				new Point_2(r.direction().getX(), r.direction().getY())); 
  }

  public String toString() {return p+" "+v; }
  public int dimension() { return 2;}
  
}




