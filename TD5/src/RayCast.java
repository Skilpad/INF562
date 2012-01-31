import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import Jcg.Fenetre;
import Jcg.geometry.GeometricOperations_2;
import Jcg.geometry.Point_2;
import Jcg.geometry.Ray_2;
import Jcg.geometry.Segment_2;
import Jcg.geometry.Vector_2;
import Jcg.triangulations2D.Delaunay_2;
import Jcg.triangulations2D.HalfedgeHandle;
import Jcg.triangulations2D.TriangulationDSFace_2;

public class RayCast {

	// public parameters
	public Point_2 position; // viewer's position
	public int orientation;  // viewer's orientation (in degrees compared to abcissa unit vector)
	public int viewAngle;    // viewing angle (in degrees)
	public int viewSize;     // size of the view window, in pixels
	public double [] distancesToObstacles;  // output of raycast 
	public double [] distancesToObstaclesBound;  // output of raycast 

	// private data structures
	Delaunay_2 del;  // data structure for ray casting amongst obstacles
	private Map map;

	public RayCast (String filename, int viewAngle, int viewSize, 
			Point_2 initialPosition, int initialOrientation) throws IOException {
		this.viewAngle = viewAngle;
		this.viewSize = viewSize;
		this.position = initialPosition;
		this.orientation = initialOrientation;
		distancesToObstacles = new double [viewSize];
		distancesToObstaclesBound = new double [viewSize];

		del = new Delaunay_2();
		readConstraints(filename);
	}

	private void readConstraints(String filename) throws IOException {
		File file;
		FileReader readfic;
		BufferedReader input;
		String line;
		System.out.print(" --- Reading obstacles file " + filename + "...");
		file = new File(filename);
		readfic = new FileReader(file);
		input = new BufferedReader(readfic);

		while ((line=input.readLine()) != null) {
			//    		System.out.println ("line=" + line);
			StringTokenizer tok = new StringTokenizer(line);
			double px=Double.parseDouble(tok.nextToken());
			double py=Double.parseDouble(tok.nextToken());            
			Point_2 p = new Point_2(px, py);
			double qx=Double.parseDouble(tok.nextToken());
			double qy=Double.parseDouble(tok.nextToken());            
			Point_2 q = new Point_2(qx, qy);
			//   		System.out.print("Inserting constraint edge [" + p + ";" + q + "]...");
			del.insert(p, q);
			//    		System.out.println(" done");
		}
		input.close();
		System.out.println(" done");
	}	



	/**
	 * Returns first constrained HalfedgeHandle that intersects the ray. 
	 * Returns null if no such intersection exists.
	 * Initial assumption: the input HalfedgeHandle e belongs to a face containing the origin of the ray
	 */ 
	public HalfedgeHandle<Point_2> castRay (Ray_2 r, HalfedgeHandle<Point_2> e) {
		if (e == null)               return null;
		if (e.getOpposite() == null) return null;
		if (e.isMarked()) return e;
		HalfedgeHandle<Point_2> h = e.getOpposite().getNext();
		if (doIntersect(r,h)) return castRay(r, h);
		return castRay(r, h.getNext());
	}

	/**
	 *  Casts all rays from viewer and records corresponding distances to viewer 
	 *  in the field distancesToObstacles
	 */
	public void castRays () {
		double orientation_ = Math.PI*orientation/180.;
//		double a = -viewAngle*Math.PI/360.;
//		double da = Math.PI*((double) viewAngle)/(360.*(double) (viewSize-1));
//		double orientation_ = -Math.PI*orientation/180.;
		double a = viewAngle*Math.PI/360.;
		double da = -Math.PI*((double) viewAngle)/(360.*(double) (viewSize-1));
		HalfedgeHandle<Point_2> e = new HalfedgeHandle<Point_2>(del.locate(position), 0);
		for (int i = 0; i < viewSize; i++) {
			Ray_2 r = new Ray_2(position, new Vector_2(Math.cos(orientation_+a), Math.sin(orientation_+a)));
			HalfedgeHandle<Point_2> e_ = e;
			if (!doIntersect(r, e_)) e_ = e_.getNext();
			if (!doIntersect(r, e_)) e_ = e_.getNext();
			e_ = castRay(r,e);
			distancesToObstacles[i] = (e_ == null) ? -1 : position.distanceFrom(GeometricOperations_2.intersect(segment(e_), r)).doubleValue()*Math.cos(a);
			a += da;
		}
	}

	private boolean doIntersect(Ray_2 r, HalfedgeHandle<Point_2> e) { return GeometricOperations_2.doIntersect(new Segment_2(e.getVertex(0).getPoint(),e.getVertex(1).getPoint()), r); }
	private Segment_2 segment(HalfedgeHandle<Point_2> e) { return new Segment_2(e.getVertex(0).getPoint(),e.getVertex(1).getPoint()); }

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage : java RayCast <filename>");
			return;
		}

		final int viewAngle = 45;
		final int viewSize = 600;
		RayCast r = new RayCast(args[0], viewAngle, viewSize, new Point_2(-1.5,1.2), -45);
		r.castRays();
		RayCastWindow f = new RayCastWindow (r);
		//		Map map = new Map(r);
		//		Collection<TriangulationDSFace_2<Point_2>> facesDel = r.del.finiteFaces();
		//		LinkedList<Point_2[]> trianglesDel = new LinkedList<Point_2[]>();
		//		for (TriangulationDSFace_2<Point_2> f : facesDel)
		//			trianglesDel.add(new Point_2[]{f.vertex(0).getPoint(), f.vertex(1).getPoint(), f.vertex(2).getPoint()});
		//		Collection<HalfedgeHandle<Point_2>> cEdgesDel = r.del.constraintEdges();
		//		LinkedList<Point_2[]> cSegmentsDel = new LinkedList<Point_2[]> ();
		//		for (HalfedgeHandle<Point_2> e : cEdgesDel)
		//			cSegmentsDel.add(new Point_2[]{e.getVertex(0).getPoint(), e.getVertex(1).getPoint()});
		//		map.addTriangles(trianglesDel);
		//		map.addFatSegments(cSegmentsDel);	
	}

}
