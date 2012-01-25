package Jcg.geometry;

// TODO: Check that REAL subclasses (Point_2, Point_3, Point_d) return Double instead of Number
// TODO: subclass for all REAL subclasses?
//       > Rename THIS class Point<X> and name real points class Point_<X>?

public class Point_<X extends Kernel> implements Comparable<Point_> {
	
	protected X data;
	
	public Point_() { this.data = (X) new Kernel0(); }

	/** Create a copy of p **/
	public Point_(Point_<X> p) { this.data = (X) p.data.copy(); }
	
	
	@Deprecated
	/** !!! DO NOT USE !!! 
	 *  Unsafe for work. Designed for package implementation. **/
	public Point_(X d) { this.data = d; }
	
	@Deprecated
	/** !!! DO NOT USE !!! 
	 *  Unsafe for work. Designed for package implementation. **/
	public Kernel getData() { return data; }
	
	/** Return the i-th coordinate **/
	public Number getCartesian(int i) { 
		return data.getCartesian(i); 
	}
	
	/** Set the i-th coordinate **/
	public void setCartesian(int i, Number x) { 
		data.setCartesian(i, x); 
	}
	
	/** Translate the point of vector v **/
	public void translateOf(Vector_ p) { 
		data.add(p.getData());
	}
	
	
	/** Return vector (this-b) **/
	public Vector_ minus(Point_ p) {
		return new Vector_(data.minus(p.data));
	}

	/** Return this+v **/
	public Point_<X> plus(Vector_ v) {
		return new Point_(data.plus(v.getData()));
	}
	/** Return this+v **/
	public Point_<X> sum(Vector_ v) {
		return new Point_(data.plus(v.getData()));
	}
	
	
	/** Return distance to p **/
	public Number distanceFrom(Point_ p) {
		return data.minus(p.data).norm();
	}
	
	/** Return square distance to p **/
	public Number squareDistance(Point_ p) {
		return data.minus(p.data).norm2();
	}
	
	
	/** Return mid point of A & B (same type as A) **/
	public static Point_ midPoint(Point_ a, Point_ b) {
		Point_ r = a.copy(); r.data.add(b.data); r.data.divideBy(2);
		return r;
	}
	
	
	/** Set point to 0 point **/
	public void setOrigin() {
		data.setOrigin();
	}
	
	/** Set coordinates to p coordinates **/
	public void set(Point_ p) {
		data.copy(p.data);
	}
	
	/** Set coordinates to get the isobarycenter of points **/
	public void barycenter(Point_[] points) {
		data.setOrigin();
		for (Point_ p : points) data.add(p.data);
		data.divideBy(points.length);
	}
	
	/** Set coordinates to get the barycenter of points with masses given by coefficients **/
	public void barycenter(Point_[] points, Number[] coefficients) {
		data.setOrigin();
		double w = 0.;
		for (int i = 0; i < points.length; i++)  {
			data.add(points[i].data.multipliedBy(coefficients[i]));
			w += coefficients[i].doubleValue();
		}
		data.divideBy(w);
	}
	
	/** Set coordinates to get SUM(coefficients[i]*points[i]) **/
	public void linearCombination(Point_[] points, Number[] coefficients) {
		data.setOrigin();
		for (int i = 0; i < points.length; i++)
			data.add(points[i].data.multipliedBy(coefficients[i]));
	}
	
	/** Set coordinates to random values such as min.getCartesian(i).doubleValue() <= this.getCartesian(i).doubleValue() < max.getCartesian(i).doubleValue() **/
	public void randomize(Point_<X> min, Point_<X> max) {
		int d = dimension();
		for (int i = 0; i < d; i++) {
			double xmin = min.getCartesian(i).doubleValue();
			this.setCartesian(i, xmin + Math.random()*(max.getCartesian(i).doubleValue()-xmin));
		}
	}
	
	
	/** Return a copy of p (fields are not shared) **/
	public Point_<X> copy() {
		return new Point_<X>(this);
	}
		
	
	/** Return dimension **/
	public int dimension() { return data.dimension(); }
	
	
	
	public String toString() { return "("+data.toString()+")"; }

	public int compareTo(Point_ o) {
		return data.compareTo(o.data);
	}

	public int hashCode () {
		double code = 0;
		int d = dimension();
		for (int j = 0; j < d; j++) {
			double c = 1;
			double c_ = getCartesian(j).doubleValue();
			for (int k = j;  k < d; k++) c *= c_;
			code += c;
		}
		return (int) code;
	}

}

