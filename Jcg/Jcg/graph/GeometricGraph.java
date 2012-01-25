package Jcg.graph;

import Jcg.geometry.*;
import Jcg.polyhedron.*;

import java.util.*;

/**
 * Implementation of a geometric graph (generic and undirected)
 *
 * @author Luca Castelli Aleardi
 */
public class GeometricGraph<X extends Point_> {     // TODO: Extends KERNEL
	
	public List<GraphNode<X>> vertices=null;
	
	public GeometricGraph() {
		this.vertices=new ArrayList<GraphNode<X>>();
	}

	/**
     * return the number of vertices
     */	
	public int sizeVertices() {
		return this.vertices.size();
	}

	/**
     * update vertex labels according to the given permutation
     */		
	public void orderVertices(int[] permutation) {
		if(this.sizeVertices()!=permutation.length)
			throw new Error("error: wrong number of vertices");
		int i=0;
		for(GraphNode<X> v: this.vertices) {
			v.setTag(permutation[i]);
			i++;
		}
	}

	/**
     * return the list of vertex labels
     */		
	public int[] vertexPermutation() {
		if(this.sizeVertices()==0)
			throw new Error("error: wrong number of vertices");
		int[] result=new int[this.sizeVertices()];
		int i=0;
		for(GraphNode<X> v: this.vertices) {
			result[i]=v.getTag();
			i++;
		}
		return result;
	}

	/**
     * label vertices according to natural increasing order
     */		
	public void computeNaturalVertexOrder() {
		int[] order=new int[this.sizeVertices()];
		for(int i=0;i<this.sizeVertices();i++)
			order[i]=i;
		this.orderVertices(order);
	}

    /**
     * add a Point (2D or 3D) to the graph
     */
	public void addNode(X p) {
		GraphNode<X> v = new GraphNode<X>(p);
		this.vertices.add(v);
	}

    /**
     * add a node (already existing) to the graph
     */
	public void addNode(GraphNode<X> v) {
		this.vertices.add(v);
	}

    /**
     * return the i-th node of the graph
     */
	public GraphNode<X> getNode(int i) {
		if(i<0 || i>=vertices.size())
			throw new Error("node index error");
		return this.vertices.get(i);
	}

    /**
     * return the list of the nodes of the graph
     */
	public List<GraphNode<X>> nodeList() {
		return this.vertices;
	}

	/**
     * Return graph informations
     */		
	public String toString() {
		String result=this.sizeVertices()+" vertices\n";
		result=result+"adjacency lists\n";
		for(GraphNode<X> v: this.vertices) {
			result=result+v.degree()+" -";
			for(GraphNode<X> u: v.neighborsList())
				result=result+" "+u.getTag();
			result=result+"\n";
		}
		for(GraphNode<X> v: this.vertices) 
			result=result+v+"\n";
		return result;
	}
    
	
	/**
	 * Return 2 points defining the bounding box for the points set:
	 *     Let RES be the returned result.
	 *     For all i in [0..dim-1] and p in the set:
	 *         RES[0].getCartesian(i) <= p.getCartesian(i) <= RES[1].getCartesian(i)
	 */		
	public X[] boundingBox() {
		X min = vertices.get(0).getPoint();
		X max = vertices.get(0).getPoint();
		for (GraphNode<X> v : vertices) {
			X p = v.getPoint();
			int d = p.dimension();
			for (int i = 0; i < d; i++) {
				double x = p.getCartesian(i).doubleValue();
				if (x < min.getCartesian(i).doubleValue()) min.setCartesian(i, x);
				if (x > max.getCartesian(i).doubleValue()) max.setCartesian(i, x);
			}
		}
		return (X[]) new Point_[] {min,max};
	}
	
	/** Returns the barycenter of the graph **/
	public X barycenter() {   // Find a clean solution to create arrays of Point_<X>!!
		X bary = (X) vertices.get(0).getPoint().copy();
		bary.getData().setOrigin();
		int n = 0;
		for (GraphNode<X> v : vertices) {
			bary.getData().add(v.getPoint().getData());
			n++;
		}
		bary.getData().divideBy(n);
		return bary;
	}
	
	
	/**
	 * Returns the list of edges of the graph (their corresponding segments):
	 * Each element E of the list represents an edge, with E[0] & E[1] as extremities.
	 */
	public List<X[]> computeEdges() {
		List<X[]> result = new ArrayList<X[]>();
		X[] segment;
		for(GraphNode<X> u: this.vertices) {
			for(GraphNode<X> v: u.neighbors) {
				segment = this.getSegment(u, v);
				result.add(segment);
			}
		}
		return result;
	}

	/**
	 * Return the segment having as extremities two given vertices of the graph
	 */
	public X[] getSegment(GraphNode<X> u, GraphNode<X> v) {
		return (X[]) new Point_[] {u.getPoint(), v.getPoint()};
	}

}

