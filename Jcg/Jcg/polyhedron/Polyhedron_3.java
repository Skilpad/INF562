package Jcg.polyhedron;

import java.util.*;
import Jcg.geometry.*;
import Jcg.Fenetre;
//import Jcg.quadedge.Primitives;
import Jcg.viewer.*;


/**
 * Half-Edge data structure for polyhedral meshes
 *
 * @author Code by Luca Castelli Aleardi
 *
 * @see Primitives for the Manipulation of an orientable surface
 */
public class Polyhedron_3<X extends Point_>{

    public ArrayList<Vertex<X>> vertices;
    public ArrayList<Face<X>> facets;
    public ArrayList<Halfedge<X>> halfedges;

    public Polyhedron_3() {
        vertices=new ArrayList<Vertex<X>>();
        facets=new ArrayList<Face<X>>();
        halfedges=new ArrayList<Halfedge<X>>();
    }
   
    public Polyhedron_3(int n, int e, int f) {
        vertices=new ArrayList<Vertex<X>>(n);
        facets=new ArrayList<Face<X>>(f);
        halfedges=new ArrayList<Halfedge<X>>(e);
    }
    
    class ColorDecorator extends Decorator<Vertex,Integer> {
    }
    
    public void DecorateVertices() {
    	ColorDecorator color=new ColorDecorator();
    	color.setDecoration(vertices.get(0),0);
    }

    public int sizeOfVertices() {
    	return this.vertices.size();
    }
    
    public int sizeOfFacets() {
    	return this.facets.size();
    }
    public int sizeOfHalfedges() {
    	return this.halfedges.size();
    }
    
    public int vertexDegree(Vertex v) {
    	int result=0;
    	Halfedge e=v.getHalfedge();
    	
    	Halfedge pEdge=e.getNext().getOpposite();
    	while(pEdge!=e) {
    		pEdge=pEdge.getNext().getOpposite();
    		result++;
    	}
    	
    	return result+1;
    }
    
	
/**
 * returns true if there are no border edges
 */
	public boolean isClosed() {
		throw new Error("a completer");
	}

/**
 * returns true if all vertices have exactly two incident edges
 */	
	public boolean isPureBivalent() {
		throw new Error("a completer");
	}

/**
 * returns true if all vertices have exactly three incident edges
 */	
	public boolean isPureTrivalent() {
		throw new Error("a completer");
	}

//bool 	P.is_pure_triangle () 	returns true if all facets are triangles.

//bool 	P.is_pure_quad () 	returns true if all facets are quadrilaterals.

/**
 * true iff the connected component denoted by h is a triangle
 */	
	public boolean isTriangle(Halfedge<X> h) {
		throw new Error("a completer");
	}
   
    public int genus() {
        int n=sizeOfVertices();
        int e=sizeOfHalfedges();
        int f=sizeOfFacets();
        return -(n-e/2+f-2)/2;
    }

/**
 * returns true if the polyhedral surface is combinatorially consistent. 
 * If borders==true normalization of the border edges is checked too. 
 * This method checks that each facet is at least a triangle and that 
 * the two incident facets of a non-border edge are distinct. 
 */	    
    public boolean isValid(boolean borders) {
    	boolean valid=true;
        System.out.print("Checking Polyhedron...");
        int n=this.vertices.size();
        int e=this.halfedges.size();
        int f=this.facets.size();

        for(int i=0; i<this.halfedges.size(); i++) {
        	Halfedge<X> pedge=this.halfedges.get(i);
        	if(pedge.getOpposite()==null) { 
        		System.out.print("error opposite: "+i); valid=false; 
        		//Face face=pedge.getFace();
        		//int[] ind=face.getVertexIndices(this);
        		//System.out.println(" "+ind[0]+" "+ind[1]+" "+ind[2]);
        	}
        	if(pedge.getNext()==null) { System.out.println("error next_edge: "+i); valid=false; }
        	if(pedge.getPrev()==null) { System.out.println("error prev_edge: "+i); valid=false; }
        	if(pedge.getVertex()==null) { System.out.println("error vertex: "+i); valid=false; }
        	if(pedge.opposite!=null && pedge.face==pedge.getOpposite().face) { System.out.println("error edge: "+i); valid=false; }
        }            
        for(int i=0; i<this.facets.size(); i++){
        	Face<X> pface=this.facets.get(i);
        	if(pface==null) { System.out.println("error face pointer"); valid=false; }
        	if(pface.halfedge==null) { System.out.println("error face.halfedge"); valid=false; }
        	if(pface.degree()<3) { System.out.println("error face degree"); return valid=false; }
        }            
        for(int i=0; i<this.vertices.size(); i++){
        	Vertex<X> pvertex=this.vertices.get(i);
        	//System.out.println(""+pvertex.toString());
        	if(pvertex==null) { System.out.println("error vertex pointer:"+i); valid=false; }
        	if(pvertex.halfedge==null) { System.out.println("error vertex.halfedge: "+i); valid=false; }
        	if(pvertex.getPoint()==null) { System.out.println("error vertex.point: "+i); valid=false; }
        }            

        if(valid==true)
        	System.out.println("ok"); 

        else
         	System.out.println("not valid"); 

        System.out.print("n: "+n+"  e: "+e/2+"  f: "+f+" - ");
        int g=-(n-e/2+f-2)/2;
        System.out.println("genus: "+g);
        
        return valid;
    }

    
    public void flipEdge(Halfedge e) {
    }
    
    public void createCenterVertex(Face<X> f){
    	int degree=f.degree();
    	Point_[] neighbors=new Point_[degree];
    	
    	Halfedge<X> e=f.getEdge();
    	neighbors[0]=e.getVertex().getPoint();
    	for(int i=1;i<degree;i++)   {
    		e=e.getNext();
    		neighbors[i]=e.getVertex().getPoint();
    	}
    	Point_ centerVertex;
    	if(neighbors[0].dimension()==2) centerVertex=new Point_2();
    	else if(neighbors[0].dimension()==3) centerVertex=new Point_3();
    	else throw new Error("error point dimension");

    }
 /**
 * replace the star of a vertex v by a new face 
 */	       
    public void eraseCenterVertex(Vertex v) {
    	throw new Error("a completer");
    }
    
    public Halfedge<X> makeHole(Halfedge h){
    	if(h==null || h.face==null) throw new Error("error making hole: h or h.face null");
    	this.facets.remove(h.getFace());

    	Halfedge p=h.next;
    	h.face=null;
    	int cont=1;
    	while(p!=h) {
    		p.face=null;
    		p=p.next;
    	}
    	return h;
    }

    public Halfedge<X> fillHole(Halfedge h){
    	if(h.face!=null) throw new Error("error filling hole: h not boundary edge");
    	
    	Face<X> newFace=new Face<X>();
    	this.facets.add(newFace);
    	newFace.setEdge(h);

    	Halfedge p=h.next;
    	h.face=newFace;
    	int cont=1;
    	while(p!=h) {
    		p.face=newFace;
    		p=p.next;
    	}
    	return h;
    }

/**
 * creates a new triangle facet within the hole incident to h by connecting 
 * the tip of h with two new halfedges and a new vertex. 
 * Returns the halfedge of the new edge that is incident to the new facet and the new vertex. 
 */	        
    public Halfedge<X> addTriangleToBorder(Halfedge h, X point) {
    	if(h.face!=null) throw new Error("no border edge");
    	System.out.println("adding triangle to "+h);
    	
    	Face<X> newFace=new Face<X>();
    	Vertex<X> newVertex=new Vertex<X>(point);
    	Halfedge<X> hPrev=new Halfedge<X>();
    	Halfedge<X> hNext=new Halfedge<X>();
    	Halfedge<X> hPrevOpp=new Halfedge<X>();
    	Halfedge<X> hNextOpp=new Halfedge<X>();
    	
    	// setting the new face
    	newFace.setEdge(h);
    	// setting hPrev (halfedge preceding h in the new face)
    	hPrev.setFace(newFace);
    	hPrev.setVertex(h.getOpposite().getVertex());
    	hPrev.setPrev(hNext);
    	hPrev.setNext(h);
    	hPrev.setOpposite(hPrevOpp);
    	// setting hNext (halfedge following h in the new face)
    	hNext.setFace(newFace);
    	hNext.setVertex(newVertex);
    	hNext.setPrev(h);
    	hNext.setNext(hPrev);
    	hNext.setOpposite(hNextOpp);
    	// setting hPrevOpp (new boundary halfedge)
    	hPrevOpp.setFace(null);
    	hPrevOpp.setVertex(newVertex);
    	hPrevOpp.setPrev(h.getPrev());
    	hPrevOpp.setNext(hNextOpp);
    	hPrevOpp.setOpposite(hPrev);
    	// setting hNextOpp (the other new boundary halfedge)
    	hNextOpp.setFace(null);
    	hNextOpp.setVertex(h.getVertex());
    	hNextOpp.setPrev(hPrevOpp);
    	hNextOpp.setNext(h.getNext());
    	hNextOpp.setOpposite(hNext);
    	// updating old boundary halfedge informations
    	h.setFace(newFace);
    	h.setPrev(hPrev);
    	h.setNext(hNext);
    	// setting newVertex
    	newVertex.setEdge(hPrev); // LCA: a controler si c'est hPrev ou hNext
    	
    	// adding new facet, vertex and the four halfedges
    	this.vertices.add(newVertex);
    	this.facets.add(newFace);
    	this.halfedges.add(hPrev);
    	this.halfedges.add(hNext);
    	this.halfedges.add(hPrevOpp);
    	this.halfedges.add(hNextOpp);
    	
    	return hNext;
    }
    
 /**
 * a triangle with border edges is added to the polyhedral surface. 
 * Returns a non-border halfedge of the triangle.
 */	        
 	public Halfedge<X> makeTriangle(X p1, X p2, X p3) {    	
    	Face<X> newFace=new Face<X>();
    	Vertex<X> newVertex1=new Vertex<X>(p1);
    	Vertex<X> newVertex2=new Vertex<X>(p2);
    	Vertex<X> newVertex3=new Vertex<X>(p3);
    	Halfedge<X> e1=new Halfedge<X>();
    	Halfedge<X> e2=new Halfedge<X>();
    	Halfedge<X> e3=new Halfedge<X>();
    	Halfedge<X> e1Opp=new Halfedge<X>();
    	Halfedge<X> e2Opp=new Halfedge<X>();
    	Halfedge<X> e3Opp=new Halfedge<X>();
    	
    	// setting the new face
    	newFace.setEdge(e1);
    	// setting the new vertices (LCA: a' controler)
    	newVertex1.setEdge(e2);
    	newVertex2.setEdge(e3);
    	newVertex3.setEdge(e1);
    	// setting e1
    	e1.setFace(newFace);
    	e1.setVertex(newVertex1);
    	e1.setPrev(e3);
    	e1.setNext(e2);
    	e1.setOpposite(e1Opp);
    	// setting e2
    	e2.setFace(newFace);
    	e2.setVertex(newVertex2);
    	e2.setPrev(e1);
    	e2.setNext(e3);
    	e2.setOpposite(e2Opp);
    	// setting e3
    	e3.setFace(newFace);
    	e3.setVertex(newVertex3);
    	e3.setPrev(e2);
    	e3.setNext(e1);
    	e3.setOpposite(e3Opp);
    	// setting e1Opp (boundary halfedge opposite to e1)
    	e1Opp.setFace(null);
    	e1Opp.setVertex(newVertex3);
    	e1Opp.setPrev(e2Opp);
    	e1Opp.setNext(e3Opp);
    	e1Opp.setOpposite(e1);
    	// setting e2Opp (boundary halfedge opposite to e2)
    	e2Opp.setFace(null);
    	e2Opp.setVertex(newVertex1);
    	e2Opp.setPrev(e3Opp);
    	e2Opp.setNext(e1Opp);
    	e2Opp.setOpposite(e2);
    	// setting e3Opp (boundary halfedge opposite to e3)
    	e3Opp.setFace(null);
    	e3Opp.setVertex(newVertex2);
    	e3Opp.setPrev(e1Opp);
    	e3Opp.setNext(e2Opp);
    	e3Opp.setOpposite(e3);
    	
    	this.facets.add(newFace);
    	this.vertices.add(newVertex1);
    	this.vertices.add(newVertex2);
    	this.vertices.add(newVertex3);
    	this.halfedges.add(e1); this.halfedges.add(e1Opp);
    	this.halfedges.add(e2); this.halfedges.add(e2Opp);
    	this.halfedges.add(e3); this.halfedges.add(e3Opp);
    	
    	return e1;
    }


/**
 * splits the facet incident to h and g into two facets with a new diagonal 
 * between the two vertices denoted by h and g respectively. 
 * The second (new) facet is a copy of the first facet. Returns h->next() 
 * after the operation, i.e., the new diagonal. The new face is to the right 
 * of the new diagonal, the old face is to the left. 
 * The time is proportional to the distance from h to g around the facet. 
 */	    
    public Halfedge splitFacet(Halfedge h, Halfedge g) {
    	if(h==null || g==null) throw new Error("splitFacet: null pointer");
    	if(h.face!=g.face) throw new Error("splitFacet: different incident facets");
    	if(h==g || h.next==g || g.next==h) {
    		throw new Error("loops and multiple edges are not allowed");
    	}
		
		Halfedge<X> newDiagonal=new Halfedge<X>();
		Halfedge<X> oppositeNewDiagonal=new Halfedge<X>();
		Face<X> newFace=new Face<X>();

		newFace.halfedge=g;
		newDiagonal.opposite=oppositeNewDiagonal;
		oppositeNewDiagonal.opposite=newDiagonal;
		newDiagonal.face=h.face;
		oppositeNewDiagonal.face=newFace;
		
		// a completer
		
		Halfedge p=h.next;
		while(p!=g) {
			p.face=newFace;
			p=p.next;
		}
		
		h.next=newDiagonal;
		g.next=oppositeNewDiagonal;
		return newDiagonal;
    } 
    
    public String verticesToString() {
        String result="List of vertices\n";
        Iterator it=this.vertices.iterator();
        int cont=0;
        while(it.hasNext()) {
        	Vertex v=(Vertex)it.next();
        	result=result+"v"+cont+" "+v.getPoint().toString()+"\n"; 
        	cont++;
        }  	
        return result;           
    }

    public String facesToString() {
        String result="List of faces\n";
        Iterator it=this.facets.iterator();
        int cont=0;
        while(it.hasNext()) {
        	Face<X> f=(Face<X>)it.next();
        	result=result+"f"+cont+" ";
        	Halfedge<X> e=f.getEdge();
        	while(e.getNext()!=f.getEdge()) {
        		result=result+vertices.indexOf(e.getVertex())+" "; 
        		e=e.getNext();
        	}
        	result=result+vertices.indexOf(e.getVertex())+"\n";
        	cont++;
        }  	
        return result;           
    }

    
}