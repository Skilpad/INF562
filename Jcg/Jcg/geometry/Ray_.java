package Jcg.geometry;

public class Ray_<X extends Kernel> implements Ray_interface<X>{
	
	protected Point_<X>  p;
	protected Vector_<X> v;
	
	public Ray_(Point_<X> p, Vector_<X> v) { 
		this.p = p; 
		this.v = v;
	}
	
	public Ray_(Segment_<X> s) { 
		this.p = s.source; 
		this.v = new Vector_<X>(s.source, s.target);
	}
	
	public Point_<X> source()     {return p; }
	public Vector_<X> direction() {return v; }
	
	public boolean equals(Ray_2 r) { 
		throw new Error("To complete");   // TODO
	}
	
	public String toString() {return p+" "+v; }
	
	public int dimension() { return p.dimension();}

}
