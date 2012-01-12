package Jcg.geometry;

/*
 * Class for representing an oriented plane in the three-dimensional Euclidean space. 
 * It is defined by the set of points with Cartesian coordinates (x,y,z) 
 * that satisfy the plane equation
 * h : a x + b y + c z + d = 0  
 *
 * The plane splits the 3D space in a positive and a negative side. 
 * A point p with Cartesian coordinates (px, py, pz) is on the positive side of h, 
 * iff 
 * a px +b py +c pz + d > 0. 
 * It is on the negative side, iff a px +b py +c pz + d < 0
 */
public class Plane_3 {
  public Double a, b, c, d;

  public Plane_3() {}
  
  public Plane_3(Number a,Number b, Number c, Number d) { 
  	this.a=a.doubleValue(); 
  	this.b=b.doubleValue();
  	this.c=c.doubleValue();
  	this.d=d.doubleValue(); 
  }

/*
 * creates a plane h passing through the points p, q and r. 
 * The plane is oriented such that p, q and r are oriented in a positive sense 
 * (that is counterclockwise) when seen from the positive side of h
 */  
  public Plane_3(Point_3 p, Point_3 q, Point_3 r) {
  	Vector_3 v1=new Vector_3(p,q);
  	Vector_3 v2=new Vector_3(p,r);
  	Vector_3 n=v1.crossProduct(v2);
  	this.a= n.getX().doubleValue();
  	this.b= n.getY().doubleValue();
  	this.c= n.getZ().doubleValue();
  	this.d= -(n.getX().doubleValue()*p.getX().doubleValue())
  			-(n.getY().doubleValue()*p.getY().doubleValue())
  			-(n.getZ().doubleValue()*p.getZ().doubleValue());
  }

/*
 * Define a plane by specifying a point and a normal vector to the plane
 * The resulting equation is:
 * nx (x - x0) + ny (y - y0) + nz (z - z0) = 0
 */  
  public Plane_3(Point_3 p, Vector_3 n) { 
  	this.a= n.getX().doubleValue();
  	this.b= n.getY().doubleValue();
  	this.c= n.getZ().doubleValue();
  	this.d= -(n.getX().doubleValue()*p.getX().doubleValue())
  			-(n.getY().doubleValue()*p.getY().doubleValue())
  			-(n.getZ().doubleValue()*p.getZ().doubleValue());
  }

/*
 * returns an arbitrary point on h
 */    
  public Point_3 point() {
  	if(d==0) return new Point_3(0.,0.,0.);
  	else if(a!=0.) return new Point_3(-d/a,0.,0.);
  	else if(b!=0.) return new Point_3(0.,-d/b,0.);
  	else if(c!=0.) return new Point_3(0.,0.,-d/c);
  	else throw new Error("plane non well defined");
  } 

/*
 * returns a vector that is orthogonal to h 
 * and that is directed to the positive side of h
 */    
  public Vector_3 orthogonalVector() {
  	return new Vector_3(this.a,this.b,this.c);
  } 

/*
 * returns a vector orthogonal to orthogonal_vector().  
 */    
  public Vector_3 hbase1() { 
  	if(a==0. && b==0. & c==0)
  		throw new Error("plane non well defined");
  	if(a!=0. && b!=0.) 
  		return new Vector_3(new Point_3(-d/a,0.,0.),new Point_3(0.,-d/b,0.));
  	else if(a!=0. && c!=0.) 
  		return new Vector_3(new Point_3(-d/a,0.,0.),new Point_3(0.,0.,-d/c));
  	else if(b!=0. && c!=0.) 
  		return new Vector_3(new Point_3(0.,-d/b,0.),new Point_3(0.,0.,-d/c));
  	else if(a==0. && b==0.)	
 		return new Vector_3(new Point_3(0.,0.,-d/c),new Point_3(1.,0.,-d/c));
  	else if(a==0. && c==0.)	
 		return new Vector_3(new Point_3(0.,-d/b,0.),new Point_3(1.,-d/b,0.));
  	else if(b==0. && c==0.)	
 		return new Vector_3(new Point_3(-d/a,0.,0.),new Point_3(-d/a,1.,0.));
 	else
 		throw new Error("plane non well defined");
  } 

/*
 * returns a vector that is both orthogonal to base1(), 
 * and to orthogonal_vector(), 
 * and such that the result of 
 * orientation ( point(), point() + base1(), point()+base2(), point() + orthogonal_vector() ) 
 * is positive
 */    
  public Vector_3 hbase2() {
  	Vector_3 n=this.orthogonalVector();
  	return n.crossProduct(this.hbase1());
  } 

  public boolean hasOn(Point_3 p) {
  	if(a*p.getX().doubleValue() + b*p.getY().doubleValue() + c*p.getZ().doubleValue() + d == 0.) return true;
  	else return false;  
  } 

  public boolean hasOnPositiveSide(Point_3 p) {
  	if(a*p.getX().doubleValue() + b*p.getY().doubleValue() + c*p.getZ().doubleValue() + d > 0.) return true;
  	else return false;  
} 

  public boolean hasOnNegativeSide(Point_3 p) {
  	if(a*p.getX().doubleValue() + b*p.getY().doubleValue() + c*p.getZ().doubleValue() + d < 0) return true;
  	else return false;  
  } 
 
  public boolean equals(Plane_3 h) { 
  	throw new Error("A completer");  
  }

  public String toString() {return ""+a+"x +"+b+"y +"+c+"z+ "+d; }
  public int dimension() { return 3;}
  
}




