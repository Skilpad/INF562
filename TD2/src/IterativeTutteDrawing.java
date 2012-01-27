

import java.util.ArrayList;

import Jama.Matrix;
import Jcg.geometry.*;
import Jcg.graph.*;

/**
 * Provides methods for drawing graphs in 2D using iterative Tutte barycentric method
 *
 * @author Luca Castelli Aleardi
 */
public class IterativeTutteDrawing<X extends Point_<?>> extends GraphDrawing<X>{

	Matrix laplacian;
	public static int nIterations = 1500;
	Point_<?>[] exteriorPoints;

	public IterativeTutteDrawing() {}

	public IterativeTutteDrawing(Graph g, Point_2[] exteriorPoints) {
		this.g = g;
    	this.points = new ArrayList<X>(g.sizeVertices());
    	this.exteriorPoints = exteriorPoints;
	}

	/**
	 * compute the Tutte drawing of a planar graph iteratively
	 * using the Force-Directed paradigm
	 * The first k vertices are assumed to be fixed on the outer face
	 */	
	public void computeDrawing() {
		
		int i; int n = g.sizeVertices();
		for (i = 0; i < exteriorPoints.length; i++) points.add((X) exteriorPoints[i]);
		for (; i < n; i++) {
			X p = (X) (new Point_2());
			p.barycenter(exteriorPoints);
			points.add(p);
		}
		
		ArrayList<Point_[]> a = new ArrayList<Point_[]>(n-3);		
		for (i = 0; i < n-3; i++) {
			int[] a_int = g.neighbors(i+3);
			Point_[] a_i = new Point_[a_int.length];
			for (int j = 0; j < a_int.length; j++) a_i[j] = points.get(a_int[j]);
			a.add(a_i);
		}
		
		ArrayList<Point_[]> new_points = new ArrayList<Point_[]>(n-3);
		for (i = 0; i < n-3; i++) { Point_[] p_ = new Point_[1]; p_[0] = new Point_2(); new_points.add(p_);}
		
		for (int n_it = 0; n_it < nIterations; n_it++) {
			for (i = 0; i < n-3; i++) new_points.get(i)[0].barycenter(a.get(i));
			for (i = 0; i < n-3; i++) points.get(i+3).barycenter(new_points.get(i));			
		}

	}

	/**
	 * Test TutteDrawing methods
	 */
	public static void main(String[] args) {
		System.out.println("Tutte Drawing iteratively");

		Graph g = AdjacencyGraph.constructDodecahedron();
//		Graph g = graphExamples.example12();

		System.out.println("graph initialized");
		
		Point_2[] exteriorPoints = new Point_2[3];
		exteriorPoints[0] = new Point_2(0.,0.);
		exteriorPoints[1] = new Point_2(1.,0.);
		exteriorPoints[2] = new Point_2(0.,1.);
		//exteriorPoints[3] = new Point_2(-10.,10.);
		IterativeTutteDrawing<Point_2> d = new IterativeTutteDrawing<Point_2>(g, exteriorPoints);
		d.computeDrawing();
		System.out.println("planar representation computed");
		d.draw2D();
		
	}

}
