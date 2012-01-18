package Jcg.geometry;

public class Kernel_d extends Kernel {

	protected double[] x;
	
	/** Create data with 0 coordinates **/
	public Kernel_d() {
		this.x = new double[0];
	}
	public static Kernel_d constructor() { return new Kernel_d(); }

	/** Create data with dim coordinates set to 0 **/
	public Kernel_d(int dim) {
		this.x = new double[dim];
	}

	/** Create data with coordinates given by x **/
	public Kernel_d(double[] x) {
		this.x = new double[x.length];
		for (int i = 0; i < x.length; i++) this.x[i] = x[i];
	}

	/** Create a copy of d **/
	public Kernel_d(Kernel d) {
		this.x = new double[d.dimension()];
		for (int i = 0; i < this.x.length; i++) this.x[i] = d.getCartesian(i).doubleValue();
	}
	
	public int compareTo(Kernel d) {
		int n = d.dimension();
		if (x.length < n) n = x.length;
		for (int i = 0; i < n; i++) {
			if (x[i] < d.getCartesian(i).doubleValue()) return -1;
			if (x[i] > d.getCartesian(i).doubleValue()) return  1;
		}
		return 0;
	}

	public Double getCartesian(int i) {
		if (i < 0 || i >= x.length) return 0.;
		return x[i];
	}

	public void setCartesian(int i, Number x) {
		if (i >= 0 && i < this.x.length) this.x[i] = x.doubleValue();
	}

	public int dimension() { return x.length; }

	public boolean equals(Kernel d) {
		if (x.length != d.dimension()) return false;
		for (int i = 0; i < x.length; i++) {
			if (x[i] != d.getCartesian(i).doubleValue()) return false;
		}
		return true;
	}

	public void setOrigin() {
		for (int i = 0; i < x.length; i++) x[i] = 0;
	}

	public void copy(Kernel d) {
		x = new double[d.dimension()];
		for (int i = 0; i < x.length; i++) x[i] = d.getCartesian(i).doubleValue();
	}

	public Kernel_d copy() {
		return new Kernel_d(this);
	}

	public void add(Kernel d) {
		for (int i = 0; i < x.length; i++) x[i] += d.getCartesian(i).doubleValue();
	}

	public void take(Kernel d) {
		for (int i = 0; i < x.length; i++) x[i] -= d.getCartesian(i).doubleValue();
	}

	public void multiplyBy(Number s) {
		for (int i = 0; i < x.length; i++) x[i] *= s.doubleValue();
	}

	public void divideBy(Number s) {
		for (int i = 0; i < x.length; i++) x[i] /= s.doubleValue();
	}

	public Kernel_d plus(Kernel d) {
		Kernel_d r = new Kernel_d(this);
		r.add(d);
		return r;
	}

	public Kernel_d minus(Kernel d) {
		Kernel_d r = new Kernel_d(this);
		r.take(d);
		return r;
	}

	public Kernel_d multipliedBy(Number s) {
		Kernel_d r = new Kernel_d(this);
		r.multiplyBy(s);
		return r;
	}

	public Kernel_d dividedBy(Number s) {
		Kernel_d r = new Kernel_d(this);
		r.divideBy(s);
		return r;
	}

	
	public Double innerProduct(Kernel d) {
		double r = 0;
		for (int i = 0; i < x.length; i++) r += x[i]*d.getCartesian(i).doubleValue();
		return r;
	}
	
	public Double norm() {
		double r = 0;
		for (int i = 0; i < x.length; i++) r += x[i]*x[i];
		return Math.sqrt(r);
	}

	public Double norm2() {
		double r = 0;
		for (int i = 0; i < x.length; i++) r += x[i]*x[i];
		return r;
	}


	public String toString() {
		if (x.length == 0) return "";
		String r = ""+x[0];
		for (int i = 1; i < x.length; i++) r += ","+x[i];
		return r;
	}

}
