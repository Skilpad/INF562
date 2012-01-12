package Jcg.triangulations3D;

import Jcg.geometry.*;

import java.util.*;


/**
 * Data structure for representing 3D triangulations that uses the standard cell/vertex encoding, 
 * represented by classes TriangulationDSCell_3 and TriangulationDSVertex_3 respectively. In this 
 * representation we store the collection of 3D cells as well as the collection of vertices of 
 * the triangulation. Each cell provides access to its vertices and neighboring cells, while each
 * vertex provides access to one of its incident cells. 
 *
 * @author Luca Castelli Aleardi and Steve Oudot
 *
 */
public class TriangulationDS_3<X extends Point_3> {

	public ArrayList<TriangulationDSCell_3<X>> cells;
    public ArrayList<TriangulationDSVertex_3<X>> vertices;
	    
    /**
     * creates an empty triangulation data structure.
     *
     */
    public TriangulationDS_3() {
    	this.cells=new ArrayList<TriangulationDSCell_3<X>>();
    	this.vertices=new ArrayList<TriangulationDSVertex_3<X>>();    
    }
    
    /**
     * creates an empty triangulation data structure with prescribed numbers of cells and vertices.
     */
    public TriangulationDS_3(int nCells, int nVertices) {
    	this.cells=new ArrayList<TriangulationDSCell_3<X>>(nCells);
    	this.vertices=new ArrayList<TriangulationDSVertex_3<X>>(nVertices);
   }
    
    /**
     * creates a new cell and adds it to the cells container of the triangulation data structure.
     */
    public TriangulationDSCell_3<X> createCell(){
    	TriangulationDSCell_3<X> newCell=new TriangulationDSCell_3<X>();
    	this.cells.add(newCell);
    	return newCell;
    }

    /**
     * returns the number of vertices of the triangulation.
     */
    public int sizeOfVertices(){
    	return this.vertices.size();
    }

    /**
     * returns the number of edges of the triangulation.
     * @return
     */
    public int sizeOfCells(){
    	return this.cells.size();
    }

    /**
     * checks whether the triangulation contains cell c.
     * @param c
     * @return
     */
    public boolean hasCell(TriangulationDSCell_3<X> c) {
    	return cells.contains(c);
    }

    /**
     * checks whether the triangulation contains vertex v.
     */
    public boolean hasVertex(TriangulationDSVertex_3<X> v) {
    	return vertices.contains(v);
    }
	
    /**
     * creates a new cell in the triangulation, 
     * with the given incident vertices and adjacent cells.
     */        
    public TriangulationDSCell_3<X> createCell(
    	 TriangulationDSVertex_3<X> v1, TriangulationDSVertex_3<X> v2,
    	 TriangulationDSVertex_3<X> v3, TriangulationDSVertex_3<X> v4,	
    	 TriangulationDSCell_3<X> c1, TriangulationDSCell_3<X> c2,
    	 TriangulationDSCell_3<X> c3, TriangulationDSCell_3<X> c4){
    	
    	TriangulationDSCell_3<X> newCell=new TriangulationDSCell_3<X>(
    			v1,v2,v3,v4,c1,c2,c3,c4);
    	for (int i=0; i<4; i++)
    		if (newCell.neighbors.get(i) != null) {
    			// update neighbor pointer in neighboring cell
    			TriangulationDSCell_3<X> c = newCell.neighbors.get(i);
    			c.setNeighbor(c.index(newCell), newCell);
    		}
    	this.cells.add(newCell);
    	return newCell;
    }

    /**
     * checks whether a facet is shared by two cells c1 and c2:
     * the function check whether the corresponding vertices
     * appear both in c1 and c2, in the correct order.
     */        
    public boolean areEqual(TriangulationDSCell_3<X> c1, int i1, TriangulationDSCell_3<X> c2, int i2) {
    	FacetHandle<X> f1=new FacetHandle<X>(c1,i1);
    	FacetHandle<X> f2=new FacetHandle<X>(c2,i2);
    	
    	TriangulationDSVertex_3<X> v1=f1.vertex(1); // first vertex of facet f1
    	TriangulationDSVertex_3<X> v2=f1.vertex(2); // second vertex of facet f1
    	TriangulationDSVertex_3<X> v3=f1.vertex(3); // third vertex of facet f1

    	boolean contained=f2.hasVertex(v1);
    	if(contained==false) return false;
    	int index=c2.index(v1); // index of vertex v in cell c2
    	
    	if(f2.vertex((index+1)%3)!=v3) return false;
    	if(f2.vertex((index+2)%3)!=v2) return false;
    	
    	return true;
    }

	/**
     * Returns the collection of all triangulation cells incident to vertex v.
     */        
	public Collection<TriangulationDSCell_3<Point_3>> incidentCells (TriangulationDSVertex_3<Point_3> v) {
		HashSet<TriangulationDSCell_3<Point_3>> res = new HashSet<TriangulationDSCell_3<Point_3>>();
		TreeSet<TriangulationDSCell_3<Point_3>> toBeVisited = new TreeSet<TriangulationDSCell_3<Point_3>>();
		toBeVisited.add(v.getCell());
		while (!toBeVisited.isEmpty()) {
			TriangulationDSCell_3<Point_3> c = toBeVisited.first();
			toBeVisited.remove(c);
			// add cell to result
			res.add(c);
			// propagate wavefront among the neighbors of the cell
			int ind = c.index(v);
			for (int i=1; i<4; i++) // iterate over facets of c that are incident to v
				if (c.neighbor((ind+i)&3) != null && !res.contains(c.neighbor((ind+i)&3)))
					toBeVisited.add(c.neighbor((ind+i)&3));
		}
		return res;
	}

	/**
     * Returns the collection of all triangulation cells incident to edge (c,i,j).
     */        
	public Collection<TriangulationDSCell_3<Point_3>> incidentCells (TriangulationDSCell_3<Point_3> c, int i, int j) {
		TriangulationDSVertex_3<Point_3> u = c.vertex(i), v = c.vertex(j);
		LinkedHashSet<TriangulationDSCell_3<Point_3>> res = new LinkedHashSet<TriangulationDSCell_3<Point_3>>();

		// circulate around the edge and insert the cells in order into the result
		TriangulationDSCell_3<Point_3> cstart = c;
		TriangulationDSCell_3<Point_3> cprev = null;
		do {
			// insert currently visited cell into result
			res.add(c);
			// look for next cell around edge
			int k=0;
			for (; k<4; k++)
				if (c.neighbor(k) != cprev && c.neighbor(k).hasVertex(u) && c.neighbor(k).hasVertex(v)) {
					cprev = c;
					c = c.neighbor(k);
					break;
				}
			// we should have changed cells here
			if (k==4)
				throw new Error ("no next cell found");
		} while (c != cstart);

		return res;
	}

	/**
     * Returns the collection of all triangulation cells incident to edge (u,v).
     */        
	public Collection<TriangulationDSCell_3<Point_3>> incidentCells (TriangulationDSVertex_3<Point_3> u, TriangulationDSVertex_3<Point_3> v) {
		// look for common cell of u,v
		Collection<TriangulationDSCell_3<Point_3>> ic = incidentCells(u);
		for (TriangulationDSCell_3<Point_3> c : ic)
			if (c.hasVertex(v))
				return incidentCells (c, c.index(u), c.index(v));
		throw new Error ("no common edge found between input vertices");
	}		

	/**
     * checks the combinatorial validity of the triangulation.
     */        
    public boolean isValid() {
    	boolean result=true;
    	System.out.print("Checking combinatorial validity...");
    	for(TriangulationDSVertex_3<X> p: this.vertices){
    		if(p==null) { 
    			result=false; 
    			System.out.println("vertex with null point"); 
    			}
    		if(p!=null && p.getCell()==null) { 
    			result=false; 
    			System.out.println("vertex with null associated cell"); 
    		}
    		if(p!=null && !hasCell(p.getCell())) { 
    			result=false; 
    			System.out.println("vertex with non-existing associated cell"); 
    		}
    	}
    	for(TriangulationDSCell_3<X> c: this.cells){
    		if (c==null) {
    			result = false;
        		System.out.println("null cell");
    		}
    		if (c!=null && (c.vertex(0)==null || c.vertex(1)==null || c.vertex(2)==null || c.vertex(3)==null)) { 
    			result=false; 
    			System.out.println("cell with null vertex"); 
    		}
    		if (c!=null && (!hasVertex(c.vertex(0)) || !hasVertex(c.vertex(1)) || !hasVertex(c.vertex(2)) || !hasVertex(c.vertex(3)))) { 
    			result=false; 
    			System.out.println("cell with non-existing vertex"); 
    		}
    		
    		// check for the coherence of the 4 neighbors of a given cell
    		if(c!=null) {
    			for(int i=0;i<4;i++) {
    				TriangulationDSCell_3<X> adjacentCell=c.neighbor(i);
    				if(adjacentCell!=null && adjacentCell.neighbors.contains(c)==false) {
    					System.out.println("adjacent cell error");
    					result=false;
    				}
    			}
    		}
    	}
    	System.out.println(" done");

    	return result;
    }

    /**
     * creates a tetrahedron with vertices p1, p2, p3 and p4.
     */        
    public TriangulationDSCell_3<X> makeTetrahedron(X p1, X p2, X p3, X p4) {
    	TriangulationDSVertex_3<X> v1=new TriangulationDSVertex_3<X>(p1);
    	TriangulationDSVertex_3<X> v2=new TriangulationDSVertex_3<X>(p2);
    	TriangulationDSVertex_3<X> v3=new TriangulationDSVertex_3<X>(p3);
    	TriangulationDSVertex_3<X> v4=new TriangulationDSVertex_3<X>(p4);
    	
    	TriangulationDSCell_3<X> c=createCell(v1,v2,v3,v4,null,null,null,null);
    	v1.setCell(c); 
    	v2.setCell(c);
    	v3.setCell(c); 
    	v4.setCell(c);
    	
    	this.vertices.add(v1);
    	this.vertices.add(v2);
    	this.vertices.add(v3);
    	this.vertices.add(v4);
    	
    	return c;
    }

    /**
     * Inserts point p in cell c. 
     * Cell c is split into 4 tetrahedra. p must lie strictly inside c.
     */    
    public TriangulationDSVertex_3<X> insertInCell(X p, TriangulationDSCell_3<X> c) {
    	if (c == null)
    		throw new Error ("Trying to star a null cell");
    	TriangulationDSVertex_3<X> v = new TriangulationDSVertex_3<X>(p);
    	vertices.add(v);
    	ArrayList<TriangulationDSCell_3<X>> createdCells = new ArrayList<TriangulationDSCell_3<X>> (); 
    	for (int i=0; i<4; i++) {
    		// create 3-cell ci joining v to the face of c opposite to vertex i
    		TriangulationDSCell_3<X> ci = createCell(c.vertex((i+1)&3), c.vertex((i+2)&3), c.vertex((i+3)&3), v, null,null,null,null);
    		// link ci to cell on the other side of the face i of c
    		TriangulationDSCell_3<X> cip = c.neighbor(i);
    		if (cip != null) {
    			cip.setNeighbor(cip.index(c), ci);
    			ci.setNeighbor(ci.vertices.indexOf(v), cip);
    		}
//    		else
//    			System.out.println("Warning: facet (" + c + "," + i +") has null incident face");
    		// connect ci to the previously constructed 3-cells
    		for (int j=0; j<i; j++) {
    			TriangulationDSCell_3<X> cj = createdCells.get(j);
    			// connection requires to retrieve opposite vertex index in each cell
    			ci.setNeighbor(ci.index(cj), cj);
    			cj.setNeighbor(cj.index(ci), ci);
    		}
    		createdCells.add(ci);
    	}
    	// assign v to one of its incident cells
    	v.setCell(createdCells.get(0));
    	// delete cell c from triangulation
    	cells.remove(c);
    	// return new vertex
    	return v;
    }
    

    /**
     * Inserts point p outside the triangulation. 
     * A new cell is added and attached to cell c 
     * (they share the facet opposite to vertex i in c).
     */    
    public TriangulationDSVertex_3<X> insertOutside(X point, TriangulationDSCell_3<X> c, int i) {
    	FacetHandle<X> f=new FacetHandle<X>(c,i);
    	TriangulationDSVertex_3<X> v0=f.vertex(2);
    	TriangulationDSVertex_3<X> v1=f.vertex(1);
    	TriangulationDSVertex_3<X> v2=f.vertex(0);
    	
    	TriangulationDSVertex_3<X> newVertex=new TriangulationDSVertex_3<X>(point);
    	TriangulationDSCell_3<X> newCell=this.createCell(
    			v0, v1, v2, newVertex, null, null, null, c);
    	newVertex.setCell(newCell);
    	this.vertices.add(newVertex);
    	
    	return newVertex;
    }

    /**
     * testing function.
     */    
    public static void main(String[] args){
    	System.out.println("Testing TriangulationDS_3");
    	Point_3 p00=new Point_3(0.,0.,0.);
    	Point_3 p10=new Point_3(1.,0.,0.);
    	Point_3 p01=new Point_3(0.,1.,0.);
    	Point_3 p001=new Point_3(0.,0.,1.);
    	Point_3 p_1=new Point_3(0.,0.,-1.);
    	
    	TriangulationDS_3<Point_3> mesh3D=new TriangulationDS_3<Point_3>();
    	TriangulationDSCell_3<Point_3> c=mesh3D.makeTetrahedron(p00, p10, p01, p001);
    	mesh3D.isValid();

    	mesh3D.insertOutside(p_1, c, 3);
    	mesh3D.isValid();

    	mesh3D.insertInCell(new Point_3(0.1, 0.1, 0.1), mesh3D.cells.get(0));
    	mesh3D.isValid();

    	mesh3D.insertInCell(new Point_3(0.1, 0.1, -0.1), mesh3D.cells.get(0));  // first cell has been removed
    	mesh3D.isValid();
    }
}
