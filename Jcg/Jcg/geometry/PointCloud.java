package Jcg.geometry;

import java.util.*;

public class PointCloud<X extends Point_> {
	
	private ArrayList<X> points;
	
	public PointCloud() {
		this.points=new ArrayList<X>();
	}

	public PointCloud(int n) {
		this.points=new ArrayList<X>(n);
	}
	
	public int size() {
		return this.points.size();
	}

	public void add(X p) {
		this.points.add(p);
	}

}
