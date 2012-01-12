package Jcg.polyhedron;

import java.util.*;
import java.io.*;
import Jcg.geometry.*;


public class Vertex<X extends Point_>{

Halfedge<X> halfedge=null;
X point=null;
public int tag;
public int index;

    public Vertex(X point) { this.point=point; }
    public Vertex() {}

    public void setEdge(Halfedge<X> halfedge) { this.halfedge=halfedge; }
    public void setPoint(X point) { this.point=point; }  
    
    public X getPoint() { return this.point; } 
    public Halfedge<X> getHalfedge() { return this.halfedge; } 
    
    public String toString(){
        return "vert. "+point.toString();
    }
}


