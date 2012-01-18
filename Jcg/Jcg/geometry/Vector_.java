package Jcg.geometry;

public class Vector_<X extends Kernel>  {

	protected X data;
	
	
	public Vector_() { this.data = (X) X.constructor(); }

	/** Create a copy of p **/
	public Vector_(Vector_<X> p) { this.data = (X) p.data.copy(); }
	
	/** Create vector from->to ( <=> to.minus(from) ) **/
	public Vector_(Point_<X> from, Point_<X> to) { this.data = (X) to.data.minus(from.data); }
	
	
	
	@Deprecated
	/** !!! DO NOT USE !!! 
	 *  Unsafe for work. Designed for package implementation. **/
	public Vector_(X d) { this.data = d; }
	
	@Deprecated
	/** !!! DO NOT USE !!! 
	 *  Unsafe for work. Designed for package implementation. **/
	public Kernel getData() { return data; }
	
	/** Get i-th coordinates **/
	public Number getCartesian(int i) {
		return data.getCartesian(i); 
	}
	/** Set i-th coordinates **/
	public void setCartesian(int i, Number x) {
		data.setCartesian(i, x);
	}

	/** Tests if colinear **/
	public boolean colinearTo(Vector_<X> v) {
		throw new Error("To complete");   // TODO
	}
	
	public boolean equals(Object v) {
		return data.equals(v);
	}

	/** Return this+v **/
	public Vector_ sum(Vector_ v) {
		return new Vector_(data.plus(v.data));
	}
	/** Return this+v **/
	public Vector_ plus(Vector_ v) {
		return new Vector_(data.plus(v.data));
	}
	/** Return this-v **/
	public Vector_ minus(Vector_ v) {
		return new Vector_(data.plus(v.data));
	}
	/** Return v-this **/
	public Vector_ difference(Vector_ v) {
		return new Vector_(v.data.minus(data));
	}
	/** Return -this **/
	public Vector_ opposite() {
		return new Vector_(data.multipliedBy(-1));
	}
	/** Return this/s **/
	public Vector_ divisionByScalar(Number s) {
		return new Vector_(data.dividedBy(s));
	}
	/** Return this*s **/
	public Vector_ multiplyByScalar(Number s) {
		return new Vector_(data.multipliedBy(s));
	}
	/** Return the normalized vector **/
	public Vector_ normalized() {
		return new Vector_(data.dividedBy(data.norm()));
	}
	
	/** Return the inner product of this & v **/
	public Number innerProduct(Vector_ v) {
		return data.innerProduct(v.data);
	}
	/** Return squared length **/
	public Number squaredLength() {
		return data.norm2();
	}
	/** Return squared length **/
	public Number length() {
		return data.norm();
	}
	
	/** this += v **/
	public void add(Vector_ v) {
		data.add(v.data);
	}
	/** this -= v **/
	public void take(Vector_ v) {
		data.take(v.data);
	}
	/** this *= s **/
	public void multiplyBy(Number s) {
		data.multiplyBy(s);
	}
	/** this /= s **/
	public void divideBy(Number s) {
		data.divideBy(s);
	}
	/** Normalize the vector **/
	public void normalize() {
		data.divideBy(data.norm());
	}
	
	public int dimension() {
		return data.dimension();
	}
	
	public String toString() {
		return "[" + data.toString() + "]";
	}
	
	/** Return a copy (fields are not shared) **/
	public Vector_<X> copy() {
		return new Vector_<X>(this);
	}

	
	
}
