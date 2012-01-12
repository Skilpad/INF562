package Jcg.geometry;

public class Segment_3 implements Segment_ {
  public Point_3 p, q;

  public Segment_3() {}
  
  public Segment_3(Point_3 p, Point_3 q) { this.p=p; this.q=q; }

  public Point_ source() {return this.p; }
  public Point_ target() {return this.q; }
  public Point_ vertex(int i) {
  	if(i==0)return this.p; 
  	else return this.q;	
  }
  
/**
 * returns the vector s.target() - s.source()
 */
  public Vector_ toVector() {
  	return new Vector_3(this.p,this.q);
  }

/**
 * returns a segment with source and target interchanged
 */
  public Vector_ opposite() {
  	return new Vector_3(this.q,this.p);
  }
/**
 * returns the squared length
 */  
  public Number squaredLength() {
  	return toVector().squaredLength();  	
  }

/**
 * A point is on s, 
 * iff it is equal to the source or target of s, 
 * or if it is in the interior of s
 */  
  public boolean hasOn(Point_ p) {
  	throw new Error("A completer");
  }

/** 
 * segment s is degenerate, if source and target fall together
 */  
  public boolean isDegenerate() {
  	return this.squaredLength().doubleValue()==0.;
  }
    
  public String toString() {return "["+p+","+q+"]"; }
  public int dimension() { return 3;}
  
}




