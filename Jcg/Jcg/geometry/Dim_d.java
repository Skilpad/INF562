package Jcg.geometry;

public class Dim_d implements DataDomain {

	protected double[] x;
	
	/** Create data with 0 coordinates **/
	public Dim_d() {
		this.x = new double[0];
	}

	/** Create data with dim coordinates set to 0 **/
	public Dim_d(int dim) {
		this.x = new double[dim];
	}

	/** Create data with coordinates given by x **/
	public Dim_d(double[] x) {
		this.x = new double[x.length];
		for (int i = 0; i < x.length; i++) this.x[i] = x[i];
	}

	/** Create a copy of d **/
	public Dim_d(DataDomain d) {
		this.x = new double[d.dimension()];
		for (int i = 0; i < this.x.length; i++) this.x[i] = d.getCartesian(1).doubleValue();
	}
	
	public int compareTo(DataDomain d) {
		int n = d.dimension();
		if (x.length < n) n = x.length;
		for (int i = 0; i < n; i++) {
			if (x[i] < d.getCartesian(i).doubleValue()) return -1;
			if (x[i] > d.getCartesian(i).doubleValue()) return  1;
		}
		return 0;
	}

	public Number getCartesian(int i) {
		if (i < 0 || i >= x.length) return 0;
		return x[i];
	}

	public void setCartesian(int i, Number x) {
		if (i >= 0 && i < this.x.length) this.x[i] = x.doubleValue();
	}

	public int dimension() { return x.length; }

	public boolean equals(DataDomain d) {
		if (x.length != d.dimension()) return false;
		for (int i = 0; i < x.length; i++) {
			if (x[i] != d.getCartesian(i).doubleValue()) return false;
		}
		return true;
	}

	public void setOrigin() {
		for (int i = 0; i < x.length; i++) x[i] = 0;
	}

	public void copy(DataDomain d) {
		x = new double[d.dimension()];
		for (int i = 0; i < x.length; i++) x[i] = d.getCartesian(i).doubleValue();
	}

	public DataDomain copy() {
		return new Dim_d(this);
	}

	public void add(DataDomain d) {
		for (int i = 0; i < x.length; i++) x[i] += d.getCartesian(i).doubleValue();
	}

	public void take(DataDomain d) {
		for (int i = 0; i < x.length; i++) x[i] -= d.getCartesian(i).doubleValue();
	}

	public void multiplyBy(Number s) {
		for (int i = 0; i < x.length; i++) x[i] *= s.doubleValue();
	}

	public void divideBy(Number s) {
		for (int i = 0; i < x.length; i++) x[i] /= s.doubleValue();
	}

	public DataDomain plus(DataDomain d) {
		Dim_d r = new Dim_d(this);
		r.add(d);
		return r;
	}

	public DataDomain minus(DataDomain d) {
		Dim_d r = new Dim_d(this);
		r.take(d);
		return r;
	}

	public DataDomain multipliedBy(Number s) {
		Dim_d r = new Dim_d(this);
		r.multiplyBy(s);
		return r;
	}

	public DataDomain dividedBy(Number s) {
		Dim_d r = new Dim_d(this);
		r.divideBy(s);
		return r;
	}

}
