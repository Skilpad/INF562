package Jcg.geometry;

public class Segment_2 implements Segment_interface<Kernel_2> {

	protected Point_2 source, target;
	
	/** Creates segment from source to target. (Will not copy source & target.) **/
	public Segment_2(Point_2 source, Point_2 target) {
		this.source = source; this.target = target;
	}
	/** Creates segment from source to target. (Will copy source & target.) **/
	public Segment_2(Point_ source, Point_ target) {
		this.source = new Point_2(source); this.target = new Point_2(target);
	}
	
	/** Returns source if i=0, target otherwise. **/
	public Point_2 vertex(int i) {
		return (i==0) ? source : target;
	}

	/** Returns the vector s.target() - s.source() **/
	public Vector_2 toVector() {
		return new Vector_2(source, target); 
	}

	/** Returns segment with source and target interchanged **/
	public Segment_2 opposite() {
		return new Segment_2(target, source);
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
	
	
	public Point_2 source() { return source; }
	public Point_2 target() { return target; }
	
	/** Set source (without copy) **/
	public void source(Point_2 X) { source = X; }
	/** Set target (without copy) **/
	public void target(Point_2 X) { target = X; }
	/** Set source (with copy) **/
	public void source(Point_<Kernel_2> X) { source = new Point_2(X); }
	/** Set target (with copy) **/
	public void target(Point_<Kernel_2> X) { target = new Point_2(X); }

}




