package Jcg.geometry;

public class Vector_2 extends Vector_<Dim_2>{

	
	/** Return x **/
	public Number getX() { return data.x; }
	/** Return y **/
	public Number getY() { return data.y; }
	
	/** Set X **/
	public void setX(Number x) { data.x = x.doubleValue(); }
	/** Set Y **/
	public void setY(Number y) { data.y = y.doubleValue(); }

	/** Return x **/
	public double x() { return data.x; }
	/** Return y **/
	public double y() { return data.y; }
	
	/** Set X **/
	public void x(double x) { data.x = x; }
	/** Set Y **/
	public void y(double y) { data.y = y; }

	
	//****************
	//  Constructors
	//****************
	
	/** Create vector (0,0) **/
	public Vector_2() { this.data = new Dim_2(); }

	/** Create vector (x,y) **/
	public Vector_2(Number x,Number y) { this.data = new Dim_2(x.doubleValue(),y.doubleValue()); }

	/** Create vector from->to ( <=> to.minus(from) ) **/
	public Vector_2(Point_2 from, Point_2 to) { this.data = to.data.minus(from.data); }

	/** Create a copy **/
	public Vector_2(Vector_2 v) { this.data = v.data.copy(); }

	
	
	//************
	//  Calculus 
	//************
	
	/** Return orthogonal vector **/
	public Vector_2 perpendicular(Orientation o) {
		return (o.isCounterclockwise()) 
				? (new Vector_2(-data.y,-data.x)) 
				: (new Vector_2( data.y,-data.x));
	}
	
	/** Return cross product this x v **/
	public Number crossProduct(Vector_2 v) {
		return data.x * v.data.y - data.y * v.data.x;
	}
	
	
	/** Return a copy **/
	public Vector_2 copy() {
		return new Vector_2(this);
	}
	
	/** Return this+v **/
	public Vector_2 sum(Vector_2 v) {
		return new Vector_2(data.x + v.data.x, data.y + v.data.y);
	}
	/** Return this+v **/
	public Vector_2 plus(Vector_2 v) {
		return new Vector_2(data.x + v.data.x, data.y + v.data.y);
	}
	/** Return this-v **/
	public Vector_2 minus(Vector_2 v) {
		return new Vector_2(data.x - v.data.x, data.y - v.data.y);
	}
	/** Return v-this **/
	public Vector_2 difference(Vector_2 v) {
		return new Vector_2(v.data.x - data.x, v.data.y - data.y);
	}
	/** Return -this **/
	public Vector_2 opposite() {
		return new Vector_2(-data.x, -data.y);
	}
	/** Return this/s **/
	public Vector_2 divisionByScalar(Number s) {
		return new Vector_2(data.x/s.doubleValue(), data.y/s.doubleValue());
	}
	/** Return this*s **/
	public Vector_2 multiplyByScalar(Number s) {
		return new Vector_2(data.x*s.doubleValue(), data.y*s.doubleValue());
	}
	/** Return the normalized vector **/
	public Vector_2 normalized() {
		return divisionByScalar(data.norm());
	}

	
}




