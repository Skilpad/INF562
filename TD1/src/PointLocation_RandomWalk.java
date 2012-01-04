import java.awt.Color;

import Jcg.*;
import Jcg.geometry.*;
import Jcg.polyhedron.*;
import Jcg.pointLocation.*;

public class PointLocation_RandomWalk implements PlanarPointLocation{
 
    /**
     * Load a plane triangulation from an OFF file
     */    
    public static Polyhedron_3<Point_2> openTriangulation(String filename) {   	
    	MeshRepresentation m=new MeshRepresentation();
    	m.readOffFile(filename);
    	
    	LoadMesh<Point_2> load2D=new LoadMesh<Point_2>();
    	Point_2[] points2D=LoadMesh.Point3DToPoint2D(m.points);
    	Polyhedron_3<Point_2> planarGraph=
    		load2D.createPolyhedron(points2D,m.faceDegrees,m.faces,m.sizeHalfedges);

    	System.out.println(planarGraph.verticesToString());   	
    	System.out.println(planarGraph.facesToString());
    	planarGraph.isValid(false);
    	return planarGraph;
    }
    
    /**
     * Returns the barycenter point of a face randomly chosen
     */    
    public static Point_2 getRandomPoint(Polyhedron_3<Point_2> polyhedron){
    	return createCenterVertex(getRandomFace(polyhedron));
    }
    
    /**
     * Returns a face randomly chosen
     */    
    public static Face<Point_2> getRandomFace(Polyhedron_3<Point_2> polyhedron){
    	//throw new Error("a completer");
    	int nFaces=polyhedron.sizeOfFacets();
    	int randomIndex=(int)(nFaces*Math.random());
    	return polyhedron.facets.get(randomIndex);
    }

    /**
     * Compute and return the barycenter point of a given face
     */    
    public static Point_2 createCenterVertex(Face<Point_2> f){
    	Point_2[]         pts = new Point_2[3];
    	Halfedge<Point_2> e   = f.getEdge();
    	pts[0] = e.getVertex().getPoint();
    	e = e.getNext();
    	pts[1] = e.getVertex().getPoint();
    	pts[2] = e.getNext().getVertex().getPoint();
    	Point_2 p = new Point_2(0,0);
    	p.barycenter(pts);
    	return p;
    }
    
    /**
     * Compute and return the first edge intersected by segment (p1, p2), inside face f.
     * Where p1 is the origin point, and p2 is the query point.
     * Face f is assumed to contain point p1.
     */    
    public static Halfedge<Point_2> getFirstIntersectedEdge(Face<Point_2> f, Point_2 p1, Point_2 p2) {
    	Segment_2 seg_0     = new Segment_2(p1,p2);
    	Halfedge<Point_2> e = f.getEdge();
    	Segment_2 seg       = new Segment_2(e.getVertex().getPoint(), e.getNext().getVertex().getPoint());
    	if (GeometricOperations_2.doIntersect(seg_0, seg)) return e;
    	e   = e.getNext();
    	seg = new Segment_2(e.getVertex().getPoint(), e.getNext().getVertex().getPoint());
    	if (GeometricOperations_2.doIntersect(seg_0, seg)) return e;
    	e   = e.getNext();
    	seg = new Segment_2(e.getVertex().getPoint(), e.getNext().getVertex().getPoint());
    	if (GeometricOperations_2.doIntersect(seg_0, seg)) return e;
    	return null;
   }

    /**
     * Compute and return the next edge intersected by segment (p1, p2), inside face f defined by half-edge first.
     * The result is null whether there in no other intersected edge.
     */    
    public static Halfedge<Point_2> getNextIntersectedEdge(Halfedge<Point_2> first, Point_2 p1, Point_2 p2) {
//    	Fenetre f=new Fenetre();
//    	System.out.println("#####");
//    	
//    	f.addFatSegment(p1,p2);
//    	
//    	f.addColoredSegment(first.getNext().getVertex().getPoint(), first.getNext().getNext().getVertex().getPoint(), new Color(0x880000));
//    	f.addColoredSegment(first.getVertex().getPoint(), first.getPrev().getVertex().getPoint(), new Color(0x000000));
//    	f.addColoredSegment(first.getVertex().getPoint(), first.getNext().getVertex().getPoint(), new Color(0xFF0000));
//    	
//    	f.addColoredSegment(first.getNext().getOpposite().getNext().getVertex().getPoint(), first.getNext().getOpposite().getNext().getNext().getVertex().getPoint(), new Color(0x000000));
//    	f.addColoredSegment(first.getNext().getOpposite().getVertex().getPoint(), first.getNext().getOpposite().getPrev().getVertex().getPoint(), new Color(0x000000));
//    	f.addColoredSegment(first.getNext().getOpposite().getVertex().getPoint(), first.getNext().getOpposite().getNext().getVertex().getPoint(), new Color(0xFF0000));
//    	
//    	if (true) return null;
    	
    	Segment_2 seg_0     = new Segment_2(p1,p2);
//    	System.out.println(seg_0); f.addFatSegment(seg_0.p, seg_0.q);
    	Halfedge<Point_2> e = first.getNext().getOpposite();
    	Segment_2 seg       = new Segment_2(e.getVertex().getPoint(), e.getNext().getVertex().getPoint());
//    	System.out.println(seg); f.addFatSegment(seg.p, seg.q);
    	if (GeometricOperations_2.doIntersect(seg_0, seg)) return e;
    	e   = e.getNext();
    	seg = new Segment_2(e.getVertex().getPoint(), e.getNext().getVertex().getPoint());
//    	System.out.println(seg); f.addFatSegment(seg.p, seg.q);
    	if (GeometricOperations_2.doIntersect(seg_0, seg)) return e;
//    	System.out.println("----");
    	return null;    	
   }

    /**
     * Compute and return the face containing the point p2 (the query point)
     * The random walk starts from the (randomly chosen) point p1.
     * The first edge is incident (and inside) to the face containing p1.
     */    
    public static Halfedge<Point_2> locatePoint(Halfedge<Point_2> first, Point_2 p1, Point_2 p2) {
    	Halfedge<Point_2> e0 = first;
    	for (Halfedge<Point_2> e = getNextIntersectedEdge(e0,p1,p2); e != null; e = getNextIntersectedEdge(e0,p1,p2)) e0 = e;
    	return e0.getNext().getOpposite();
   }

    /**
     * Compute and return the (an incident half-edge inside the) face containing the query point
     */    
    public Halfedge<Point_2> locatePoint(Polyhedron_3<Point_2> triangulation, Point_2 queryPoint) {
    	Face f1    = getRandomFace(triangulation);
    	Point_2 p1 = createCenterVertex(f1);
    	Halfedge<Point_2> first = getFirstIntersectedEdge(f1, p1, queryPoint);
    	return locatePoint(first, p1, queryPoint);
   }

    /**
     * main testing function.
     */    
    public static void main (String[] args) {
    	// Load a plane triangulation from an OFF file
    	Polyhedron_3<Point_2> triangulation=openTriangulation("delaunay100.off");
    	Face<Point_2> outerFace=triangulation.facets.get(0);
    	Halfedge<Point_2> rootEdge=outerFace.getEdge();
    	triangulation.makeHole(rootEdge); // delete the outer face
    	triangulation.isValid(false);
    	
    	// Select the query point at random inside a face of the triangulation
    	Point_2 queryPoint=getRandomPoint(triangulation);
    	
    	// Main computation: locate the face containing the query point
    	PlanarPointLocation location=new PointLocation_RandomWalk();
    	Halfedge<Point_2> lastEdge=location.locatePoint(triangulation, queryPoint);
    	
    	// visualize the result
    	Fenetre f=new Fenetre();
    	f.addPolyhedronEdges(triangulation);
    	f.addPoint(queryPoint);
    	f.addFatSegment((Point_2)lastEdge.getVertex().getPoint(),
    					(Point_2)lastEdge.getOpposite().getVertex().getPoint());
    	f.addFatSegment((Point_2)lastEdge.getNext().getVertex().getPoint(),
    					(Point_2)lastEdge.getNext().getOpposite().getVertex().getPoint());
    	f.addFatSegment((Point_2)lastEdge.getPrev().getVertex().getPoint(),
    					(Point_2)lastEdge.getPrev().getOpposite().getVertex().getPoint());

    
    
    }
}
