package Jcg.geometry;

public interface DataDomain extends Comparable<DataDomain> {
	
	/** Return the i-th coordinate **/
	public Number getCartesian(int i);  
	/** Set the i-th coordinate **/
	public void setCartesian(int i, Number x);
	/** Return the dimension of the data **/	
	public int dimension();
	/** Return a string representation of the data.
	 *  For example, (1,2,7) will be represented by string "1,2,7". **/
	public String toString();
	/** Test if equal. To be equal, DataDomains must have the same dimension. **/
	public boolean equals(DataDomain d);
	
	/** Set to 0 **/
	public void setOrigin();
	/** Set values to those of d. d must have the same dimension. **/
	public void copy(DataDomain d);
	/** Return an independent copy. **/
	public DataDomain copy();
	
	/** this += d **/
	public void add(DataDomain d);
	/** this -= d **/
	public void take(DataDomain d);
	/** this *= s **/
	public void multiplyBy(Number s);
	/** this /= s **/
	public void divideBy(Number s);
		
	/** Return this + d **/
	public DataDomain plus(DataDomain d);
	/** Return this -= d **/
	public DataDomain minus(DataDomain d);
	/** Return this * s **/
	public DataDomain multipliedBy(Number s);
	/** Return this / s **/
	public DataDomain dividedBy(Number s);
	
}


class Orientation {
	
	static int CLOCKWISE=1;
	static int COUNTERCLOCKWISE=-1;
	static int COLLINEAR=0;
	
	private int or;
	
	public Orientation(int or) {
		this.or = or;
	}
	
	public boolean isClockwise() {
		return this.or == CLOCKWISE;
	}

	public boolean isCounterclockwise() {
		return this.or == COUNTERCLOCKWISE;
	}

	public boolean isCollinear() {
		return this.or == COLLINEAR;
	}
	
}

