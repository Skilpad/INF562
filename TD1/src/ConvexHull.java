import java.util.*;

import Jcg.geometry.*;
import Jcg.Fenetre;

public class ConvexHull {
	
	class SortPointsByCoordinates implements Comparator<Point_2> {
		public int compare(Point_2 p1, Point_2 p2) {
			return p1.compareTo(p2);
		}
	}
	
    public void computePartialHull(LinkedList<Point_2> sortedPoints, LinkedList<Point_2> hull, LinkedList<Point_2> invertedPoints) {
    	int n = hull.size();
    	while (!sortedPoints.isEmpty()) {
    		Point_2 p = sortedPoints.pop();
    		while (n > 1 && GeometricOperations_2.isCounterClockwise(hull.peek(), hull.get(1), p)) { hull.pop(); n--; }
    		hull.push(p); n++;
    		invertedPoints.push(p);
    	}
    	hull.pop();
    }
    
    public List<Point_2> computeConvexHull(LinkedList<Point_2> points) {
    	Collections.sort(points, new SortPointsByCoordinates());
    	LinkedList<Point_2> points_ = new LinkedList<Point_2>();
    	LinkedList<Point_2> hull    = new LinkedList<Point_2>();
    	computePartialHull(points, hull, points_);
    	computePartialHull(points_, hull, points);
    	return hull;
    }


    
    public static void main (String[] args) {
    	
    	ConvexHull convex = new ConvexHull();
 
		/* create input points */
	    LinkedList<Point_2> vertices = new LinkedList<Point_2>();
		for (int i=0; i<1000;) {
		    Point_2 p = new Point_2 (10.-20*Math.random(), 10.-20*Math.random());
		    if (p.squareDistance(new Point_2(0.,0.)).doubleValue() <= 100.) {
				vertices.add (p);
			    i++;		    
		    }
		}
		//System.out.println(vertices);
		List<Point_2> hull = convex.computeConvexHull(vertices);
		Fenetre f=new Fenetre();
		f.addPoints(vertices);
		Point_2 q = null, q0 = null;
		for (Point_2 p : hull) {
			if (q == null) q0 = p; else f.addSegment(p, q);
			q = p;
		}
		f.addSegment(q0, q);
		
    }

}