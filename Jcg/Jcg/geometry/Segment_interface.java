package Jcg.geometry;

public interface Segment_interface<X extends Kernel> {
	
	/** Returns source **/
	public Point_<X> source();
	/** Returns target **/
	public Point_<X> target();
	/** Set source **/
	public void source(Point_<X> X);
	/** Set target **/
	public void target(Point_<X> X);
	
	/** Returns source if i=0, target otherwise. **/
	public Point_<X> vertex(int i);

	/** Returns the vector s.target() - s.source() **/
	public Vector_<X> toVector();

	/** Returns segment with source and target interchanged **/
	public Segment_interface<X> opposite();

	/** Returns squared length **/
	public Number squaredLength();

	/**
	 * A point is on s, 
	 * iff it is equal to the source or target of s, 
	 * or if it is in the interior of s
	 */  
	public boolean hasOn(Point_ p);

	/** Segment s is degenerate, if source and target fall together **/
	public boolean isDegenerate();

	public String toString();

	public int dimension();


}
