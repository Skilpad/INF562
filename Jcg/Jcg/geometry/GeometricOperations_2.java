package Jcg.geometry;

import java.math.BigDecimal;
import java.util.LinkedList;

import Jcg.Fenetre;

public class GeometricOperations_2 {

	private static final double epsilon2 = 1e-15;
	private static final double epsilon3 = 1e-12;

	/**
	 * Returns the square of the distance between two points
	 *
	 * @param p1,p2 the two points
	 * @return dist the square of the distance
	 */
	//    public static double squaredistance(Point p1, Point p2) {
	//	return (p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y);
	//    }

	/**
	 * Returns the (approximate) circumcenter of input triangle
	 *
	 * @param p0,p1,p2 the three vertices of the triangle
	 *
	 */
	public static Point_2 circumCenter (Point_2 p0, Point_2 p1, Point_2 p2) {
		double ex=p1.x()-p0.x(), ey=p1.y()-p0.y();
		double nx=p2.y()-p1.y(), ny=p1.x()-p2.x();
		double dx=(p0.x()-p2.x())*0.5, dy=(p0.y()-p2.y())*0.5;
		double s=(ex*dx+ey*dy)/(ex*nx+ey*ny);
		double cx=(p1.x()+p2.x())*0.5+s*nx;
		double cy=(p1.y()+p2.y())*0.5+s*ny;
		return new Point_2( cx, cy );
	}

	/**
	 * Returns the distance (non exact computation) of a point to a segment
	 *
	 * @param ps,pe the start/end of the segment
	 * @param p the query point
	 * @return the distance of p to [ps,pe]
	 */
	public static Number distanceToSegment(Point_2 ps, Point_2 pe, Point_2 p) {
		if (ps.x()==pe.x() && ps.y()==pe.y()) return ps.squareDistance(p);

		double sx=pe.x()-ps.x();
		double sy=pe.y()-ps.y();

		double ux=p.x()-ps.x();
		double uy=p.y()-ps.y();

		double dp=sx*ux+sy*uy;
		if (dp<0) return ps.squareDistance(p);

		double sn = sx*sx+sy*sy;
		if (dp>sn) return pe.squareDistance(p);

		double ratio = (double)dp/sn;
		double projx = (ps.x() + ratio*sx);
		double projy = (ps.y() + ratio*sy);

		return p.squareDistance(new Point_2(projx,projy));
	}


	/** Returns true if a, b and c turn in counter-clockwise direction
	 *
	 * @param a,b,c the 3 points to test
	 * @return true if a, b and c turn in counter-clockwise direction
	 *
	 * Test is filtered: if precision bound epsilon is reached, then
	 * perform computation with infinite precision arithmetic (using
	 * the java.util.BigDecimal class)
	 */
	public static boolean isCounterClockwise(Point_2 a, Point_2 b, Point_2 c) {
		double det = Algebra.det33(new double[] {a.x(), a.y(), 1, b.x(), b.y(), 1, c.x(), c.y(), 1});
		if (det > epsilon2)
			return true;
		else if (det < -epsilon2)
			return false;

		// else perform exact computation
		BigDecimal ax = BigDecimal.valueOf(a.x());
		BigDecimal ay = BigDecimal.valueOf(a.y());
		BigDecimal bx = BigDecimal.valueOf(b.x());
		BigDecimal by = BigDecimal.valueOf(b.y());
		BigDecimal cx = BigDecimal.valueOf(c.x());
		BigDecimal cy = BigDecimal.valueOf(c.y());

		return AlgebraExact.det33 (new BigDecimal[] {ax, ay, BigDecimal.valueOf(1),
				bx, by, BigDecimal.valueOf(1), 
				cx, cy, BigDecimal.valueOf(1)}).compareTo(BigDecimal.ZERO) > 0;
	}

	/** Returns true if a, b and c turn in Counter Clockwise direction
	 *
	 * @param a,b,c the 3 points to test
	 * @return true if a, b and c turn in Counter Clockwise direction
	 *
	 * Test is filtered: if precision bound epsilon is reached, then
	 * perform computation with infinite precision arithmetic (using
	 * the java.util.BigDecimal class)
	 */
	public static boolean collinear(Point_2 a, Point_2 b, Point_2 c) {
		double det = Algebra.det33(new double[] {a.x(), a.y(), 1, b.x(), b.y(), 1, c.x(), c.y(), 1});
		if (Math.abs(det) > epsilon2)
			return false;

		// else perform exact computation
		BigDecimal ax = BigDecimal.valueOf(a.x());
		BigDecimal ay = BigDecimal.valueOf(a.y());
		BigDecimal bx = BigDecimal.valueOf(b.x());
		BigDecimal by = BigDecimal.valueOf(b.y());
		BigDecimal cx = BigDecimal.valueOf(c.x());
		BigDecimal cy = BigDecimal.valueOf(c.y());

		return AlgebraExact.det33 (new BigDecimal[] {ax, ay, BigDecimal.valueOf(1),
				bx, by, BigDecimal.valueOf(1), 
				cx, cy, BigDecimal.valueOf(1)}).compareTo(BigDecimal.ZERO) == 0;
	}

	/**
	 * Tests if point p lies inside the circumcircle of triangle a,b,c
	 *
	 * @param a,b,c triangle
	 * @param p point to test
	 * @return  true/false
	 *
	 * Test is filtered: if precision bound epsilon is reached, then
	 * perform computation with infinite precision arithmetic (using
	 * the java.util.BigDecimal class)
	 */
	public static boolean inCircle(Point_2 p, Point_2 a, Point_2 b, Point_2 c) {
		double a2 = a.x()*a.x() + a.y()*a.y();
		double b2 = b.x()*b.x() + b.y()*b.y();
		double c2 = c.x()*c.x() + c.y()*c.y();
		double p2 = p.x()*p.x() + p.y()*p.y();
		double det44 = Algebra.det44 (new double[] {a.x(), a.y(), a2, 1, b.x(), b.y(), b2, 1, c.x(), c.y(), c2, 1, p.x(), p.y(), p2, 1});

		if (det44 < -epsilon3) 
			return true;
		else if (det44 > epsilon3)
			return false;

		// else use exact computation
		BigDecimal ax = BigDecimal.valueOf(a.x());
		BigDecimal ay = BigDecimal.valueOf(a.y());
		BigDecimal bx = BigDecimal.valueOf(b.x());
		BigDecimal by = BigDecimal.valueOf(b.y());
		BigDecimal cx = BigDecimal.valueOf(c.x());
		BigDecimal cy = BigDecimal.valueOf(c.y());
		BigDecimal px = BigDecimal.valueOf(p.x());
		BigDecimal py = BigDecimal.valueOf(p.y());
		BigDecimal ba2 = ax.multiply(ax).add(ay.multiply(ay));
		BigDecimal bb2 = bx.multiply(bx).add(by.multiply(by));
		BigDecimal bc2 = cx.multiply(cx).add(cy.multiply(cy));
		BigDecimal bp2 = px.multiply(px).add(py.multiply(py));
		return AlgebraExact.det44(new BigDecimal[] {ax, ay, ba2, BigDecimal.valueOf(1),
				bx, by, bb2, BigDecimal.valueOf(1),
				cx, cy, bc2, BigDecimal.valueOf(1),
				px, py, bp2, BigDecimal.valueOf(1)}).compareTo(BigDecimal.ZERO) <= 0;
	}


	/**
	 * Returns true if segments s and t intersect
	 * @param s,t the 2 segments
	 * @return true if s,t intersect each other
	 *
	 * Test is filtered: if precision bound epsilon is reached, then
	 * perform computation with infinite precision arithmetic (using
	 * the java.util.BigDecimal class)
	 */
	public static boolean doIntersect(Segment_2 s, Segment_2 t) {
		return 
				isCounterClockwise((Point_2)s.source, (Point_2)t.source, (Point_2)t.target) !=
				isCounterClockwise((Point_2)s.target, (Point_2)t.source, (Point_2)t.target) &&
				isCounterClockwise((Point_2)t.source, (Point_2)s.source, (Point_2)s.target) !=
				isCounterClockwise((Point_2)t.target, (Point_2)s.source, (Point_2)s.target);
	}


	/** Returns true if segment s and ray r intersect
	 *
	 * @param s the segment
	 * @param r the ray
	 *
	 * @return true if s intersects r
	 *
	 * Test is not filtered.
	 */
	public static boolean doIntersect(Segment_2 s, Ray_2 r) {
		Vector_2 pa = new Vector_2((Point_2)r.source(), (Point_2)s.source);
		Vector_2 pb = new Vector_2((Point_2)r.source(), (Point_2)s.target);
		double sqNorm = 1+Math.max(pa.squaredLength().doubleValue(), 
				pb.squaredLength().doubleValue());
		return doIntersect(s, new Segment_2
				(r.source(), 
						r.source().sum(r.direction().multiplyByScalar(sqNorm))));
	}


	/** 
	 * Returns approximate intersection between segments
	 * 
	 * @param s,t the 2 segments
	 * @return approximate intersection point of s,t
	 *
	 */
	public static Point_2 intersect(Segment_2 s, Segment_2 t) {
		double ax = ((Point_2)s.source).getX().doubleValue();  
		double ay = ((Point_2)s.source).getY().doubleValue();  
		double bx = ((Point_2)s.target).getX().doubleValue();  
		double by = ((Point_2)s.target).getY().doubleValue();  
		double cx = ((Point_2)t.source).getX().doubleValue();  
		double cy = ((Point_2)t.source).getY().doubleValue();  
		double dx = ((Point_2)t.target).getX().doubleValue();  
		double dy = ((Point_2)t.target).getY().doubleValue();  
		double det = Algebra.det22(bx-ax, by-ay, dx-cx, dy-cy);
		double alpha = Algebra.det22(dx-cx, dy-cy, dx-bx, dy-by) / det;
		return new Point_2 (alpha*ax + (1-alpha)*bx, alpha*ay + (1-alpha)*by);
	}

	/** 
	 * Returns approximate intersection between segment s and ray r
	 *
	 * @param s the segment
	 * @param r the ray
	 *
	 * @return the intersection of s and r
	 *
	 */
	public static Point_2 intersect(Segment_2 s, Ray_2 r) {
		Vector_2 pa = new Vector_2((Point_2)r.source(), (Point_2)s.source);
		Vector_2 pb = new Vector_2((Point_2)r.source(), (Point_2)s.target);
		double sqNorm = 1+Math.max(pa.squaredLength().doubleValue(), 
				pb.squaredLength().doubleValue());
		return intersect(s, new Segment_2
				(r.source(), 
						r.source().sum(r.direction().multiplyByScalar(sqNorm))));
	}

	/** 
	 * Returns true if point p lies on segment ab
	 *
	 * @param a,b,p the 3 points
	 * @return true if ab contains point p
	 */    
	public static boolean liesOn(Point_2 p, Point_2 a,Point_2 b) {    
		/* If ab not vertical, check betweenness on x; else on y. */
		if(collinear(p,a,b)==false)
			return false;
		if ( a.x() != b.x() )
			return ((a.x() <= p.x()) && (p.x() <= b.x())) ||
					((a.x() >= p.x()) && (p.x() >= b.x()));
		else
			return ((a.y() <= p.y()) && (p.y() <= b.y())) ||
					((a.y() >= p.y()) && (p.y() >= b.y()));
	}


	static public void main(String[] args){
		LinkedList<Point_2> intersections = new LinkedList<Point_2> ();
		LinkedList<Point_2[]> segments = new LinkedList<Point_2[]> ();
		for (int i=0; i< 1;) {
			Point_2 p = new Point_2(10*Math.random(), 10*Math.random());
			Point_2 q = new Point_2(10*Math.random(), 10*Math.random());
			Point_2 a = new Point_2(p.getX().doubleValue()+1e-1*(0.5-Math.random()), 
					p.getY().doubleValue()+1e-1*(0.5-Math.random()));
			Point_2 b = new Point_2(q.getX().doubleValue()+1e-1*(0.5-Math.random()), 
					q.getY().doubleValue()+1e-1*(0.5-Math.random()));
			Segment_2 s = new Segment_2 (p, q);
			Ray_2 r = new Ray_2 (a, (Vector_2)(b.minus(a)));
			if (doIntersect (s,r)) {
				segments.add(new Point_2[]{p,q});
				segments.add(new Point_2[]{a,b});
				intersections.add(intersect (s,r));
				i++;
			}
		}
		Fenetre f = new Fenetre();
		f.addSegments(segments);
		f.addPoints (intersections);
	}

}



