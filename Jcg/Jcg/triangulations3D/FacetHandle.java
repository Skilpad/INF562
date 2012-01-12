package Jcg.triangulations3D;

import Jcg.geometry.Point_3;

/**
 * A dummy class used to represent a triangulation facet as a pair (TriangulationDSCell_3, int).
 * The integer parameter is the index of the vertex opposite to the facet in the cell. Note that no facet is
 * stored in the triangulation data structure, as this information is redundant with the one provided by
 * the cells themselves and their vertices.
 *
 * @author Steve Oudot
 *
*/
public class FacetHandle<X extends Point_3> {

	private TriangulationDSCell_3<X> c;
	private int ind;
	private TriangulationDSVertex_3[] vertices=new TriangulationDSVertex_3[3];
	
	public FacetHandle(TriangulationDSCell_3<X> c, int i) {
    	if (i<0 || i>3)
    		throw new Error("Bad vertex index: " + i);
		this.c = c;
		this.ind = i;
		for (int j=0; j<3; j++)
    		this.vertices[j]=c.vertex((i+1+j)&3);
	}
	
	/**
	 * returns a cell containing the facet.
	 */
	public TriangulationDSCell_3<X> cell() {
		return this.c;
	}

	/**
	 * returns the index of the vertex opposite of the current fact in cell().
	 */
	public int index() {
		return this.ind;
	}

	/**
	 * returns the vertex of prescribed index in the facet.
	 */
	public TriangulationDSVertex_3<X> vertex(int index) {
    	if(index<0 || index>2) throw new Error("vertex index error");
    	else return (TriangulationDSVertex_3<X>)this.vertices[index];
	}
	

	/**
	 * returns whether the current facet has v as vertex.
	 */
	public boolean hasVertex(TriangulationDSVertex_3<X> v){
		for(int i=0;i<3;i++)
			if(this.vertex(i)==v) return true;
		return false;
	}

	/**
	 * returns the index of v in the current facet. The pre-requisite is that v must be a vertex of the facet.
	 */
	public int index(TriangulationDSVertex_3<X> v){
		for(int i=0;i<3;i++)
			if(this.vertex(i)==v) return i;
		throw new Error("index error: no incident vertex");
	}

	/**
	 * tests equality between FaceHandles, which is defined by equality of their vertices.
	 */
	public boolean equals (Object o) {
		FacetHandle<X> f = (FacetHandle<X>) o;
		return f.hasVertex(this.vertex(0)) && f.hasVertex(this.vertex(1)) && f.hasVertex(this.vertex(2)) &&
		this.hasVertex (f.vertex(0)) && this.hasVertex (f.vertex(1)) && this.hasVertex (f.vertex(2)); 
	}

	/**
	 * provides a hashing index for a FaceHandle, based on the hashing indices of its vertices. 
	 */
	public int hashCode () {
		Point_3 u = vertex(0).getPoint(), v = vertex(1).getPoint(), w = vertex(2).getPoint();
		if (u.compareTo(v) > 0) {
			Point_3 temp = u;
			u = v;
			v = temp;
		}
		if (u.compareTo(w) > 0) {
			Point_3 temp = u;
			u = w;
			w = temp;
		}
		if (v.compareTo(w) > 0) {
			Point_3 temp = v;
			v = w;
			w = temp;
		}
		if (u.compareTo(v) > 0 || v.compareTo(w) > 0)
			throw new Error("Problem in vertex sorting");

		return u.hashCode() + v.hashCode() + w.hashCode();
	}
}
