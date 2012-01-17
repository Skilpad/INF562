

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import Jcg.geometry.*;
import Jcg.Fenetre;
import Jcg.viewer.*;

/**
 * A class for visualizing point clouds in 2D and 3D
 */
public class Draw {

    public static void draw2D(PointCloud N, String title) {
    	System.out.println("Drawing point cloud in 2D... ");
    	if(N==null) {
    		System.out.println("No point to draw");
    		return;
    	}
    	if(N.p.dimension()<2) {
    		System.out.println("error: wrong point dimension "+N.p.dimension());
    		return;
    	}
    	if(N.p.dimension()!=2) {
    		System.out.println("warning: wrong point dimension "+N.p.dimension());
    	}
    	
    	List<Point_2> points2D=new ArrayList<Point_2>();
    	PointCloud t=N;
    	while(t!=null) {
    		points2D.add(new Point_2(t.p));
    		t=t.next;
    	}
    	Fenetre f=new Fenetre(title);
    	f.addPoints(points2D);
    	
    	double[] box=Point_D.boundingBox(N);
    	int dim=N.p.dimension();
    	Point_2 p00=new Point_2(box[0], box[1]);
    	Point_2 p10=new Point_2(box[dim], box[1]);
    	Point_2 p01=new Point_2(box[0], box[dim+1]);
    	Point_2 p11=new Point_2(box[dim], box[dim+1]);
    	f.addSegment(p00, p10);
    	f.addSegment(p00, p01);
    	f.addSegment(p10, p11);
    	f.addSegment(p01, p11);
    	//System.out.println("2D drawing done");
    }

    public static void draw3DNoColors(PointCloud N) {
    	System.out.println("Drawing point cloud in 3D...");
    	if(N==null) {
    		System.out.println("No point to draw");
    		return;
    	}
    	if(N.p.dimension()<3) {
    		System.out.println("warning: wrong point dimension "+N.p.dimension());
    		//return;
    	}
    	
    	List<Point_3> points3D=new ArrayList<Point_3>();
    	PointCloud t=N;
    	while(t!=null) {
    		Point_3 newPoint=null;
    		if(N.p.dimension()==3)
    			newPoint=new Point_3(t.p);
    		else
    			newPoint=new Point_3(t.p.Cartesian(0), t.p.Cartesian(1), 0.);
    		points3D.add(newPoint);
    		t=t.next;
    	}
    	MeshViewer v=new MeshViewer(points3D);
    	//System.out.println("3D drawing done");
    }

    public static void draw3D(PointCloud N) {
    	int sizeCloud=PointCloud.size(N);
    	Color[] colors=new Color[sizeCloud];
    	Color[] randomColors=new Color[sizeCloud];
    	
    	Clustering.assignColors(sizeCloud, null, randomColors);
    	
    	PointCloud t=N;
    	int i=0;
    	while(t!=null) {
    		if(t.p.cluster==-1)
    			colors[i]=Color.red;
    		else
    			colors[i]=randomColors[t.p.cluster];
    		t=t.next;
    		i++;
    	}
    	
    	draw3D(N, colors);
    }

    public static void draw3D(PointCloud N, Color[] colors) {
    	//System.out.println("Drawing point cloud in 3D...");
    	if(N==null) {
    		System.out.println("No point to draw");
    		return;
    	}
    	if(N.p.dimension()<3) {
    		System.out.println("Warning: drawing in dimension 3 a cloud of points of dimension "+N.p.dimension());
    		//return;
    	}
    	
    	List<Point_3> points3D=new ArrayList<Point_3>();
    	PointCloud t=N;
    	while(t!=null) {
    		Point_3 newPoint=null;
    		if(N.p.dimension()==3)
    			newPoint=new Point_3(t.p);
    		else
    			newPoint=new Point_3(t.p.Cartesian(0), t.p.Cartesian(1), 0.);
    		points3D.add(newPoint);
    		t=t.next;
    	}
    	if(colors==null || points3D.size()!=colors.length) {
    		System.out.println("error: colors null");
    		return;
    	}
    	MeshViewer v=new MeshViewer(points3D, colors);
    	//System.out.println("3D drawing done");
    }
    
    public static void main(String[] args) {
    }
}
