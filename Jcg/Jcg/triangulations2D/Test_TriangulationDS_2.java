package Jcg.triangulations2D;

import Jcg.Fenetre;
import Jcg.geometry.Point_2;
import Jcg.geometry.Point_3;
import Jcg.viewer.*;

/**
 * This class provides methods for testing the Triangle based Data Structure for triangulation
 */    
public class Test_TriangulationDS_2 {

    /**
     * testing function for 2D meshes.
     */    
    public static void test2D(){
    	System.out.println("Testing TriangulationDS_2");
    	Point_2 p0=new Point_2(0.,0.);
    	Point_2 p1=new Point_2(1.,0.);
    	Point_2 p2=new Point_2(0.,1.);
    	
    	TriangulationDS_2<Point_2> triangulation2D=new TriangulationDS_2<Point_2>();
    	TriangulationDSFace_2<Point_2> c=triangulation2D.makeTriangle(p0, p1, p2);
    	triangulation2D.isValid();

    	triangulation2D.insertBarycenter(triangulation2D.faces.get(0));
    	triangulation2D.insertBarycenter(triangulation2D.faces.get(0));
    	triangulation2D.insertBarycenter(triangulation2D.faces.get(1));
    	triangulation2D.insertBarycenter(triangulation2D.faces.get(2));
    	triangulation2D.insertBarycenter(triangulation2D.faces.get(0));
    	triangulation2D.insertBarycenter(triangulation2D.faces.get(1));
    	triangulation2D.insertBarycenter(triangulation2D.faces.get(2));
    	triangulation2D.isValid();
    	
    	Fenetre f=new Fenetre();
    	f.addTriangles(IO.getTriangles(triangulation2D));
    }

    /**
     * testing function for 3D surface triangle meshes.
     */    
    public static void test3D(String filename){
    	System.out.println("Testing TriangulationDS_2 for 3D surface meshes");
    	
    	TriangulationDS_2<Point_3> surfaceMesh=IO.getTriangleMeshFromFile(filename);
    	surfaceMesh.isValid();
    	new MeshViewer(surfaceMesh);
    	//System.out.println(surfaceMesh);
    }

    /**
     * testing function for plane triangle meshes.
     */    
    public static void testPlaneTriangulation(String filename){
    	System.out.println("Testing TriangulationDS_2 for planar triangulations");
    	
    	TriangulationDS_2<Point_2> triangulation=IO.getPlaneTriangulationFromFile(filename);
    	triangulation.isValid();
    	Fenetre f=new Fenetre();
    	f.addTriangles(IO.getTriangles(triangulation));
    }

    /**
     * main testing function.
     */    
    public static void main(String[] args){
    	//test2D();
    	//testPlaneTriangulation("OFF/triangulation.off");
    	test3D("OFF/tri_round_cube.off");
    	System.out.println("Testing TriangulationDS_2... done");
    }

}
