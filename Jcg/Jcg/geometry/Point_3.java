package Jcg.geometry;

public class Point_3 extends Point_<Dim_3> {
	
	/** Return x **/
	public Number getX() { return data.getX(); }
	/** Return y **/
	public Number getY() { return data.getY(); }
	/** Return z **/
	public Number getZ() { return data.getZ(); }
	
	/** Set X **/
	public void setX(Number x) { data.setX(x.doubleValue()); }
	/** Set Y **/
	public void setY(Number y) { data.setY(y.doubleValue()); }
	/** Set Y **/
	public void setZ(Number z) { data.setZ(z.doubleValue()); }

	/** Return x **/
	public double x() { return data.getX(); }
	/** Return y **/
	public double y() { return data.getY(); }
	/** Return z **/
	public double z() { return data.getZ(); }
	
	/** Set X **/
	public void x(double x) { data.setX(x); }
	/** Set Y **/
	public void y(double y) { data.setY(y); }
	/** Set Y **/
	public void z(double z) { data.setZ(z); }

	
	//****************
	//  Constructors
	//****************
	
	/** Creates (0,0,0) point **/
	public Point_3() { this.data = new Dim_3(); }

	/** Creates (x,y) point **/
	public Point_3(Number x,Number y,Number z) { this.data = new Dim_3(x.doubleValue(),y.doubleValue(),z.doubleValue()); }

	/** Creates a point with current values of p coordinates **/
	public Point_3(Point_ p) { this.data = new Dim_3(p.data); }

	/** Creates the barycenter of points **/
	public Point_3(Point_[] points) {
		this.data = new Dim_3();
		this.barycenter(points);
	}
	
	/** Return a copy of p (fields are not shared) **/
	public Point_3 copy() { return new Point_3(this); }
	
	
	//*******************
	//  Global calculus 
	//*******************
	
	/** Return the isobarycenter of points **/
	static public Point_3 barycenter_(Point_[] points) {
		Point_3 res = new Point_3();
		res.barycenter(points);
		return res;
	}

	/** Return the barycenter of points with masses given by coefficients **/
	static public Point_3 barycenter_(Point_[] points, Number[] coefficients) {
		Point_3 res = new Point_3();
		res.barycenter(points, coefficients);
		return res;
	}

	/** Return SUM(coefficients[i]*points[i]) **/
	static public Point_3 linearCombination_(Point_[] points, Number[] coefficients) {
		Point_3 res = new Point_3();
		res.linearCombination(points, coefficients);
		return res;
	}

	
	
	
	
	
	
	/** Return vector (this-b) **/
	public Vector_3 minus(Point_3 p) {
		return new Vector_3(data.x - p.data.x, data.y - p.data.y, data.z - p.data.z);
	}

	/** Return this+v **/
	public Point_3 plus(Vector_3 v) {
		return new Point_3(data.x + v.data.x, data.y + v.data.y, data.z + v.data.z);
	}
	/** Return this+v **/
	public Point_3 sum(Vector_3 v) {
		return new Point_3(data.x + v.data.x, data.y + v.data.y, data.z + v.data.z);
	}


	
	/** Return dimension **/
	public int dimension() { return 2;}
	
	
	

}




