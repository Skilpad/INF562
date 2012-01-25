package Jcg.geometry;

public class Kernel_3 extends Kernel_2 {

	protected double z;
	
	/** Create (0,0,0) **/
	public Kernel_3() {
		this.x = 0.; this.y = 0.; this.z = 0;
	}

	/** Create (x,y,z) **/
	public Kernel_3(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z;
	}

	/** Create (x,y,z) when d = (x,y,z,...) **/
	public Kernel_3(Kernel d) {
		this.x = d.getCartesian(0).doubleValue(); 
		this.y = d.getCartesian(1).doubleValue();
		this.z = d.getCartesian(2).doubleValue();
	}

	
	public int compareTo(Kernel d) {
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

	
	public boolean equals(Kernel d) {
		return d.dimension() == 3
				&& x == d.getCartesian(0).doubleValue() 
				&& y == d.getCartesian(1).doubleValue()
				&& z == d.getCartesian(2).doubleValue();
	}
	
	
	public Double getCartesian(int i) {
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

	
	public void copy(Kernel d) {
		x = d.getCartesian(0).doubleValue();
		y = d.getCartesian(1).doubleValue();
		z = d.getCartesian(2).doubleValue();
	}

	
	public Kernel_3 copy() {
		return new Kernel_3(x,y,z);
	}

	
	public void add(Kernel d) {
		x += d.getCartesian(0).doubleValue();
		y += d.getCartesian(1).doubleValue();
		z += d.getCartesian(2).doubleValue();
	}

	
	public void take(Kernel d) {
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

	
	public Kernel_3 plus(Kernel d) {
		return new Kernel_3(x + d.getCartesian(0).doubleValue(),
						 y + d.getCartesian(1).doubleValue(),
						 z + d.getCartesian(2).doubleValue());
	}

	
	public Kernel_3 minus(Kernel d) {
		return new Kernel_3(x - d.getCartesian(0).doubleValue(),
						 y - d.getCartesian(1).doubleValue(),
						 z - d.getCartesian(2).doubleValue());
	}

	
	public Kernel_3 multipliedBy(Number s) {
		return new Kernel_3(x * s.doubleValue(),
				 		 y * s.doubleValue(),
						 z * s.doubleValue());
	}

	
	public Kernel_3 dividedBy(Number s) {
		return new Kernel_3(x / s.doubleValue(),
		 		 		 y / s.doubleValue(),
		 		 		 z / s.doubleValue());
	}


	public Double innerProduct(Kernel d) {
		return x*d.getCartesian(0).doubleValue() + y*d.getCartesian(1).doubleValue() + z*getCartesian(2).doubleValue();
	}
	
	public Double norm() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public Double norm2() {
		return (x*x+y*y+z*z);
	}
	

	public String toString() {
		return x +","+ y+","+z;
	}

}
