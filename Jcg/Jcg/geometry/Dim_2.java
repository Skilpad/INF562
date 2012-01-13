package Jcg.geometry;

public class Dim_2 implements DataDomain {

	protected double x,y;
	
	/** Create (0,0) **/
	public Dim_2() {
		this.x = 0.; this.y = 0.;
	}

	/** Create (x,y) **/
	public Dim_2(double x, double y) {
		this.x = x; this.y = y;
	}

	/** Create (x,y) when d = (x,y,...) **/
	public Dim_2(DataDomain d) {
		this.x = d.getCartesian(0).doubleValue(); this.y = d.getCartesian(1).doubleValue();
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
		return 0;
	}

	
	public boolean equals(DataDomain d) {
		return d.dimension() == 2 && x == d.getCartesian(0).doubleValue() && y == d.getCartesian(1).doubleValue();
	}
	
	
	public Number getCartesian(int i) {
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

	
	public void copy(DataDomain d) {
		x = d.getCartesian(0).doubleValue();
		y = d.getCartesian(1).doubleValue();
	}

	
	public DataDomain copy() {
		return new Dim_2(x,y);
	}

	
	public void add(DataDomain d) {
		x += d.getCartesian(0).doubleValue();
		y += d.getCartesian(1).doubleValue();
	}

	
	public void take(DataDomain d) {
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

	
	public DataDomain plus(DataDomain d) {
		return new Dim_2(x + d.getCartesian(0).doubleValue(),
						 y + d.getCartesian(1).doubleValue());
	}

	
	public DataDomain minus(DataDomain d) {
		return new Dim_2(x - d.getCartesian(0).doubleValue(),
						 y - d.getCartesian(1).doubleValue());
	}

	
	public DataDomain multipliedBy(Number s) {
		return new Dim_2(x * s.doubleValue(),
				 		 y * s.doubleValue());
	}

	
	public DataDomain dividedBy(Number s) {
		return new Dim_2(x / s.doubleValue(),
		 		 		 y / s.doubleValue());
	}

}
