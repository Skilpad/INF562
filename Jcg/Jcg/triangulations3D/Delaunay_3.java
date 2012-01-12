package Jcg.triangulations3D;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import Jcg.geometry.GeometricOperations_3;
import Jcg.geometry.Point_3;
import Jcg.geometry.Segment_3;
import Jcg.geometry.Triangle_3;
import Jcg.viewer.MeshViewer;

/**
 * Incremental 3D Delaunay Triangulation. Data points can be inserted in any arbitrary order. Each 
 * point insertion is performed in three steps:
 * - locate the point in the triangulation, i.e. find a cell containing it;
 * - perform a breadth-first search from this cell to discover the conflicts zone of the point.
 * - delete the conflicts zone and re-triangulate it by starring it with respect to the new vertex.
 *
 * @author Steve Oudot
 *
 * Notes: 
 * - the implementation assumes that the points lie in general position (no 3 points must be aligned, nor 
 *   4 points coplanar, nor 5 points cospherical.
 * - there is still an issue in the handling of infinite cells in the detection of conflicts
 *   (see the bounding box factor in method updateBB()).
 */

public class Delaunay_3 {

	private TriangulationDS_3<Point_3> TDS;  // triangulation data structure
	private Point_3[] boundingBox = {
			new Point_3(-1,-1,-1), new Point_3(1,-1,-1), new Point_3(1,1,-1), new Point_3(-1,1,-1), 
			new Point_3(-1,-1,1), new Point_3(1,-1,1), new Point_3(1,1,1), new Point_3(-1,1,1)
			};  // initial vertices of the bounding box
	

	
	private void updateBB (Point_3 p) {
		double v = Math.max(
				Math.max(Math.max(p.getX().doubleValue(), -p.getX().doubleValue()), 
						Math.max(p.getY().doubleValue(), -p.getY().doubleValue())),
						Math.max(p.getZ().doubleValue(), -p.getZ().doubleValue()));
		double ref = Math.max(boundingBox[0].getX().doubleValue(), -boundingBox[0].getX().doubleValue()); 
		// we use a factor of 1e6 for the bounding box, so the flaws on the bounding box are negligible
		// later we should handle conflicts with infinite cells more carefully
		if (1e6*v > ref) {
			for (int i=0; i<8; i++)
				boundingBox[i].multiply(1e6*v/ref);
		}
	}

	/**
	 * returns the status of a 3D point: infinite if it is a bounding box vertex, finite otherwise. 
	 * This method runs in constant time and does not check whether p is a vertex of the triangulation.
	 */
	public boolean isInfinite(Point_3 p) {
		for (int i=0; i<8; i++)
			if (p.equals(boundingBox[i]))
				return true;
		return false;
	}
	
	/** returns the status of a triangulation vertex: infinite if it is a vertex of the bounding box,
	 * finite otherwise.
	 */
	public boolean isInfinite(TriangulationDSVertex_3 v) {
		return isInfinite(v.getPoint());
	}


	/**
	 * returns the status of a triangulation cell: infinite if it is incident to at least one infinite
	 * vertex, and finite otherwise.
	 */
	public boolean isInfinite (TriangulationDSCell_3 c) {
		return isInfinite(c.vertex(0)) || isInfinite(c.vertex(1)) || isInfinite(c.vertex(2)) || isInfinite(c.vertex(3));
	}
	
	/**
	 * 
	 * returns the status of a triangulation facet: infinite if it is incident to at least one infinite
	 * vertex, and finite otherwise.
	 */
	public boolean isInfinite (FacetHandle f) {
		for (int i=0; i<3; i++)
			if (isInfinite(f.vertex(i)))
				return true;
		return false;
	}
	
	/**
	 * creates an empty 3D Delaunay triangulation, ready to be used for insertions and localizations.
	 * In effect the triangulation contains only infinite vertices (the vertices of the bounding box)
	 * after its construction.
	 */
	public Delaunay_3 () {
		TDS = new TriangulationDS_3<Point_3> ();
		// Triangulate bounding box
//    	System.out.print ("Creating bounding box... ");
    	TriangulationDSCell_3<Point_3> c1 = TDS.makeTetrahedron(boundingBox[0], boundingBox[1], boundingBox[3], boundingBox[4]);
    	TriangulationDSCell_3<Point_3> c2 = TDS.insertOutside(boundingBox[5], c1, 0).getCell();
    	TriangulationDSCell_3<Point_3> c3 = TDS.insertOutside(boundingBox[7], c2, c2.index(boundingBox[1])).getCell();
    	TriangulationDSCell_3<Point_3> c4 = TDS.insertOutside(boundingBox[6], c3, c3.index(boundingBox[4])).getCell();
    	TriangulationDSCell_3<Point_3> c5 = TDS.insertOutside(boundingBox[2], c2, c2.index(boundingBox[4])).getCell();
    	TDS.createCell(
    			TDS.vertices.get(4),
    			TDS.vertices.get(2), 
    			TDS.vertices.get(6),
    			TDS.vertices.get(7), 
    			null, null, c5, c4);
//    	System.out.println ("done");
	}
	
	/**
	 * cloning constructor that only copies internal pointers and does not actually duplicate the content.
	 */
	public Delaunay_3 (Delaunay_3 del) {
		this.TDS = del.TDS;
		this.boundingBox = del.boundingBox;
	}
	
	/**
	 * checks the combinatorial validity of the Delaunay triangulation.
	 */
	public boolean isValid() {
		return TDS.isValid();
	}
	
	/**
	 * returns a random (possibly infinite) cell of the triangulation, chosen according to uniform 
	 * distribution over the set of all triangulation cells.
	 */
	public TriangulationDSCell_3<Point_3> randomCell()  {
		return TDS.cells.get((int)(Math.random()*TDS.sizeOfCells())); 
	}

	/**
	 * returns a random finite cell of the triangulation, chosen according to a uniform distribution over
	 * the set of finite triangulation cells. 
	 */
	public TriangulationDSCell_3<Point_3> randomFiniteCell()  {
		while (true) {
			TriangulationDSCell_3<Point_3> c = TDS.cells.get((int)(Math.random()*TDS.sizeOfCells())); 
			if (!isInfinite(c))
				return c;
		}
	}
		
	/**
	 * returns an infinite cell of the triangulation. 
	 */
	public TriangulationDSCell_3<Point_3> infiniteCell()  {
		return infiniteVertex().getCell();
	}

	/**
	 * returns a random (possibly infinite) vertex of the triangulation, chosen according to uniform 
	 * distribution over the set of all triangulation vertices.
	 */
	public TriangulationDSVertex_3<Point_3> randomVertex()  {
		return TDS.vertices.get((int)(Math.random()*TDS.sizeOfVertices()));
	}

	/**
	 * returns a random finite vertex of the triangulation, chosen according to a uniform distribution over
	 * the set of finite triangulation vertices. 
	 */
	public TriangulationDSVertex_3<Point_3> randomFiniteVertex()  {
		return TDS.vertices.get((int)(Math.random()*(TDS.sizeOfVertices()-8)) + 8);  // avoid infinite vertices
	}

	/**
	 * returns an infinite vertex of the triangulation. 
	 */
	public TriangulationDSVertex_3<Point_3> infiniteVertex()  {
		return TDS.vertices.get(0);
	}

	/**
	 * returns the collection of all vertices of the triangulation.
	 */
	public Collection<TriangulationDSVertex_3<Point_3>> vertices()  {
		return TDS.vertices;
	}

	/**
	 * returns the collection of all finite vertices of the triangulation.
	 */
	public Collection<TriangulationDSVertex_3<Point_3>> finiteVertices()  {
		ArrayList<TriangulationDSVertex_3<Point_3>> res = new ArrayList<TriangulationDSVertex_3<Point_3>>();
		for (TriangulationDSVertex_3<Point_3> v : TDS.vertices)
			if (!isInfinite(v))
				res.add(v);
		return res;
	}

	/**
	 * returns the collection of all cells of the triangulation.
	 */
	public Collection<TriangulationDSCell_3<Point_3>> cells()  {
		return TDS.cells;
	}

	/**
	 * returns the collection of all finite cells of the triangulation.
	 */
	public Collection<TriangulationDSCell_3<Point_3>> finiteCells()  {
		ArrayList<TriangulationDSCell_3<Point_3>> res = new ArrayList<TriangulationDSCell_3<Point_3>>();
		for (TriangulationDSCell_3<Point_3> c : TDS.cells)
			if (!isInfinite(c))
				res.add(c);
		return res;
	}

	/**
     * locates point p using a straight-line walk from a random cell. 
     */    
    public TriangulationDSCell_3<Point_3> locate (Point_3 p) {
    	// choose a random cell and start the locate in that cell
    	return locate(p, randomCell());
    }

    /**
     * locates point p using straight-line walk from cell cstart. 
     */    
    private TriangulationDSCell_3<Point_3> locate(Point_3 p, TriangulationDSCell_3<Point_3> cstart) {
    	if (cstart == null)
    		throw new Error ("Problem in locate: cell cstart should not be null");
    	// update the bounding box if necessary, to avoid returning a null cell
    	updateBB(p);
    	// choose arbitrary point in cell cstart, e.g. its barycenter
    	Point_3 pstart = new Point_3 (0,0,0);
    	pstart.barycenter(new Point_3[] {cstart.vertex(0).getPoint(), cstart.vertex(1).getPoint(), 
    			cstart.vertex(2).getPoint(),cstart.vertex(3).getPoint()});
    	if (GeometricOperations_3.sideOfTetrahedron(pstart, cstart.verticesPoints()) > 0)
    		throw new Error ("Problem in walk initialization: " + cstart + " does not contain its barycenter " + pstart);
    	TriangulationDSCell_3<Point_3> c = cstart;
    	// walk from pstart towards p along line segment s=[p,pstart]
    	Segment_3 s = new Segment_3 (p, pstart);
    	TriangulationDSCell_3 cprev = null;
    	while (GeometricOperations_3.sideOfTetrahedron(p, c.verticesPoints()) > 0) {
//    		System.out.println("now in cell "+c);
    		// look for facet that intersects the segment [p,pstart]
    		boolean change = false;
    		for (int i=0; i<4; i++)
    			if (c.neighbor(i) != null && c.neighbor(i) != cprev &&
    					GeometricOperations_3.doIntersect(s, 
    							new Triangle_3(c.vertex((i+1)&3).getPoint(), 
    									c.vertex((i+2)&3).getPoint(), 
    									c.vertex((i+3)&3).getPoint()))) {
    				cprev = c;
    				c = c.neighbor(i);
    				change = true;
    				break;
    			}
    		if (!change)
    			throw new Error ("Problem in walk: no intersecting facet found in " + c + " for segment " + s);
    	}
    	return c;  // should contain p at this stage
    }
    	

    /**
     * Computes the set of cells that are in conflict with p, that is, the cells whose circumspheres have p 
     * in their interior.
     * 
     * @param cstart any cell that is in conflict with p (for instance a cell containing p)
     * 
     */
    public 	Set<TriangulationDSCell_3<Point_3>> findConflicts (Point_3 p, TriangulationDSCell_3<Point_3> cstart) {
    	if (cstart == null)
    		throw new Error ("Problem in findConflicts: cell cstart should not be null");
    	if (GeometricOperations_3.sideOfSphere(p, cstart.vertex(0).getPoint(), 
				cstart.vertex(1).getPoint(), cstart.vertex(2).getPoint(), cstart.vertex(3).getPoint()) > 0)
    		throw new Error("Problem in findConflicts: starting cell " + cstart + " is not in conflict with query point " + p);

    	TreeSet<TriangulationDSCell_3<Point_3>> res = new TreeSet<TriangulationDSCell_3<Point_3>>();
    	TreeSet<TriangulationDSCell_3<Point_3>> toBeVisited = new TreeSet<TriangulationDSCell_3<Point_3>>();
    	toBeVisited.add(cstart);
    	
    	while(!toBeVisited.isEmpty()) {
    		// retrieve and remove first cell in set of cells to be visited
    		TriangulationDSCell_3<Point_3> c = toBeVisited.first();
    		toBeVisited.remove(c);
    		// if the cell is in conflict...
    		if (GeometricOperations_3.sideOfSphere(p, c.vertex(0).getPoint(), 
    				c.vertex(1).getPoint(), c.vertex(2).getPoint(), c.vertex(3).getPoint()) <= 0) {
    			// ... add it to the output and propagate wavefront to its neighbors
    			res.add(c);
    			for (int i=0; i<4; i++)
    				if (c.neighbor(i) != null && !res.contains(c.neighbor(i)))
    					toBeVisited.add(c.neighbor(i));  // adds the cell only if it is not already there
    		}
    	}
    	return res;
    }
    

    /**
     * Triangulates a zone (= facet-connected set of cells) by creating a star of simplices joining vertex v 
     * to the triangles on the zone's boundary. This method is used typically in the Delaunay insertion, 
     * to re-triangulate the conflicts zone.
     * @param v the new vertex to become the center of the star
     * @param zone the facet-connected set of cells forming the zone
     */
    public void starZone (TriangulationDSVertex_3<Point_3> v, TreeSet<TriangulationDSCell_3<Point_3>> zone) {

    	// keep track of the facets of the created cells, to set adjacencies
    	HashMap<FacetHandle<Point_3>, FacetHandle<Point_3>> createdFacets = 
    		new HashMap<FacetHandle<Point_3>, FacetHandle<Point_3>>(); 
    		
    	// iterator over cells in conflict	
    	for (Iterator<TriangulationDSCell_3<Point_3>> cit = zone.iterator(); cit.hasNext();) {

    		// retrieve next cell in conflict
    		TriangulationDSCell_3<Point_3> c = cit.next();

    		// create as many new cells as the number of facets of c on the boundary of the conflicts zone
    		for (int i=0; i<4; i++)
    			if (c.neighbor(i) == null || !zone.contains(c.neighbor(i)))  { // c is then on the boundary of the conflicts zone
    	
    				// create new cell cn with vertex v and the vertices of the face of c on the boundary
    				TriangulationDSCell_3<Point_3> cn = TDS.createCell
    				(c.vertex((i+1)&3), c.vertex((i+2)&3), c.vertex((i+3)&3), v, null,null,null,null);

    		    	// Assign cn to its vertices
    				for (int j=0; j<4; j++)
    					cn.vertex(j).setCell(cn);

    		    	// connect cn to the cell on the other side of the face i of c (which lies outside the conflicts zone)
					TriangulationDSCell_3<Point_3> ci = c.neighbor(i);
					if (ci != null) {
//						System.out.println("connecting " + cn + " to outer cell " + ci); 
						ci.setNeighbor(ci.index(c), cn);
						cn.setNeighbor(cn.vertices.indexOf(v), ci);
					}

					// connect cn to the previously constructed 3-cells
    				for (int j=0; j<4; j++) {
    					FacetHandle<Point_3> f = new FacetHandle<Point_3> (cn, j);
    					FacetHandle<Point_3> fo = createdFacets.get(f);
    					if (fo != null) {
  //  						System.out.println("connecting " + cn + " to " + fo.cell()); 
    						cn.setNeighbor(j, fo.cell());
    						fo.cell().setNeighbor(fo.index(), cn);
    					}
    				}
    				
    				// add the facets of cn to the pool of created facets
    				for (int j=0; j<4; j++) {
    					FacetHandle<Point_3> f = new FacetHandle<Point_3> (cn, j);
    					createdFacets.put(f, f);
    				}
    			}
    	}
    	if (createdFacets.isEmpty())
    		throw new Error ("No created facet in starZone");
    }
    
    
	/**
     * Inserts point p in the triangulation, using cell cstart to initiate the conflicts zone computation. 
     * @param cstart a cell that is in conflict with p (for instance a cell containing p). This method casts
     * an Error if cstart turns out not to be in conflict with p. 
     */    
    public TriangulationDSVertex_3<Point_3> insert(Point_3 p, TriangulationDSCell_3<Point_3> cstart) {
    	// First, retrieve the conflicts zone
    	TreeSet<TriangulationDSCell_3<Point_3>> conflicts = 
    		(TreeSet<TriangulationDSCell_3<Point_3>>) findConflicts(p, cstart);
//    	System.out.println("Conflicts zone [" + conflicts.size() + "]: " + conflicts);
    	
    	// Then, add p as a new vertex v
    	TriangulationDSVertex_3<Point_3> v = new TriangulationDSVertex_3<Point_3>(p);
    	TDS.vertices.add(v);

    	// Then, star the conflicts zone with respect to v
    	starZone (v, conflicts);
    	
    	// Then, remove old cells in conflicts zone from the triangulation
    	for (Iterator<TriangulationDSCell_3<Point_3>> cit = conflicts.iterator(); cit.hasNext();)
    		TDS.cells.remove(cit.next());
    	
    	// Finally, return the created vertex
    	return v;
    }

	/**
     * Inserts point p in the triangulation, using a random cell to locate p then the output of the locate
     * to compute the conflicts zone of p.
     */    
    public TriangulationDSVertex_3<Point_3> insert(Point_3 p) {
    	// First, locate p and check whether p is already a vertex
    	TriangulationDSCell_3<Point_3> cstart = locate(p);
    	for (int i=0; i<4; i++)
    	if (p.equals(cstart.vertex(i).getPoint()))
    		throw new RuntimeException("Point " + p + " is already a vertex of the triangulation!");	
    	// Then, use cstart to initiate the conflicts zone computation in the insertion of p
    	return insert (p, cstart);
    }

	/**
     * Outputs the collection of all finite facets of the triangulation (the ones not linked to 
     * bounding box vertices). This method takes linear time in the triangulation size.
     */
	public Collection<FacetHandle<Point_3>> finiteFacets() {
		ArrayList<FacetHandle<Point_3>> res = new ArrayList<FacetHandle<Point_3>>(); 
		for (TriangulationDSCell_3<Point_3> c : TDS.cells) {
			for (int i=0; i<4; i++)
				 // to insert facet only once, we insert it through its "smallest" incident cell
				if (!isInfinite(new FacetHandle<Point_3>(c,i)) && c.compareTo(c.neighbor(i)) < 0)
					res.add(new FacetHandle<Point_3>(c,i));
		}
		return res;
	}

	/**
     * Outputs the collection of all facets of the boundary of the convex hull. 
     * This method takes linear time in the triangulation size.
     */
	public Collection<FacetHandle<Point_3>> convexHullFacets () {
		ArrayList<FacetHandle<Point_3>> res = new ArrayList<FacetHandle<Point_3>>(); 
		for (TriangulationDSCell_3<Point_3> c : TDS.cells) {
			for (int i=0; i<4; i++)
				 // to insert facet only once, we insert it through its "smallest" incident cell
				if (!isInfinite(new FacetHandle<Point_3>(c,i)) && c.compareTo(c.neighbor(i)) < 0 &&
						(isInfinite(c) || isInfinite(c.neighbor(i)))) 
					res.add(new FacetHandle<Point_3>(c,i));
		}
		return res;
	}

	
	/**
	 * Useful method
	 */
	private <X extends TriangulationDSCell_3<Point_3>> Collection<X> removeInfiniteElements (Collection<X> c) {
		for (Iterator<X> xit = c.iterator(); xit.hasNext();) {
			X x = xit.next();
			if (isInfinite(x))
				xit.remove();
		}
		return c;
	}
	
	/**
	 * Outputs the collection of all cells incident to vertex v.
	 */
	public Collection<TriangulationDSCell_3<Point_3>> incidentCells (TriangulationDSVertex_3<Point_3> v) {
		return TDS.incidentCells(v);
	}

	/**
	 * Outputs the collection of all finite cells incident to vertex v.
	 */
	public Collection<TriangulationDSCell_3<Point_3>> incidentFiniteCells (TriangulationDSVertex_3<Point_3> v) {
		return removeInfiniteElements(incidentCells(v));
	}

	/**
     * Returns the collection of all cells incident to edge (c,i,j).
     */        
	public Collection<TriangulationDSCell_3<Point_3>> incidentCells (TriangulationDSCell_3<Point_3> c, int i, int j) {
		return TDS.incidentCells(c,i,j);
	}
	
	/**
     * Returns the collection of all finite cells incident to edge (c,i,j).
     */        
	public Collection<TriangulationDSCell_3<Point_3>> incidentFiniteCells (TriangulationDSCell_3<Point_3> c, int i, int j) {
		return removeInfiniteElements(incidentCells(c,i,j));
	}

	/**
     * Returns the collection of all cells incident to edge (u,v).
     */        
	public Collection<TriangulationDSCell_3<Point_3>> incidentCells (TriangulationDSVertex_3<Point_3> u, TriangulationDSVertex_3<Point_3> v) {
		return TDS.incidentCells(u, v);
	}

	/**
     * Returns the collection of all finite cells incident to edge (u,v).
     */        
	public Collection<TriangulationDSCell_3<Point_3>> incidentFiniteCells (TriangulationDSVertex_3<Point_3> u, TriangulationDSVertex_3<Point_3> v) {
		return removeInfiniteElements(incidentCells(u, v));
	}
	
	/**
	 * Outputs the Voronoi vertex dual to cell c. 
	 * By definition, this vertex coincides with the circumcenter of c. 
	 * Throws a RuntimeException if c is infinite.
	 */
	public Point_3 dual (TriangulationDSCell_3<Point_3> c) {
		if (isInfinite(c))
			throw new RuntimeException(c + " is infinite and therefore has no dual.");
		return GeometricOperations_3.circumCenter(c.vertex(0).getPoint(), c.vertex(1).getPoint(), c.vertex(2).getPoint(), c.vertex(3).getPoint());
	}
	
	/**
	 * Outputs the (triangulated) boundary of the Voronoi region dual to vertex v. 
	 * Each Voronoi face forming the Voronoi region boundary is triangulated. 
	 * When vertex v lies on the boundary of the convex hull, only the finite faces 
	 * of the boundary of its region are output.
	 * Throws a RuntimeException if v is infinite.
	 */
	public Collection<Triangle_3> dual (TriangulationDSVertex_3<Point_3> v) {
	    if (isInfinite(v))
		throw new RuntimeException(v + " is infinite and therefore has no dual.");

		// compute incident edges from incident cells
		Collection<TriangulationDSCell_3<Point_3>> ic = incidentCells(v);
		
		HashSet<TriangulationDSVertex_3[]> ie = new HashSet<TriangulationDSVertex_3[]> ();
		for (TriangulationDSCell_3<Point_3> c : ic)
			for (int i=1; i<4; i++) 
				if (!isInfinite(c.vertex((c.index(v)+i)&3)))
					// add edge only if it is not already present
					ie.add(new TriangulationDSVertex_3[]{v, c.vertex((c.index(v)+i)&3)});
		
		LinkedList<Triangle_3> res = new LinkedList<Triangle_3> ();

		// for each edge, compute sorted list of incident finite cells and then triangulate Voronoi face
		for (TriangulationDSVertex_3<Point_3>[] e : ie) {
			ic = incidentFiniteCells(e[0], e[1]);
			if (ic.size() < 3)
				continue;
			Iterator<TriangulationDSCell_3<Point_3>> cit = ic.iterator();
			TriangulationDSCell_3<Point_3> cstart = cit.next();
			TriangulationDSCell_3<Point_3> c = cit.next();
			TriangulationDSCell_3<Point_3> cprev = null;
			do {
				cprev = c;
				c = cit.next();
				res.add(new Triangle_3(dual(cstart), dual(cprev), dual(c)));
			} while (cit.hasNext());
		}
		return res;
	}

	
	/**
     * outputs the triangulation in an OFF file.
     */
    public void writeToFile (String filename) {
    	// retrieve finite facets
    	Collection<FacetHandle<Point_3>> facets = finiteFacets();
    	// store vertex indices in map 
    	HashMap <TriangulationDSVertex_3<Point_3>,Integer> vert = 
    		new HashMap<TriangulationDSVertex_3<Point_3>,Integer> ();
    	try {
    	 BufferedWriter out = new BufferedWriter (new FileWriter(filename));
    	 out.write ("OFF\n");
    	 out.write ((TDS.vertices.size()-boundingBox.length) + " " + facets.size() + " 0\n");  // ignore infinite vertices
    	 int i=0;
    	 for (TriangulationDSVertex_3<Point_3> v : TDS.vertices) 
    	 	if (!isInfinite(v)) {  // skip infinite vertices
    	     out.write(v.getPoint().getX().doubleValue() + " " +
    	    		 v.getPoint().getY().doubleValue() + " " +
    	    		 v.getPoint().getZ().doubleValue() + "\n");
    	     vert.put(v, new Integer(i++));
    	 	}    
    	 
    	 for (FacetHandle<Point_3> f : facets) {
    	     Integer[] ind = new Integer [3];
    	     for (int j=0; j<3; j++) {
    		 ind[j] =  vert.get(f.vertex(j));
    		 if (ind[j] == null)
    		     throw new Error("Index issue in facet");
    	     }
    	     out.write("3");
    	     for (int j=0; j<3; j++)
    		 out.write (" " + ind[j].intValue());
    	     out.write ("\n"); 
    	 }
    	 out.close();
    	} catch (IOException e) {}
    }


	//////////////////////////////// TESTING METHODS ////////////////////////////////////////////
    /**
     * Testing function.
     */	
	public static void main(String[] args) {
		test5();		
	}		

		
	// Ci-dessous un exemple de bug lie a l'update de la bounding box quand on n'est pas Delaunay
	// La deuxieme instruction permet d'eviter le bug car pas de redimensionnement => pas d'inversion de simplexe
	private static void test0 () {
		Delaunay_3 del = new Delaunay_3();
		del.updateBB(new Point_3(1,1,1));
		Point_3[] P = new Point_3[3];
		P[0] = new Point_3(0.9388093186748128,0.9283684913785756,0.3468020578989015);
		P[1] = new Point_3(0.8660756975171705,0.05496804111962572,0.17547142955266026);
		P[2] = new Point_3(0.9516605758014842,0.693362378082091,0.2125255534606474);
		for (int i=0; i<3; i++) {
			System.out.println("inserting " + P[i]);
			TriangulationDSCell_3<Point_3> c = del.locate(P[i]);
			del.TDS.insertInCell(P[i], c);
		}
		for (int i=0; i<10000; i++) {
			Point_3 p = new Point_3(Math.random(), Math.random(), Math.random());
			System.out.println("inserting " + p);
			TriangulationDSCell_3<Point_3> c = del.locate(p);
			del.TDS.insertInCell(p, c);
		}
		for (int i=0; i<100000; i++) {
			Point_3 p = new Point_3(Math.random(), Math.random(), Math.random());
			System.out.println("locating " + p);
			del.locate(p);
		}		
		del.isValid();
		System.out.println("done.");
	}

	// Pour mettre au jour le probleme de la gestion de l'enveloppe convexe avec une bounding box 
	private static void test1 () {
		Delaunay_3 del = new Delaunay_3();
		Point_3[] P = new Point_3[] {
				new Point_3(0.26007989982647706,0.0770141866106312,0.6290481228869144),
				new Point_3(0.8025368614435817,0.25034263978260585,0.2243090162475877),
				new Point_3(0.9717773399026567,0.3016046408321127,0.8939414964788058),
				new Point_3(0.12952105603163888,0.07022340687643247,0.6225397225778694),
				new Point_3(0.18260316615910155,0.9272357010551369,0.9256600272248054),
				new Point_3(0.858401394305793,0.34482931382408555,0.2587439777872731),
				new Point_3(0.5463797187734596,0.7005142964718137,0.9090360998511524),
				new Point_3(0.584355844698825,0.0919257346382939,0.8265054854683757),
				new Point_3(0.9265764380442612,0.6948216656943689,0.12549598703990839),
				new Point_3(0.22959046504762892,0.552863572280065,0.2873314505740887)
		};
		for (int i=0; i<10; i++) {
			System.out.println("Inserting " + P[i]);
			del.insert(P[i]);
		}
		del.isValid();
		del.insert(new Point_3(0.8025368614435817,0.25034263978260585,0.2243090162475877));
		del.isValid();
		System.out.println("done.");
	}

	// Standard test with uniform random points in cube
	private static void test2 () {
		Delaunay_3 del = new Delaunay_3();
		for (int i=0; i<10000; i++) {
			Point_3 p = new Point_3(Math.random(), Math.random(), Math.random());
			System.out.println("inserting " + p);
			del.insert(p);
		}
		for (int i=0; i<100000; i++) {
			Point_3 p = new Point_3(Math.random(), Math.random(), Math.random());
			System.out.println("locating " + p);
			del.locate(p);
		}
		del.isValid();
		del.writeToFile("delaunay.off");
//		new MeshViewer(del);
	}

	// Example of two non-coplanar lines, on which the running time is quadratic
	private static void test3 () {
		Delaunay_3 del = new Delaunay_3();
		for (int i=0; i<5000; i++) {
			Point_3 p = new Point_3(1-2*Math.random(), 1e-6-2e-6*Math.random(), -1+1e-6-2e-6*Math.random());
			Point_3 q = new Point_3(1e-6-2e-6*Math.random(), 1-2*Math.random(), 1+1e-6-2e-6*Math.random());
			System.out.println("inserting " + p);
			del.insert(p);
			System.out.println("inserting " + q);
			del.insert(q);
		}
		System.out.println("done.");
	}

	// Test for cells duals
	private static void test4 () {
		Delaunay_3 del = new Delaunay_3();
		for (int i=0; i<50; i++) {
			Point_3 p = new Point_3(Math.random(), Math.random(), Math.random());
			System.out.println("inserting " + p);
			del.insert(p);
		}
		del.isValid();

		Point_3[] points = new Point_3[2*del.cells().size()];
		int[][] edges = new int[points.length/2][2];
		int i=0;
		for (TriangulationDSCell_3<Point_3> c : del.finiteCells()) {
			points[2*i] = del.dual(c);
			points[2*i+1] = new Point_3(points[2*i].getX().doubleValue()*1.01, 
					points[2*i].getY().doubleValue()*1.01,
					points[2*i].getZ().doubleValue()*1.01);
			edges[i] = new int[] {2*i, 2*i+1};
			i++;
		}		

//		del.writeToFile("delaunay3D.off");
		MeshViewer m = new MeshViewer(del, true);
		m.points = points;
		m.edges = edges;
		m.edgeColors = new Color[edges.length];
	}

	// Test for vertices duals
	private static void test5 () {
		Delaunay_3 del = new Delaunay_3();
		for (int i=0; i<50; i++) {
			Point_3 p = new Point_3(Math.random(), Math.random(), Math.random());
			System.out.println("inserting " + p);
			del.insert(p);
		}
		del.isValid();

//		del.writeToFile("delaunay3D.off");
		MeshViewer m = new MeshViewer(del, true);
		m.trianglesCollection = del.dual(del.randomFiniteVertex());
	}
}
