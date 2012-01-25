package Jcg.triangulations2D;

// TODO: Package Jcg.triangulation2D & Jcg.triangulation3D are for 2D & 3D.
// => Do a more global package & keep no more general Point_ in 2D & 3D versions 

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import Jcg.geometry.*;
import Jcg.triangulations3D.TriangulationDSCell_3;
 
/**
 * Incremental 2D Delaunay Triangulation using Face/Vertex data structure (TriangulationDS_2).
 *
 * @author Java-code by Luca Castelli-Aleardi and Steve Oudot
 *
 */
public class Delaunay_2 extends Triangulation_2 {

	/**
	 * Constuctor: builds an empty Delaunay triangulation.
	 */
	public Delaunay_2 () {
		super();
	}
	
	public Delaunay_2 (Point_2 a, Point_2 b, Point_2 c) {
		super(a, b, c);
	}
 
	/**
	 *  Inserts point p in the Delaunay triangulation and returns the newly created vertex
	 *
	 *  @param p the point to insert
	 *  @exception: a RuntimeException is thrown when point p is already a vertex of the triangulation
	 *  
	 */
	public TriangulationDSVertex_2<Point_2> insert(Point_2 p) {
//		System.out.println("Inserting " + p);
		TriangulationDSFace_2<Point_2> fp = locate(p);
 
		// point is already a vertex of the triangulation -> throw RuntimeException
		if (p.equals(fp.vertex(0).getPoint()) || p.equals(fp.vertex(1).getPoint()) || p.equals(fp.vertex(2).getPoint()))
			throw new RuntimeException ("Point " + p + " is already in the triangulation");
			
		TriangulationDSVertex_2<Point_2> vp = tri.insertInTriangle(p, fp);
		
		// Examine incident triangles to ensure that the locally Delaunay condition holds
		 while (true) {
 			// circulate around vertex and examine the opposite face of each incident face
			 TriangulationDSFace_2<Point_2> f = vp.getFace();
			 boolean flip = false;
			 do {
				 // if locally Delaunay test fails with an incident face, then flip the opposite edge
				 int ip = f.index(vp);
				 TriangulationDSFace_2<Point_2> opposite = f.neighbor(ip);
				 if (opposite != null && !f.isMarked(ip) && 
						 GeometricOperations_2.inCircle(opposite.vertex(0).getPoint(), opposite.vertex(1).getPoint(), opposite.vertex(2).getPoint(), p)) {
					 if (opposite.isMarked(opposite.index(f)))
						 throw new Error ("Flipping a marked edge");
					 // flip opposite edge
					 tri.flipEdge(new HalfedgeHandle<Point_2>(f, ip));
					 flip = true;
					 break;
				 }
				 // go to next face around vertex vp
				 f = f.neighbor((f.index(vp)+1)%3);
			 } while (f != vp.getFace());
			 // terminate when no more flips need to be done
			 if (!flip)
				 break;
		 }

		// return new vertex
		return vp;  
	}

	/**
	 *  Inserts a constraint edge in the Delaunay triangulation. 
	 *  Warning: in the current implementation, constraint edges cannot intersect one another except at a common 
	 *  vertex, otherwise there is a risk of falling into an infinite loop...
	 *
	 * @param p,q the vertices of the edge
	 * @return the halfedge of origin p that points in the direction of q along the input constraint edge
	 */
	public HalfedgeHandle<Point_2> insert(Point_2 p, Point_2 q) {
//		System.out.println("Inserting edge [" + p + "," + q + "]");
		TriangulationDSVertex_2<Point_2> vq = null;
		try{
			vq = insert (q);
		} catch (RuntimeException e) {
//			System.out.println (q + " is already in the triangulation");
			TriangulationDSFace_2<Point_2> fq = locate(q);
			for (int i=0; i<3; i++)
				if (fq.vertex(i).getPoint().equals(q))
					vq = fq.vertex(i);
			if (vq == null)
				throw new Error("q is supposed to be in the triangulation but cannot be found there");
		}
		TriangulationDSVertex_2<Point_2> vp = null;
		try{
			vp = insert (p);
		} catch (RuntimeException e) {
//			System.out.println (p + " is already in the triangulation");
			TriangulationDSFace_2<Point_2> fp = locate(p);
			for (int i=0; i<3; i++)
				if (fp.vertex(i).getPoint().equals(p))
					vp = fp.vertex(i);
			if (vp == null)
				throw new Error("p is supposed to be in the triangulation but cannot be found there");
		}
		
		// circulate around vp to find vq among the neighbors
//		System.out.println("Circulating around " + vp);
		HalfedgeHandle<Point_2> ep = new HalfedgeHandle<Point_2> (vp.getFace(), (vp.getFace().index(vp)+2)%3);
		HalfedgeHandle<Point_2> e = ep;
		do {
//			System.out.println("looking at halfedge " + e);
			if (e.getVertex() == vq) {  // constraint edge is in triangulation => mark it and its opposite
				e.mark();
				e.getOpposite().mark();
//				System.out.println ("constraint edge " + e + " is in triangulation");
				return e;
			}
			 // go to next edge around vertex vp
			 e = e.getNext().getNext().getOpposite();
		} while (!e.equals(ep));
		// constraint edge is still not in triangulation => recursively insert midpoint
		Point_2 m = Point_2.midPoint(p, q);
		insert (m, q);
		return insert (p, m);
	}
		
		
		
	/**
	 *  Computes and returns the list of constraint edges.
	 *  Each edge is represented as an array of two points.
	 */
    public Collection<HalfedgeHandle<Point_2>> constraintEdges() {
    	Collection<HalfedgeHandle<Point_2>> edges = this.finiteEdges();
    	LinkedList<HalfedgeHandle<Point_2>> res = new LinkedList<HalfedgeHandle<Point_2>> ();
	for(HalfedgeHandle<Point_2> e : edges)
	    if (e.isMarked())
		res.add (e);
	return res;
    }

	/**
	 * Outputs the Voronoi vertex dual to face f. 
	 * By definition, this vertex coincides with the circumcenter of f. 
	 * Throws a RuntimeException if f is infinite.
	 */
	public Point_2 dual (TriangulationDSFace_2<Point_2> f) {
		if (isInfinite(f))
			throw new RuntimeException(f + " is infinite and therefore has no dual.");
		return GeometricOperations_2.circumCenter(f.vertex(0).getPoint(), f.vertex(1).getPoint(), f.vertex(2).getPoint());
	}
	
}
