package Jcg.triangulations3D;

import Jcg.geometry.*;

/**
 * 
 * The class used to encode a vertex of a triangulation. Each vertex stores a pointer to one of 
 * its incident cells. 
 *
 * @author Luca Castelli Aleardi and Steve Oudot
 *
 */
public class TriangulationDSVertex_3<X extends Point_3> {
	
	TriangulationDSCell_3<X> cell=null;
	X point=null;
	public int tag;

	/**
	 * creates an empty vertex, with no associated geometric point.
	 *
	 */
	public TriangulationDSVertex_3() {}

	/**
	 * creates a vertex with an associated geometric point.
	 * @param point
	 */
	public TriangulationDSVertex_3(X point) { this.point=point; }

	/**
	 * creates a vertex with an associated geometric point and an associated cell.
	 */
	public TriangulationDSVertex_3(TriangulationDSCell_3<X> c, X point) {
	    	this.cell=c;
	    	this.point=point; 
	    }

	/**
	 * sets the cell associated with the vertex.
	 */
	public void setCell(TriangulationDSCell_3<X> c) { this.cell=c; }

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
	public TriangulationDSCell_3<X> getCell() { return this.cell; } 
	    
	/**
	 * returns a string listing the coordinates of the geometric point associated with the vertex.
	 */
	public String toString(){
	        return "vertex3D "+point.toString();
	    }	

}
