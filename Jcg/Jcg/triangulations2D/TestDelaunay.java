package Jcg.triangulations2D;

import java.util.ArrayList;
import java.util.*;

import Jcg.geometry.*;
import Jcg.polyhedron.MeshRepresentation;

/**
 * A class for testing the construction of a Delaunay triangulation.
 * @author Luca Castelli Aleardi
 */
public class TestDelaunay {

    public static void main (String[] args) {
    	double scale=1.; // scale factor (radius of the circle)
    	int n; // number of vertices
    	if(args.length>0) {
    		n=Integer.parseInt(args[0]);
    	}
    	else
    		n=10;
 
	/* create the data structure (Delaunay triangulation with one triangle) */
	Delaunay_2 delaunay = new Delaunay_2(new Point_2(-2.*scale, -1.*scale), new Point_2(2.*scale, -1.*scale), new Point_2(0.*scale, 2.*scale));
    //Delaunay_2 delaunay=new Delaunay_2();	
    	
	/* create and insert vertices, updating Delaunay incrementally */
	int cont=0;
	for (int i=0; i<n;) {
	    Point_2 p = new Point_2 (1*scale-2*scale*Math.random(), 1*scale-2*scale*Math.random());
	    // test whether the random point lies in the circle
	    if (p.squareDistance(new Point_2(0.,0.)).doubleValue() <= scale*scale) {
	    	delaunay.insert( p );
	    	i++;
	    }
		cont++;
	}
	int nVertices=delaunay.tri.sizeOfVertices();
	System.out.println("Delaunay triangulation computed: "+nVertices+" vertices");
	IO.writeTriangleMeshToOFF(delaunay.tri, "Delaunay_20.off");

    }
}
