package Jcg.geometry;

/*
 * Class for representing a point in d-dimension space 
 */
public class Point_d implements Point_{

	public Double[] coordinates;

	//****************
	//  Constructors
	//****************

	/** Creates 0 point of d coordinates **/
	public Point_d(int d) {
		this.coordinates = new Double[d];
	}

	/** Creates point with coordinates given by coord **/
	public Point_d(double[] coord) { 
		this.coordinates=new Double[coord.length];
		for(int i=0;i<coord.length;i++)
			this.coordinates[i]=coord[i];
	}

	/** Creates a Point_d with current values of p coordinates **/
	public Point_d(Point_ p) { 
		this.coordinates = new Double[p.dimension()];
		for (int i=0;i<p.dimension();i++) this.coordinates[i]=p.getCartesian(i).doubleValue();
	}

	/** Creates the barycenter of points. The dimension is given by points[0]. **/
	public Point_d(Point_[] points) {
		int d = points[0].dimension();
		double[] x_ = new double[d];
		for (int i = 0; i < points.length; i++)
			for (int j = 0; j < d; j++)
				x_[j] += points[i].getCartesian(j).doubleValue();
		for (int j = 0; j < d; j++)
			this.coordinates[j] = x_[j]/points.length;
	}

	/** Return a copy of p (fields are not shared) **/
	public Point_d clone() { return new Point_d(this); }


	//*******************
	//  Global calculus 
	//*******************

	/** Return the isobarycenter of points. The dimension is given by points[0]. **/
	public Point_d barycenter_(Point_[] points) {
		return new Point_d(points);
	}

	/** Return the barycenter of points with masses given by coefficients. The dimension is given by points[0]. **/
	public Point_d barycenter_(Point_[] points, Number[] coefficients) {
		int d = points[0].dimension();
		double[] x_ = new double[d]; double w_ = 0.;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < d; j++) {
				x_[j] += points[i].getCartesian(j).doubleValue()*coefficients[i].doubleValue();
				w_ += coefficients[i].doubleValue();
			}
		}	
		for (int j = 0; j < d; j++) x_[j] /= w_;
		return new Point_d(x_);
	}

	/** Return SUM(coefficients[i]*points[i]). The dimension is given by points[0]. **/
	public Point_d linearCombination_(Point_[] points, Number[] coefficients) {
		int d = points[0].dimension();
		double[] x_ = new double[d];
		for (int i = 0; i < points.length; i++)
			for (int j = 0; j < d; j++)
				x_[j] += points[i].getCartesian(j).doubleValue()*coefficients[i].doubleValue();
		return new Point_d(x_);
	}

	/** Return (p+q)/2. The dimension is given by p. **/
	public static Point_d midPoint (Point_d p, Point_d q) {
		int d = p.dimension();
		double[] x_ = new double[d];
		for (int j = 0; j < d; j++) x_[j] = (p.coordinates[j]+q.coordinates[j])/2;
		return new Point_d(x_);
	}


	//******************
	//  Other Calculus
	//******************

	/** Return distance to p **/
	public Number distanceFrom(Point_ p) {
		double res = 0;
		for (int j = 0; j < coordinates.length; j++) {
			double dX = p.getCartesian(j).doubleValue() - coordinates[j];
			res += dX*dX;
		}
		return Math.sqrt(res);
	}

	/** Return square distance to p **/
	public Number squareDistance(Point_ p) {
		double res = 0;
		for (int j = 0; j < coordinates.length; j++) {
			double dX = p.getCartesian(j).doubleValue() - coordinates[j];
			res += dX*dX;
		}
		return res;
	}

	/** Return vector (this-b) **/
	public Vector_ minus(Point_ b) {
		double x_[] = new double[coordinates.length];
		for (int j = 0; j < coordinates.length; j++) x_[j] = coordinates[j].doubleValue() - b.getCartesian(j).doubleValue();
		return new Vector_d(x_);
	}

	/** Return this+v **/
	public Point_ plus (Vector_ v) {
		double x_[] = new double[coordinates.length];
		for (int j = 0; j < coordinates.length; j++) x_[j] = coordinates[j].doubleValue() + v.getCartesian(j).doubleValue();
		return new Point_d(x_);
	}



	//*************
	//  Modifiers
	//*************

	/** Set point to (0,0) **/
	public void setOrigin() {
		for (int j = 0; j < coordinates.length; j++) coordinates[j] = 0.;
	}

	/** Set coordinates to p coordinates **/
	public void set(Point_ p) {
		coordinates = new Double[p.dimension()];
		for (int j = 0; j < coordinates.length; j++) coordinates[j] = p.getCartesian(j).doubleValue();
	}

	/** Set coordinates to get the isobarycenter of points **/
	public void barycenter(Point_[] points) {
		set(barycenter_(points));
	}

	/** Set coordinates to get the barycenter of points with masses given by coefficients **/
	public void barycenter(Point_[] points, Number[] coefficients) {
		set(barycenter_(points,coefficients));
	}

	/** Set coordinates to get SUM(coefficients[i]*points[i]) **/
	public void linearCombination(Point_[] points, Number[] coefficients) {
		set(linearCombination_(points,coefficients));
	}

	/** Translate the point of vector v **/
	public void translateOf(Vector_ v) {
		for(int i=0;i<dimension();i++) coordinates[i] += v.getCartesian(i).doubleValue();
	}


	//*************
	//  Accessors
	//*************

	/** Get i-th coordinate **/
	public Number getCartesian(int i) {
		return this.coordinates[i];
	}
	
	/** Set i-th coordinate **/
	public void setCartesian(int i, Number x) {
		this.coordinates[i]=x.doubleValue();
	}

	public int dimension() { return this.coordinates.length;}


	//*********************

	public boolean equals(Object o) {
		Point_ p = (Point_) o;
		for(int i=0;i<dimension();i++) {
			if (! this.coordinates[i].equals(p.getCartesian(i)))
				return false;
		}
		return true;
	}

	public int hashCode () {
		double code = 0;
		for (int j = 0; j < coordinates.length; j++) {
			double c = 0;
			for (int k = j;  k < coordinates.length; k++) c += coordinates[j];
			code += c;
		}
		return (int) code;
	}

	public String toString() {
		String result="(";
		for(int i=0;i<dimension()-1;i++)
			result=result+this.getCartesian(i)+",";
		return result+this.getCartesian(dimension()-1)+")";
	}

	public int compareTo(Point_ o) {
		Point_d p = (Point_d) o;
		for (int j = 0; j < coordinates.length; j++) {
			if (coordinates[j] < p.coordinates[j]) return -1;
			if (coordinates[j] > p.coordinates[j]) return -1;			
		}
		return 0;
	}


}
