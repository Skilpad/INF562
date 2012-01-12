package Jcg.triangulations2D;

import Jcg.geometry.*;

/**
 * 
 * The class used to encode a vertex of a triangulation. Each vertex stores a pointer to one of 
 * its incident faces. 
 *
 * @author Luca Castelli Aleardi and Steve Oudot
 *
 */
public class TriangulationDSVertex_2<X extends Point_> implements Comparable<TriangulationDSVertex_2<X>> {
	
	TriangulationDSFace_2<X> face=null;
	X point=null;
	public int tag;
	public int index; // useful for input/output operations and format conversions

	/**
	 * creates an empty vertex, with no associated geometric point.
	 *
	 */
	public TriangulationDSVertex_2() {}

	/**
	 * creates a vertex with an associated geometric point.
	 * @param point
	 */
	public TriangulationDSVertex_2(X point) { this.point=point; }

	/**
	 * creates a vertex with an associated geometric point and an associated cell.
	 */
	public TriangulationDSVertex_2(TriangulationDSFace_2<X> c, X point) {
	    	this.face=c;
	    	this.point=point; 
	    }

	/**
	 * sets the face associated with the vertex.
	 */
	public void setFace(TriangulationDSFace_2<X> c) { this.face=c; }

	/**
	 * sets the geometric point associated with the vertex.
	 */
	public void setPoint(X point) { this.point=point; }  
	    
	/**
	 * returns the geometric point associated with the vertex.
	 */
	public X getPoint() { return this.point; } 

	/**
	 * returns the cell associated with the vertex.
	 */
	public TriangulationDSFace_2<X> getFace() { return this.face; } 
	    
	/**
	 * returns a string listing the coordinates of the geometric point associated with the vertex.
	 */
	public String toString(){
	        return "vertex "+point.toString();
	    }

	public int compareTo(TriangulationDSVertex_2<X> v) {
		return getPoint().compareTo(v.getPoint());
	}	

}
