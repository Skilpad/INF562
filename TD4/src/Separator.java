import java.util.ArrayList;
import java.util.List;

import Jcg.geometry.*;
import Jcg.graph.GeometricGraph;
import Jcg.graph.GeometricGraph_2;
import Jcg.graph.GraphNode;

/**
 * a class for dealing with generic separators (in 2D or 3D)
 *
 * @author Luca Castelli Aleardi
 */
public abstract class Separator<X extends Kernel> {

	/**
     * compute an edge separator of a given graph.
     */
    public GeometricGraph<Point_<X>>[] Cut(GeometricGraph<Point_<X>> g) {
		Point_<X> bary = g.barycenter();
		Vector_<X> n = bary.minus(new Point_<X>());
		n.randomize();
		return geometricCut(g, new Hyperplan<X>(bary,n));
    }
    
    
	public GeometricGraph<Point_<X>>[] geometricCut(GeometricGraph<Point_<X>> g, Hyperplan<X> randomLine) {
		GeometricGraph<Point_<X>>[] res = new GeometricGraph[] {new GeometricGraph<Point_<X>>(), new GeometricGraph<Point_<X>>()};   // TODO: works?
		for (GraphNode<Point_<X>> v : g.vertices)
			if (randomLine.hasOnPositiveSide(v.getPoint())) {
				res[0].addNode(v);
				List<GraphNode<Point_<X>>> neighbors_ = v.neighbors;
				v.neighbors = new ArrayList<GraphNode<Point_<X>>>();
				for (GraphNode<Point_<X>> n : neighbors_)
					if (randomLine.hasOnPositiveSide(n.getPoint())) v.neighbors.add(n); 
			} else {
				res[1].addNode(v);
				List<GraphNode<Point_<X>>> neighbors_ = v.neighbors;
				v.neighbors = new ArrayList<GraphNode<Point_<X>>>();
				for (GraphNode<Point_<X>> n : neighbors_)
					if (!randomLine.hasOnPositiveSide(n.getPoint())) v.neighbors.add(n); 
			}
		return res;
	}
	
	
	/**
     * Compute a graph partitioning and return the list of nodes reordered
     */		    
	public List<GraphNode<Point_<X>>> computeGraphSequence(GeometricGraph<Point_<X>> g) {
		List<GraphNode<Point_<X>>> res = new ArrayList<GraphNode<Point_<X>>>();
		computeGraphSequence(g,res);
		return res;
    }
	
	private void computeGraphSequence(GeometricGraph<Point_<X>> g, List<GraphNode<Point_<X>>> L) {
		int s = g.vertices.size();
		if (s == 0) return;
		if (s == 1) { L.add(g.vertices.get(0)); return; }
		GeometricGraph<Point_<X>>[] gg = Cut(g);
		computeGraphSequence(gg[0], L);
		computeGraphSequence(gg[1], L);
    }


	/**
     * Relabel the vertices according to the order in the list
     */		    
	public void reorderVertices(List<GraphNode> l) {
		throw new Error("a' completer");
		
	}

	/**
     * The main function: compute a graph partitioning based on separators, and relabel vertices
     */		    
	public void reorderVertices(GeometricGraph g) {
		throw new Error("a' completer");

	}
	
}
