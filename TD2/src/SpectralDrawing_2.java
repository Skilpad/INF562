

import Jcg.geometry.*;
import Jcg.graph.*;

import Jama.*;
import java.util.*;

/**
 * Provides methods for drawing graphs in 2D using spectral based methods
 *
 * @author Luca Castelli Aleardi
 */
public class SpectralDrawing_2<X extends Point_> extends GraphDrawing<X>{

	Matrix laplacian;

	public SpectralDrawing_2() {}

	public SpectralDrawing_2(int n) {
    	this.points = new ArrayList<X>(n);
	}

	public SpectralDrawing_2(Graph g) {
		this.g = g;
    	this.points = new ArrayList<X>(g.sizeVertices());
	}
	
	/**
	 * computes and returns the adjacency matrix of a graph
	 */
	public Matrix AdjacencyMatrix() {
		double[][] m = new double[this.g.sizeVertices()][this.g.sizeVertices()];
		for (int i = 0; i < this.g.sizeVertices(); i++) {
			for (int j = 0; j < this.g.sizeVertices(); j++)
				if (this.g.adjacent(i,j) == true)
					 m[i][j] = 1.;
				else m[i][j] = 0.;
	   	}
	   	return new Matrix(m);
	}

	/**
	 * computes and returns the laplacian matrix of a graph
	 */	
	public Matrix LaplacianMatrix() {
		throw new Error("Spectral Drawing 2D: a'completer");
	}
	
	public double[] Eigenvalues(Matrix m){
		throw new Error("Spectral Drawing 2D: a'completer");
	}

	public double[][] Eigenvectors(Matrix m){
		throw new Error("Spectral Drawing 2D: a'completer");
	}
	
	public Point_2 computeCoordinates_2(int i,double[] eigenvalues, double[][] eigenvectors) {
		throw new Error("Spectral Drawing 2D: a'completer");
	}
	
	
	private Vector_2 unit_vect(Point_2 from, Point_2 to) {
		Vector_2 v = (Vector_2) to.minus(from);
		return v.divisionByScalar(Math.sqrt(v.squaredLength().doubleValue()));
	}
	
	public void init() {
		int n = g.sizeVertices();
		for (int i = 0; i < n; i++) points.add((X) (new Point_2(Math.random(),Math.random())));
	}
	
	public void iterate() {
		int n = g.sizeVertices();
		Vector_2[] F = new Vector_2[n];
		for (int i = 0; i < n; i++) F[i] = new Vector_2(0,0);
		
	}
	
	public void computeDrawing() {
		
		init();
		
		
	}

	/**
	 * Test SpectralDrawing methods
	 */
	public static void main(String[] args) {
		System.out.println("Spectral Drawing 2D");		
		Graph g = AdjacencyGraph.constructCube();
		System.out.println("graph initialized");
		
		SpectralDrawing_2<Point_2> d = new SpectralDrawing_2<Point_2>(g);
		d.computeDrawing();
		d.draw2D();
	}

}
