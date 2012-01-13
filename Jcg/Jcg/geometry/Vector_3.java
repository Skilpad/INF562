package Jcg.geometry;

public class Vector_3 extends Vector_<Dim_3> {
	
	
	
	/** Return x **/
	public Number getX() { return data.x; }
	/** Return y **/
	public Number getY() { return data.y; }
	/** Return y **/
	public Number getZ() { return data.z; }
	
	/** Set X **/
	public void setX(Number x) { data.x = x.doubleValue(); }
	/** Set Y **/
	public void setY(Number y) { data.y = y.doubleValue(); }
	/** Set Y **/
	public void setZ(Number z) { data.z = z.doubleValue(); }

	/** Return x **/
	public double x() { return data.x; }
	/** Return y **/
	public double y() { return data.y; }
	/** Return y **/
	public double z() { return data.z; }
	
	/** Set X **/
	public void x(double x) { data.x = x; }
	/** Set Y **/
	public void y(double y) { data.y = y; }
	/** Set Y **/
	public void z(double z) { data.z = z; }

	
	//****************
	//  Constructors
	//****************
	
	/** Create vector (0,0) **/
	public Vector_3() { this.data = new Dim_3(); }

	/** Create vector (x,y) **/
	public Vector_3(Number x,Number y,Number z) { this.data = new Dim_3(x.doubleValue(),y.doubleValue(),z.doubleValue()); }

	/** Create vector from->to ( <=> to.minus(from) ) **/
	public Vector_3(Point_3 from, Point_3 to) { this.data = to.data.minus(from.data); }
	
	/** Create a copy **/
	public Vector_3(Vector_3 v) { this.data = v.data.copy(); }

	
	//************
	//  Calculus 
	//************

	/** Return cross porduct this x v **/
	public Vector_3 crossProduct(Vector_3 v) {
		return new Vector_3(data.y * v.data.z - data.z * v.data.y,
							data.z * v.data.x - data.x * v.data.z,
							data.x * v.data.y - data.y * v.data.x);
	}
	
	
	
	
	
	/** Return a copy **/
	public Vector_3 copy() {
		return new Vector_3(this);
	}
	
	/** Return this+v **/
	public Vector_3 sum(Vector_3 v) {
		return new Vector_3(data.x + v.data.x, data.y + v.data.y, data.z + v.data.z);
	}
	/** Return this+v **/
	public Vector_3 plus(Vector_3 v) {
		return new Vector_3(data.x + v.data.x, data.y + v.data.y, data.z + v.data.z);
	}
	/** Return this-v **/
	public Vector_3 minus(Vector_3 v) {
		return new Vector_3(data.x - v.data.x, data.y - v.data.y, data.z - v.data.z);
	}
	/** Return v-this **/
	public Vector_3 difference(Vector_3 v) {
		return new Vector_3(v.data.x - data.x, v.data.y - data.y, v.data.z - data.z);
	}
	/** Return -this **/
	public Vector_3 opposite() {
		return new Vector_3(-data.x, -data.y, -data.z);
	}
	/** Return this/s **/
	public Vector_3 divisionByScalar(Number s) {
		return new Vector_3(data.x/s.doubleValue(), data.y/s.doubleValue(), data.z/s.doubleValue());
	}
	/** Return this*s **/
	public Vector_3 multiplyByScalar(Number s) {
		return new Vector_3(data.x*s.doubleValue(), data.y*s.doubleValue(), data.z*s.doubleValue());
	}
	/** Return the normalized vector **/
	public Vector_3 normalized() {
		return divisionByScalar(data.norm());
	}

	
}




