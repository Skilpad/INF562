package Jcg.closestPair;

import Jcg.geometry.*;
import java.util.*;

// Point a deux coordonnes reelles (latitude, longitude)
class TestClosestPair {
	
  public static LinkedList<Point_2> ListFromArray(Point_2[] t) {
  	LinkedList<Point_2> result=new LinkedList<Point_2>();
  	for(int i=0;i<t.length;i++)
  		result.add(t[i]);
  	return result;
  }
  
  static Point_2[] randomPoints(int n) {
  	System.out.println("Generating "+n+" random points");
  	Point_2[] points=new Point_2[n];
  	for(int i=0;i<n;i++) {
  		double x=Math.random()*50.;
  		double y=Math.random()*50.;
  		points[i]=new Point_2(x,y);
  	}
  	return points;
  }
  
  // test pour la recherche des voisins dans les cellules environnantes
  // avec points aleatoires
  public static void test(Point_2[] points) {
  	System.out.println("Testing closest pair algorithms");
  	
  	points=randomPoints(20000);
  	
  	System.out.print("Computing closest pair (fast)...");
  	ClosestPair fast=new RandomizedClosestPair();
  	Pair closest=fast.findClosestPair(points);
  	System.out.println("done");
	  	
  	LinkedList<Point_2> l=ListFromArray(points);

  	System.out.println("closest points (fast):\n"+closest);  		
  	System.out.println("min distance: "+closest.distance());  		
  	
  	System.out.print("Computing closest pair (slow)...");
  	ClosestPair slow=new SlowClosestPair();
  	Pair slowClosest=slow.findClosestPair(points);
  	System.out.println("done");
  	System.out.println("Closest points (slow):\n"+slowClosest);
  	System.out.println("min distance: "+slowClosest.distance());
  	
  	System.out.println("End test");
  }
  
    
    
  //--------------------------------------
  //--------------------------------------
  //--------------------------------------


  
  public static void main(String[] args) {
	  Point_2[] points=randomPoints(20000);
	  test(points);
  }

}

