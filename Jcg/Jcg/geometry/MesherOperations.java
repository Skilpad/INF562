package Jcg.geometry;

    
public class MesherOperations {

    /* The following geometric operations are for the mesher */

    /** 
     * Tests whether point p lies in the diametral circle of edge e
     */
    public static boolean encroachesEdge (Point_2 p, Point_2[] e) {
		if (e.length != 2) {
		    System.out.println ("Index problem in encroachesEdge()");
		    System.exit (-1);
		}
		Point_2 a = e[0], b = e[1];
		Point_2 m = new Point_2 ((a.x+b.x)/2, (a.y+b.y)/2);
		Point_2 c = new Point_2 (m.x + (b.x-a.x)/2, m.y + (a.y-b.y)/2);
		if (GeometricOperations_2.isCounterClockwise (a,b,c))
		    return  GeometricOperations_2.inCircle (a,b,c,p);
		else
		    return  GeometricOperations_2.inCircle (a,c,b,p);
    }

    /**
     * Computes the points of the bisector of edge e that lie on 
     * the unit circle, and returns the one closest to e
     */
    public static Point_2 midpointOnUnitCircle (Point_2[] e) {
	if (e.length != 2) {
	    System.out.println ("Index problem in midpointOnUnitCircle()");
	    System.exit (-1);
	}
	Point_2 p = e[0], q = e[1];
	Point_2 m = new Point_2 ((p.x+q.x)/2, (p.y+q.y)/2);
	double dist = Math.sqrt (p.squareDistance(q).doubleValue());
	Point_2 a = new Point_2 (m.x + 3*(p.y-q.y)/dist, 
			     m.y + 3*(q.x-p.x)/dist);  
	Point_2 b = new Point_2 (m.x + 3*(q.y-p.y)/dist, 
			     m.y + 3*(p.x-q.x)/dist);  
	Point_2[] sols = intersectSegmentWithCircle (a, b, new Point_2 (0,0), 1);
	if (sols[0] == null || sols[1] == null) {
	    System.out.println("Pb with size of segment/circle intersection:");
	    System.out.println("[" + a + "," + b + "] -> " 
			       + sols[0] + " and " + sols[1]);
	    System.exit (-1);
	}
	if (p.squareDistance(sols[0]).doubleValue() < p.squareDistance(sols[1]).doubleValue())
	    return sols[0];
	else
	    return sols[1];
    }

    /**
     * This function is used by midpointOnUnitCircle(); it returns the 
     * intersection points between segment [a,b] and circle 
     * (center, radius), if they exist
     */
    private static Point_2[] intersectSegmentWithCircle(Point_2 a, Point_2 b, Point_2 center, double radius) {

	// We parameterize segment [ab] by a+lambda*b, for lambda in [0,1]
	double alpha, betaprime, gamma, deltaprime, root1, root2;
	
	// Compute coefficients of trinomial
	betaprime = (a.x-center.x)*(b.x-a.x) + (a.y-center.y)*(b.y-a.y);
	alpha = a.squareDistance(b).doubleValue();
	gamma = a.squareDistance(center).doubleValue() - radius*radius;
	
	// Compute the reduced discriminant
	deltaprime = betaprime*betaprime - alpha*gamma; 
	
	// If the discriminant is negative, then no intersection
	if (deltaprime < 0)
	    return new Point_2[] {null, null};
	
	// Else compute the two roots
	deltaprime=Math.sqrt(deltaprime);
	root1 = (-betaprime + deltaprime)/alpha;
	root2 = (-betaprime - deltaprime)/alpha;
    
	// There is an intersection iff a root is between 0 and 1
	Point_2 sols[] = new Point_2 [2];
	if (root1>=0 && root1<=1)
	    sols[0] = new Point_2(a.x + root1*(b.x - a.x), a.y + root1*(b.y - a.y));
	else 
	    sols[0] = null;
	if (root2>=0 && root2<=1)
	    sols[1] = new Point_2(a.x + root2*(b.x - a.x), a.y + root2*(b.y - a.y));
	else
	    sols[1] = null;
	
	return sols;
    }

}
