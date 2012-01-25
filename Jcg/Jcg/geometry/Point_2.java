package Jcg.geometry;

public class Point_2 extends Point_<Kernel_2>{

	/** Return x **/
	public Double getX() { return data.getX(); }
	/** Return y **/
	public Double getY() { return data.getY(); }
	
	/** Set X **/
	public void setX(Number x) { data.setX(x.doubleValue()); }
	/** Set Y **/
	public void setY(Number y) { data.setY(y.doubleValue()); }

	/** Return x **/
	public double x() { return data.getX(); }
	/** Return y **/
	public double y() { return data.getY(); }
	
	/** Set X **/
	public void x(double x) { data.setX(x); }
	/** Set Y **/
	public void y(double y) { data.setY(y); }

	
	//****************
	//  Constructors
	//****************
	
	/** Creates (0,0) point **/
	public Point_2() { this.data = new Kernel_2(); }

	/** Creates (x,y) point **/
	public Point_2(Number x,Number y) { this.data = new Kernel_2(x.doubleValue(),y.doubleValue()); }

	/** Creates a point with current values of p coordinates **/
	public Point_2(Point_<?> p) { this.data = new Kernel_2(p.data); }
	

	/** Creates the barycenter of points **/
	public Point_2(Point_[] points) {
		this.data = new Kernel_2();
		this.barycenter(points);
	}
	
	/** Return a copy of p (fields are not shared) **/
	public Point_2 copy() { return new Point_2(this); }
		

	//*******************
	//  Global calculus 
	//*******************
	
	/** Return the isobarycenter of points **/
	static public Point_2 barycenter_(Point_[] points) {
		Point_2 res = new Point_2();
		res.barycenter(points);
		return res;
	}

	/** Return the barycenter of points with masses given by coefficients **/
	static public Point_2 barycenter_(Point_[] points, Number[] coefficients) {
		Point_2 res = new Point_2();
		res.barycenter(points, coefficients);
		return res;
	}

	/** Return SUM(coefficients[i]*points[i]) **/
	static public Point_2 linearCombination_(Point_[] points, Number[] coefficients) {
		Point_2 res = new Point_2();
		res.linearCombination(points, coefficients);
		return res;
	}
	

	
	
	/** Return vector (this-b) **/
	public Vector_2 minus(Point_2 p) {
		return new Vector_2(data.x - p.data.x, data.y - p.data.y);
	}

	/** Return this+v **/
	public Point_2 plus(Vector_2 v) {
		return new Point_2(data.x + v.data.x, data.y + v.data.y);
	}
	/** Return this+v **/
	public Point_2 sum(Vector_2 v) {
		return new Point_2(data.x + v.data.x, data.y + v.data.y);
	}
	
	
	
	/** Return mid point of A & B (same type as A) **/
	public static Point_2 midPoint(Point_2 a, Point_2 b) {
		Point_2 r = a.copy(); r.data.add(b.data); r.data.divideBy(2);
		return r;
	}
	
	
	
	/** Return dimension **/
	public int dimension() { return 2;}
	
	/** Return the i-th coordinate **/
	public Double getCartesian(int i) { 
		return data.getCartesian(i); 
	}

}




