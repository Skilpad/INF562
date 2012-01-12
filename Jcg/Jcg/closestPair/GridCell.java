package Jcg.closestPair;

import Jcg.geometry.*;

class GridCell {
	
	int i,j;
	double R;
	
	public GridCell(Point_2 p, double R) {
		this.i=(int)Math.floor(p.getX().doubleValue()/R);
		this.j=(int)Math.floor(p.getY().doubleValue()/R);
		this.R=R;
	}
	
	public boolean equals(Object o) {
		GridCell c=(GridCell)o;
		
		if(c.i==this.i && c.j==this.j)
			return true;
		else
			return false;
	}
	
	public int hashCode() {
		return this.i*this.j;
	}

	public String toString() {
		return "("+i*R+","+j*R+")-("+((i+1)*R)+","+((j+1)*R)+")";
	}
  
}
