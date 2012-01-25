package Jcg.graph;

import Jcg.geometry.*;
import java.util.*;

/**
 * A class for representing a node of a geometric graph
 *
 * @author Luca Castelli Aleardi
 */
public class GraphNode<X extends Point_<?>> {
	
	//public static int numberNodes=0;
	
	public List<GraphNode<X>> neighbors=null;
	X point=null;
	public int tag;

	/** Creates a new graph node centered on p as point. **/
	public GraphNode(X p) { 
		this.neighbors = new ArrayList<GraphNode<X>>();
		this.point = p;
	}


	/** Adds the node v as neighbor. **/
	public void addNeighbors(GraphNode<X> v) {
		if(v!=this && this.neighbors.contains(v)==false)
			this.neighbors.add(v);
	}

	/** Removes the node v from neighbors. **/
	public void removeNeighbor(GraphNode<X> v) {
		this.neighbors.remove(v);
	}

	/** Returns if v is adjacent. **/
	public boolean adjacent(GraphNode<X> v) {
		return this.neighbors.contains(v);
	}

	/** Returns the list of neighbors. **/
	public List<GraphNode<X>> neighborsList() {
		return this.neighbors;
	}
	
	public void setPoint(X point) { 
		this.point = point;
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
		return "Vert. "+tag+" -> "+point.toString();
	}

}
