package Jcg.geometry;

/*
 * Class for representing a point in d-dimension space 
 */
public class Point_d extends Point_<Kernel_d>{
	
	//****************
	//  Constructors
	//****************

	/** Creates 0 point of d coordinates **/
	public Point_d(int d) { this.data = new Kernel_d(d); }

	/** Creates point with coordinates given by coord **/
	public Point_d(double[] coord) { this.data = new Kernel_d(coord); }

	/** Creates a Point_d with current values of p coordinates **/
	public Point_d(Point_ p) { this.data = new Kernel_d(p.data); }

	/** Creates the barycenter of points. The dimension is given by points[0]. **/
	public Point_d(Point_[] points) {
		this.data = new Kernel_d(points[0].dimension());
		this.barycenter(points);
	}

	/** Return a copy of p (fields are not shared) **/
	public Point_d copy() { return new Point_d(this); }


	//*******************
	//  Global calculus 
	//*******************

	/** Return the isobarycenter of points. The dimension is given by points[0]. **/
	static public Point_d barycenter_(Point_[] points) {
		Point_d res = new Point_d(points[0].dimension());
		res.barycenter(points);
		return res;
	}

	/** Return the barycenter of points with masses given by coefficients. The dimension is given by points[0]. **/
	static public Point_d barycenter_(Point_[] points, Number[] coefficients) {
		Point_d res = new Point_d(points[0].dimension());
		res.barycenter(points, coefficients);
		return res;
	}

	/** Return SUM(coefficients[i]*points[i]). The dimension is given by points[0]. **/
	static public Point_d linearCombination_(Point_[] points, Number[] coefficients) {
		Point_d res = new Point_d(points[0].dimension());
		res.linearCombination(points, coefficients);
		return res;
	}
	
	
	/** Return vector (this-b) **/
	public Vector_d minus(Point_d p) {
		return new Vector_d(this, p);
	}

	/** Return this+v **/
	public Point_d plus(Vector_d v) {
		Point_d p = new Point_d(0); p.data = data.plus(v.getData()); return p;
	}
	/** Return this+v **/
	public Point_d sum(Vector_d v) {
		Point_d p = new Point_d(0); p.data = data.plus(v.getData()); return p;
	}
	
	
	/** Return the i-th coordinate **/
	public Double getCartesian(int i) { 
		return data.getCartesian(i); 
	}

	

}
