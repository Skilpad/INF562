

import Jcg.geometry.Point_2;
import Jcg.geometry.Point_3;
import Jcg.graph.*;
import Jcg.polyhedron.LoadMesh;
import Jcg.polyhedron.MeshRepresentation;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.viewer.MeshViewer;
import Jcg.io.*;

public class TestDrawing {

	public static void printErrors() {
		System.out.println("Usage: java TestDrawing <drawing method> <graph>");
		System.out.println("Input error: 2 parameters needed");
		System.out.println("1 - Iterative Tutte drawing");
		System.out.println("2 - Force directed drawing 2D");
		System.out.println("3 - Spectral drawing 2D");
		System.out.println("4 - Tutte barycentric drawing 2D (solving linear equations)");
		System.out.println("5 - Spectral drawing 3D");
		//System.out.println("6 - Force directed drawing 3D");	
		System.out.println("\nExample: java TestDrawing 1 K4");	
	}

	/**
	 * return the vertices of a regular polygon
	 */	
	public static Point_2[] regularPolygonVertices(int n, double r) {
		Point_2[] vertices=new Point_2[n];
		double x,y;
		
		for(int i=0;i<n;i++) {
			x=r*Math.cos((2.*Math.PI/n)*i);
			y=r*Math.sin((2.*Math.PI/n)*i);
			vertices[i]=new Point_2(x,y);
		}
		return vertices;
	}

	/**
	 * Test graph algorithms
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Graph drawing algorithms");
		String inputGraph="";
		GraphDrawing d=null;
		if(args.length<2) {
			printErrors();
			System.exit(0);
		}
		int methods=6;
		Graph g=null;
		int choice=Integer.parseInt(args[0]);		
		
		if(choice>methods) {
			printErrors();
			System.exit(0);
		}
		
		inputGraph=args[1];
		if(inputGraph.equals("K4")) {
			g=AdjacencyGraph.constructK4();
		}
		else if(inputGraph.equals("Dodecahedron")) {
			g=AdjacencyGraph.constructDodecahedron();			
		}
		else if(inputGraph.equals("3DCube")) {
			g=AdjacencyGraph.constructCube();			
		}
		else {
			MeshRepresentation m=new MeshRepresentation();
	    	m.readOffFile(args[1]);
	    	LoadMesh<Point_3> load3D=new LoadMesh<Point_3>();
	    	Polyhedron_3<Point_3> polyhedron3D=
	    		load3D.createPolyhedron(m.points,m.faceDegrees,m.faces,m.sizeHalfedges);
	    	polyhedron3D.isValid(false);
			g=AdjacencyGraph.constructFromPolyhedron(polyhedron3D, 0);
			
	    	MeshViewer viewer=new MeshViewer(polyhedron3D);
		}
		
		//System.out.println("Adjacency matrix of G\n"+g.toString());
		
		if(Integer.parseInt(args[0])==2) {
			System.out.println("Force directed methods, spring model in 2D");
			d= new SpringDrawing_2<Point_2>(g);
		}
		else if(Integer.parseInt(args[0])==1) {
			System.out.println("Tutte Drawing 2D, computed iteratively");
			
			int sizeOuterFace=3;
			if(inputGraph.equals("3DCube"))
				sizeOuterFace=4;
			else if(inputGraph.equals("Dodecahedron"))
				sizeOuterFace=5;
			else
				sizeOuterFace=3;
			Point_2[] exteriorPoints=regularPolygonVertices(sizeOuterFace,5.);

			d= new IterativeTutteDrawing<Point_2>(g, exteriorPoints);
		}
		else if(Integer.parseInt(args[0])==3) {
			System.out.println("Spectral Drawing 2D");		
			d= new SpectralDrawing_2<Point_2>(g);
		}
		else if(Integer.parseInt(args[0])==4) {
			System.out.println("Tutte Drawing, solving linear systems");

			int sizeOuterFace=3;
			if(inputGraph.equals("3DCube"))
				sizeOuterFace=4;
			else if(inputGraph.equals("Dodecahedron"))
				sizeOuterFace=5;
			else
				sizeOuterFace=3;

			Point_2[] exteriorPoints=regularPolygonVertices(sizeOuterFace,5.);
			d= new TutteDrawing<Point_2>(g, exteriorPoints);
		}
		else if(Integer.parseInt(args[0])==5) {
			System.out.println("Spectral Drawing 3D");		
			d= new SpectralDrawing_3<Point_3>(g);
			d.computeDrawing();
			d.draw3D();
			return;
		}
		/*else if(Integer.parseInt(args[0])==6) {
			System.out.println("Force directed methods, spring spring model in 3D");
			d= new SpringDrawing_3<Point_3>(g);
			d.computeDrawing();
			d.draw3D();
			return;
		}*/
		else {
			System.out.println("error: no right choice");
			return;
		}
		
		d.computeDrawing();
		System.out.println("planar representation computed");
		Thread.sleep(100);
		d.draw2D();
		
		IO.writeNewTextFile("graph.txt");
		IO.print(d.toString());
		//System.out.println("Embedded planar graph: \n"+d.toString());
		//System.out.println("exit");
	}

}
