package Jcg.polyhedron;

import java.util.*;
import java.awt.Color;

import Jcg.geometry.*;

/**
 * @author Luca Castelli Aleardi
 * This class provides methods for building an half-egde DS from a shared vertex representation
 */
public class LoadMesh<X extends Point_> {
		
	public Polyhedron_3<X> createPolyhedron(X[] points, int[] faceDegrees, int[][] faces, int sizeHalfedges)
    {   
    	int i,j;
    	Polyhedron_3<X> result=new Polyhedron_3<X>(points.length,sizeHalfedges,faceDegrees.length);
        
        //System.out.println("Creating a new polyhedron");

        for(i=0;i<points.length;i++){
            result.vertices.add(new Vertex<X>(points[i]));
        }
        
        // auxiliary array containing the index of halfedges
        int[][] incidence=new int[result.sizeOfVertices()][result.sizeOfVertices()];
        for(i=0;i<result.sizeOfVertices();i++) // initialization
            for(j=0;j<result.sizeOfVertices();j++) incidence[i][j]=-1;
        
        int cont=0; // counts the number of inserted halfedges
        
        Face<X> _face;
        for(i=0;i<faceDegrees.length;i++) {         
            _face=new Face<X>(); // creating a face
            result.facets.add(_face);
            
            Halfedge<X> _edge, _prev=null, _begin=null;
            for(j=0;j<faceDegrees[i]-1;j++) {
                _edge=new Halfedge<X>();
                _edge.setVertex(result.vertices.get(faces[i][j+1]));
                _edge.setFace(_face);
                if(incidence[faces[i][j+1]][faces[i][j]]!=-1) {
                    _edge.setOpposite(result.halfedges.get(incidence[faces[i][j+1]][faces[i][j]]));
                    result.halfedges.get(incidence[faces[i][j+1]][faces[i][j]]).setOpposite(_edge);
                }
                if(j>0) { _edge.setPrev(_prev); _prev.setNext(_edge); }
                else _begin=_edge;
                incidence[faces[i][j]][faces[i][j+1]]=cont;
                cont++;
                _prev=_edge;
                result.halfedges.add(_edge);
            }
            // creating the last halfedge relative to the current face
            _edge=new Halfedge<X>();
            _edge.setVertex(result.vertices.get(faces[i][0])); // the target vertex is the first vertex in ccw order incident to the current face
            _edge.setFace(_face);
            if(incidence[faces[i][0]][faces[i][j]]!=-1) {
                _edge.setOpposite(result.halfedges.get(incidence[faces[i][0]][faces[i][j]]));
                result.halfedges.get(incidence[faces[i][0]][faces[i][j]]).setOpposite(_edge);
            }
            _edge.setPrev(_prev);
            _prev.setNext(_edge);
            _edge.setNext(_begin);
            _begin.setPrev(_edge);
            incidence[faces[i][j]][faces[i][0]]=cont;
            cont++;
            result.halfedges.add(_edge);
            
            _face.setEdge(_edge); // the current face points to the last incidence halfedge
        }
        
        for(i=0;i<result.halfedges.size();i++) {
        	Halfedge<X> pedge=(Halfedge<X>)result.halfedges.get(i);
        	Vertex<X> pvertex=pedge.vertex;
        	pvertex.halfedge=pedge;
        }
        
        return result;        
    }
	
	/**
	 * Efficient construction of a half-egde DS for a triangle mesh (from a share vertex representation)
	 * Warning: it works only for triangle meshes
	 */
	public Polyhedron_3<X> createTriangleMesh(X[] points, int[] faceDegrees, int[][] faces, int sizeHalfedges)
    {   
		System.out.print("Building a (pointer based) halfedge representation of a triangle mesh...");
    	int i,j;
    	// allows to efficient retrieving an inserted halfedge
    	HashMap<HalfedgePair, Halfedge<X>> insertedHalfedges=new HashMap(sizeHalfedges);
        int nInsertedHalfedges=0; // counts the number of inserted halfedges
    	
    	Polyhedron_3<X> result=new Polyhedron_3<X>(points.length,sizeHalfedges,faceDegrees.length);
        
        for(i=0;i<points.length;i++){
        	Vertex<X> v=new Vertex<X>(points[i]);
        	v.index=i;
            result.vertices.add(v); // adding a vertex
        }
        
        Face<X> _face;
        for(i=0;i<faceDegrees.length;i++) {         
            _face=new Face<X>(); // creating a face
            result.facets.add(_face); // adding the created face
            
            // creating and storing the three halfedges in a face
            Halfedge<X> e0=new Halfedge<X>();
            Halfedge<X> e1=new Halfedge<X>();
            Halfedge<X> e2=new Halfedge<X>();
            result.halfedges.add(e0);
            result.halfedges.add(e1);
            result.halfedges.add(e2);
            
            // setting incident (target vertex)
            e0.setVertex(result.vertices.get(faces[i][1]));
            e1.setVertex(result.vertices.get(faces[i][2]));
            e2.setVertex(result.vertices.get(faces[i][0]));
            
            // halfedges must be incident to the current face
            e0.setFace(_face);
            e1.setFace(_face);
            e2.setFace(_face);
            
            // incidence relations between halfedges in the same face
            e0.setNext(e1); e0.setPrev(e2);
            e1.setNext(e2); e1.setPrev(e0);
            e2.setNext(e0); e2.setPrev(e1);
            
            // setting first opposite halfedge
            HalfedgePair pair0=new HalfedgePair(faces[i][0], faces[i][1]);
            if(insertedHalfedges.containsKey(pair0)==false) {
            	insertedHalfedges.put(pair0, e0);
            	//System.out.println("inserted edge ");
            	nInsertedHalfedges++;
            }
            else {
            	//System.out.println("edge already inserted");
            	Halfedge<X> eOpposite=insertedHalfedges.get(pair0);
            	e0.setOpposite(eOpposite);
            	eOpposite.setOpposite(e0);
            }
            // setting second opposite halfedge
            HalfedgePair pair1=new HalfedgePair(faces[i][1], faces[i][2]);
            if(insertedHalfedges.containsKey(pair1)==false) {
            	insertedHalfedges.put(pair1, e1);
            	nInsertedHalfedges++;
            }
            else {
            	//System.out.println("edge already inserted");
            	Halfedge<X> eOpposite=insertedHalfedges.get(pair1);
            	e1.setOpposite(eOpposite);
            	eOpposite.setOpposite(e1);
            }
            // setting third opposite halfedge
            HalfedgePair pair2=new HalfedgePair(faces[i][2], faces[i][0]);
            if(insertedHalfedges.containsKey(pair2)==false) {
            	insertedHalfedges.put(pair2, e2);
            	nInsertedHalfedges++;
            }
            else {
            	//System.out.println("edge already inserted");
            	Halfedge<X> eOpposite=insertedHalfedges.get(pair2);
            	e2.setOpposite(eOpposite);
            	eOpposite.setOpposite(e2);
            }
            
            // setting an edge incident to the current face
            _face.setEdge(e0);
        }
        
        // setting the vertex incident to a halfedge
		for(i=0;i<result.halfedges.size();i++) {
        	Halfedge<X> pedge=(Halfedge<X>)result.halfedges.get(i);
        	Vertex<X> pvertex=pedge.vertex;
        	pvertex.halfedge=pedge;
        }
		System.out.println("done");
		
        return result;        
    }

    
    public static Point_2[] Point3DToPoint2D(Point_3[] points) {
    	Point_2[] result=new Point_2[points.length];
    	for(int i=0;i<points.length;i++) {
    		result[i]=new Point_2(points[i].getX().doubleValue(), points[i].getY().doubleValue());
    	}
    	return result;
    }
            
}
