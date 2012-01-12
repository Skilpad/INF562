package Jcg.geometry;

public class Pair<X extends Comparable<X>> {

    private X first;
    private X second;
    final static int hashCodeParameter=30;

    public Pair(X first, X second) {
    	this.first = first;
    	this.second = second;
    }

    public X getFirst() { return first; }
    public X getSecond() { return second; }

    public void setFirst(X v) { first=v; }
    public void setSecond(X v) { second=v; }

    public String toString() {
        return "Pair(" + first + ", " + second + ")";
    }
   
    public boolean equals(Object o) {
    	if((o instanceof Pair)==false) throw new Error("bad type error");
    	Pair b=(Pair)o;
    	if(this.first.equals(b.first) && this.second.equals(b.second))
    		return true;
    	if(this.second.equals(b.first) && this.first.equals(b.second))
    		return true;
    	return false;
    }

    public int hashCode() {
    	if (first == null) return (second == null) ? 0 : second.hashCode() + 1;
    	else if (second == null) return first.hashCode() + 2;
    	else return first.hashCode() * hashCodeParameter + second.hashCode();
    }
}
