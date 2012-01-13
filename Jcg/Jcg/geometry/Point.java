package Jcg.geometry;

public class Point<X extends DataDomain> implements Point_ {

	X data;
	
	public int compareTo(Point_ p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Number getCartesian(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCartesian(int i, Number x) {
		// TODO Auto-generated method stub

	}

	@Override
	public void translateOf(Vector_ p) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector_ minus(Point_ p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point_ plus(Vector_ v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOrigin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void set(Point_ p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void barycenter(Point_[] points) {
		// TODO Auto-generated method stub

	}

	@Override
	public void barycenter(Point_[] points, Number[] coefficients) {
		// TODO Auto-generated method stub

	}

	@Override
	public void linearCombination(Point_[] points, Number[] coefficients) {
		// TODO Auto-generated method stub

	}

	@Override
	public Point_ barycenter_(Point_[] points) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point_ barycenter_(Point_[] points, Number[] coefficients) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point_ linearCombination_(Point_[] points, Number[] coefficients) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Number distanceFrom(Point_ p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Number squareDistance(Point_ p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int dimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point_ copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
