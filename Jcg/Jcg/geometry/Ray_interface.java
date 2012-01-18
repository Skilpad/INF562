package Jcg.geometry;

public interface Ray_interface<X extends Kernel> {
	
	public Point_<X>  source();
	public Vector_<X> direction();
	
	public boolean equals(Ray_2 r);
	
	public String toString();
	
	public int dimension();
	
}
