package Jcg.polyhedron;

/**
 * Class for representing a pair of half edges
 */
public class HalfedgePair {
	int first, second;
	
	public HalfedgePair(int first, int second) {
		this.first=first;
		this.second=second;
	}
	
	@Override
	public boolean equals(Object o) {
		HalfedgePair e=(HalfedgePair)o;
		if(first==e.first && second==e.second)
			return true;
		if(first==e.second && second==e.first)
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return first*second;
	}
}
