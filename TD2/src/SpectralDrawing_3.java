

import java.util.ArrayList;
import java.util.List;

import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.graph.*;

/**
 * Provides methods for drawing graphs in 3D using spectral based methods
 *
 * @author Luca Castelli Aleardi
 */
public class SpectralDrawing_3<X extends Point_> extends SpectralDrawing_2<X> {

	public SpectralDrawing_3() {}

	public SpectralDrawing_3(int n) {
    	this.points=new ArrayList<X>(n);
	}

	public SpectralDrawing_3(Graph g) {
		this.g=g;
    	this.points=new ArrayList<X>(g.sizeVertices());
	}

	public Point_3 computeCoordinates_3(int i,double[] eigenvalues, double[][] eigenvectors) {
		throw new Error("Spectral drawing 3D: a' completer");
	}
	
	public void computeDrawing() {
		throw new Error("Spectral drawing 3D: a' completer");
	}
	
	public static void main(String[] args) {
		System.out.println("Spectral Drawing 3D");
		Graph g=AdjacencyGraph.constructDodecahedron();
		System.out.println("graph initialized");
		
		SpectralDrawing_3<Point_3> d= new SpectralDrawing_3<Point_3>(g);
		d.computeDrawing();
		d.draw3D();
		
	}
}
