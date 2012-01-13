package Jcg.geometry;

public class Point_3 implements Point_ {
	
	public Double x,y,z;

	//****************
	//  Constructors
	//****************
	
	/** Creates (0,0) point **/
	public Point_3() { this.x = new Double(0); this.y = new Double(0); this.z = new Double(0); }

	/** Creates (x,y) point **/
	public Point_3(Number x,Number y,Number z) { 
		this.x = x.doubleValue(); 
		this.y = y.doubleValue();
		this.z = z.doubleValue();
	}

	/** Creates a point with current values of p coordinates **/
	public Point_3(Point_ p) { 
		this.x=p.getCartesian(0).doubleValue(); 
		this.y=p.getCartesian(1).doubleValue(); 
		this.z=p.getCartesian(2).doubleValue();
	}

	/** Creates the barycenter of points **/
	public Point_3(Point_[] points) {
		double x_ = 0., y_ = 0., z_ = 0.;
		for (int i=0;i<points.length;i++) {
			x_ += points[i].getCartesian(0).doubleValue();
			y_ += points[i].getCartesian(1).doubleValue();
			z_ += points[i].getCartesian(2).doubleValue();
		}
		this.x = x_/points.length;
		this.y = y_/points.length;
		this.z = z_/points.length;
	}
	
	/** Return a copy of p (fields are not shared) **/
	public Point_3 copy() { return new Point_3(this); }
		

	//*******************
	//  Global calculus 
	//*******************

	/** Return the isobarycenter of points **/
	public Point_3 barycenter_(Point_[] points) {
		return new Point_3(points);
	}

	/** Return the barycenter of points with masses given by coefficients **/
	public Point_3 barycenter_(Point_[] points, Number[] coefficients) {
		double x_ = 0., y_ = 0., z_ = 0., w_ = 0.;
		for(int i=0;i<points.length;i++) {
			x_ += points[i].getCartesian(0).doubleValue()*coefficients[i].doubleValue();
			y_ += points[i].getCartesian(1).doubleValue()*coefficients[i].doubleValue();
			z_ += points[i].getCartesian(2).doubleValue()*coefficients[i].doubleValue();
			w_ += coefficients[i].doubleValue();
		}
		return new Point_3(x_/w_, y_/w_, z_/w_);
	}

	/** Return SUM(coefficients[i]*points[i]) **/
	public Point_3 linearCombination_(Point_[] points, Number[] coefficients) {
		double x_ = 0., y_ = 0., z_ = 0.;
		for (int i=0;i<points.length;i++) {
			x_ += points[i].getCartesian(0).doubleValue()*coefficients[i].doubleValue();
			y_ += points[i].getCartesian(1).doubleValue()*coefficients[i].doubleValue();
			z_ += points[i].getCartesian(2).doubleValue()*coefficients[i].doubleValue();
		}
		return new Point_3(x_, y_, z_);
	}
	
	/** Return (p+q)/2 **/
	public static Point_3 midPoint (Point_3 p, Point_3 q) {
		return new Point_3((p.x+q.x)/2.,(p.y+q.y)/2.,(p.z+q.z)/2.);
	}


	//******************
	//  Other Calculus
	//******************
	
	/** Return distance to p **/
	public Number distanceFrom(Point_ p) {
		double dX = p.getCartesian(0).doubleValue()-x;
		double dY = p.getCartesian(1).doubleValue()-y;
		double dZ = p.getCartesian(2).doubleValue()-z;		
		return Math.sqrt(dX*dX+dY*dY+dZ*dZ);
	}

	/** Return square distance to p **/
	public Number squareDistance(Point_ p) {
		double dX = p.getCartesian(0).doubleValue()-x;
		double dY = p.getCartesian(1).doubleValue()-y;
		double dZ = p.getCartesian(2).doubleValue()-z;		
		return dX*dX+dY*dY+dZ*dZ;
	}
	
	/** Return vector (this-b) **/
	public Vector_ minus(Point_ b){
		return new Vector_3(x-b.getCartesian(0).doubleValue(), 
							y-b.getCartesian(1).doubleValue(),
							z-b.getCartesian(2).doubleValue());
	}
	
	/** Return this+v **/
	public Point_ plus (Vector_ v) {
		return new Point_3(this.x + v.getCartesian(0).doubleValue(),
						   this.y + v.getCartesian(1).doubleValue(),  	
						   this.z + v.getCartesian(2).doubleValue());  	
	}
	

	//*************
	//  Modifiers
	//*************
	
	/** Set point to (0,0) **/
	public void setOrigin() {
		this.x=0.;
		this.y=0.;
		this.z=0.;
	}

	/** Set coordinates to p coordinates **/
	public void set(Point_ p) {
		this.x = p.getCartesian(0).doubleValue();
		this.y = p.getCartesian(1).doubleValue();
		this.z = p.getCartesian(2).doubleValue();
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
		this.z += v.getCartesian(2).doubleValue();
	}
	
	public void multiply (Number n) {
		this.x*=n.doubleValue();
		this.y*=n.doubleValue();
		this.z*=n.doubleValue();
	}

	

	//*************
	//  Accessors
	//*************
	
	/** Return x **/
	public Number getX() { return x; }
	/** Return y **/
	public Number getY() { return y; }
	/** Return z **/
	public Number getZ() { return z; }

	/** Set X **/
	public void setX(Number x) {this.x = x.doubleValue(); }
	/** Set Y **/
	public void setY(Number y) {this.y = y.doubleValue(); }
	/** Set Z **/
	public void setZ(Number z) {this.z = z.doubleValue(); }
	
	/** Get i-th coordinate **/
	public Number getCartesian(int i) {
		if(i==0) return x;
		else if(i==1) return y;
		return z;
	} 
	
	/** Set i-th coordinate **/
	public void setCartesian(int i,Number x) {
		if(i==0) this.x=x.doubleValue();
		else if(i==1) this.y=y.doubleValue();
		else this.z=x.doubleValue();
	}

	public int dimension() { return 3;}
	
	
	//*********************

	public boolean equals(Object o) { 
		Point_ p = (Point_) o;
		return this.x.equals(p.getCartesian(0)) && this.y.equals(p.getCartesian(1)) && 
				this.z.equals(p.getCartesian(2)); 
	}

	public int hashCode () {
		double code = getX().doubleValue();
		code = code * code * code;
		code += getY().doubleValue()*getY().doubleValue();
		code += getZ().doubleValue();
		return (int)code;
	}

	public String toString() {return "("+x+","+y+","+z+")"; }

	public int compareTo(Point_ o) {
		Point_3 p = (Point_3) o;
		if (this.getX().doubleValue() < p.getX().doubleValue())
			return -1;
		if (this.getX().doubleValue() > p.getX().doubleValue())
			return 1;
		if (this.getY().doubleValue() < p.getY().doubleValue())
			return -1;
		if (this.getY().doubleValue() > p.getY().doubleValue())
			return 1;
		if (this.getZ().doubleValue() < p.getZ().doubleValue())
			return -1;
		if (this.getZ().doubleValue() > p.getZ().doubleValue())
			return 1;
		return 0;
	}


}




