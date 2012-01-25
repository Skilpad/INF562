package Jcg.triangulations2D;

import java.util.Collection;
import java.util.LinkedList;
import Jcg.geometry.*;
 
/**
 * Incremental Triangulation using Face/Vertex data structure (TriangulationDS_2).
 *
 * @author Java-code by Luca Castelli-Aleardi and Steve Oudot
 *
 */
public class Triangulation_2 {
 
	// triangulation data structure
	protected TriangulationDS_2<Point_2> tri;

	// starting edge for walk (see locate() method)
	protected TriangulationDSFace_2<Point_2> startingFace;
 
	// Bounding box of the triangulation
	class BoundingBox {
		double minx,miny,maxx,maxy;
		Point_2 a = new Point_2(0.,0.); // lower left
		Point_2 b = new Point_2(0.,0.); // lower right
		Point_2 c = new Point_2(0.,0.); // upper right
		Point_2 d = new Point_2(0.,0.); // upper left
	}
	protected BoundingBox bbox = new BoundingBox();
 
	/**
	 * Constuctor: builds an empty triangulation.
	 */
	public Triangulation_2() {
 
		bbox.minx = Double.MAX_VALUE;
		bbox.maxx = Double.MIN_VALUE;
		bbox.miny = Double.MAX_VALUE;
		bbox.maxy = Double.MIN_VALUE;
 
		// create the triangulation of the bounding box
		tri = new TriangulationDS_2<Point_2>();
    	TriangulationDSVertex_2<Point_2> v1=tri.createVertex(bbox.a, null);
    	TriangulationDSVertex_2<Point_2> v2=tri.createVertex(bbox.b, null);
    	TriangulationDSVertex_2<Point_2> v3=tri.createVertex(bbox.c, null);
    	TriangulationDSVertex_2<Point_2> v4=tri.createVertex(bbox.d, null);

    	TriangulationDSFace_2<Point_2> c1 = tri.createFace(v1,v2,v3,null,null,null);
		v1.setFace(c1);
		v2.setFace(c1);
		v3.setFace(c1);

    	TriangulationDSFace_2<Point_2> c2 = tri.createFace(v1,v3,v4,null,null,c1);
		v4.setFace(c2);
		
		c1.setNeighbor(1, c2);

		// select starting face
		this.startingFace = c1;
	}
 
	/**
	 * Constuctor: builds a triangulation consisting of a triangle.
	 */
	public Triangulation_2(Point_2 a, Point_2 b, Point_2 c) {
 
		bbox.minx = Double.MAX_VALUE;
		bbox.maxx = Double.MIN_VALUE;
		bbox.miny = Double.MAX_VALUE;
		bbox.maxy = Double.MIN_VALUE;
 
		// create the triangulation of the bounding box
		tri = new TriangulationDS_2<Point_2>();
    	TriangulationDSVertex_2<Point_2> v1=tri.createVertex(a, null);
    	TriangulationDSVertex_2<Point_2> v2=tri.createVertex(b, null);
    	TriangulationDSVertex_2<Point_2> v3=tri.createVertex(c, null);

    	TriangulationDSFace_2<Point_2> c1 = tri.createFace(v1,v2,v3,null,null,null);
		v1.setFace(c1);
		v2.setFace(c1);
		v3.setFace(c1);

		// select starting face
		this.startingFace = c1;
	}

	/**
	 * update the dimension of the bounding box
	 *
	 * @param minx,miny,maxx,maxy summits of the rectangle
	 */
	protected void setBoundingBox(double minx,double miny,
				   double maxx,double maxy) {
		// update saved values
		bbox.minx=minx; bbox.maxx=maxx;
		bbox.miny=miny; bbox.maxy=maxy;
 
		// extend the bounding-box to surround min/max
		double centerx = (minx+maxx)/2;
		double centery = (miny+maxy)/2;
		double x_min = ((minx-centerx-1)*10+centerx);
		double x_max = ((maxx-centerx+1)*10+centerx);
		double y_min = ((miny-centery-1)*10+centery);
		double y_max = ((maxy-centery+1)*10+centery);
 
		// set new positions
		bbox.a.x(x_min);	bbox.a.y(y_min);
		bbox.b.x(x_max);	bbox.b.y(y_min);
		bbox.c.x(x_max);	bbox.c.y(y_max);
		bbox.d.x(x_min);	bbox.d.y(y_max);
	}
 
	// update the size of the bounding box (cf locate() method)
	protected void updateBoundingBox(Point_2 p) {
		double minx = Math.min(bbox.minx, p.x());
		double maxx = Math.max(bbox.maxx, p.x());
		double miny = Math.min(bbox.miny, p.y());
		double maxy = Math.max(bbox.maxy, p.y());
		setBoundingBox(minx, miny, maxx, maxy);
//		System.out.println("resizing bounding-box: "+minx+" "+miny+" "+maxx+" "+maxy);
	}
 
	/**
	 * Locates point p in the triangulation and returns a triangle containing p. 
	 *
	 * @param p the point to locate
	 * @return one of the triangles containing p
	 */
	public TriangulationDSFace_2<Point_2> locate(Point_2 p) {
 
		/* outside the bounding box ? */
		if ( p.x()<bbox.minx || p.x()>bbox.maxx || p.y()<bbox.miny || p.y()>bbox.maxy ) {
			updateBoundingBox(p);
		}
 
		TriangulationDSFace_2<Point_2> f = startingFace;
		while (true) {
//			System.out.println("Checking face " + f);
			/* duplicate point ? */
			if(p.equals(f.vertex(0).getPoint()) || p.equals(f.vertex(1).getPoint()) || p.equals(f.vertex(2).getPoint())) return f;
 
			/* walk */
			if (GeometricOperations_2.isCounterClockwise(p,f.vertex(1).getPoint(),f.vertex(0).getPoint()))
				f = f.neighbor(2);
			else if (GeometricOperations_2.isCounterClockwise(p,f.vertex(2).getPoint(),f.vertex(1).getPoint()))
				f = f.neighbor(0);
			else if (GeometricOperations_2.isCounterClockwise(p,f.vertex(0).getPoint(),f.vertex(2).getPoint()))
				f = f.neighbor(1);
			else
				return f;
		}
	}
 

	/**
	 *  Inserts point p in the triangulation and returns the newly created vertex
	 *
	 * @param p the point to insert
	 * @exception: a RuntimeException is thrown when point p is already a vertex of the triangulation
	 *  
	 */
	public TriangulationDSVertex_2<Point_2> insert(Point_2 p) {
//		System.out.println("Inserting " + p);
		TriangulationDSFace_2<Point_2> fp = locate(p);
 
		// point is already a vertex of the triangulation => throw RuntimeException
		if (p.equals(fp.vertex(0).getPoint()) || p.equals(fp.vertex(1).getPoint()) || p.equals(fp.vertex(2).getPoint()))
			throw new RuntimeException ("Point " + p + " is already in the triangulation");
		
		TriangulationDSVertex_2<Point_2> vp = tri.insertInTriangle(p, fp);
		
		// Examine suspicious infinite triangles when p lies outside the convex hull
		 while (true) {
 			// circulate around vertex and examine the opposite face of each incident face
			 TriangulationDSFace_2<Point_2> f = vp.getFace();
			 boolean flip = false;
			 do {
				 // if local Delaunay test fails with an infinite face, then flip the edge
				 int ip = f.index(vp);
				 TriangulationDSFace_2<Point_2> opposite = f.neighbor(ip);
				 if (opposite != null && isInfinite(opposite) && 
						 GeometricOperations_2.inCircle(opposite.vertex(0).getPoint(), opposite.vertex(1).getPoint(), opposite.vertex(2).getPoint(), p)) {
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
	 *  Returns true if Point p is infinite, i.e. if it is a vertex of the bounding box
	 */
    public boolean isInfinite (Point_2 p) {
    	return p.equals(bbox.a) || p.equals(bbox.b) 
	    	|| p.equals(bbox.c) || p.equals(bbox.d);
    }

	/**
	 *  Returns true if Vertex v is infinite, i.e. if it is a vertex of the bounding box
	 */
    public boolean isInfinite (TriangulationDSVertex_2<Point_2> v) {
    	return isInfinite(v.getPoint());
	}

    /**
	 *  Returns true if Face f is infinite, i.e. if it is connected 
	 *  to a vertex of the bounding box.
	 */
    public boolean isInfinite (TriangulationDSFace_2<Point_2> f) {
    	return isInfinite(f.vertex(0)) || isInfinite(f.vertex(1)) || isInfinite(f.vertex(2));
    }

    /**
	 *  Returns true if Edge e is infinite, i.e. if it is connected 
	 *  to a vertex of the bounding box.
	 */
    public boolean isInfinite (HalfedgeHandle<Point_2> e) {
    	return isInfinite(e.getVertex(0)) || isInfinite(e.getVertex(1));
    }

	/**
	 *  Computes and returns the collection of all finite vertices of the triangulation 
	 *  (that is, vertices lying in the interior of the bounding box).
	 */
	public Collection<TriangulationDSVertex_2<Point_2>> finiteVertices() {
		LinkedList<TriangulationDSVertex_2<Point_2>> res = new LinkedList<TriangulationDSVertex_2<Point_2>>();
		// do not return bounding box vertices
		for(TriangulationDSVertex_2<Point_2> v : tri.vertices)
			if ( !isInfinite(v))
					res.add(v);
		return res;
	}

	/**
	 *  Computes and returns the collection of all finite faces of the triangulation 
	 *  (that is, faces lying in the interior of the bounding box).
	 */
	public Collection<TriangulationDSFace_2<Point_2>> finiteFaces() {
		LinkedList<TriangulationDSFace_2<Point_2>> res = new LinkedList<TriangulationDSFace_2<Point_2>>();
		// do not return faces connected to bounding box vertices
		for(TriangulationDSFace_2<Point_2> f : tri.faces)
			if ( !isInfinite(f))
					res.add(f);
		return res;
	}

	/**
	 *  Computes and returns the collection of the points defining all finite faces of the triangulation
	 *  (that is, faces lying in the interior of the bounding box).
	 */
	public Collection<Point_2[]> finiteFacesPoints() {
		LinkedList<Point_2[]> res = new LinkedList<Point_2[]>();
		// do not return faces connected to bounding box vertices
		for(TriangulationDSFace_2<Point_2> f : tri.faces)
			if ( !isInfinite(f))
				res.add(f.verticesPoints());
		return res;
	}

	/**
	 *  Computes and returns the collection of all finite edges in the triangulation 
	 *  (that is, all edges connecting vertices in the interior of the bounding box).
	 */
	public Collection<HalfedgeHandle<Point_2>> finiteEdges() {
		LinkedList<HalfedgeHandle<Point_2>> res = new LinkedList<HalfedgeHandle<Point_2>>();
		// do not return edges pointing to/from bounding box vertices
		for(TriangulationDSFace_2<Point_2> f : tri.faces) 
			for (int i=0; i<3 ; i++) {
				HalfedgeHandle<Point_2> e = new HalfedgeHandle<Point_2> (f, i);
				if (!isInfinite(e) && f.compareTo(f.neighbor(i)) > 0)
					res.add(e);
			}
		return res;
	}

	/**
	 *  Computes and returns the collection of all edges on the convex hull (collects only one HalfedgeHandle per geometric edge).
	 */
    public Collection<HalfedgeHandle<Point_2>> convexHullEdges() {
	LinkedList<HalfedgeHandle<Point_2>> res = new LinkedList<HalfedgeHandle<Point_2>>();
	for(TriangulationDSFace_2<Point_2> f:tri.faces)
		for (int i=0; i<3; i++)
			if (!isInfinite(f) && isInfinite (f.neighbor(i)))
				res.add (new HalfedgeHandle<Point_2> (f, i));
	return res;
    }


	/**
     * checks the combinatorial validity of the triangulation.
     */        
    public boolean isValid() {
    	return tri.isValid();
    }

    public String toString() {
    	return ""+tri;
    }

}
