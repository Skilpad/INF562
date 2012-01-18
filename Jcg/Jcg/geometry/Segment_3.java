package Jcg.geometry;

public class Segment_3 implements Segment_interface<Kernel_3> {
	
	protected Point_3 source, target;
	
	/** Creates segment from source to target. (Will not copy source & target.) **/
	public Segment_3(Point_3 source, Point_3 target) {
		this.source = source; this.target = target;
	}
	/** Creates segment from source to target. (Will copy source & target.) **/
	public Segment_3(Point_ source, Point_ target) {
		this.source = new Point_3(source); this.target = new Point_3(target);
	}
	
	/** Returns source if i=0, target otherwise. **/
	public Point_3 vertex(int i) {
		return (i==0) ? source : target;
	}

	/** Returns the vector s.target() - s.source() **/
	public Vector_3 toVector() {
		return new Vector_3(source, target); 
	}

	/** Returns segment with source and target interchanged **/
	public Segment_3 opposite() {
		return new Segment_3(target, source);
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

	
	public Point_3 source() { return source; }
	public Point_3 target() { return target; }
	
	/** Set source (without copy) **/
	public void source(Point_3 X) { source = X; }
	/** Set target (without copy) **/
	public void target(Point_3 X) { target = X; }
	/** Set source (with copy) **/
	public void source(Point_<Kernel_3> X) { source = new Point_3(X); }
	/** Set target (with copy) **/
	public void target(Point_<Kernel_3> X) { target = new Point_3(X); }
	
}




