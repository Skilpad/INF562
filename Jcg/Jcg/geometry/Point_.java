package Jcg.geometry;

public interface Point_ extends Comparable<Point_>{
	
	public Number getCartesian(int i);  
	public void setCartesian(int i, Number x);
	
	/** Translate the point of vector v **/
	public void translateOf(Vector_ p);
	/** Return vector (this-b) **/
	public Vector_ minus(Point_ p);
	/** Return this+v **/
	public Point_ plus(Vector_ v);
	
	// Modifiers
	/** Set point to 0 point **/
	public void setOrigin();
	/** Set coordinates to p coordinates **/
	public void set(Point_ p);
	/** Set coordinates to get the isobarycenter of points **/
	public void barycenter(Point_ [] points);
	/** Set coordinates to get the barycenter of points with masses given by coefficients **/
	public void barycenter(Point_ [] points, Number[] coefficients);
	/** Set coordinates to get SUM(coefficients[i]*points[i]) **/
	public void linearCombination(Point_[] points, Number[] coefficients);
	
	// Global calculus
	/** Return a copy of p (fields are not shared) **/
	public Point_ copy();
	/** Return the isobarycenter of points **/
	public Point_ barycenter_(Point_ [] points);
	/** Return the barycenter of points with masses given by coefficients **/
	public Point_ barycenter_(Point_ [] points, Number[] coefficients);
	/** Return SUM(coefficients[i]*points[i]) **/
	public Point_ linearCombination_(Point_[] points, Number[] coefficients);
	
	
	
	// Other calculus
	/** Return distance to p **/
	public Number distanceFrom(Point_ p);
	/** Return square distance to p **/
	public Number squareDistance(Point_ p);
	
	
	public int dimension();
	public String toString();
	
}
