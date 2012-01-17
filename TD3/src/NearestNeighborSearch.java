

public interface NearestNeighborSearch {
	
    /**
     * Range search: return the list of nearest points to a given query point q.
     * The output is the set of points at distance at most sqRad from q.
     */    
    public PointCloud NearestNeighbor (Point_D q, double sqRad);
    
}
