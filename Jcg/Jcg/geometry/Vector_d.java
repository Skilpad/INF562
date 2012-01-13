package Jcg.geometry;

public class Vector_d extends Vector_<Dim_d>{


	
	//****************
	//  Constructors
	//****************
	
	/** Create vector 0 of d coordinates **/
	public Vector_d(int d) { data = new Dim_d(d); }

	/** Create vector with coordinates given by coord **/
	public Vector_d(double[] coord) { data = new Dim_d(coord); }

	/** Create vector from->to ( <=> to.minus(from) ) **/
	public Vector_d(Point_d from, Point_d to) { this.data = to.data.minus(from.data); }
	
	/** Create a copy **/
	public Vector_d(Vector_d v) { this.data = v.data.copy(); }

	
	
	
	/** Return a copy **/
	public Vector_d copy() {
		return new Vector_d(this);
	}
	
	/** Return this+v **/
	public Vector_d sum(Vector_d v) {
		Vector_d r = this.copy(); r.add(v); return r;
	}
	/** Return this+v **/
	public Vector_d plus(Vector_d v) {
		Vector_d r = this.copy(); r.add(v); return r;
	}
	/** Return this-v **/
	public Vector_d minus(Vector_d v) {
		Vector_d r = this.copy(); r.take(v); return r;
	}
	/** Return v-this **/
	public Vector_d difference(Vector_d v) {
		Vector_d r = v.copy(); r.take(this); return r;
	}
	/** Return -this **/
	public Vector_d opposite() {
		Vector_d r = this.copy(); r.multiplyBy(-1); return r;
	}
	/** Return this/s **/
	public Vector_d divisionByScalar(Number s) {
		Vector_d r = this.copy(); r.divideBy(s); return r;
	}
	/** Return this*s **/
	public Vector_d multiplyByScalar(Number s) {
		Vector_d r = this.copy(); r.multiplyBy(s); return r;
	}
	/** Return the normalized vector **/
	public Vector_d normalized() {
		return divisionByScalar(data.norm());
	}

	
}




