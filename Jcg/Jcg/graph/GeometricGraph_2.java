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
		this.vertices=new ArrayList<GraphNode<Point_2>>();
		
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
     * compute and return the bounding box for the points set.
     * The result is stored in a table.
     */		
    public double[] boundingBox() {
      	double[] box=new double[4];
      	double xmax,xmin,ymax,ymin;
      	
      	Point_2 p=this.vertices.get(0).getPoint();
      	xmax=p.getX().doubleValue(); xmin=p.getX().doubleValue(); 
      	ymax=p.getY().doubleValue(); ymin=p.getY().doubleValue();
      	
      	for(GraphNode<Point_2> v: this.vertices) {
      		Point_2 q=v.getPoint();
      		if(q.getX().doubleValue()>xmax) xmax=q.getX().doubleValue();
      		if(q.getX().doubleValue()<xmin) xmin=q.getX().doubleValue();
      		if(q.getY().doubleValue()>ymax) ymax=q.getY().doubleValue();
      		if(q.getY().doubleValue()<ymin) ymin=q.getY().doubleValue();
      	}
      	
      	box[0]=xmin; box[1]=ymin;
      	box[2]=xmax; box[3]=ymax;
      	return box;
      }
    /**
     * Return the list of edges of the graph (their corresponding segments)
     */		   
    public List<Point_2[]> computeEdges() {
    	List<Point_2[]> result=new ArrayList<Point_2[]>();
    	Point_2[] segment;
    	for(GraphNode<Point_2> u: this.vertices) {
    		for(GraphNode<Point_2> v: u.neighbors) {
    			segment=this.getSegment(u, v);
    			result.add(segment);
    		}
    	}
    	return result;
    }

    /**
     * Return the segment having as extremities two given vertices of the graph
     */		   
    public Point_2[] getSegment(GraphNode<Point_2> u, GraphNode<Point_2> v) {
    	Point_2[] result=new Point_2[2];
    	result[0]=u.getPoint();
    	result[1]=v.getPoint();
    	return result;
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
