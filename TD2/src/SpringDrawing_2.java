

import java.util.ArrayList;

import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.graph.*;


public class SpringDrawing_2<X extends Point_> extends GraphDrawing<X>{

	static double c1=2., c2=1., c3=1., c4=0.01;
	static int nIterations=1500;
	
	int n;
	
	public SpringDrawing_2(Graph g) {
		this.g = g;
    	this.n = g.sizeVertices();
    	this.points = new ArrayList<X>(n);
	}
	

	public Vector_2 attractiveForce(X to, X from) {
		return (new Vector_2((Point_2) to, (Point_2) from)).normalized().multiplyByScalar(c1*(Math.log(to.distanceFrom(from).doubleValue()/c2)));
	}

	public Vector_2 repulsiveForce(X to, X from) {
		return (new Vector_2((Point_2) from, (Point_2) to)).normalized().multiplyByScalar(c3/Math.sqrt(to.distanceFrom(from).doubleValue()));
	}
	
	
	public void setRandomPoints() {		
		for (int i = 0; i < n; i++) points.add((X) (new Point_2(Math.random(),Math.random())));
	}

	/**
	 * compute the drawing of a planar graph iteratively
	 * using the Force-Directed paradigm.
	 * Positions of vertices are updated according to their
	 * mutual attractive and repulsive forces.
	 */	
	public void computeDrawing() {
		setRandomPoints();
		for (int it = 0; it < nIterations; it++) moveOnce();
	}

	public void computeDrawing_Animated() {
		setRandomPoints();
		Fenetre f = new Fenetre();
		for (int it = 0; it < nIterations; it++) {
			f.clearWindow(); 
			int[][] edges = this.g.getEdges();
			for (int i = 0; i < edges.length; i++)
				f.addSegment(this.getPoint2(edges[i][0]), this.getPoint2(edges[i][1]));
			f.repaint(10);
			try { Thread.currentThread().sleep(10); } catch (InterruptedException e) {} 
			moveOnce();
		}
	}
	
	public Vector_[] computeForce(X to) {
		Vector_2[] F = new Vector_2[n];
		for (int i = 0; i < n; i++) {
			F[i] = new Vector_2();
			for (int j = 0; j < n; j++) {
				if (i == j) continue;
				F[i].add(repulsiveForce(points.get(i), points.get(j)));
			}
		}
		return F;
	}
	
	public void moveOnce() {
		// Compute forces
		Vector_2[] F = new Vector_2[n];
		for (int i = 0; i < n; i++) F[i] = new Vector_2();
		for (int i = 1; i < n; i++) {
			X p_i = points.get(i);
			for (int j = 0; j < i; j++) {
				X p_j = points.get(j);
				Vector_2 fromItoJ = repulsiveForce(p_j, p_i);
				if (g.adjacent(i, j)) fromItoJ.add(attractiveForce(p_j, p_i));
				F[j].add(fromItoJ);
				F[i].take(fromItoJ);
			}
		}
		// Act
		for (int i = 0; i < n; i++) points.get(i).translateOf(F[i].multiplyByScalar(c4));
	}

	/**
	 * Test SpringDrawing methods
	 */
	public static void main(String[] args) {
		System.out.println("Force directed methods, spring model in 2D");
		Graph g=AdjacencyGraph.constructDodecahedron();
		System.out.println("graph initialized");
		
		SpringDrawing_2<Point_2> d= new SpringDrawing_2<Point_2>(g);
//		d.computeDrawing();
		d.computeDrawing_Animated();
		System.out.println("planar representation computed");
		d.draw2D();
		
	}
}
