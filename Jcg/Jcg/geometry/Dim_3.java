package Jcg.geometry;

public class Dim_3 extends Dim_2 implements DataDomain {

	protected double z;
	
	/** Create (0,0,0) **/
	public Dim_3() {
		this.x = 0.; this.y = 0.; this.z = 0;
	}

	/** Create (x,y,z) **/
	public Dim_3(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z;
	}

	/** Create (x,y,z) when d = (x,y,z,...) **/
	public Dim_3(DataDomain d) {
		this.x = d.getCartesian(0).doubleValue(); 
		this.y = d.getCartesian(1).doubleValue();
		this.z = d.getCartesian(2).doubleValue();
	}

	
	public int compareTo(DataDomain d) {
		if (this.x < d.getCartesian(0).doubleValue())
			return -1;
		if (this.x > d.getCartesian(0).doubleValue())
			return 1;
		if (this.y < d.getCartesian(1).doubleValue())
			return -1;
		if (this.y > d.getCartesian(1).doubleValue())
			return 1;
		if (this.z < d.getCartesian(2).doubleValue())
			return -1;
		if (this.z > d.getCartesian(2).doubleValue())
			return 1;
		return 0;
	}

	
	public boolean equals(DataDomain d) {
		return d.dimension() == 3
				&& x == d.getCartesian(0).doubleValue() 
				&& y == d.getCartesian(1).doubleValue()
				&& z == d.getCartesian(2).doubleValue();
	}
	
	
	public Number getCartesian(int i) {
		return (i == 0) ? x : (i == 1) ? y : (i == 2) ? z : 0.;
	}
	
	public double getZ() { return z; }

	
	public void setCartesian(int i, Number x) {
		switch(i) {
			case 0: this.x = x.doubleValue();
			case 1: this.y = x.doubleValue();
			case 2: this.z = x.doubleValue();
		}
	}
	
	public void setZ(double z) { this.z = z; }
	
	
	public int dimension() { return 3; }

	
	public void setOrigin() { x = 0; y = 0; z = 0; }

	
	public void copy(DataDomain d) {
		x = d.getCartesian(0).doubleValue();
		y = d.getCartesian(1).doubleValue();
		z = d.getCartesian(2).doubleValue();
	}

	
	public DataDomain copy() {
		return new Dim_3(x,y,z);
	}

	
	public void add(DataDomain d) {
		x += d.getCartesian(0).doubleValue();
		y += d.getCartesian(1).doubleValue();
		z += d.getCartesian(2).doubleValue();
	}

	
	public void take(DataDomain d) {
		x -= d.getCartesian(0).doubleValue();
		y -= d.getCartesian(1).doubleValue();
		z -= d.getCartesian(2).doubleValue();
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

	
	public DataDomain plus(DataDomain d) {
		return new Dim_3(x + d.getCartesian(0).doubleValue(),
						 y + d.getCartesian(1).doubleValue(),
						 z + d.getCartesian(2).doubleValue());
	}

	
	public DataDomain minus(DataDomain d) {
		return new Dim_3(x - d.getCartesian(0).doubleValue(),
						 y - d.getCartesian(1).doubleValue(),
						 z - d.getCartesian(2).doubleValue());
	}

	
	public DataDomain multipliedBy(Number s) {
		return new Dim_3(x * s.doubleValue(),
				 		 y * s.doubleValue(),
						 z * s.doubleValue());
	}

	
	public DataDomain dividedBy(Number s) {
		return new Dim_3(x / s.doubleValue(),
		 		 		 y / s.doubleValue(),
		 		 		 z / s.doubleValue());
	}

}
