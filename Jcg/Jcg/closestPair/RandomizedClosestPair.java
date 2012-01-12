package Jcg.closestPair;

import Jcg.geometry.*;
import java.util.*;

/**
 * Implementation of a randomized linear time algorithm for computing the closest pair,
 * of a set of points in the plane.
 *
 * @author Code by Luca Castelli Aleardi
 *
 */
class RandomizedClosestPair implements ClosestPair{
	    
    public Pair findClosestPair(Point_2[] points) {
		
		Pair closestPair=new Pair(points[0], points[1]);	
		double minDistance=closestPair.distance();
		Grid grid=new Grid(minDistance);
		grid.addPoint(points[0]);
		grid.addPoint(points[1]);
		
		for(int i=2;i<points.length;i++) {
			double distance=0;
			Point_2 closestPoint=grid.findClosestNeighbor(points[i]);
			if(closestPoint!=null)
				distance=Math.sqrt(points[i].distanceFrom(closestPoint).doubleValue());
			
			if(closestPoint==null || minDistance<=distance)
				grid.addPoint(points[i]);
			else {
				closestPair=new Pair(points[i], closestPoint);
				minDistance=Math.sqrt(points[i].distanceFrom(closestPoint).doubleValue());
				grid=new Grid(minDistance);
				grid.addPoints(points, i+1);
			}
		}
		return closestPair;
    }
       
}