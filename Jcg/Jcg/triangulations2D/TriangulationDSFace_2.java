package Jcg.triangulations2D;

import Jcg.geometry.GeometricOperations_2;
import Jcg.geometry.Point_;

import java.util.*;

/**
 * The class used to encode a face of a triangulation. Each cell stores pointers to its three vertices as
 * well as to its four neighboring faces. The vertices and neighbors are indexed 0, 1, and 2. 
 * Neighbor i lies opposite to vertex i. By convention, edge i is the intersection of the face and of 
 * neighbor i, therefore it also lies opposite to vertex i.
 *
 * @author Luca Castelli Aleardi and Steve Oudot
 * 
 */
public class TriangulationDSFace_2<X extends Point_> implements Comparable<TriangulationDSFace_2<X>> {

	protected ArrayList<TriangulationDSFace_2<X>> neighbors;
	protected ArrayList<TriangulationDSVertex_2<X>> vertices;
	// for constraint Delaunay triangulations, marking of the edges is necessary
	protected ArrayList<Boolean> cMarks;
	// add a tag for walks
	public int tag;
	
	/**
	 * creates an empty cell, whose vertices and neighbors need to be specified later.
	 */
	public TriangulationDSFace_2() {
		this.neighbors=new ArrayList<TriangulationDSFace_2<X>>(3);
		this.vertices=new ArrayList<TriangulationDSVertex_2<X>>(3);
		this.cMarks=new ArrayList<Boolean>(3);
		// By default, marks are set to false
		for (int i=0; i<3; i++)
			cMarks.add(false);
	}

	/**
	 * creates a face with vertices v0 through v2 and with neighbors c0 through c2. 
	 * Each ci is opposite to vi.
	 */
	public TriangulationDSFace_2(
		    	 TriangulationDSVertex_2<X> v0, TriangulationDSVertex_2<X> v1,
		    	 TriangulationDSVertex_2<X> v2,	
		    	 TriangulationDSFace_2<X> c0, TriangulationDSFace_2<X> c1,
		    	 TriangulationDSFace_2<X> c2){
		this.neighbors=new ArrayList<TriangulationDSFace_2<X>>(3);
		this.vertices=new ArrayList<TriangulationDSVertex_2<X>>(3);

		this.neighbors.add(c0); this.neighbors.add(c1); this.neighbors.add(c2);
		this.vertices.add(v0); this.vertices.add(v1); this.vertices.add(v2);

		this.cMarks=new ArrayList<Boolean>(3);
		// By default, marks are set to false
		for (int i=0; i<3; i++)
			cMarks.add(false);
	}
	
	/**
	 * returns a pointer to the i-th vertex of the triangle (0<=i<3).
	 */
	public TriangulationDSVertex_2<X> vertex(int i){
		if(i<0 || i>2) throw new Error("vertex index error");
		//System.out.println("index: "+i);
		return this.vertices.get(i);
	}

	/** 
	 * returns an array containing the geometric points associated with the vertices of the triangle
	 */
	public Point_[] verticesPoints () {
		return new Point_[] {
				(Point_)this.vertices.get(0).getPoint(), 
				(Point_)this.vertices.get(1).getPoint(), 
				(Point_)this.vertices.get(2).getPoint()
		};
	}
	
	/**
	 * returns a pointer to the i-th neighbor of the cell (0<=i<3), 
	 * which is by definition the one opposite to the i-th vertex of the cell.
	 */
	public TriangulationDSFace_2<X> neighbor(int i){
		if(i<0 || i>2) throw new Error("vertex index error");
		else return this.neighbors.get(i);
	}

	/**
	 * sets the i-th vertex of the cell to be v (0<=i<3).
	 */
	public void setVertex(int i, TriangulationDSVertex_2<X> v){
		if(i<0 || i>2) throw new Error("vertex index error");
		if(v==null) throw new Error("null vertex error");
		else this.vertices.set(i, v);
	}

	/**
	 * sets the i-th neighbor of the cell to be c (0<=i<3).
	 */
	public void setNeighbor(int i, TriangulationDSFace_2<X> c){
		if(i<0 || i>2) throw new Error("vertex index error");
		else this.neighbors.set(i, c);
	}
	
	/**
	 * returns the index of vertex v in the cell, and throws an Error if v is no vertex of the cell.
	 */
	public int index(TriangulationDSVertex_2<X> v) {
		if(!hasVertex(v)) throw new Error("index error: " + v + " is not a vertex of " + this);
		return this.vertices.indexOf(v);
	}

	/**
	 * returns the index of the neighboring face c, 
	 * and throws an Error if c is no neighbor of the face.
	 */
	public int index(TriangulationDSFace_2<X> c) {
		if (this.neighbors.contains(c))
			return neighbors.indexOf(c);
		for (int k=0; k<3; k++)
			if (c.hasVertex(vertices.get((k+1)%3)) && c.hasVertex(vertices.get((k+2)%3))) {
//				System.out.println("Warning: asking for index of geometric neighbor that is not neighbor");
				return k;
			}
		throw new Error("Error: asking neighbor index when there is no neighbor in " + this);
	}

	/**
	 * checks whether v is a vertex of the cell.
	 */
	public boolean hasVertex(TriangulationDSVertex_2<X> v) {
		return this.vertices.contains(v);
	}

	/**
	 * checks whether x is a point associated with a vertex of the face.
	 */
	public boolean hasVertex(X x) {
		for (TriangulationDSVertex_2<X> v : vertices)
			if (v.getPoint().equals(x))
				return true;
		return false;
	}

	/**
	 * returns the index of the vertex of the face associated with x, and throws an Error if x is 
	 * associated with no vertex of the face.
	 */
	public int index(X x) {
		for (TriangulationDSVertex_2<X> v : vertices)
			if (v.getPoint().equals(x))
				return vertices.indexOf(v);
		throw new Error ("No vertex " + x + " found in " + this);
	}

	/**
	 * checks whether c is a neighbor of the face.
	 */
	public boolean hasNeighbor(TriangulationDSFace_2<X> c) {
		return this.neighbors.contains(c);
	}

	/**
	 * checks whether c is geometrically a neighbor of the face, that is, whether the two triangles 
	 * share a common edge.
	 */
	public boolean hasGeometricNeighbor(TriangulationDSFace_2<X> c) {
		if (this.neighbors.contains(c))
			return true;
		for (int k=0; k<3; k++)
			if (c.hasVertex(vertices.get((k+1)%3)) && c.hasVertex(vertices.get((k+2)%3)))
				return true;
		return false;
	}

    /**
     * generates a string listing the coordinates of the vertices of the face.
     */
    public String toString() {
    	String result= "Triangle";
    	String incidentVertices=""+vertices.get(0) + ","+ vertices.get(1) + ","+ vertices.get(2);
    	
    	return result+"("+incidentVertices+")";
    }
    
    /**
     * compares the face to another face using a clockwise order on their vertices 
     */
//    public int compareTo(Object o) { 
//		TriangulationDSFace_2<Point_> c = (TriangulationDSFace_2<Point_>) o;
//		TreeSet<Point_> v = new TreeSet<Point_>();
//		TreeSet<Point_> vc = new TreeSet<Point_>();
//		for (int i=0; i<3; i++) {
//			v.add((Point_)this.vertex(i).getPoint());
//			vc.add((Point_)c.vertex(i).getPoint());
//		}
//		for (Iterator<Point_> pit = v.iterator(), qit = vc.iterator(); pit.hasNext();) {
//			Point_ p = pit.next();
//			Point_ q = qit.next();
//			int comp = p.compareTo(q);
//			if (comp != 0)
//				return comp;
//		}
//		return 0;
//	}
	public int compareTo(TriangulationDSFace_2<X> f) {
		int i0 = vertex(0).compareTo(f.vertex(0));
		int i1 = vertex(1).compareTo(f.vertex(1));
		int i2 = vertex(2).compareTo(f.vertex(2));
		if (i0 != 0)
			return i0;
		if (i1 != 0)
			return i1;
		return i2;
	}

    /**
     * returns the mark of the index-th edge 
     */
    public boolean isMarked(int index) { 
    	return cMarks.get(index).booleanValue();
    }

    /**
     * sets the mark of the index-th edge 
     */
    public void mark(int index) { 
    	cMarks.set(index, true);
    }

    /**
     * unsets the mark of the index-th edge 
     */
    public void unmark(int index) { 
    	cMarks.set(index, false);
    }

    /**
     * inverts the mark of the index-th edge 
     */
    public void invertMark(int index) { 
    	cMarks.set(index, !cMarks.get(index).booleanValue());
    }
}
