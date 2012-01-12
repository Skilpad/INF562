package Jcg.geometry;

public class Vector_3 implements Vector_ {

	public Double x,y,z;


	//****************
	//  Constructors
	//****************

	/** Create vector (0,0,0) **/
	public Vector_3() { this.x = 0.; this.y = 0.; this.z = 0.; }

	/** Create vector (x,y,z) **/
	public Vector_3(Number x, Number y, Number z) { 
		this.x = x.doubleValue(); 
		this.y = y.doubleValue();
		this.z = z.doubleValue();
	}

	/** Create vector AB ( <=> B.minus(A) ) **/
	public Vector_3(Point_3 a, Point_3 b) { 
		this.x = b.getX().doubleValue()-a.getX().doubleValue(); 
		this.y = b.getY().doubleValue()-a.getY().doubleValue(); 
		this.z = b.getZ().doubleValue()-a.getZ().doubleValue(); 
	}


	//************
	//  Calculus 
	//************

	public Vector_3 sum(Vector_ v) {
		return new Vector_3(this.x+v.getCartesian(0).doubleValue(),
							this.y+v.getCartesian(1).doubleValue(),  	
							this.z+v.getCartesian(2).doubleValue());  	
	}

	public Vector_3 minus(Vector_ v) {
		return new Vector_3(this.x-v.getCartesian(0).doubleValue(),
							this.y-v.getCartesian(1).doubleValue(),  	
							this.z-v.getCartesian(2).doubleValue());  	
	}

	public Vector_3 difference(Vector_ v) {
		return new Vector_3(v.getCartesian(0).doubleValue()-this.x,
							v.getCartesian(1).doubleValue()-this.y,  	
							v.getCartesian(2).doubleValue()-this.z);  	
	}

	public Vector_3 opposite() {
		return new Vector_3(-x,-y,-z); 
	}

	public Vector_3 divisionByScalar(Number s) {
		return new Vector_3(x/s.doubleValue(),y/s.doubleValue(),z/s.doubleValue());  	
	}

	public Vector_3 multiplyByScalar(Number s) {
		return new Vector_3(x*s.doubleValue(),y*s.doubleValue(),z*s.doubleValue());  	
	}

	public Number innerProduct(Vector_ v) {
		return this.x*v.getCartesian(0).doubleValue() + this.y*v.getCartesian(1).doubleValue() + this.z*v.getCartesian(2).doubleValue();  	
	}

	public Number squaredLength() {
		return x*x+y*y+z*z;
	}

	public Number length() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public Vector_3 crossProduct(Vector_ b) {
		return 
				new Vector_3(y*b.getCartesian(2).doubleValue()-z*b.getCartesian(1).doubleValue(),
						z*b.getCartesian(0).doubleValue()-x*b.getCartesian(2).doubleValue(),
						x*b.getCartesian(1).doubleValue()-y*b.getCartesian(0).doubleValue());
	}



	//**********
	//  Modify
	//**********

	public void add(Vector_ v) {
		x += v.getCartesian(0).doubleValue();
		y += v.getCartesian(1).doubleValue();
		z += v.getCartesian(2).doubleValue();
	}

	public void take(Vector_ v) {
		x -= v.getCartesian(0).doubleValue();
		y -= v.getCartesian(1).doubleValue();
		z -= v.getCartesian(2).doubleValue();
	}

	public void multiplyBy(Number s) {
		x *= s.doubleValue();
		y *= s.doubleValue();		
		z *= s.doubleValue();		
	}

	public void divideBy(Number s) {
		x /= s.doubleValue();
		y /= s.doubleValue();		
		z /= s.doubleValue();		
	}





	//*************
	//  Accessors 
	//*************

	public Number getX() { return x; }
	public Number getY() { return y; }
	public Number getZ() { return z; }
	public void setX(Number x) { this.x=x.doubleValue(); }
	public void setY(Number y) { this.y=y.doubleValue(); }
	public void setZ(Number z) { this.z=z.doubleValue(); }

	public int dimension() { return 3;}

	public Number getCartesian(int i) {
		if(i==0) return x;
		else if(i==1) return y;
		else return z;
	} 

	public void setCartesian(int i, Number x) {
		if(i==0) this.x=x.doubleValue();
		else if(i==1) this.y=x.doubleValue();
		else this.z=x.doubleValue();
	}



	//************

	public boolean equals(Vector_ v) { 
		return(this.x==v.getCartesian(0).doubleValue() 
				&& this.y==v.getCartesian(1).doubleValue() 
				&& this.z==v.getCartesian(2).doubleValue()); 
	}

	public String toString() {return "["+x+","+y+","+z+"]"; }


}




