package Jcg.polyhedron;

import java.util.*;
import java.io.*;
import Jcg.geometry.*;


public class Halfedge<X extends Point_>{

public Halfedge<X> next;
public Halfedge<X> opposite;
public Vertex<X> vertex;

public Halfedge<X> prev;
public Face<X> face;
public int tag;
public int index;

	public void setNext(Halfedge<X> e) { this.next=e; }
	public void setOpposite(Halfedge<X> e) { this.opposite=e; }
	public void setPrev(Halfedge<X> e) { this.prev=e; }
	public void setVertex(Vertex<X> v) { this.vertex=v; }
	public void setFace(Face<X> f) { this.face=f; }
	
	public Halfedge<X> getNext() { return this.next; }
	public Halfedge<X> getOpposite() { return this.opposite; }
	public Halfedge<X> getPrev() { return this.prev; }
	public Vertex<X> getVertex() { return this.vertex; }
	public Face<X> getFace() { return this.face; }
	
    public Halfedge() {}
    
    public String toString(){
    	return "("+opposite.getVertex().getPoint()+" - "+vertex.getPoint()+")";
    }
    
}


