package Jcg.triangulations3D;

import Jcg.geometry.GeometricOperations_3;
import Jcg.geometry.Point_3;

import java.util.*;

/**
 * The class used to encode a cell of a triangulation. Each cell stores pointers to its four vertices as
 * well as to its four neighboring cells. The vertices and neighbors are indexed 0, 1, 2 and 3. 
 * Neighbor i lies opposite to vertex i. By convention, facet i is the intersection of the cell and of 
 * neighbor i, therefore it also lies opposite to vertex i.
 *
 * @author Luca Castelli Aleardi and Steve Oudot
 * 
 */
public class TriangulationDSCell_3<X extends Point_3> implements Comparable{

	ArrayList<TriangulationDSCell_3<X>> neighbors;
	ArrayList<TriangulationDSVertex_3<X>> vertices;
	public int tag;
	
	/**
	 * creates an empty cell, whose vertices and neighbors need to be specified later.
	 */
	public TriangulationDSCell_3() {
		this.neighbors=new ArrayList<TriangulationDSCell_3<X>>(4);
		this.vertices=new ArrayList<TriangulationDSVertex_3<X>>(4);
	}

	/**
	 * creates a cell with vertices v0 through v3 and with neighbors c0 through c4. Each ci is opposite to vi.
	 */
	public TriangulationDSCell_3(
		    	 TriangulationDSVertex_3<X> v0, TriangulationDSVertex_3<X> v1,
		    	 TriangulationDSVertex_3<X> v2, TriangulationDSVertex_3<X> v3,	
		    	 TriangulationDSCell_3<X> c0, TriangulationDSCell_3<X> c1,
		    	 TriangulationDSCell_3<X> c2, TriangulationDSCell_3<X> c3){
		this.neighbors=new ArrayList<TriangulationDSCell_3<X>>(4);
		this.vertices=new ArrayList<TriangulationDSVertex_3<X>>(4);

		this.neighbors.add(c0); this.neighbors.add(c1);
		this.neighbors.add(c2); this.neighbors.add(c3);
		this.vertices.add(v0); this.vertices.add(v1);
		this.vertices.add(v2); this.vertices.add(v3);
	}
	
	/**
	 * returns a pointer to the i-th vertex of the cell (0<=i<4).
	 */
	public TriangulationDSVertex_3<X> vertex(int i){
		if(i<0 || i>3) throw new Error("vertex index error");
		else return this.vertices.get(i);
	}

	/** 
	 * returns an array containing the geometric points associated with the vertices of the cell
	 */
	public Point_3[] verticesPoints () {
		return new Point_3[] {vertex(0).getPoint(), vertex(1).getPoint(), vertex(2).getPoint(), vertex(3).getPoint()};
	}
	
	/**
	 * returns a pointer to the i-th neighbor of the cell (0<=i<4), which is by definition the one opposite
	 * to the i-th vertex of the cell.
	 */
	public TriangulationDSCell_3<X> neighbor(int i){
		if(i<0 || i>3) throw new Error("vertex index error");
		else return this.neighbors.get(i);
	}

	/**
	 * sets the i-th vertex of the cell to be v (0<=i<4).
	 */
	public void setVertex(int i, TriangulationDSVertex_3<X> v){
		if(i<0 || i>3) throw new Error("vertex index error");
		else this.vertices.set(i, v);
	}

	/**
	 * sets the i-th neighbor of the cell to be c (0<=i<4).
	 */
	public void setNeighbor(int i, TriangulationDSCell_3<X> c){
		if(i<0 || i>3) throw new Error("vertex index error");
		else this.neighbors.set(i, c);
	}
	
	/**
	 * returns the index of vertex v in the cell, and throws an Error if v is no vertex of the cell.
	 */
	public int index(TriangulationDSVertex_3<X> v) {
		if(!hasVertex(v)) throw new Error("index error: " + v + " is not a vertex of " + this);
		return this.vertices.indexOf(v);
	}

	/**
	 * returns the index of the neighboring cell c, and throws an Error if c is no neighbor of the cell.
	 */
	public int index(TriangulationDSCell_3<X> c) {
		if (this.neighbors.contains(c))
			return neighbors.indexOf(c);
		for (int k=0; k<4; k++)
			if (c.hasVertex(vertices.get((k+1)&3)) &&
					c.hasVertex(vertices.get((k+2)&3)) &&
					c.hasVertex(vertices.get((k+3)&3))) {
//				System.out.println("Warning: asking for index of geometric neighbor that is not neighbor");
				return k;
			}
		throw new Error("Error: asking neighbor index when there is no neighbor in " + this);
	}

	/**
	 * checks whether v is a vertex of the cell.
	 */
	public boolean hasVertex(TriangulationDSVertex_3<X> v) {
		return this.vertices.contains(v);
	}

	/**
	 * checks whether x is a the point associated with a vertex of the cell.
	 */
	public boolean hasVertex(X x) {
		for (TriangulationDSVertex_3<X> v : vertices)
			if (v.getPoint().equals(x))
				return true;
		return false;
	}

	/**
	 * returns the index of the vertex of the cell associated with x, and throws an Error if x is 
	 * associated with no vertex of the cell.
	 */
	public int index(X x) {
		for (TriangulationDSVertex_3<X> v : vertices)
			if (v.getPoint().equals(x))
				return vertices.indexOf(v);
		throw new Error ("No vertex " + x + " found in " + this);
	}

	/**
	 * checks whether c is a neighbor of the cell.
	 */
	public boolean hasNeighbor(TriangulationDSCell_3<X> c) {
		return this.neighbors.contains(c);
	}

	/**
	 * checks whether c is geometrically a neighbor of the cell, that is, whether the two tetrahedra 
	 * share a common 2-face.
	 */
	public boolean hasGeometricNeighbor(TriangulationDSCell_3<X> c) {
		if (this.neighbors.contains(c))
			return true;
		for (int k=0; k<4; k++)
			if (c.hasVertex(vertices.get((k+1)&3)) &&
					c.hasVertex(vertices.get((k+2)&3)) &&
					c.hasVertex(vertices.get((k+3)&3)))
				return true;
		return false;
	}

    /**
     * generates a string listing the corrdinates of the vertices of the cell.
     */
    public String toString() {
    	return "Cell(" + vertices.get(0) + ","
    	+ vertices.get(1) + ","
    	+ vertices.get(2) + ","
    	+ vertices.get(3) + ")";
    }
    
    /**
     * compares the cell to another cell using a lexicographical order on their sorted vertices 
     * (treated as 3D points).
     */
    public int compareTo(Object o) { 
		TriangulationDSCell_3<Point_3> c = (TriangulationDSCell_3<Point_3>) o;
		TreeSet<Point_3> v = new TreeSet<Point_3>();
		TreeSet<Point_3> vc = new TreeSet<Point_3>();
		for (int i=0; i<4; i++) {
			v.add(this.vertex(i).getPoint());
			vc.add(c.vertex(i).getPoint());
		}
		for (Iterator<Point_3> pit = v.iterator(), qit = vc.iterator(); pit.hasNext();) {
			Point_3 p = pit.next();
			Point_3 q = qit.next();
			int comp = p.compareTo(q);
			if (comp != 0)
				return comp;
		}
		return 0;
	}


}
