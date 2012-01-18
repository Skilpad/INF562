package Jcg.geometry;

public class Triangle_3 implements Triangle_interface<Kernel_3>{
	
	protected Point_3 a,b,c;
		
	/** Creates segment from source to target. (Will not copy source & target.) **/
	public Triangle_3(Point_3 a, Point_3 b, Point_3 c) {
		this.a = a; this.b = b; this.c = c;
	}
	/** Creates segment from source to target. (Will copy source & target.) **/
	public Triangle_3(Point_ a, Point_ b, Point_ c) {
		this.a = new Point_3(a); this.b = new Point_3(b); this.c = new Point_3(c);
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
		return "Triangle ( " + a + " , " + b + " , " + c + " )";
	}
	
}
