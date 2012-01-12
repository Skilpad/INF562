package Jcg.schnyderWoods;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import Jcg.polyhedron.*;
import Jcg.geometry.Point_;

/**
 * @author Luca Castelli Aleardi
 * This class allows to compute a (minimal) Schnyder wood of a planar triangulation.
 * It implements a "cut border": a simple cycle of edges defining a
 * topological disk, and provides basic operations such as vertex removal
 * (the vertex to be removed must belong to the boundary)
 * It works for planar triangle meshes without boundaries.
 * Faces are assumed to be ccw oriented (only for combinatorics)
 */
public class CutBorder {

    public Polyhedron_3 polyhedron;
    ArrayList<Halfedge> boundary; // edges defining the cut border

    static int maxBoundarySize=100; // for initializing the size of the cut border

    // definition of the root face and root vertices v0, v1, v2
    public Halfedge rootEdge; // half-edge (v_0, v_1) oriented toward v_1, ccw orientation of faces
    public Vertex v0, v1, v2; // vertices on the root face
    
    // auxiliary information needed for computing a Schnyder wood
    boolean[] isWellOriented; // says whether an half-edge is "oriented" toward his target vertex
    byte[] edgeColor; // stored an integer representing the coloration of an edge
    boolean[] isChord; // for half-edges, says whether is a chord
    
    // information associated to vertices
    boolean[] isOnCutBorder; // say whether a vertex is lying on the cut-border
    short[] hasIncidentChords; // store the number of chords incident to a vertex (on the cut-border)
    
    /**
     * Construct the cut-border starting from the root edge (v0, v1)
     * At the beginning the cut-border contains edges (v2, v0) and (v1, v2)
     * Edges are ccw oriented around faces
     */
    public CutBorder(Polyhedron_3 polyhedron, Halfedge rootEdge) {
    	System.out.print("Cut Border Initialisation... ");
    	if(polyhedron==null)
    		throw new Error("error: null polyhedron");
    	if(rootEdge==null)
    		throw new Error("error: root edge null");
    	if(polyhedron.genus()>0)
    		throw new Error("error: non planar mesh");
    	
    	int i=0;
    	for(i=0;i<polyhedron.sizeOfVertices();i++) {
    		Vertex v=(Vertex)polyhedron.vertices.get(i);
    		v.index=i;
    	}
    	
    	i=0;
    	for(Object o: polyhedron.halfedges) {
    		Halfedge e=(Halfedge)o;
    		e.index=i;
    		i++;
    	}
    	
    	this.boundary=new ArrayList(maxBoundarySize);
    	this.polyhedron=polyhedron;
    	this.rootEdge=rootEdge;    	
        this.v0=this.rootEdge.getOpposite().getVertex();
        this.v1=this.rootEdge.getVertex();
    	
    	this.edgeColor=new byte[this.polyhedron.sizeOfHalfedges()];
    	for(i=0;i<edgeColor.length;i++)
    		this.edgeColor[i]=-1;
    	
    	this.isChord=new boolean[this.polyhedron.sizeOfHalfedges()];
    	this.isWellOriented=new boolean[this.polyhedron.sizeOfHalfedges()];
    	this.isOnCutBorder=new boolean[this.polyhedron.sizeOfVertices()];
    	this.hasIncidentChords=new short[this.polyhedron.sizeOfVertices()];
    	
    	Halfedge edge10=this.rootEdge.getOpposite();
    	Halfedge edge02=edge10.getNext();
    	Halfedge edge21=edge02.getNext();
    	
    	this.v2=edge02.getVertex();
    	
    	// setting information concerning the root-edge
    	this.addToCutBorder(edge21.getOpposite(), 0);
    	this.addToCutBorder(edge02.getOpposite(), 0);	
    	this.isOnCutBorder[v1.index]=true;
    	
    	// set the orientation of the root edge
    	this.isWellOriented[rootEdge.index]=false;
    	this.isWellOriented[rootEdge.getOpposite().index]=true;
    	// set the color of the root edge
    	this.edgeColor[rootEdge.index]=0;
    	this.edgeColor[rootEdge.getOpposite().index]=0;
    	
    	System.out.println("done");
    }
    
    /**
     * It removes a vertex from the cut-border (vertex conquest)
     * It updates the cut-border, assigning color and orientation to edges
     */
    public int vertexRemoval(int i) {
    	if(i>=this.boundary.size()) {
    		System.out.println("no more vertex to remove "+i);
    		return -1;
    	}
    	// vertex v0 cannot be removed
    	if(i==0) return 1;
    	//if(i==-1) return 1;
    	
    	Halfedge rightEdge=this.boundary.get(i);
    	Halfedge leftEdge=this.boundary.get(i-1);
    	
    	// the vertex is incident to a triangle
    	if(rightEdge.getNext()==leftEdge)
    		return triangleRemoval(i);

    	// if the vertex has still incident chords it cannot be removed
    	// then return next vertex on cut-border
    	/*if(this.hasIncidentChords[rightEdge.getVertex().index]>0)
    		return i+1; */
    	if(this.hasIncidentChords(i)==true)
    		return i+1;
    	
    	// general case: the vertex is incident to more than one triangle
    	
    	// process left and right edges incident to v on the cut-border
    	this.setOutgoingEdge1(rightEdge);
    	this.setOutgoingEdge0(leftEdge);
    	this.boundary.remove(i);
    	this.boundary.remove(i-1);
    	
    	// v is not anymore on the cut-border
    	this.isOnCutBorder[rightEdge.getVertex().index]=false;
    	
    	// add new edges to the cut-border
    	this.addToCutBorder(rightEdge.getPrev().getOpposite(), i-1);
    	
    	Halfedge pEdge=rightEdge.getNext().getOpposite();
    	while(pEdge!=leftEdge.getOpposite()) {
    		this.addToCutBorder(pEdge.getPrev().getOpposite(), i-1);
    		this.setIngoingEdge2(pEdge);
    		pEdge=pEdge.getNext().getOpposite();
    	}
    	
    	return i;
    }

    /**
     * It removes a vertex incident to one triangle from the cut-border
     */
    private int triangleRemoval(int i) {
    	Halfedge rightEdge=this.boundary.get(i);
    	Halfedge leftEdge=this.boundary.get(i-1);

    	this.setOutgoingEdge1(rightEdge);
    	this.setOutgoingEdge0(leftEdge);
    	this.boundary.remove(i);
    	this.boundary.remove(i-1);

    	this.isOnCutBorder[rightEdge.getVertex().index]=false;
    	this.addToCutBorder(rightEdge.getPrev().getOpposite(), i-1);
    	
    	return i-1;
    }

    /**
     * Return the number of chords incident to a given vertex i
     * Vertex i must belong to the cut-border
     * It also updates the status of incident chords
     */
    private int computeIncidentChords(int i) {
    	if(i<1 || i>=this.boundary.size())
    		throw new Error("vertex index error");
    	
    	int result=0;
    	Halfedge rightEdge=this.boundary.get(i);
    	Halfedge leftEdge=this.boundary.get(i-1);
    	
    	Halfedge pEdge=rightEdge.next;
    	
    	throw new Error("to be completed");
    }

    /**
     * Check whether the vertex has incident chords
     * In that case it cannot be removed from the cut-border
     */
    private boolean hasIncidentChords(int i) {
    	//if(i<1 || i>=this.boundary.size())
    	//	throw new Error("vertex index error");
    	//System.out.println("not free vertex "+i);
    	
    	Halfedge rightEdge=this.boundary.get(i);
    	Halfedge leftEdge=this.boundary.get(i-1);

    	Halfedge pEdge=rightEdge.getNext().getOpposite();
    	while(pEdge!=leftEdge.getOpposite()) {
    		Vertex v=pEdge.getOpposite().getVertex();
    		if(this.isOnCutBorder[v.index]==true)
    			return true;
    		pEdge=pEdge.getNext().getOpposite();
    	}
    	return false;
    }

    private void setIngoingEdge2(Halfedge e) {
    	this.edgeColor[e.index]=2;
    	this.edgeColor[e.getOpposite().index]=2;
    	
    	this.isWellOriented[e.index]=true;
    	this.isWellOriented[e.getOpposite().index]=false;
    }

    private void setOutgoingEdge1(Halfedge e) {
    	this.edgeColor[e.index]=1;
    	this.edgeColor[e.getOpposite().index]=1;
    	
    	this.isWellOriented[e.index]=false;
    	this.isWellOriented[e.getOpposite().index]=true;
    }

    private void setOutgoingEdge0(Halfedge e) {
    	this.edgeColor[e.index]=0;
    	this.edgeColor[e.getOpposite().index]=0;
    	
    	this.isWellOriented[e.index]=true;
    	this.isWellOriented[e.getOpposite().index]=false;
    }

    /**
     * Add a halfedge to the cut-border, at a given position (in the cut-border)
     * Update all concerned information (colors, existent chords, boundary vertices, ...)
     */
    public void addToCutBorder(Halfedge e, int position) {
    	//System.out.println("edge added to cut-border");
    	if(e==null)
    		throw new Error("halfedge not defined");
    	if(position<0)
    		throw new Error("wrong position "+position);
    	
    	this.boundary.add(position, e);

    	this.edgeColor[e.index]=3;
    	this.edgeColor[e.getOpposite().index]=3;
    	
    	this.isChord[e.index]=false;
    	this.isChord[e.getOpposite().index]=false;
    	
    	this.isOnCutBorder[e.getVertex().index]=true;
    	this.isOnCutBorder[e.getOpposite().getVertex().index]=true;
    	
    }
    
    /**
     * Perform all steps of the graph traversal, computing the Schnyder wood
     */
    public void performTraversal() {
    	System.out.print("Computing minimal Schnyder wood...");
    	int i=1;
    	while(this.boundary.size()>1) {
    		i=this.vertexRemoval(i);
    	}
    	System.out.println("done");

    	// set the color of the root edge, which is of color 0 (oriented toward v0)
    	this.edgeColor[rootEdge.index]=0;
    	this.edgeColor[rootEdge.getOpposite().index]=0;

    }
    
    public boolean[] getEdgeOrientation() {
    	return this.isWellOriented;
    }
    
    public byte[] getEdgeColoration() {
    	return this.edgeColor;
    }
    
    /**
     * Return an array of indices representing the original vertex ordering
     */
    public int[] getOriginalVertexOrdering() {
    	int[] result=new int[this.polyhedron.sizeOfVertices()];
    	
    	int i=0;
    	for(Object o: this.polyhedron.vertices) {
    		Vertex v=(Vertex)o;
    		result[i]=v.index;
    		i++;
    	}
    	return result;    	
    }


    
    //----------------------------------------------
    //--- Methods for visualizing the cut-border ---
    //----------------------------------------------
    
    public Color[] getEdgeColors() {
    	Color[] result=new Color[this.edgeColor.length];
    	
    	for(int i=0;i<this.edgeColor.length;i++) {
    		if(edgeColor[i]==0) result[i]=Color.red;
    		else if(edgeColor[i]==1) result[i]=Color.blue;
    		else if(edgeColor[i]==2) result[i]=Color.black;
    		else if(edgeColor[i]==3) result[i]=Color.orange;
    		else if(edgeColor[i]==5) result[i]=Color.lightGray;
    		
    		else result[i]=Color.gray;
    	}
    	return result;
    }
    
    public String[] originalVertexOrderingToString() {
    	String[] result=new String[this.polyhedron.sizeOfVertices()];
    	
    	int i=0;
    	for(Object o: this.polyhedron.vertices) {
    		Vertex v=(Vertex)o;
    		result[i]=""+v.index;
    		i++;
    	}
    	return result;    	
    }

    public Collection<Point_> getCutBorderVertices() {
    	Collection<Point_> result=new ArrayList<Point_>();
    	
    	for(Object o: polyhedron.vertices) {
    		Vertex v=(Vertex)o;
    		if(this.isOnCutBorder[v.index]==true)
    			result.add(v.getPoint());
    	}
    	return result;
    }
    
    public String toString() {
    	String result="cut-border size: "+this.boundary.size();
    	result=result+"\nroot edge (v0,v1)=("+v0.index+","+v1.index+")"+"   ("+v0+","+v1+")";
    	result=result+"\nroot face (v0,v1,v2)=("+v0+","+v1+","+v2+")";
    	
    	result=result+"\n boundary edges [";
    	
    	for(Halfedge e: this.boundary) {
    		result=result+e.getVertex().index+",";
    	}
    	result=result+"]";
    	
    	return result;
    }
    
}
