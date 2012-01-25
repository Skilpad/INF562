package Jcg.graph;

import Jcg.geometry.*;

import java.util.*;

/**
 * Implementation of a geometric graph, whose vertices correspond to 2D points
 *
 * @author Luca Castelli Aleardi
 */
public class GeometricGraph_2 extends GeometricGraph<Point_2>{

	public GeometricGraph_2() {
		super();
	}

	/**
	 * Create a copy of a given graph: points are shared, but not vertices.
	 * It requires the input graph has already a vertex labeling
	 */	
	public GeometricGraph_2(GeometricGraph<Point_2> g) {
		this.vertices = new ArrayList<GraphNode<Point_2>>();
		
		for(GraphNode<Point_2> v: g.vertices) 
			this.addNode(v.getPoint());

		int i=0;
		for(GraphNode<Point_2> v: g.vertices) {
			List<GraphNode<Point_2>> vNeighbors=v.neighbors;
			GraphNode<Point_2> vertexToUpdate=this.vertices.get(i);
			for(GraphNode<Point_2> u: vNeighbors) {
				int index=u.getTag();
				vertexToUpdate.addNeighbors(this.vertices.get(index));
			}
			i++;
		}
	}

	/**
	 * Construct the graph corresponding to a 2D triangulation.
	 * It requires the list of points corresponding
	 * to the vertices of the graph.
	 */		   
	public static GeometricGraph_2 getGraphFrom2DTriangulation(List<Point_2> vertices, List<Point_2[]> faces) {
		System.out.print("constructing geometric graph from 2D triangulation: ");
		System.out.print(""+vertices.size()+" vertices ...");
		// store vertex indices in map 
		HashMap <Point_2,Integer> vert = new HashMap<Point_2,Integer> ();

		GeometricGraph_2 graph=new GeometricGraph_2();

		int i=0;
		for (Point_2 p : vertices) {
			graph.addNode(p);
			vert.put(p, new Integer(i++));
		}    

		for (Point_2[] f : faces) {
			Integer[] ind = new Integer [f.length];
			int j=0;
			for (; j<f.length; j++) {
				ind[j] =  vert.get(f[j]);
				if (ind[j] == null)
					break;
			}
			if (j < f.length) {
				System.out.println ("Index pb in faces set in writeToFile().");
				System.exit (-1);
			}

			GraphNode<Point_2> v1,v2 ;
			for (j=0; j<f.length-1; j++) {
				v1=graph.getNode(ind[j]);
				v2=graph.getNode(ind[j+1]);
				v1.addNeighbors(v2);
				v2.addNeighbors(v1);
			}
			v1=graph.getNode(ind[f.length-1]);
			v2=graph.getNode(ind[0]);
			v1.addNeighbors(v2);
			v2.addNeighbors(v1);
		}
		System.out.println(" done");
		System.out.println("vertices: "+graph.sizeVertices());
		return graph;
	}

}
