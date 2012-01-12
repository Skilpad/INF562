package Jcg.geometry;

public interface Segment_ {

	public Point_ source();
	public Point_ target();
	public Point_ vertex(int i);
	  
	/**
	 * returns the vector s.target() - s.source()
	 */
	public Vector_ toVector();

}
