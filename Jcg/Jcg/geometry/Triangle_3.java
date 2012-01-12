package Jcg.geometry;

public class Triangle_3 {

	private Point_3 a,b,c;
	
	public Triangle_3 (Point_3 a, Point_3 b, Point_3 c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public Point_3 vertex(int i) {
		switch (i) {
		case 0: return a;
		case 1: return b;
		case 2: return c;
		default: throw new Error ("Bad vertex index " + i + " in triangle");
		}
	}
	
	public String toString() {
		return "Triangle (" + a + "," + b + "," + c + ")";
	}
	
}
