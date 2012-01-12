

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
		double[][] X0 = new double[3][1];
		double[][] Y0 = new double[3][1];
		for (int j = 0; j < 3; j++) { 
			Point_ p = points.get(j);
			X0[j][0] = p.getCartesian(0).doubleValue();
			Y0[j][0] = p.getCartesian(1).doubleValue();
		}
		// First we calculate M and B
		double[][] M = new double[n-3][n-3];
		double[][] B = new double[n-3][3];
		for (int i = 3; i < n; i++) {
			for (int j = 0; j < 3; j++) {
				if (g.adjacent(i,j)) { M[i-3][i-3]++; B[i-3][j] = 1; }
			}
			for (int j = 3; j < n; j++) {
				if (j == i) continue;
				if (g.adjacent(i,j)) { M[i-3][i-3]++; M[i-3][j-3] = -1; }
			}			
		}
		// Then we solve the system
		Matrix M_  = new Matrix(M);
		Matrix B_  = new Matrix(B);
		Matrix X0_ = new Matrix(X0);
		Matrix Y0_ = new Matrix(Y0);
		
		M_.print(5, 5);
		B_.print(5, 5);
		X0_.print(5, 5);
		Y0_.print(5, 5);
		
		Matrix X_  = M_.inverse().times(B_).times(X0_);
		Matrix Y_  = M_.inverse().times(B_).times(Y0_);

		X_.print(5, 5);
		Y_.print(5, 5);
		
		// We finally register the obtained data
		for (int i = 3; i < n; i++) {
			points.set(i, (X) (new Point_2(X_.get(i-3,0),Y_.get(i-3,0))));
		}
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
