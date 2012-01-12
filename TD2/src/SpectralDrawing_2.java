

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
	private int n;
	
	private int[] borderPoints, insidePoints;
	private int   k;
	

	public SpectralDrawing_2() {}

	public SpectralDrawing_2(int n) {
		this.n = n;
		this.points = new ArrayList<X>(n);
	}

	public SpectralDrawing_2(Graph g) {
		this.g = g;
		this.n = g.sizeVertices();
		this.points = new ArrayList<X>(n);
	}

	
	private void choseBorder() {
		int[] b = new int[n];
		b[0] = 0;
		k = 1;
		int last = 0, last_ = -1;
		while (0 == last || 0 == last_ || !g.adjacent(last, 0)) {
			int i = 1;
			while (i == last || i == last_ || !g.adjacent(last, i)) i++;
			last_ = last; last = i;
			b[k] = i; k++;
		}
		borderPoints = new int[k];
		for (int i = 0; i < k; i++) borderPoints[i] = b[i];
		insidePoints = new int[n-k];
		int j = 0;
		for (int i = 0; i < n; i++) {
			boolean inside = true;
			for (int d : borderPoints) if (i == d) { inside = false; break; }
			if (inside) { insidePoints[j] = i; j++; }
		}
	}


	public void init() {
		choseBorder();
		for (int i = 0; i < n; i++) points.add((X) (new Point_2(Math.random(),Math.random())));
		for (int i : borderPoints) points.set(i, (X) (new Point_2(Math.cos(Math.PI*i*2/k),Math.sin(Math.PI*i*2/k)))); 
	}

	public void computeDrawing() {
		init();
		// We will solve the equation: M.[x3,x4,...]t = B.[x0,x1,x2]t
		double[][] X0 = new double[k][1];
		double[][] Y0 = new double[k][1];
		for (int j = 0; j < k; j++) { 
			Point_ p = points.get(borderPoints[j]);
			X0[j][0] = p.getCartesian(0).doubleValue();
			Y0[j][0] = p.getCartesian(1).doubleValue();
		}
		// First we calculate M and B
		double[][] M = new double[n-k][n-k];
		double[][] B = new double[n-k][k];
		
		for (int i = 0; i < n-k; i++) {
			for (int j = 0; j < k; j++) {
				if (g.adjacent(insidePoints[i],borderPoints[j])) { M[i][i]++; B[i][j] = 1; }
			}
			for (int j = 0; j < n-k; j++) {
				if (j == i) continue;
				if (g.adjacent(insidePoints[i],insidePoints[j])) { M[i][i]++; M[i][j] = -1; }
			}			
		}
		// Then we solve the system
		Matrix M_  = new Matrix(M);
		Matrix B_  = new Matrix(B);
		Matrix X0_ = new Matrix(X0);
		Matrix Y0_ = new Matrix(Y0);
		
		Matrix X_  = M_.inverse().times(B_).times(X0_);
		Matrix Y_  = M_.inverse().times(B_).times(Y0_);
		
		// We finally register the obtained data
		for (int i = 0; i < n-k; i++) {
			points.set(insidePoints[i], (X) (new Point_2(X_.get(i,0),Y_.get(i,0))));
		}
	}

	/**
	 * Test SpectralDrawing methods
	 */
	public static void main(String[] args) {
		System.out.println("Spectral Drawing 2D");		
		Graph g = AdjacencyGraph.constructCube();
//		Graph g = graphExamples.example12();
		System.out.println("graph initialized");

		SpectralDrawing_2<Point_2> d = new SpectralDrawing_2<Point_2>(g);
		d.computeDrawing();
		d.draw2D();
	}

}
