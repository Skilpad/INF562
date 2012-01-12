

import java.util.ArrayList;

import Jama.Matrix;
import Jcg.geometry.*;
import Jcg.graph.*;
import Jcg.polyhedron.LoadMesh;
import Jcg.polyhedron.MeshRepresentation;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.viewer.*;

public class TutteDrawing<X extends Point_> extends GraphDrawing<X>{

	Matrix laplacian;
	static int nIterations=5;
	Point_[] exteriorPoints;

	public TutteDrawing() {}

	public TutteDrawing(Graph g, Point_2[] exteriorPoints) {
		this.g=g;
    	this.points=new ArrayList<X>(g.sizeVertices());
    	this.exteriorPoints=exteriorPoints;
	}

	/**
	 * return the laplacian matrix of the graph G-F
	 * where F is a peripherical cycle.
	 * The k first vertices of G are assumed to be the vertices of the cycle F
	 */	
	public Matrix computeLaplacianG_F() {
		throw new Error("a' completer");
	}

	/**
	 * return the column vector Bx
	 */	
	public Matrix computeBx() {
		throw new Error("a' completer");
	}

	/**
	 * return the column vector By
	 */	
	public Matrix computeBy() {
		throw new Error("a' completer");
	}

	/**
	 * compute the Tutte drawing of a planar 3-connected graph
	 * solving two systems of linear equations
	 */	
	public void computeDrawing() {
		throw new Error("a' completer");
	}

}
