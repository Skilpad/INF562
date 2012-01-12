import Jcg.viewer.*;
import Jcg.geometry.*;
import Jcg.Fenetre;


/**
 * @author Luca Castelli Aleardi
 *
 */
public class TestJCG {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Testing Jcg library: 2d square");
		
		Point_2 p0=new Point_2(0., 0.);
		Point_2 p1=new Point_2(10., 0.);
		Point_2 p2=new Point_2(10., 10.);
		Point_2 p3=new Point_2(0., 10.);
		
		Fenetre fenetre=new Fenetre();
		fenetre.addPoint(p0);
		fenetre.addPoint(p1);
		fenetre.addPoint(p2);
		fenetre.addPoint(p3);
		fenetre.addSegment(p0, p1);
		fenetre.addSegment(p1, p2);
		fenetre.addSegment(p2, p3);
		fenetre.addSegment(p3, p0);
	}

}
