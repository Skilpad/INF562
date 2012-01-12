package Jcg.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Jcg.geometry.*;
import Jcg.polyhedron.*;
import Jcg.triangulations3D.*;

/**
 * Implementation of a geometric graph, whose vertices correspond to 3D points
 *
 * @author Luca Castelli Aleardi
 */
public class GeometricGraph_3 extends GeometricGraph<Point_3>{

	public GeometricGraph_3() {
		super();
	}

	/**
     * compute and return the bounding box for the points set.
     * The result is stored in a table.
     */		
    public double[] boundingBox() {
      	double[] box=new double[6];
      	double xmax,xmin,ymax,ymin, zmin, zmax;
      	
      	Point_3 p=this.vertices.get(0).getPoint();
      	xmax=p.getX().doubleValue(); xmin=p.getX().doubleValue(); 
      	ymax=p.getY().doubleValue(); ymin=p.getY().doubleValue();
      	zmax=p.getZ().doubleValue(); zmin=p.getZ().doubleValue();
      	
      	for(GraphNode<Point_3> v: this.vertices) {
      		Point_3 q=v.getPoint();
      		if(q.getX().doubleValue()>xmax) xmax=q.getX().doubleValue();
      		if(q.getX().doubleValue()<xmin) xmin=q.getX().doubleValue();
      		if(q.getY().doubleValue()>ymax) ymax=q.getY().doubleValue();
      		if(q.getY().doubleValue()<ymin) ymin=q.getY().doubleValue();
      		if(q.getZ().doubleValue()>zmax) zmax=q.getZ().doubleValue();
      		if(q.getZ().doubleValue()<zmin) zmin=q.getZ().doubleValue();
      	}
      	
      	box[0]=xmin; box[1]=ymin;
      	box[2]=xmax; box[3]=ymax;
      	box[4]=zmax; box[5]=zmax;
      	return box;      
    }

	/**
     * Create a copy of a given graph: points are shared, but not vertices.
     * It requires the input graph has already a vertex labeling
     */	
	public GeometricGraph_3(GeometricGraph<Point_3> g) {
		this.vertices=new ArrayList<GraphNode<Point_3>>();
		
		for(GraphNode<Point_3> v: g.vertices) 
			this.addNode(v.getPoint());
		
		int i=0;
		for(GraphNode<Point_3> v: g.vertices) {
			List<GraphNode<Point_3>> vNeighbors=v.neighbors;
			GraphNode<Point_3> vertexToUpdate=this.vertices.get(i);
			for(GraphNode<Point_3> u: vNeighbors) {
				int index=u.getTag();
				vertexToUpdate.addNeighbors(this.vertices.get(index));
			}
			i++;
		}
	}


    /**
     * Return the list of edges of the graph (their corresponding segments)
     */		   
    public List<Point_3[]> computeEdges() {
    	List<Point_3[]> result=new ArrayList<Point_3[]>();
    	Point_3[] segment;
    	for(GraphNode<Point_3> u: this.vertices) {
    		for(GraphNode<Point_3> v: u.neighbors) {
    			segment=this.getSegment(u, v);
    			result.add(segment);
    		}
    	}
    	return result;
    }

    /**
     * Return the segment having as extremities two given vertices of the graph
     */		   
    public Point_3[] getSegment(GraphNode<Point_3> u, GraphNode<Point_3> v) {
    	Point_3[] result=new Point_3[2];
    	result[0]=u.getPoint();
    	result[1]=v.getPoint();
    	return result;
    }

    /**
     * Construct the graph corresponding to a 3D triangulation.
     */		   
    public static GeometricGraph_3 getGraphFrom3DTriangulation(Delaunay_3 tri) {
    	System.out.print("constructing geometric graph from 3D triangulation: ");
    	
    	GeometricGraph_3 graph=new GeometricGraph_3();
    	return graph;
    }
    
    /**
     * Construct the graph corresponding to a 3D mesh (OFF file).
     * It requires all faces are triangles.
     */		   
    public static GeometricGraph_3 getGraphFrom3DMesh(String filename) {
    	System.out.println("constructing geometric graph from an OFF file...");
		MeshRepresentation m=new MeshRepresentation();
    	m.readOffFile(filename);
    	
    	//System.out.println("m.vertices "+m.points.length);
    	//System.out.println("m.faces "+m.faces.length);
    	//System.out.println("m.halfedges "+m.sizeHalfedges);    	
    	
    	GeometricGraph_3 g3D=new GeometricGraph_3();
    	for(int i=0;i<m.sizeVertices;i++) {
    		Point_3 p=m.points[i];
    		g3D.addNode(p);
    	}
    	
    	for(int i=0;i<m.faces.length;i++) {
    		int index1=m.faces[i][0];
    		int index2=m.faces[i][1];
    		int index3=m.faces[i][2];
    		//System.out.println(""+index1+" "+index2+" "+index3);
    		GraphNode<Point_3> v1=g3D.getNode(index1);
    		GraphNode<Point_3> v2=g3D.getNode(index2);
    		GraphNode<Point_3> v3=g3D.getNode(index3);
    		v1.addNeighbors(v2);
    		v2.addNeighbors(v3);
    		v3.addNeighbors(v1);
    		
    	}
    	
    	System.out.println("3D geometric graph construction... done");
    	return g3D;
    }

}
