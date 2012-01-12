package Jcg.closestPair;

import Jcg.geometry.*;
import java.util.*;

/**
 * Implementation of a quadratic time algorithm for computing the closest pair,
 * based on exhaustive search.
 *
 * @author Code by Luca Castelli Aleardi
 *
 */
class SlowClosestPair implements ClosestPair {
	
    public Pair findClosestPair(Point_2[] points) {
		if(points.length<2) throw new Error("Error: too few points");
		
		Pair result=new Pair(points[0], points[1]);
		Pair pair;
		double distance=result.distance();
		for(int i=0;i<points.length;i++) {
			for(int j=i+1;j<points.length;j++) {
				double pairDistance=Math.sqrt(points[i].distanceFrom(points[j]).doubleValue());
				pair=new Pair(points[i], points[j]);
				if(pairDistance<distance) {
					result=pair;
					distance=pairDistance;
				}
			}
		}
		return result;			
    }

}