package Jcg.triangulations2D;

import Jcg.geometry.Point_;

/**
 * A dummy class used to represent a triangulation edge as a pair (TriangulationDSFace_2, int).
 * The integer parameter is the index of the vertex opposite to the edge in the triangle. 
 * Note that no edge is explicitly stored in the triangulation data structure, 
 * as this information is redundant with the one provided by
 * the faces themselves and their vertices.
 *
 * @author Luca Castelli Aleardi
 *
*/
public class HalfedgeHandle<X extends Point_> {

	private TriangulationDSFace_2<X> c;
	private int ind;
//	private TriangulationDSVertex_2<X>[] vertices=new TriangulationDSVertex_2<X>[2];
	private TriangulationDSVertex_2<X> orig, dest;
	
	public HalfedgeHandle(TriangulationDSFace_2<X> c, int i) {
    	if (i<0 || i>2)
    		throw new Error("Bad vertex index: " + i);
		this.c = c;
		this.ind = i;
//		for (int j=0; j<2; j++)
//    		this.vertices[j]=c.vertex((i+1+j)%3);
		orig = c.vertex((i+1)%3);
		dest = c.vertex((i+2)%3);
	}
	
	/**
	 * returns the face containing the half-edge.
	 */
	public TriangulationDSFace_2<X> getFace() {
		return this.c;
	}

	/**
	 * returns the (destination) vertex incident the edge.
	 */
	public TriangulationDSVertex_2<X> getVertex() {
    	return dest;
	}

	/**
	 * returns next half-edge in ccw order in the same face.
	 */
	public HalfedgeHandle<X> getNext() {
    	return new HalfedgeHandle<X>(getFace(), (ind+1)%3);
	}

	/**
	 * returns the previous half-edge in ccw order in the same face.
	 */
	public HalfedgeHandle<X> getPrev() {
    	return new HalfedgeHandle<X>(getFace(), (ind+2)%3);
	}

	/**
	 * returns opposite half-edge (lying in the adjacent face).
	 */
	public HalfedgeHandle<X> getOpposite() {
    	TriangulationDSFace_2<X> adjacentFace=this.getFace().neighbor(ind);
    	if(adjacentFace==null) return null; // boundary edge
    	int newIndex=adjacentFace.index(this.getFace());
    	return new HalfedgeHandle<X>(adjacentFace, newIndex);
	}

	/**
	 * returns the vertex of prescribed index in the edge: 0 for origin, 1 for destination
	 */
	public TriangulationDSVertex_2<X> getVertex(int index) {
    	switch(index) {
    	case 0 : return orig;
    	case 1 : return dest;
    	default : throw new Error("vertex index error: " + index);
    	}
	}

	/**
	 * returns the index of the vertex opposite of the current edge
	 * in the corresponding incident face.
	 */
	public int index() {
		return this.ind;
	}

	/**
	 * returns whether the current edge has v as vertex.
	 */
	public boolean hasVertex(TriangulationDSVertex_2<X> v){
		if(this.getVertex(0)==v) return true;
		if(this.getVertex(1)==v) return true;
		return false;
	}

	/**
	 * returns the index of v in the current edge. 
	 * The pre-requisite is that v must be a vertex of the edge.
	 */
	public int index(TriangulationDSVertex_2<X> v){
		if(this.getVertex(0)==v) return 0;
		if(this.getVertex(1)==v) return 1;
		throw new Error("index error: no incident vertex");
	}

	/**
	 * tests equality between EdgeHandles, which is defined by equality of their vertices.
	 */
	public boolean equals (Object o) {
		HalfedgeHandle<X> f = (HalfedgeHandle<X>) o;
		return f.hasVertex(this.getVertex(0)) && f.hasVertex(this.getVertex(1)) &&
		this.hasVertex (f.getVertex(0)) && this.hasVertex (f.getVertex(1)); 
	}

	/**
	 * provides a hashing index for a EdgeHandle, based on the hashing indices of its vertices. 
	 */
	public int hashCode () {
		Point_ u =(Point_) getVertex(0).getPoint(), v = (Point_)getVertex(1).getPoint();
		if (u.compareTo(v) > 0) {
			Point_ temp = u;
			u = v;
			v = temp;
		}
		return u.hashCode() + v.hashCode();
	}
	
	public String toString() {
		return "("+this.getVertex(0).getPoint()+","+this.getVertex(1).getPoint()+")";
	}

    /**
     * returns the mark of the edge 
     */
    public boolean isMarked() { 
    	return c.cMarks.get(ind).booleanValue();
    }

    /**
     * sets the mark of the edge (the mark of the opposite edge remains unchanged) 
     */
    public void mark() { 
    	c.cMarks.set(ind, true);
    }

    /**
     * unsets the mark of the edge (the mark of the opposite edge remains unchanged) 
     */
    public void unmark() { 
    	c.cMarks.set(ind, false);
    }

    /**
     * inverts the mark of the edge (the mark of the opposite edge remains unchanged)
     */
    public void invertMark() { 
    	c.cMarks.set(ind, !c.cMarks.get(ind).booleanValue());
    }

    /**
     * sets the mark of the edge (the mark of the opposite edge remains unchanged)
     */
    public void setMark(boolean m) { 
    	c.cMarks.set(ind, m);
    }
}
