package Jcg.geometry;

public class Triangle_<X extends Kernel> {

	protected Point_<X> a,b,c;
	
	/** Creates segment from source to target. (Will not copy source & target.) **/
	public Triangle_(Point_<X> a, Point_<X> b, Point_<X> c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public Point_<X> vertex(int i) {
		switch (i) {
			case 0: return a;
			case 1: return b;
			case 2: return c;
			default: throw new Error ("Bad vertex index " + i + " in triangle");
		}
	}
	
	public String toString() {
		return "Triangle ( " + a + " , " + b + " , " + c + " )";
	}
	
}
