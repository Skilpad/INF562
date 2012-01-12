package Jcg.triangulations2D;

import Jcg.geometry.*;

import java.util.*;


/**
 * Data structure for representing 2D triangulations that uses the standard face/vertex encoding, 
 * represented by classes TriangulationDSFace_2 and TriangulationDSVertex_2 respectively. In this 
 * representation we store the collection of faces as well as the collection of vertices of 
 * the triangulation. Each face provides access to its vertices and neighboring faces, while each
 * vertex provides access to one of its incident faces. 
 *
 * @author Luca Castelli Aleardi and Steve Oudot
 *
 */
public class TriangulationDS_2<X extends Point_> {

	public ArrayList<TriangulationDSFace_2<X>> faces;
    public ArrayList<TriangulationDSVertex_2<X>> vertices;
	    
    /**
     * creates an empty triangulation data structure.
     *
     */
    public TriangulationDS_2() {
    	this.faces=new ArrayList<TriangulationDSFace_2<X>>();
    	this.vertices=new ArrayList<TriangulationDSVertex_2<X>>();    
    }
    
    /**
     * creates an empty triangulation data structure with prescribed numbers of faces and vertices.
     */
    public TriangulationDS_2(int nFaces, int nVertices) {
    	this.faces=new ArrayList<TriangulationDSFace_2<X>>(nFaces);
    	this.vertices=new ArrayList<TriangulationDSVertex_2<X>>(nVertices);
   }

    /**
     * Create a new triangulation from the face-vertex incidence relations
     * It takes linear space and linear time.
     * 
     */
    public TriangulationDS_2(X[] points, int[][] neighbors) {
    	    	
    	if(points==null || neighbors==null || points[0]==null) 
    		throw new Error("error: null vertices or null faces");
    	System.out.print("Creating triangulated 3D mesh... ");
    	
    	this.faces=new ArrayList<TriangulationDSFace_2<X>>();
    	this.vertices=new ArrayList<TriangulationDSVertex_2<X>>();    
    	
    	for(int i=0;i<points.length;i++) {
    		if(points[i]==null) throw new Error("null vertex error");
    		TriangulationDSVertex_2<X> v=new TriangulationDSVertex_2<X>(points[i]);
    		this.vertices.add(v);
    	}
    	
    	// first pair (the key) represents the edges of the triangulation
    	// second pair (the associated value) represents the two neighboring faces (sharing an edge)
    	HashMap<Pair<Integer>, Pair<Integer>> edges=new HashMap<Pair<Integer>, Pair<Integer>>();
    	for(int i=0;i<neighbors.length;i++) {
    		int i0=neighbors[i][0], i1=neighbors[i][1], i2=neighbors[i][2];
    		createFace(this.vertices.get(i0), vertices.get(i1), vertices.get(i2), null, null, null);
    		
    		for(int j=0;j<3;j++) {
    			int index1=neighbors[i][(j+1)%3];
    			int index2=neighbors[i][(j+2)%3];
    			Pair<Integer> edge;
    			if(index1<=index2) edge=new Pair<Integer>(index1, index2);
    			else edge=new Pair<Integer>(index2, index1);
    			
    			if(edges.containsKey(edge)==false) {
    				Pair<Integer> face;
    				if(index1<=index2) face=new Pair<Integer>(i, -1);
    				else face=new Pair<Integer>(-1, i);
    				edges.put(edge, face);
    			}
    			else {
    				Pair<Integer> face=edges.get(edge);
    				if(face.getFirst()==-1) face.setFirst(i);
    				else face.setSecond(i);
    			}
    		}
    	}
    	
    	//System.out.println("setting neighboring faces");
    	for(int i=0;i<neighbors.length;i++) {
    		TriangulationDSFace_2<X> currentFace=this.faces.get(i);
    		for(int j=0;j<3;j++) {    		
    			
    			// setting vertex adjacent face
    			TriangulationDSVertex_2<X> v=this.vertices.get(neighbors[i][j]);
    			v.setFace(this.faces.get(i));
    			
    			int index1=neighbors[i][(j+1)%3];
    			int index2=neighbors[i][(j+2)%3];
    			Pair<Integer> edge;
    			if(index1<=index2) edge=new Pair<Integer>(index1, index2);
    			else edge=new Pair<Integer>(index2, index1);
    			
    			if(edges.containsKey(edge)==false) 
    				throw new Error("error: edge not found");
    			else {
    				Pair<Integer> face=edges.get(edge);
    				if(face.getFirst()==-1 && face.getSecond()==-1)
    					throw new Error("error: wrong adjacent faces");
    				if(face.getFirst()==-1 || face.getSecond()==-1); // boundary edge
    				else {
    					int neighborFace;
    					if(index1<=index2) neighborFace=face.getSecond();
    					else neighborFace=face.getFirst();
    					if(neighborFace<0 || neighborFace>=this.faces.size())
    						throw new Error("error neighbor face index");
    					currentFace.setNeighbor(j, this.faces.get(neighborFace));
    				}
    			}
    		}
    	}
    	System.out.println("done");
    }
    
    /**
     * creates a new face and adds it to the faces container of the triangulation data structure.
     */
    public TriangulationDSFace_2<X> createFace(){
    	TriangulationDSFace_2<X> newFace=new TriangulationDSFace_2<X>();
    	this.faces.add(newFace);
    	return newFace;
    }

    /**
     * returns the number of vertices of the triangulation.
     */
    public int sizeOfVertices(){
    	return this.vertices.size();
    }

    /**
     * returns the number of faces of the triangulation.
     * @return
     */
    public int sizeOfFaces(){
    	return this.faces.size();
    }

    /**
     * checks whether the triangulation contains face c.
     * @param c
     * @return
     */
    public boolean hasFace(TriangulationDSFace_2<X> c) {
    	return faces.contains(c);
    }

    /**
     * checks whether the triangulation contains vertex v.
     */
    public boolean hasVertex(TriangulationDSVertex_2<X> v) {
    	return vertices.contains(v);
    }

    /**
     * checks whether the triangulation has a boundary.
     */
    public boolean hasBoundary() {
    	for(TriangulationDSFace_2<X> f: this.faces) {
    		for(int i=0;i<3;i++)
    			if(f.neighbor(i)==null) return true;
    	}
    	return false;
    }

    /**
     * creates a new vertex in the triangulation, 
     * with the given incident triangle.
     */        
    public TriangulationDSVertex_2<X> createVertex(X p, TriangulationDSFace_2<X> f){
    	TriangulationDSVertex_2<X> v = new TriangulationDSVertex_2<X>(f, p);
    	this.vertices.add(v);
    	return v;
    }

    /**
     * creates a new face in the triangulation, 
     * with the given incident vertices and adjacent triangles.
     */        
    public TriangulationDSFace_2<X> createFace(
    	 TriangulationDSVertex_2<X> v1, TriangulationDSVertex_2<X> v2,
    	 TriangulationDSVertex_2<X> v3,	
    	 TriangulationDSFace_2<X> c1, TriangulationDSFace_2<X> c2,
    	 TriangulationDSFace_2<X> c3){
    	
    	TriangulationDSFace_2<X> newFace=new TriangulationDSFace_2<X>(v1,v2,v3,c1,c2,c3);
    	for (int i=0; i<3; i++) {
    		TriangulationDSFace_2<X> face = newFace.neighbors.get(i);
    		if (face!= null) {
    			// update neighbor pointer in neighboring cell
    			face.setNeighbor(face.index(newFace), newFace);
    		}
    	}
    	this.faces.add(newFace);
    	return newFace;
    }

    /**
     * flips an edge in the triangulation and returns the new edge
     */        
    public HalfedgeHandle<X> flipEdge(HalfedgeHandle<X> e){
		 TriangulationDSFace_2<X> f1 = e.getFace();
		 TriangulationDSFace_2<X> f2 = e.getOpposite().getFace();

		 // retrieve vertices of quadrangle
		 ArrayList<TriangulationDSVertex_2<X>> vert = new ArrayList<TriangulationDSVertex_2<X>> (); 
		 vert.add(f1.vertex(e.index()));
		 vert.add(f1.vertex((e.index()+1)%3));
		 vert.add(f2.vertex(e.getOpposite().index()));
		 vert.add(f1.vertex((e.index()+2)%3));

		 // retrieve neighboring faces of quadrangle
		 ArrayList<TriangulationDSFace_2<X>> neighb = new ArrayList<TriangulationDSFace_2<X>> (); 
		 neighb.add(f1.neighbor((e.index()+2)%3));
		 neighb.add(f2.neighbor((e.getOpposite().index()+1)%3));
		 neighb.add(f2.neighbor((e.getOpposite().index()+2)%3));
		 neighb.add(f1.neighbor((e.index()+1)%3));
		 
		 // reset the vertices of the 2 quadrangle faces
		 f1.setVertex(0, vert.get(0));
		 f1.setVertex(1, vert.get(1));
		 f1.setVertex(2, vert.get(2));
		 f2.setVertex(0, vert.get(0));
		 f2.setVertex(1, vert.get(2));
		 f2.setVertex(2, vert.get(3));
   	
		 // reset the incident faces of the quandrangle vertices
		 vert.get(0).setFace(f1);
		 vert.get(1).setFace(f1);
		 vert.get(2).setFace(f2);
		 vert.get(3).setFace(f2);
		 
		 // reset the neighbors of the 2 quadrangle faces
		 f1.setNeighbor(0, neighb.get(1));
		 f1.setNeighbor(1, f2);
		 f1.setNeighbor(2, neighb.get(0));
		 f2.setNeighbor(0, neighb.get(2));
		 f2.setNeighbor(1, neighb.get(3));
		 f2.setNeighbor(2, f1);

		 // reset the neighbors of the quadrangle neighbors (only modify what needs to be modified)
		 if (neighb.get(1) != null)
			 neighb.get(1).setNeighbor(neighb.get(1).index(f2), f1);
		 if (neighb.get(3) != null)
			 neighb.get(3).setNeighbor(neighb.get(3).index(f1), f2);
		 
		 // reset the marks of the edges of the two faces
		 for (int i=0; i<3; i++) {
			 f1.unmark(i);
			 f2.unmark(i);
		 }
		 
		 // restore the marks of the quadrangle edges (or rather their inner halfedges)
		 for (int i=0; i<3; i++) {
			 e = new HalfedgeHandle<X> (f1, i);
			 if (f1.neighbor(i) != null)
				 e.setMark(e.getOpposite().isMarked());
			 e = new HalfedgeHandle<X> (f2, i);
			 if (f2.neighbor(i) != null)
				 e.setMark(e.getOpposite().isMarked());
		 }
			 
		 // return the new diagonal edge
		 return new HalfedgeHandle<X>(f1, 1);
    }

    /**
     * checks whether an edge is shared by two faces f1 and f2:
     * the function check whether the corresponding vertices
     * appear both in f1 and f2, in the correct order.
     * LCA: a' tester
     */        
    public boolean areEqual(TriangulationDSFace_2<X> face1, int i1, TriangulationDSFace_2<X> face2, int i2) {
    	HalfedgeHandle<X> edge1=new HalfedgeHandle<X>(face1,i1);
    	HalfedgeHandle<X> edge2=new HalfedgeHandle<X>(face2,i2);
    	
    	TriangulationDSVertex_2<X> v1=edge1.getVertex(0); // first vertex of edge1
    	TriangulationDSVertex_2<X> v2=edge1.getVertex(1); // second vertex of edge1

    	boolean contained=edge2.hasVertex(v1);
    	if(contained==false) return false;
    	contained=edge2.hasVertex(v2);
    	if(contained==false) return false;
    	
    	if(v1.equals(edge2.getVertex(0))) return false;
    	
    	return true;
    }


	/**
     * Returns the collection of all faces incident to vertex v.
     */        
	public Collection<TriangulationDSFace_2<X>> incidentFaces (TriangulationDSVertex_2<X> v) {
		throw new Error("a' completer");
	}

	/**
     * checks the combinatorial validity of the triangulation.
     */        
    public boolean isValid() {
    	boolean result=true;
    	System.out.print("Checking combinatorial validity...");
    	for(TriangulationDSVertex_2<X> p: this.vertices){
    		if(p==null) { 
    			result=false; 
    			System.out.println("\n vertex with null point"); 
    		}
    		if(p!=null && p.getFace()==null) { 
    			result=false; 
    			System.out.println("\n vertex with null associated triangle"); 
    		}
    		if(p!=null && !hasFace(p.getFace())) { 
    			result=false; 
    			System.out.println("\n vertex with non-existing associated triangle"); 
    		}
    	}
    	for(TriangulationDSFace_2<X> c: this.faces){
    		if (c==null) {
    			result = false;
        		System.out.println("null face");
    		}
    		if (c!=null && (c.vertex(0)==null || c.vertex(1)==null || c.vertex(2)==null)) { 
    			result=false; 
    			System.out.println("\n face with null vertex"); 
    		}
    		if (c!=null && (!hasVertex(c.vertex(0)) || !hasVertex(c.vertex(1)) || !hasVertex(c.vertex(2)))) { 
    			result=false; 
    			System.out.println("\n face with non-existing vertex"); 
    		}
    		
    		// check for the coherence of the 3 neighbors of a given face
    		if(c!=null) {
    			for(int i=0;i<3;i++) {
    				TriangulationDSFace_2<X> adjacentFace=c.neighbor(i);
    				if(adjacentFace!=null && adjacentFace.neighbors.contains(c)==false) {
    					System.out.println("\n adjacent face error");
    					result=false;
    				}
    			}
    		}
    	}
    	if(result==true)
    		System.out.println(" ok");
    	else
    		System.out.println("errors in combinatorial validity");
    	
    	return result;
    }

    /**
     * creates a triangle with vertices p1, p2, p3.
     */        
    public TriangulationDSFace_2<X> makeTriangle(X p1, X p2, X p3) {
    	TriangulationDSVertex_2<X> v1=new TriangulationDSVertex_2<X>(p1);
    	TriangulationDSVertex_2<X> v2=new TriangulationDSVertex_2<X>(p2);
    	TriangulationDSVertex_2<X> v3=new TriangulationDSVertex_2<X>(p3);
    	
    	TriangulationDSFace_2<X> c=createFace(v1,v2,v3,null,null,null);
    	v1.setFace(c); 
    	v2.setFace(c);
    	v3.setFace(c); 
    	
    	this.vertices.add(v1);
    	this.vertices.add(v2);
    	this.vertices.add(v3);
    	
    	return c;
    }

    /**
     * Inserts point p in face c. 
     * Face c is split into 3 triangles.
     */    
    public TriangulationDSVertex_2<X> insertInTriangle(X p, TriangulationDSFace_2<X> t) {
    	if (t == null)
    		throw new Error ("Trying to star a null cell");
    	TriangulationDSFace_2<X> f1=t.neighbor(1);
    	TriangulationDSFace_2<X> f2=t.neighbor(2);
    	TriangulationDSFace_2<X> f0=t.neighbor(0);
    	TriangulationDSVertex_2<X> v0=t.vertex(0);
    	TriangulationDSVertex_2<X> v1=t.vertex(1);
    	TriangulationDSVertex_2<X> v2=t.vertex(2);
    	
    	// create new vertex
    	TriangulationDSVertex_2<X> v = new TriangulationDSVertex_2<X>(p);
    	vertices.add(v);
    	
    	// create and set two new faces
    	TriangulationDSFace_2<X> newFace2=createFace(v0, v1, v, t, null, f2);
    	TriangulationDSFace_2<X> newFace1=createFace(v0, v, v2, t, f1, newFace2);
    	newFace2.setNeighbor(1, newFace1);

    	// set new vertex and neighbors of old triangle
    	t.setVertex(0, v);
    	t.setNeighbor(1, newFace1);
    	t.setNeighbor(2, newFace2);
    	
    	// set the faces incident to v and v0
    	v.setFace(t);
    	v0.setFace(newFace1);
    	
    	// restore the marks of the initial triangle edges
    	for (int i=0; i<3; i++)
    		t.unmark(i);
    	if (t.neighbor(0) != null) {
    		HalfedgeHandle<X> e = new HalfedgeHandle<X> (t, 0);
    		e.setMark(e.getOpposite().isMarked());
    	}
    	if (newFace1.neighbor(1) != null) {
    		HalfedgeHandle<X> e = new HalfedgeHandle<X> (newFace1, 1);
    		e.setMark(e.getOpposite().isMarked());
    	}
    	if (newFace2.neighbor(2) != null) {
    		HalfedgeHandle<X> e = new HalfedgeHandle<X> (newFace2, 2);
    		e.setMark(e.getOpposite().isMarked());
    	}
    	
    	// return the new vertex
    	return v;
    }
    

    /**
     * Inserts point p outside the triangulation. 
     * A new triangle is added and attached to a boundary triangle c
     * (they share the edge opposite to vertex i in c).
     */    
    public TriangulationDSVertex_2<X> insertOutside(X point, TriangulationDSFace_2<X> c, int i) {
    	throw new Error("a' completer");    	
    	/*    	FacetHandle<X> f=new FacetHandle<X>(c,i);
    	TriangulationDSVertex_3<X> v0=f.vertex(2);
    	TriangulationDSVertex_3<X> v1=f.vertex(1);
    	TriangulationDSVertex_3<X> v2=f.vertex(0);
    	
    	TriangulationDSVertex_3<X> newVertex=new TriangulationDSVertex_3<X>(point);
    	TriangulationDSCell_3<X> newCell=this.createCell(
    			v0, v1, v2, newVertex, null, null, null, c);
    	newVertex.setCell(newCell);
    	this.vertices.add(newVertex);
    	
    	return newVertex;*/
    }
    
    public String toString() {
    	String result="Triangulation information: ";
    	result=result+this.vertices.size()+" vertices, "+this.faces.size()+" faces\n";
    	
    	int cont=0;
    	for(TriangulationDSFace_2<X> f: this.faces) {
    		int neighborIndex0=this.faces.indexOf(f.neighbor(0));
    		int neighborIndex1=this.faces.indexOf(f.neighbor(1));
    		int neighborIndex2=this.faces.indexOf(f.neighbor(2));
    		
    		result=result+"face "+cont+": "+f;
    		result=result+" - neighbors: ("+neighborIndex0+", "+neighborIndex1+" ,"+neighborIndex2+")";
    		
    		result=result+"\n";
    		cont++;
    	}
    	
    	return result;
    }
    
    /**
     * insert in a triangle the barycenter point of its incident vertices
     */    
    public void insertBarycenter(TriangulationDSFace_2<X> f) {
    	if(f==null) throw new Error("null face error");
    	Point_[] points=f.verticesPoints();
    	Point_ newPoint=new Point_2();
    	newPoint.barycenter(points);
    	this.insertInTriangle((X) newPoint, f);
    }

}
