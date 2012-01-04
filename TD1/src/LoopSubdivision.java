import Jcg.Fenetre;
import Jcg.geometry.*;
import Jcg.triangulations2D.*;
import Jcg.triangulations2D.IO;
import Jcg.viewer.*;
import Jcg.subdivision.*;

import java.util.*;


/**
 * Class implementing the Loop subdivision scheme (only for triangle meshes, without boundaries)
 *
 * @author Luca Castelli Aleardi
 *
 */
public class LoopSubdivision implements SubdivisionMethod_3{
	
	class Point extends Point_3 {
		int index;
		
		public Point(Point_3 p) {
			super(p);
		}
		
		public void setIndex(int i) { this.index=i; 	 }
		public int getIndex() 		{ return this.index; }

	}
	
	TriangulationDS_2<Point_3> originalMesh;
	HashMap<HalfedgeHandle<Point_3>, Point> edgePoints;
	
	/**
	 * Initialize the class
	 */
	public LoopSubdivision(TriangulationDS_2<Point_3> mesh) {
		System.out.println("LoopSubdivision: initialisation");
		this.originalMesh = mesh;
		computeEdgePoints();
	}
	
	/**
	 * Store the new edge points in a Hash Table: 
	 * (in order to efficiently retrieve an edge)
	 */
	public void computeEdgePoints() {
		edgePoints = new HashMap<HalfedgeHandle<Point_3>, Point>();
		
		int cont = 0;
		for(TriangulationDSFace_2<Point_3> f: originalMesh.faces) {
			for (int i = 0; i<3; i++) {
				HalfedgeHandle<Point_3> edge = new HalfedgeHandle<Point_3>(f,i);
				if (!this.edgePoints.containsKey(edge)) {
					Point p = (Point) computeEdgePoint(edge);
					p.setIndex(cont);
					edgePoints.put(edge, p);
					cont++;
				}
			}
		}
	}

	/**
	 *  Returns the geometric position of a new edge vertex
	 */
	public Point_3 computeEdgePoint(HalfedgeHandle<Point_3> edge) {
		Point_3 res = new Point_3();
		Point_3[] pts = new Point_3[8];
		pts[0] = edge.getVertex(0).getPoint(); pts[1] = pts[0]; pts[2] = pts[0];
		pts[3] = edge.getVertex(1).getPoint(); pts[4] = pts[3]; pts[5] = pts[3];
		pts[6] = edge.getNext().getVertex(1).getPoint();
		pts[7] = edge.getOpposite().getNext().getVertex(1).getPoint();
		res.barycenter(pts);
		return res;
	}

	/**
	 *  Returns the new (geometric) position of a vertex
	 *  (for vertices already existing in the original mesh)
	 */
	public Point_3 computeNewVertexPoint(TriangulationDSVertex_2<Point_3> v) {
		int d = vertexDegree(v);
		Point_3[] res = new Point_3[16];
		res[ 0] = v.getPoint();
		res[15] = new Point_3(); res[15].barycenter(getVertexNeighbors(v));
		res[ 1] = res[0];
		res[ 2] = res[0];
		res[ 3] = res[0];
		res[ 4] = res[0];
		res[ 5] = res[0];
		res[ 6] = (d == 3) ? res[0] : res[15];
		res[ 7] = (d == 3) ? res[0] : res[15];
		res[ 8] = (d == 3) ? res[0] : res[15];
		res[ 9] = res[15];
		res[10] = res[15];
		res[11] = res[15];
		res[12] = res[15];
		res[13] = res[15];
		res[14] = res[15];
		Point_3 r = new Point_3(); r.barycenter(res);
		return r;
	}
	
	/**
	 * Compute the degree of a vertex (only for non-boundary vertices)
	 */
	public static int vertexDegree(TriangulationDSVertex_2<Point_3> v) {
		int result=1;
		
		TriangulationDSFace_2<Point_3> incidentFace = v.getFace();
		HalfedgeHandle<Point_3> firstEdge = new HalfedgeHandle<Point_3>(incidentFace, 0);
		
		// retrieving the edge (in the given face) having v as destination vertex
		if (firstEdge.getVertex() == v);
		else if (firstEdge.getPrev().getVertex() == v) firstEdge = firstEdge.getPrev();
		else firstEdge = firstEdge.getNext();
		
		HalfedgeHandle<Point_3> e = firstEdge.getNext().getOpposite();
		while(e.equals(firstEdge) == false) {
			e = e.getNext().getOpposite();
			result++;
		}
		
		return result;
	}

	/**
	 * Returns the list of neighbors of a vertex (only for non-boundary vertices)
	 */
	public static Point_3[] getVertexNeighbors(TriangulationDSVertex_2<Point_3> v) {
		Point_3 result[] = new Point_3[vertexDegree(v)];
		
		TriangulationDSFace_2<Point_3> incidentFace = v.getFace();
		HalfedgeHandle<Point_3> firstEdge = new HalfedgeHandle<Point_3>(incidentFace, 0);
		
		// retrieving the edge (in the given face) having v as destination vertex
		if (firstEdge.getVertex() == v);
		else if (firstEdge.getPrev().getVertex() == v) firstEdge = firstEdge.getPrev();
		else firstEdge = firstEdge.getNext();
		
		result[0] = firstEdge.getOpposite().getVertex().getPoint();
		int cont = 1;
		HalfedgeHandle<Point_3> e = firstEdge.getNext().getOpposite();
		while (e.equals(firstEdge) == false) {
			result[cont] = e.getOpposite().getVertex().getPoint();
			e = e.getNext().getOpposite();
			cont++;
		}
		
		return result;
	}
	
    /**
     * Returns an array containing all vertices in the subdivided mesh.
     * New vertices include: new edge points, and the updated original points.
     * Old vertices are stored before.
     */    
	public Point_3[] computeSubdivisionVertices() {
		int nEdges = this.edgePoints.size();
		
		Point_3[] result = new Point_3[this.originalMesh.vertices.size()+nEdges];
		
		int cont = 0;
		for(TriangulationDSVertex_2<Point_3> v : this.originalMesh.vertices) {
			result[cont] = computeNewVertexPoint(v);
			cont++;
		}
		
		Collection<Point> c = this.edgePoints.values();
		for(Point p : c) {
			result[p.getIndex() + this.originalMesh.vertices.size()] = p;
			cont++;
		}
		return result;
	}

    /**
     * Returns an array containing all faces adjacencies.
     * For each face, the resulting array store the index of the corresponding vertices.
     */    
	public int[][] computeSubdivisionFaces() {
		int f = 666; //TODO
		int[][] res = new int[4*f][3];
		
		
		return res;
	}

    /**
     * Subdivide once a triangle mesh (no boundaries).
     */    
    public TriangulationDS_2<Point_3> subdivide(){
		throw new Error("a' completer: etape 4");
    }

    /**
     * Subdivide n times a triangle mesh.
     */    
    public TriangulationDS_2<Point_3> subdivide(int n){
		throw new Error("a' completer: etape 4");
    }
    
    
    
    
    /**
     * testing LoopSubdivision for 3D surface triangle meshes.
     */    
    public static void test3D(String filename){
    	System.out.println("Testing LoopSubdivision for 3D surface meshes");
    	
    	// Open the input surface from an OFF file
    	TriangulationDS_2<Point_3> surfaceMesh=IO.getTriangleMeshFromFile(filename);
    	surfaceMesh.isValid();
    	//new MeshViewer(surfaceMesh);
    	System.out.println("mesh with boundaries? "+surfaceMesh.hasBoundary());
    	
    	// compute the subdivision
    	LoopSubdivision subScheme=new LoopSubdivision(surfaceMesh);   	    	
    	TriangulationDS_2<Point_3> result=subScheme.subdivide(3);

    	// visualize the result
    	new MeshViewer(result);
    }

    /**
     * main testing function.
     */    
    public static void main(String[] args){
    	test3D("cube.off");
    	System.out.println("Testing LoopSubdivision... done");
    }
	
}
