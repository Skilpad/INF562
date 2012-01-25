package Jcg.geometry;

public class Hyperplan<X extends Kernel>{
	
	private Point_<X> M;
	private Vector_<X> n;
	
	public Hyperplan() {}
	
	public Hyperplan(Point_<X> element, Vector_<X> normal) {
		this.M = element;
		this.n = normal;
	}
	
	public boolean hasOn(Point_<X> p) {
		return (Double) n.innerProduct(p.minus(M)) == 0;
		// TODO: Good for any Kernel?
	}

	public boolean hasOnPositiveSide(Point_<X> p) {
		return (Double) n.innerProduct(p.minus(M)) > 0;
		// TODO: Good for any Kernel?
	} 

	public boolean hasOnNegativeSide(Point_<X> p) {
		return (Double) n.innerProduct(p.minus(M)) < 0;
		// TODO: Good for any Kernel?
	} 

	public boolean equals(Hyperplan<X> h) {
		return (h.n.colinearTo(n) && this.hasOn(h.M));
	}

	public String toString() { return "Hyperplan passing by "+M+" orthogonal to "+n;}
	public int dimension() { return M.dimension();}

}
