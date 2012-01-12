package Jcg.io;

import Jcg.geometry.Point_2;
import Jcg.geometry.Point_3;
import Jcg.meshGeneration.RandomSamplingTriangulations;
import Jcg.polyhedron.LoadMesh;
import Jcg.polyhedron.MeshRepresentation;
import Jcg.polyhedron.Polyhedron_3;

public class MeshLoader {

	/**
	 * Load a planar triangle mesh (Polyhedron_3<Point_2>) from an .off file
	 * It uses a shared vertex representation as intermediate data structure
	 */
	public static Polyhedron_3<Point_2> getPlanarTriangulation(String filename) {
    	MeshRepresentation m=new MeshRepresentation();
    	m.readOffFile(filename);
    	
    	LoadMesh<Point_2> load2D=new LoadMesh<Point_2>();
    	Point_2[] points2D=LoadMesh.Point3DToPoint2D(m.points);
    	Polyhedron_3<Point_2> planarTriangleMesh=
    		//load2D.createPolyhedron(points2D,m.faceDegrees,m.faces,m.sizeHalfedges);
    		load2D.createTriangleMesh(points2D, m.faceDegrees, m.faces, m.sizeHalfedges);

    	planarTriangleMesh.isValid(false);
    	return planarTriangleMesh;
	}

	/**
	 * Load a surface triangle mesh (Polyhedron_3<Point_3>) from an .off file
	 * It uses a shared vertex representation as intermediate data structure
	 */
	public static Polyhedron_3<Point_3> getTriangleMesh(String filename) {
    	MeshRepresentation m=new MeshRepresentation();
    	m.readOffFile(filename);
    	
    	LoadMesh<Point_3> load3D=new LoadMesh<Point_3>();
    	//Point_2[] points2D=LoadMesh.Point3DToPoint2D(m.points);
    	Polyhedron_3<Point_3> planarTriangleMesh=
    		//load2D.createPolyhedron(points2D,m.faceDegrees,m.faces,m.sizeHalfedges);
    		load3D.createTriangleMesh(m.points, m.faceDegrees, m.faces, m.sizeHalfedges);

    	planarTriangleMesh.isValid(false);
    	return planarTriangleMesh;
	}

	/**
	 * Generating a random planar triangulation of size n+2 (with uniform distribution)
	 */
	public static Polyhedron_3<Point_2> getRandomPlanarTriangulation(int n) {
		RandomSamplingTriangulations generator;
		generator=RandomSamplingTriangulations.randomTriangulation(n, false);
		return generator.poly;
	}
}
