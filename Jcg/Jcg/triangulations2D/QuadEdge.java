package Jcg.triangulations2D;


import Jcg.geometry.*;

 
/**
 * Quad-Edge data structure
 *
 * @author Code by X.Philippeau - Structure by Guibas and Stolfi
 *
 * @see Primitives for the Manipulation of General Subdivisions
 *      and the Computation of Voronoi Diagrams (Leonidas Guibas,Jorge Stolfi)
 */
public class QuadEdge {
 
	// pointer to the next (direct order) QuadEdge
	private QuadEdge next;
 
	// pointer to the dual QuadEdge (faces graph <-> edges graph)
	private QuadEdge dual;
 
	// origin point of the edge/face
	private Point_2 orig;
 
	// marker for triangle generation
	public boolean mark=false;
	
	// marker for constraints
	public boolean cMark=false;
	 
	/**
	 * (private) constructor. Use makeEdge() to create a new QuadEdge
	 *
	 * @param next pointer to the next QuadEdge starting from orig in direct order
	 * @param dual   pointer to the next (direct order) crossing edge
	 * @param orig  Origin point
	 */
	protected QuadEdge(QuadEdge next, QuadEdge dual, Point_2 orig) {
		this.next = next;
		this.dual = dual;
		this.orig = orig;
	}
 
	public String toString () {
		return orig() + " " + dest();
	}
	
	// ----------------------------------------------------------------
	//                             Getter/Setter
	// ----------------------------------------------------------------
 
	/**
	 * Returns the next QuadEdge starting at orig() in direct order
	 */
	public QuadEdge next() {
		return next;
	}
 
	/**
	 * Returns the dual QuadEdge, oriented at 90 degrees in direct order
	 */
	public QuadEdge dual() {
		return dual;
	}
 
	/**
	 * Returns the origin vertex of the QuadEdge.
	 */
	public Point_2 orig() {
		return orig;
	}
 
	/**
	 * Sets next QuadEdge starting from orig() in direct order.
	 */
	public void setNext(QuadEdge next) {
		this.next = next;
	}
 
	/**
	 * sets dual QuadEdge.
	 */
	public void setDual(QuadEdge dual) {
		this.dual = dual;
	}
 
	/**
	 * Sets origin vertex of QuadEdge.
	 */
	public void setOrig(Point_2 p) {
		this.orig = p;
	}
 
	// ----------------------------------------------------------------
	//                      QuadEdge Navigation
	// ----------------------------------------------------------------
 
	/**
	 * Returns the symmetric QuadEdge, with origin and destination reversed.
	 */
	public QuadEdge sym() {
		return dual.dual();
	}
 
	/**
	 * Returns the destination vertex of the QuadEdge.
	 */
	public Point_2 dest() {
		return sym().orig();
	}
 
	/**
	 * Returns the symmetric of the dual QuadEdge 
	 * (which is also the dual of the symmetric QuadEdge).
	 */
	public QuadEdge dualSym() {
		return dual.sym();
	}
 
	/**
	 * Returns the previous QuadEdge starting at orig() in direct order 
	 */
	public QuadEdge prev() {
		return dual.next().dual();
	}
 
	/**
	 * Returns the previous QuadEdge along the right cycle (rprev.dest() == this.orig()) 
	 */
	public QuadEdge rprev() {
		return dual().next().dualSym();
	}
 
	/**
	 * Returns the next QuadEdge along the right cycle (rnext.orig() == this.dest())
	 */
	public QuadEdge rnext() {
		return dual().prev().dualSym();
	}
 
	/**
	 * Returns the next QuadEdge along the left cycle (lnext.orig() == this.dest())
	 */
	public QuadEdge lnext() {
		return dualSym().next().dual();
	}
 
	/**
	 * Returns the previous QuadEdge along the left cycle (lprev.dest() == this.orig())
	 */
	public QuadEdge lprev() {
		return dualSym().prev().dual();
	}
 
 
	// ************************** STATIC ******************************
 
 
	/**
	 * Creates a new QuadEdge, setting next and dual fields to default null values.
	 *
	 * @param  orig origin of the segment
	 * @param  dest end of the segment
	 * @return the QuadEdge of the origin point
	 */
	public static QuadEdge makeEdge(Point_2 orig, Point_2 dest) {
		QuadEdge q0 = new QuadEdge(null, null, orig);
		QuadEdge q1 = new QuadEdge(null, null, null);
		QuadEdge q2 = new QuadEdge(null, null, dest);
		QuadEdge q3 = new QuadEdge(null, null, null);
 
		// create the segment
		q0.setNext(q0); q2.setNext(q2); // lonely segment: no "next" quadedge
		q1.setNext(q3); q3.setNext(q1); // in the dual: 2 communicating facets
 
		// dual switch
		q0.dual = q1; q1.dual = q2;
		q2.dual = q3; q3.dual = q0;
 
		return q0;
	}
 
	/**
	 * Merges/splits umbrella around q1.orig() with umbrella around q2.orig(). In other words, 
	 * connects/disconnects their dual cycles
	 *
	 * @param q1,q2 the 2 QuadEdge to attach/detach
	 */
	public static void splice(QuadEdge a, QuadEdge b) {
		QuadEdge alpha = a.next().dual();
		QuadEdge beta  = b.next().dual();
 
		QuadEdge t1 = b.next();
		QuadEdge t2 = a.next();
		QuadEdge t3 = beta.next();
		QuadEdge t4 = alpha.next();
 
		a.setNext(t1);
		b.setNext(t2);
		alpha.setNext(t3);
		beta.setNext(t4);
	}
 
	/**
	 * Creates a new QuadEdge that connects e1.dest() to e2.orig()
     *
	 * @param e1,e2 the 2 QuadEdges to connect
	 * @return the new QuadEdge
	 */
	public static QuadEdge connect(QuadEdge e1, QuadEdge e2) {
		QuadEdge q = makeEdge(e1.dest(), e2.orig());
		splice(q, e1.lnext());
		splice(q.sym(), e2);
		return q;
	}
 
	/**
	 * Performs a flip of QuadEdge e.
     *
	 * @param e the 2 QuadEdges to connect
	 * @return the new QuadEdge
	 */
	public static void flipEdge(QuadEdge e) {
		QuadEdge a = e.prev();
		QuadEdge b = e.sym().prev();
		splice(e, a);
		splice(e.sym(), b);
		splice(e, a.lnext());
		splice(e.sym(), b.lnext());
		e.orig = a.dest();
		e.sym().orig = b.dest();
	}
 
	/**
	 * Deletes a QuadEdge, that is, disconnects it from its cycle 
	 * (same for its dual and its symmetric).
	 *
	 * @param q the QuadEdge to delete
	 */
	public static void deleteEdge(QuadEdge q) {
		splice(q, q.prev());
		splice(q.sym(), q.sym().prev());
	}
	

	public boolean equals(Object o) {
		QuadEdge q = (QuadEdge)o;
		return this.next == q.next && this.dual == q.dual && this.orig == q.orig;
	}

	public int hashCode() {
		int cOrig = orig().hashCode();
		return cOrig*cOrig + dest().hashCode();
	}

}

