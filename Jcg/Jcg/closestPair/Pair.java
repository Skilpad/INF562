package Jcg.closestPair;

import Jcg.geometry.*;

// Pair of points
public class Pair {

	Point_2 a, b;

  public Pair(Point_2 a, Point_2 b) {
  	this.a=a;
  	this.b=b;
  }
  
  public Point_2 getFirst(){
  	return this.a;
  }
  
  public Point_2 getSecond() {
  	return this.b;
  }
  
  public String toString() {
  	Point_2 first, second;
  	if(getFirst().getX().doubleValue()<getSecond().getX().doubleValue()) {
  		first=getFirst();
  		second=getSecond();
  	}
  	else if(getFirst().getX().doubleValue()>getSecond().getX().doubleValue()) {
  		second=getFirst();
  		first=getSecond();
  	}
  	else {
  		  	if(getFirst().getY().doubleValue()<getSecond().getY().doubleValue()) {
  		  		first=getFirst();
  		  		second=getSecond();
  		  	}
  		  	else {
  		  		second=getFirst();
  		  		first=getSecond();
  		  	}
  	}
  	return "["+first+"-"+second+"]";
  }
  
public  double distance() {
  	return Math.sqrt(a.squareDistance(b).doubleValue());
  }

}

