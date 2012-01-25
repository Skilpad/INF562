import Jcg.graph.*;
import Jcg.geometry.*;

import java.util.*;

/**
 * a class for dealing with graph separators in 2D
 *
 * @author Luca Castelli Aleardi
 */
public class Separator_2 extends Separator<Kernel_2> {

//	/**
//	 * compute an edge separator of a given graph.
//	 */
//	public GeometricGraph_2[] Cut(GeometricGraph_2 g) {
//		Point_2 bary = new Point_2(pts);
//		double a = Math.random();
//		double b = Math.random();
//		Line_2 randomLine = new Line_2(a,b, -a*bary.x()-b*bary.y());
//		return geometricCut(g, randomLine);
//	}
//
//	/**
//	 * compute a "geometric" edge separator of a given graph.
//	 * The cut is computed accordingly to a given random plane.
//	 * Return the two (disjoint) corresponding sub-graphs, stored in a table.
//	 */		
//	public GeometricGraph_2[] geometricCut(GeometricGraph_2 g, Line_2 randomLine) {
//		GeometricGraph_2[] res = new GeometricGraph_2[2];
//		for (GraphNode<Point_2> v : g.vertices)
//			if (randomLine.hasOnPositiveSide(v.getPoint())) {
//				res[0].addNode(v);
//				List<GraphNode<Point_2>> neibhbors_ = v.neighbors;
//				v.neighbors = new ArrayList<GraphNode<Point_2>>();
//				for (GraphNode<Point_2> n : neibhbors_)
//					if (randomLine.hasOnPositiveSide(n.getPoint())) v.neighbors.add(n); 
//			} else {
//				res[1].addNode(v);
//				List<GraphNode<Point_2>> neibhbors_ = v.neighbors;
//				v.neighbors = new ArrayList<GraphNode<Point_2>>();
//				for (GraphNode<Point_2> n : neibhbors_)
//					if (!randomLine.hasOnPositiveSide(n.getPoint())) v.neighbors.add(n); 
//			}
//		return res;
//	}
//	

}