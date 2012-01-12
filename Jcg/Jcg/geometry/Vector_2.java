package Jcg.geometry;

public class Vector_2 implements Vector_{

	public Double x,y;

	
	//****************
	//  Constructors
	//****************
	
	/** Create vector (0,0) **/
	public Vector_2() { this.x = 0.; this.y = 0.; }

	/** Create vector (x,y) **/
	public Vector_2(Number x,Number y) { 
		this.x = x.doubleValue(); 
		this.y = y.doubleValue();
	}

	/** Create vector AB ( <=> B.minus(A) ) **/
	public Vector_2(Point_2 a, Point_2 b) { 
		this.x = b.getX().doubleValue()-a.getX().doubleValue(); 
		this.y = b.getY().doubleValue()-a.getY().doubleValue(); 
	}

	
	//************
	//  Calculus 
	//************

	public Vector_2 sum(Vector_ v) {
		return new Vector_2(this.x+v.getCartesian(0).doubleValue(),
							this.y+v.getCartesian(1).doubleValue());  	
	}

	public Vector_2 minus(Vector_ v) {
		return new Vector_2(this.x-v.getCartesian(0).doubleValue(),
							this.y-v.getCartesian(1).doubleValue());  	
	}

	public Vector_2 difference(Vector_ v) {
		return new Vector_2(v.getCartesian(0).doubleValue()-this.x,
							v.getCartesian(1).doubleValue()-this.y);  	
	}

	public Vector_2 opposite() {
		return new Vector_2(-x,-y);  	
	}

	public Vector_2 divisionByScalar(Number s) {
		return new Vector_2(x/s.doubleValue(),y/s.doubleValue());  	
	}

	public Vector_2 multiplyByScalar(Number s) {
		return new Vector_2(x*s.doubleValue(),y*s.doubleValue());  	
	}

	public Number innerProduct(Vector_ v) {
		return this.x*v.getCartesian(0).doubleValue() + this.y*v.getCartesian(1).doubleValue();  	
	}

	public Number squaredLength() {
		return x*x+y*y;
	}
	
	public Number length() {
		return Math.sqrt(x*x+y*y);
	}

	public Vector_2 perpendicular(Orientation o) {
		return (o.isCounterclockwise()) ? (new Vector_2(-y,x)) : (new Vector_2(y,-x));
	}

	
	//**********
	//  Modify
	//**********
	
	public void add(Vector_ v) {
		x += v.getCartesian(0).doubleValue();
		y += v.getCartesian(1).doubleValue();
	}

	public void take(Vector_ v) {
		x -= v.getCartesian(0).doubleValue();
		y -= v.getCartesian(1).doubleValue();
	}

	public void multiplyBy(Number s) {
		x *= s.doubleValue();
		y *= s.doubleValue();		
	}

	public void divideBy(Number s) {
		x /= s.doubleValue();
		y /= s.doubleValue();		
	}

	
	
	
	
	//*************
	//  Accessors 
	//*************
	
	public Number getX() { return x; }
	public Number getY() { return y; }
	public void setX(Number x) { this.x=x.doubleValue(); }
	public void setY(Number y) { this.y=y.doubleValue(); }

	public int dimension() { return 2;}

	public Number getCartesian(int i) {
		if(i==0) return x.doubleValue();
		return y.doubleValue();
	} 
	public void setCartesian(int i, Number x) {
		if(i==0) this.x=x.doubleValue();
		else this.y=x.doubleValue();
	}

	
	//************
	
	public boolean equals(Vector_ v) { 
		return(this.x.equals(v.getCartesian(0).doubleValue()) 
				&& this.y.equals(v.getCartesian(1).doubleValue())); 
	}

	public String toString() {return "["+x+","+y+"]"; }



}




