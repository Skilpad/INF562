package Jcg.geometry;

public class Point_2 implements Point_{

	public Double x,y;

	
	//****************
	//  Constructors
	//****************
	
	/** Creates (0,0) point **/
	public Point_2() { this.x = new Double(0); this.y = new Double(0); }

	/** Creates (x,y) point **/
	public Point_2(Number x,Number y) { 
		this.x = x.doubleValue(); 
		this.y = y.doubleValue();
	}

	/** Creates a point with current values of p coordinates **/
	public Point_2(Point_ p) { 
		this.x = p.getCartesian(0).doubleValue(); 
		this.y = p.getCartesian(1).doubleValue(); 
	}

	/** Creates the barycenter of points **/
	public Point_2(Point_[] points) {
		double x_ = 0., y_ = 0.;
		for (int i=0;i<points.length;i++) {
			x_ += points[i].getCartesian(0).doubleValue();
			y_ += points[i].getCartesian(1).doubleValue();
		}
		this.x = x_/points.length;
		this.y = y_/points.length;
	}
	
	/** Return a copy of p (fields are not shared) **/
	public Point_2 clone() { return new Point_2(this); }
		

	//*******************
	//  Global calculus 
	//*******************
	
	/** Return the isobarycenter of points **/
	public Point_2 barycenter_(Point_[] points) {
		return new Point_2(points);
	}

	/** Return the barycenter of points with masses given by coefficients **/
	public Point_2 barycenter_(Point_[] points, Number[] coefficients) {
		double x_ = 0., y_ = 0., w_ = 0.;
		for(int i=0;i<points.length;i++) {
			x_ += points[i].getCartesian(0).doubleValue()*coefficients[i].doubleValue();
			y_ += points[i].getCartesian(1).doubleValue()*coefficients[i].doubleValue();
			w_ += coefficients[i].doubleValue();
		}
		return new Point_2(x_/w_, y_/w_);
	}

	/** Return SUM(coefficients[i]*points[i]) **/
	public Point_2 linearCombination_(Point_[] points, Number[] coefficients) {
		double x_=0., y_=0.;
		for (int i=0;i<points.length;i++) {
			x_ += points[i].getCartesian(0).doubleValue()*coefficients[i].doubleValue();
			y_ += points[i].getCartesian(1).doubleValue()*coefficients[i].doubleValue();
		}
		return new Point_2(x_, y_);
	}
	
	/** Return (p+q)/2 **/
	public static Point_2 midPoint (Point_2 p, Point_2 q) {
		return new Point_2((p.x+q.x)/2.,(p.y+q.y)/2.);
	}


	//******************
	//  Other Calculus
	//******************
	
	/** Return distance to p **/
	public Number distanceFrom(Point_ p) {
		double dX = p.getCartesian(0).doubleValue()-x;
		double dY = p.getCartesian(1).doubleValue()-y;
		return Math.sqrt(dX*dX+dY*dY);
	}

	/** Return square distance to p **/
	public Number squareDistance(Point_ p) {
		double dX = p.getCartesian(0).doubleValue()-x;
		double dY = p.getCartesian(1).doubleValue()-y;
		return dX*dX+dY*dY;
	}
	
	/** Return vector (this-b) **/
	public Vector_ minus(Point_ b){
		return new Vector_2(x-b.getCartesian(0).doubleValue(), 
							y-b.getCartesian(1).doubleValue());
	}
	
	/** Return this+v **/
	public Point_2 sum(Vector_ v) {
		return new Point_2(this.x+v.getCartesian(0).doubleValue(),
						   this.y+v.getCartesian(1).doubleValue());  	
	}
	
	/** Return this+v **/
	public Point_ plus (Vector_ v) {
		return new Point_2(this.x + v.getCartesian(0).doubleValue(),
						   this.y + v.getCartesian(1).doubleValue());  	
	}


	
	//*************
	//  Modifiers
	//*************
	
	/** Set point to (0,0) **/
	public void setOrigin() {
		this.x=0.;
		this.y=0.;
	}

	/** Set coordinates to p coordinates **/
	public void set(Point_ p) {
		this.x = p.getCartesian(0).doubleValue();
		this.y = p.getCartesian(1).doubleValue();
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
		this.x += v.getCartesian(0).doubleValue();
		this.y += v.getCartesian(1).doubleValue();
	}

	
	//*************
	//  Accessors
	//*************
	
	/** Return x **/
	public Number getX() { return x; }

	/** Return y **/
	public Number getY() { return y; }
	
	/** Set X **/
	public void setX(Number x) {this.x=x.doubleValue(); }
	/** Set Y **/
	public void setY(Number y) {this.y=y.doubleValue(); }
	
	/** Get i-th coordinate **/
	public Number getCartesian(int i) {
		if (i==0) return x;
		return y;
	} 
	
	/** Set i-th coordinate **/
	public void setCartesian(int i, Number x) {
		if (i==0) this.x=x.doubleValue();
		else this.y=x.doubleValue();
	}

	public int dimension() { return 2;}
	
	
	//*********************

	public boolean equals(Object o) {
		if (o instanceof Point_) {
			Point_ p = (Point_) o;
			return this.x.equals(p.getCartesian(0)) && this.y.equals(p.getCartesian(1)); 
		}
		throw new RuntimeException ("Comparing Point_2 with object of type " + o.getClass());  	
	}

	public int hashCode () {
		return (int)(this.x*this.x + this.y);
	}

	public String toString() { return "("+x+","+y+")"; }

	public int compareTo(Point_ o) {
		Point_2 p = (Point_2) o;
		if(this.x<p.getX().doubleValue())
			return -1;
		if(this.x>p.getX().doubleValue())
			return 1;
		if(this.y<p.getY().doubleValue())
			return -1;
		if(this.y>p.getY().doubleValue())
			return 1;
		return 0;
	}

}




