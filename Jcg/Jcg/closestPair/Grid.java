package Jcg.closestPair;

import Jcg.geometry.*;
import java.util.*;

class Grid {
	
	HashMap<GridCell,LinkedList<Point_2>> hMap;
	double R;
	
	public Grid(double R) {
		this.hMap=new HashMap<GridCell,LinkedList<Point_2>>();
		this.R=R;
	}
	
    static Point_2 findClosestPoint(LinkedList<Point_2> pointList, Point_2 p) {
    	if(pointList==null || pointList.size()==0)
    		return null;

		Point_2 result=pointList.get(0);
		double distance=Math.sqrt(p.distanceFrom(result).doubleValue());
		
		for(int i=1;i<pointList.size();i++) {
			Point_2 p_i=pointList.get(i);
			double d=Math.sqrt(p.distanceFrom(p_i).doubleValue());
			if(d<distance && p_i!=p) {
				distance=d;
				result=p_i;
			}
		}
		return result;			
    }
    
    // ajoute tous les points a' la grille
    public void addPoints(Point_2[] points) {
    	this.addPoints(points, points.length);	
    }
    
    // ajoute a' la grille les premiers n points du tableau
    public void addPoints(Point_2[] points, int n) {
    	for(int i=0;i<n;i++)
    		this.addPoint(points[i]);    	    	
    }

    // ajoute un nouveau point a' la grille
    public void addPoint(Point_2 p) {
    	GridCell c=new GridCell(p, this.R);
    	
    	if(this.hMap.containsKey(c)==false) {
    		LinkedList<Point_2> l=new LinkedList<Point_2>();
    		l.add(p);
    		this.hMap.put(c,l);
    	}
    	else {
    		LinkedList<Point_2> l=this.hMap.get(c);
    		l.add(p);
    	}
    }
    
    public LinkedList<Point_2> findNeighbors(Point_2 p) {
    	LinkedList<Point_2> result=new LinkedList<Point_2>();
    	for(int i=-1;i<2;i++) {
    		for(int j=-1;j<2;j++) {
    			GridCell c=new GridCell(new Point_2(p.getX().doubleValue()+i*R, p.getY().doubleValue()+j*R), R);
    			if(this.hMap.containsKey(c)!=false) {
    				LinkedList<Point_2> l=this.hMap.get(c);
    				result.addAll(l);
    			}
    		}
    	}
    	return result;
    }
    
    public Point_2 findClosestNeighbor(Point_2 p) {
    	return findClosestPoint(this.findNeighbors(p),p);
    }
    
}