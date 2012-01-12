package Jcg.geometry;

public class Vector_d implements Vector_{

	public Double[] coordinates;

	
	//****************
	//  Constructors
	//****************
	
	/** Create vector 0 of d coordinates **/
	public Vector_d(int d) { coordinates = new Double[d]; for (int c = 0; c < dimension(); c++) coordinates[c] = 0.; }

	/** Create vector with coordinates given by coord **/
	public Vector_d(double[] coord) { 
		coordinates = new Double[coord.length];
		for (int c = 0; c < dimension(); c++) coordinates[c] = coord[c];
	}

	/** Create vector AB ( <=> B.minus(A) ) **/
	public Vector_d(Point_d a, Point_d b) { 
		coordinates = new Double[a.dimension()];
		for (int c = 0; c < dimension(); c++) coordinates[c] = b.getCartesian(c).doubleValue() - a.getCartesian(c).doubleValue();
	}

	
	//************
	//  Calculus 
	//************

	public Vector_d sum(Vector_ v) {
		double r[] = new double[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) r[i] = coordinates[i]+v.getCartesian(i).doubleValue();
		return new Vector_d(r);
	}

	public Vector_d minus(Vector_ v) {
		double r[] = new double[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) r[i] = coordinates[i]-v.getCartesian(i).doubleValue();
		return new Vector_d(r);
	}

	public Vector_d difference(Vector_ v) {
		double r[] = new double[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) r[i] = v.getCartesian(i).doubleValue()-coordinates[i];
		return new Vector_d(r);
	}

	public Vector_d opposite() {
		double r[] = new double[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) r[i] = -coordinates[i];
		return new Vector_d(r);
	}

	public Vector_d divisionByScalar(Number s) {
		double r[] = new double[coordinates.length];
		double s_ = s.doubleValue();
		for (int i = 0; i < coordinates.length; i++) r[i] = coordinates[i]/s_;
		return new Vector_d(r);
	}

	public Vector_d multiplyByScalar(Number s) {
		double r[] = new double[coordinates.length];
		double s_ = s.doubleValue();
		for (int i = 0; i < coordinates.length; i++) r[i] = s_*coordinates[i];
		return new Vector_d(r);
	}

	public Number innerProduct(Vector_ v) {
		double r = 0;
		for (int i = 0; i < coordinates.length; i++) r += coordinates[i]*v.getCartesian(i).doubleValue();
		return r;
	}

	public Number squaredLength() {
		double r = 0;
		for (int i = 0; i < coordinates.length; i++) r += coordinates[i]*coordinates[i];
		return r;
	}
	
	public Number length() {
		double r = 0;
		for (int i = 0; i < coordinates.length; i++) r += coordinates[i]*coordinates[i];
		return Math.sqrt(r);
	}

	
	//**********
	//  Modify
	//**********
	
	public void add(Vector_ v) {
		for (int i = 0; i < coordinates.length; i++) coordinates[i] += v.getCartesian(i).doubleValue();
	}

	public void take(Vector_ v) {
		for (int i = 0; i < coordinates.length; i++) coordinates[i] -= v.getCartesian(i).doubleValue();
	}

	public void multiplyBy(Number s) {
		for (int i = 0; i < coordinates.length; i++) coordinates[i] *= s.doubleValue();
	}

	public void divideBy(Number s) {
		for (int i = 0; i < coordinates.length; i++) coordinates[i] /= s.doubleValue();
	}

	
	
	
	
	//*************
	//  Accessors 
	//*************
	
	public int dimension() { return coordinates.length;}

	public Number getCartesian(int i) {
		return this.coordinates[i];
	} 
	public void setCartesian(int i, Number x) {
		this.coordinates[i]=x.doubleValue();
	}

	
	//************
	
	public boolean equals(Object o) { 
		Vector_ v = (Vector_) o;
		for(int i=0;i<dimension();i++) {
			if (! this.coordinates[i].equals(v.getCartesian(i)))
				return false;
		}
		return true;
	}

	public String toString() {
		String result="[";
		for(int i=0;i<dimension()-1;i++)
			result=result+this.getCartesian(i)+",";
		return result+this.getCartesian(dimension()-1)+"]";
	}



}




