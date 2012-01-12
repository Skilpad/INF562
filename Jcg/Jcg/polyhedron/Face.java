package Jcg.polyhedron;

import java.util.*;
import java.io.*;
import Jcg.geometry.*;


public class Face<X extends Point_>{

Halfedge<X> halfedge=null;
public int tag;

    public Face() {}
        
    public int degree() {
    	Halfedge<X> e,p;
    	if(this.halfedge==null) return 0;
    	
    	e=halfedge; p=halfedge.next;
    	int cont=1;
    	while(p!=e) {
    		cont++;
    		p=p.next;
    	}
    	return cont;
    }
    
    public int[] getVertexIndices(Polyhedron_3<X> polyhedron) {
    	int d=this.degree();
    	int[] result=new int[d];
    	Vertex v;
    	
    	Halfedge<X> e,p;
    	if(this.halfedge==null) return null;
    	
    	e=halfedge; p=halfedge.next;
    	v=e.getVertex();
    	result[0]=polyhedron.vertices.indexOf(v);
    	int cont=1;
    	while(p!=e) {
    		v=p.getVertex();
    		result[cont]=polyhedron.vertices.indexOf(v);
    		cont++;
    		p=p.next;
    	}
    	return result;
    }
    
    
    public void setEdge(Halfedge<X> halfedge) { this.halfedge=halfedge; }
    public Halfedge<X> getEdge() { return this.halfedge; }
        
    public String toString(){
    	throw new Error("a completer");
    }

}
