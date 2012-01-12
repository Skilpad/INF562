
import java.util.ArrayList;

import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.viewer.*;
import Jcg.graph.*;


/**
 * Provides methods for drawing graphs using spectral based methods
 *
 * @author Luca Castelli Aleardi
 */
public abstract class GraphDrawing<X extends Point_>{

	Graph g;
	ArrayList<X> points;

	/**
	 * returns the position of a vertex in the space (2D or 3D)
	 */
	public X getPoint(int i){
		if (i < 0 || i > this.points.size())
			throw new Error("vertex index error");
		return this.points.get(i);
	}

	public Point_2 getPoint2(int i){
		if(i<0 || i>this.points.size())
			throw new Error("vertex index error");
		return new Point_2(this.points.get(i));
	}

	/**
	 * return the dimension of the representation: 2 or 3
	 */
	public int dimension(){
		X p = this.points.get(0);
		if (p == null) throw new Error("error: no first point computed");
		return p.dimension();
	}
	
	/**
	 * return vertex coordinates
	 */
	public String toString() {
		int n = g.sizeVertices();
		int[][] edges = g.getEdges();
		int e = edges.length;
		
		String result = n+" "+e+"\n";
		for (X p : this.points) { // writing geometric coordinates
			double x = p.getCartesian(0).doubleValue();
			double y = p.getCartesian(1).doubleValue();
			x = ((int)(x*100.))/100.;
			y = ((int)(y*100.))/100.;
			result = result+x+" "+y+"\n";
		}
		for (int i = 0; i < edges.length; i++) { // writing edge/vertex incidence relations
			result = result+edges[i][0]+" "+edges[i][1]+"\n";
		}
		
		return result;
	}

	/**
	 * (abstract method) compute the geometric representation of the graph
	 */
	public abstract void computeDrawing();

	/**
	 * draw the graph in a 2D frame
	 */
	public void draw2D() {
		if (this.dimension() != 2) throw new Error("error: not 2D space");
		int[][] edges = this.g.getEdges();
		Fenetre f = new Fenetre();
		for(int i = 0; i < edges.length; i++) {
			f.addSegment(this.getPoint2(edges[i][0]), this.getPoint2(edges[i][1]));
		}
	}

	/**
	 * draw the graph in 3D (using class MeshViewer)
	 */
	public void draw3D() {
		if (this.dimension() != 3) throw new Error("error: not 3D space");
		int[][] edges = this.g.getEdges();
		Point_3[] vertices = new Point_3[this.points.size()];
		
		int i = 0;
		for (X p : this.points) {
			vertices[i] = (Point_3) p;
			i++;
		}
		
		new MeshViewer(vertices, edges);
	}

}
