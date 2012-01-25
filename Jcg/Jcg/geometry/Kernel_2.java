package Jcg.geometry;

public class Kernel_2 extends KernelDbl {

	protected double x,y;
	
	/** Create (0,0) **/
	public Kernel_2() {
		this.x = 0.; this.y = 0.;
	}
	public static Kernel_2 constructor() { return new Kernel_2(); }

	/** Create (x,y) **/
	public Kernel_2(double x, double y) {
		this.x = x; this.y = y;
	}

	/** Create (x,y) when d = (x,y,...) **/
	public Kernel_2(Kernel d) {
		this.x = d.getCartesian(0).doubleValue(); this.y = d.getCartesian(1).doubleValue();
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
		return 0;
	}

	
	public boolean equals(Kernel d) {
		return d.dimension() == 2 && x == d.getCartesian(0).doubleValue() && y == d.getCartesian(1).doubleValue();
	}
	
	
	public Double getCartesian(int i) {
		return (i == 0) ? x : (i == 1) ? y : 0.;
	}
	
	public double getX() { return x; }
	public double getY() { return y; }

	
	public void setCartesian(int i, Number x) {
		switch(i) {
			case 0: this.x = x.doubleValue();
			case 1: this.y = x.doubleValue();
		}
	}
	
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	
	
	public int dimension() { return 2; }

	
	public void setOrigin() { x = 0; y = 0; }

	
	public void copy(Kernel d) {
		x = d.getCartesian(0).doubleValue();
		y = d.getCartesian(1).doubleValue();
	}

	
	public Kernel_2 copy() {
		return new Kernel_2(x,y);
	}

	
	public void add(Kernel d) {
		x += d.getCartesian(0).doubleValue();
		y += d.getCartesian(1).doubleValue();
	}

	
	public void take(Kernel d) {
		x -= d.getCartesian(0).doubleValue();
		y -= d.getCartesian(1).doubleValue();
	}

	
	public void multiplyBy(Number s) {
		x *= s.doubleValue();
		y *= s.doubleValue();
	}

	
	public void divideBy(Number s) {
		x /= s.doubleValue();
		y /= s.doubleValue();
	}

	
	public Kernel_2 plus(Kernel d) {
		return new Kernel_2(x + d.getCartesian(0).doubleValue(),
						 y + d.getCartesian(1).doubleValue());
	}

	
	public Kernel_2 minus(Kernel d) {
		return new Kernel_2(x - d.getCartesian(0).doubleValue(),
						 y - d.getCartesian(1).doubleValue());
	}

	
	public Kernel_2 multipliedBy(Number s) {
		return new Kernel_2(x * s.doubleValue(),
				 		 y * s.doubleValue());
	}

	
	public Kernel_2 dividedBy(Number s) {
		return new Kernel_2(x / s.doubleValue(),
		 		 		 y / s.doubleValue());
	}
	
	
	public Double innerProduct(Kernel d) {
		return x*d.getCartesian(0).doubleValue() + y*d.getCartesian(1).doubleValue();
	}
	
	public Double norm() {
		return Math.sqrt(x*x+y*y);
	}
	
	public Double norm2() {
		return (x*x+y*y);
	}
	

	public String toString() {
		return x +","+ y;
	}

}
