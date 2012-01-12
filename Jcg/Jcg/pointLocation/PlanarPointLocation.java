package Jcg.pointLocation;

import Jcg.geometry.Point_2;
import Jcg.polyhedron.Halfedge;
import Jcg.polyhedron.Polyhedron_3;

public interface PlanarPointLocation {
	
    /**
     * Compute and return the face (an incident half-edge) containing the query point
     */    
    public Halfedge<Point_2> locatePoint(Polyhedron_3<Point_2> triangulation, Point_2 queryPoint);

}
