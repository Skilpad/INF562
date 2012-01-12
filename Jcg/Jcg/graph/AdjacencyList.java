package Jcg.graph;

import java.util.*;

public class AdjacencyList<X extends Number>{
	
	List<X> neighbors;
	
	public AdjacencyList() {
		this.neighbors=new ArrayList<X>();
	}

	public AdjacencyList(int d) {
		this.neighbors=new ArrayList<X>(d);
	}

	public X getNeighbor(int i) {
		return this.neighbors.get(i);
	}

	public void setNeighbor(int i, X element) {
		this.neighbors.set(i, element);
	}

	public void addNeighbor(X element) {
		this.neighbors.add(element);
	}

	public void removeNeighbor(int index) {
		this.neighbors.remove(index);
	}

	public void removeElement(X element) {
		int cont=0;
		for(X e: neighbors) {
			if(e.equals(element)==true) {
				this.neighbors.remove(cont);
				return;
			}
			else
				cont++;				
		}
	}
	
	public boolean contains(int index) {
		for(X e: neighbors) {
			if(e.intValue()==index)
				return true;
		}
		return false;
	}
	
	public Number[] getNeighborsList() {
		Number[] result=new Number[neighbors.size()];
		int cont=0;
		for(X e: this.neighbors) {
			result[cont]=e;
			cont++;
		}
		return result;		
	}

	public int size() {
		return this.neighbors.size();
	}

	public String toString() {
		String result="";
		for(X e: neighbors) {
			result=result+" "+e;
		}
		return result;
	}

}
