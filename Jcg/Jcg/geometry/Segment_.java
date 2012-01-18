package Jcg.geometry;

public class Segment_<X extends Kernel> implements Segment_interface<X>{

	protected Point_<X> source, target;

	public Segment_(Point_<X> source, Point_<X> target) {
		this.source = source; this.target = target;
	}

	/** Returns source if i=0, target otherwise. **/
	public Point_<X> vertex(int i) {
		return (i==0) ? source : target;
	}

	/** Returns the vector s.target() - s.source() **/
	public Vector_<X> toVector() {
		return new Vector_<X>(source, target); 
	}

	/** Returns segment with source and target interchanged **/
	public Segment_<X> opposite() {
		return new Segment_<X>(target, source);
	}

	/** Returns squared length **/
	public Number squaredLength() {
		return source.squareDistance(target);
	}

	/**
	 * A point is on s, 
	 * iff it is equal to the source or target of s, 
	 * or if it is in the interior of s
	 */  
	public boolean hasOn(Point_ p) {
		throw new Error("A completer");
		// TODO
	}

	/** Segment s is degenerate, if source and target fall together **/
	public boolean isDegenerate() {
		return source.equals(target);
	}

	public String toString() {return "[ "+source+" , "+target+" ]"; }

	public int dimension() {
		return source.dimension();
	}


}
