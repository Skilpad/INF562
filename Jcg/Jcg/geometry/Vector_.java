package Jcg.geometry;

public interface Vector_ {

	/** Get i-th coordinates **/
	public Number getCartesian(int i);  
	/** Set i-th coordinates **/
	public void setCartesian(int i, Number x);

	public boolean equals(Object v);

	/** Return this+v **/
	public Vector_ sum(Vector_ v);
	/** Return this-v **/
	public Vector_ minus(Vector_ v);
	/** Return v-this **/
	public Vector_ difference(Vector_ v);
	/** Return -this **/
	public Vector_ opposite();
	/** Return this/s **/
	public Vector_ divisionByScalar(Number s);
	/** Return this*s **/
	public Vector_ multiplyByScalar(Number s);
	
	/** Return the inner product of this & v **/
	public Number innerProduct(Vector_ v);
	/** Return squared length **/
	public Number squaredLength();
	/** Return squared length **/
	public Number length();
	
	/** this += v **/
	public void add(Vector_ v);	
	/** this -= v **/
	public void take(Vector_ v);
	/** this *= s **/
	public void multiplyBy(Number s);
	/** this /= s **/
	public void divideBy(Number s);	
	
	public int dimension();
	public String toString();
}
