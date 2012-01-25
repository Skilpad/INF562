import Jcg.graph.*;
import Jcg.geometry.*;
import Jcg.triangulations2D.*;
import Jcg.triangulations3D.*;
import Jcg.Fenetre;
import Jcg.viewer.*;
import Jcg.triangulations2D.IO;

import java.awt.Color;
import java.util.*;

/**
 * Main class for testing TD5
 *
 * @author Luca Castelli Aleardi
 */
public class CompactGraph {

	public static void main(String[] args) {
		System.out.println("TD5: mesh compression");
//		testCreate2DGraph();
//		testCreate3DGraph();
//		testEncoding2D();
		// TODO: in graphics function (when plot): recurrent error: "java.util.ConcurrentModificationException"
//		testEncoding3D();

//		test1a();
//		test1b(); // test graph partitioning in 2D
//		test1b3D(); // test graph partitioning in 3D
//		test2();
		test3();

//		testEncodingFinal();
		//testEncodingFinal3D();
		System.out.println("end TD5");
	}

	/**
	 * Create and return a geometric graph from a 2D Delaunay triangulation
	 */	
	public static GeometricGraph_2 createDelaunayGraph_2(int n) {
		/* create data structures (empty triangulations) */
		Delaunay_2 del = new Delaunay_2 ();		
		/* create and insert vertices, updating triangulations incrementally */
		List<Point_2> vertices = new ArrayList<Point_2>();

		for (int i=0; i<n;) {
		    Point_2 p = new Point_2 (10-20*Math.random(), 10-20*Math.random());
		    if (p.squareDistance(new Point_2(0.,0.)).doubleValue() <= 10.) {
			vertices.add (p);
			del.insert( p );
			i++;
		    }
		}

		/* Get list of all triangles */
		List<Point_2[]> trianglesDel = (List<Point_2[]>) del.finiteFacesPoints();
		GeometricGraph_2 g;
		g=GeometricGraph_2.getGraphFrom2DTriangulation(vertices, trianglesDel);

		/* Output triangulation to OFF file */
		IO.writeToFile (vertices, trianglesDel, "delaunay.off");
		System.out.println("off files created");
		return g;
	}

	/**
	 * Create and return a geometric graph from a 3D Delaunay triangulation
	 */	
	public static GeometricGraph_3 createDelaunayGraph_3(int n) {
		Delaunay_3 del = new Delaunay_3();
		for (int i=0; i<n; i++) {
			Point_3 p = new Point_3(Math.random(), Math.random(), Math.random());
			System.out.println("inserting " + p);
			del.insert(p);
		}
		del.isValid();
		del.writeToFile("delaunay3.off");

		GeometricGraph_3 g;
		g=GeometricGraph_3.getGraphFrom3DMesh("delaunay3.off");
		System.out.println("3D graph created\n");
		//System.out.println(""+g);

		return g;
	}

	/**
	 * Create and return a geometric graph from a generic 2D triangulation
	 */	
	public static GeometricGraph_2 createTriangulationGraph_2(int n) {
		/* create data structures (empty triangulations) */
		Triangulation_2 tri = new Triangulation_2 ();		
		/* create and insert vertices, updating triangulations incrementally */
		List<Point_2> vertices = new ArrayList<Point_2>();

		for (int i=0; i<n;) {
		    Point_2 p = new Point_2 (10-20*Math.random(), 10-20*Math.random());
		    if (p.squareDistance(new Point_2(0.,0.)).doubleValue() <= 10.) {
			vertices.add (p);
			tri.insert( p );
			i++;
		    }
		}

		/* Get list of all triangles */
		List<Point_2[]> trianglesTri = (List<Point_2[]>) tri.finiteFacesPoints();
		
		GeometricGraph_2 g;
		g=GeometricGraph_2.getGraphFrom2DTriangulation(vertices, trianglesTri);

		/* Output triangulation to OFF file */
		IO.writeToFile (vertices, trianglesTri, "triangulation.off");
		System.out.println("off files created");
		return g;
	}	

	
	
//----------------------------------------
//----------------------------------------
//----------------------------------------
	
	
	
	public static void testCreate2DGraph() {
		System.out.println("Test creating 2D graph");
		GeometricGraph_2 g=CompactGraph.createDelaunayGraph_2(300);
		g.computeNaturalVertexOrder();
		CompactGraph.drawGeometricGraph_2(g);
		System.out.println(g);
		
		GeometricGraph_2 gCopy=new GeometricGraph_2(g);
		System.out.println(gCopy);
		CompactGraph.drawGeometricGraph_2(gCopy);		
	}
	
	public static void test1a() {
		System.out.println("Test 1a: method Separator_2.geometricCut()");
		GeometricGraph_2 g=CompactGraph.createDelaunayGraph_2(300);
		CompactGraph.drawGeometricGraph_2(g);
		
		Separator_2 separator=new Separator_2();
		GeometricGraph<Point_<Kernel_2>>[] subgraphs = separator.geometricCut(g, new Line_2(1., 1., 0.));
		
		System.out.println("first sub-graph: "+subgraphs[0].sizeVertices());
		System.out.println("second sub-graph: "+subgraphs[1].sizeVertices());
		
		CompactGraph.drawGeometricGraph_2(subgraphs[0]);
		CompactGraph.drawGeometricGraph_2(subgraphs[1]);
		
		for (GraphNode<Point_<Kernel_2>> v : subgraphs[1].vertices)
			subgraphs[0].addNode(v);
		CompactGraph.drawGeometricGraph_2(subgraphs[0]);		
	}

	public static void test1b() {
		System.out.println("Test 1b: testing recursive decomposition in 2D");
		GeometricGraph_2 g=CompactGraph.createDelaunayGraph_2(50);
		CompactGraph.drawGeometricGraph_2(g);
		
		Separator_2 separator=new Separator_2();
		List<GraphNode<Point_<Kernel_2>>> result=separator.computeGraphSequence(g);
		int cont=0;
		for(GraphNode<Point_<Kernel_2>> graph: result) {
				cont++;
		}
		System.out.println(cont+" isolated vertices");
		if(cont==g.sizeVertices())
			System.out.println("computed partition ok");
		else
			System.out.println("error in computing the partition");
	}

	public static void test1b3D() {
		System.out.println("Test 1b: testing recursive decomposition in 3D");
		GeometricGraph_3 g=CompactGraph.createDelaunayGraph_3(500);
		
		Separator_3 separator=new Separator_3();
		List<GraphNode<Point_<Kernel_3>>> result=separator.computeGraphSequence(g);
		int cont=0;
		for(GraphNode<Point_<Kernel_3>> graph : result) {
				cont++;
		}
		System.out.println(cont+" isolated vertices");
		if(cont==g.sizeVertices())
			System.out.println("computed partition ok");
		else
			System.out.println("error in computing the partition");
	}

	public static void testCreate3DGraph() {
		System.out.println("Opening 3D graph");
		GeometricGraph_3 g=CompactGraph.createDelaunayGraph_3(10);
		g.computeNaturalVertexOrder();
		System.out.println("3D graph created "+g);
	}

	public static void testEncoding2D() {
		System.out.println("Test encoding (not using separators) in 2D");
		GeometricGraph_2 g=CompactGraph.createDelaunayGraph_2(10000);
		//GeometricGraph_2 g=CompactGraph.createTriangulationGraph_2(1000);
		g.computeNaturalVertexOrder();
		CompactGraph.drawGeometricGraph_2(g);
		
		Encoding encoding=new Encoding(g);
		encoding.encodeToFile("graphCompressed.txt");
		encoding.writeToFile("graphNotCompressed.txt");
	}

	public static void testEncoding3D() {
		System.out.println("Test encoding (not using separators) in 3D");
		GeometricGraph_3 g=CompactGraph.createDelaunayGraph_3(10);
		g.computeNaturalVertexOrder();
		System.out.println("3D graph created "+g);		

		Encoding encoding=new Encoding(g);
		encoding.encodeToFile("3DgraphCompressed.txt");
		encoding.writeToFile("3DgraphNotCompressed.txt");
	}

	public static void testEncodingFinal() {
		System.out.println("Test encoding (using separators)");
		
		// create a geometric graph from a 2D Delaunay triangulation
		GeometricGraph_2 g=CompactGraph.createDelaunayGraph_2(1000);
		
		// create a geometric graph from a 2D triangulation (not Delaunay)
		//GeometricGraph_2 g=CompactGraph.createTriangulationGraph_2(1000);

		g.computeNaturalVertexOrder();
		//CompactGraph.drawGeometricGraph_2(g);
		//System.out.println("original graph\n"+g);
		Encoding normalEncoding=new Encoding(g);
		normalEncoding.encodeToFile("normalGraphEncoding.txt");
		System.out.println("normalGraphEncoding.txt");
		normalEncoding.writeToFile("graphNotCompressed.txt");
		System.out.println("graphNotCompressed.txt");
		
		GeometricGraph_2 gCopy=new GeometricGraph_2(g);
				
		Separator s=new Separator_2();
		s.reorderVertices(g);
		
		gCopy.orderVertices(g.vertexPermutation());
		
		Encoding compactEncoding=new Encoding(gCopy);
		compactEncoding.encodeToFile("compactGraphEncoding.txt");
		System.out.println("compactGraphEncoding.txt");
		System.out.println("");
	}

	public static void testEncodingFinal3D() {
		System.out.println("Test encoding (using separators) in 3D");
		
		// create a geometric graph from a 3D Delaunay triangulation
		GeometricGraph_3 g=CompactGraph.createDelaunayGraph_3(50);
		
		g.computeNaturalVertexOrder();
		Encoding normalEncoding=new Encoding(g);
		normalEncoding.encodeToFile("normalGraphEncoding.txt");
		System.out.println("normalGraphEncoding.txt");
		normalEncoding.writeToFile("graphNotCompressed.txt");
		System.out.println("graphNotCompressed.txt");
		
		GeometricGraph_3 gCopy=new GeometricGraph_3(g);
				
		Separator s=new Separator_3();
		s.reorderVertices(g);
		
		gCopy.orderVertices(g.vertexPermutation());
		
		Encoding compactEncoding=new Encoding(gCopy);
		compactEncoding.encodeToFile("compactGraphEncoding.txt");
		System.out.println("compactGraphEncoding.txt");
		System.out.println("");
	}
	
	public static void test2() {
		GeometricGraph_2 g=CompactGraph.createDelaunayGraph_2(300);
		CompactGraph.drawGeometricGraph_2(g);
		
		Separator_2 separator=new Separator_2();
		List<GraphNode<Point_<Kernel_2>>> result=separator.computeGraphSequence(g);
		int cont=0;
		for(GraphNode<Point_<Kernel_2>> v: result) {
				System.out.println("vertex: "+cont+" - "+v);
				cont++;
		}
	}

	public static void test3() {
		System.out.println("Reordering of the vertices (using separators)");
		GeometricGraph_2 g=CompactGraph.createDelaunayGraph_2(500);
		g.computeNaturalVertexOrder();
		CompactGraph.drawGeometricGraph_2(g);
		List<Point_2> echantillon1=new ArrayList<Point_2>();
		for(int i=0;i<50;i++)
			echantillon1.add((Point_2) g.vertices.get(i).getPoint());
		Fenetre f1 = new Fenetre();
		f1.addPoints(echantillon1);
		
		Separator_2 separator=new Separator_2();
		separator.reorderVertices(g);
		System.out.println(g);
		List<Point_2> echantillon2=new ArrayList<Point_2>();
		for(GraphNode<Point_<Kernel_2>> v: g.vertices)
			if(v.getTag()<15)echantillon2.add((Point_2) v.getPoint());
		Fenetre f2=new Fenetre();
		f2.addPoints(echantillon2);
	}

	/**
	 * Draw a 2D geometric graph
	 */	
	public static void drawGeometricGraph_2(GeometricGraph<Point_<Kernel_2>> g) {
		List<Point_<Kernel_2>[]> edgeList = g.computeEdges();
		Fenetre f=new Fenetre();
		for (Point_<Kernel_2>[] seg : edgeList) f.addSegment((Point_2) seg[0], (Point_2) seg[1]);
		// TODO: Graphic functions should be compatible with Point_<Kernel_2> instead of Point_2
		// => Is an automatic cast possible?
	}
	
	// TODO: Why the F*** List<Point_2[]> does not extends List<Point_<Kernel_2>[]>?! 
	
}
