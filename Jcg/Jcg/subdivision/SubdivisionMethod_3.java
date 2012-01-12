package Jcg.subdivision;

import Jcg.geometry.Point_3;
import Jcg.triangulations2D.TriangulationDS_2;

/**
 * Interface defining the main methods of a subdivision scheme
 *
 * @author Luca Castelli Aleardi
 *
 */
public interface SubdivisionMethod_3 {

    /**
     * Subdivide once a 3D surface mesh.
     */    
    public TriangulationDS_2<Point_3> subdivide();

    /**
     * Subdivide 'i' times a 3D surface mesh.
     */    
    public TriangulationDS_2<Point_3> subdivide(int i);

}
