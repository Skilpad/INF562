package Jcg.closestPair;

import Jcg.geometry.*;

/**
 * Interface for closest pair algorithms
 *
 * @author Code by Luca Castelli Aleardi
 *
 */
public interface ClosestPair {
	
    public Pair findClosestPair(Point_2[] points);
       
}