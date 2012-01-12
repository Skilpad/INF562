package Jcg.graph;

import Jcg.geometry.*;
import java.util.*;

/**
 * A class for representing a node of a geometric graph
 *
 * @author Luca Castelli Aleardi
 */
public class GraphNode<X extends Point_> {
	
	//public static int numberNodes=0;

	public List<GraphNode<X>> neighbors=null;
	X point=null;
	public int tag;

	    public GraphNode(X point) { 
	    	this.neighbors=new ArrayList<GraphNode<X>>();
	    	this.point=point;
	    	//numberNodes++;
	    }

	    public void addNeighbors(GraphNode<X> v) {
	    	if(v!=this && this.neighbors.contains(v)==false)
	    		this.neighbors.add(v);
	    }

	    public void removeNeighbor(GraphNode<X> v) {
	    	this.neighbors.remove(v);
	    }

	    public boolean adjacent(GraphNode<X> v) {
	    	return this.neighbors.contains(v);
	    }
	    
	    public List<GraphNode<X>> neighborsList() {
	    	return this.neighbors;
	    }
	    
	    public void setPoint(X point) { 
	    	this.point=point;
	    }  
	    
	    public X getPoint() { 
	    	return this.point; 
	    }

	    public void setTag(int tag) { 
	    	this.tag=tag;
	    }  
	    
	    public int getTag() { 
	    	return this.tag; 
	    }

	    public int degree() {
	    	return this.neighbors.size();
	    }
	    
	    public String toString(){
	        return "vert. "+tag+" - "+point.toString();
	    }

}
