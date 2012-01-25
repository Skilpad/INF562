import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    public abstract GeometricGraph<X>[] Cut(GeometricGraph g);

	/**
     * Compute a graph partitioning and return the list of nodes reordered
     */		    
    public List<GraphNode> computeGraphSequence(GeometricGraph g) {
    	throw new Error("a' completer");

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
