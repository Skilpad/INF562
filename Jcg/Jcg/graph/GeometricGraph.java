package Jcg.graph;

import Jcg.geometry.*;
import Jcg.polyhedron.*;

import java.util.*;

/**
 * Implementation of a geometric graph (generic and undirected)
 *
 * @author Luca Castelli Aleardi
 */
public class GeometricGraph<X extends Point_> {
	
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
		GraphNode<X> v=new GraphNode<X>(p);
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

}
