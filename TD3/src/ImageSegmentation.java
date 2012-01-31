


import java.io.*;
import java.util.Calendar;
import java.math.*;
import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.*;

import Jcg.geometry.*;
import Jcg.Fenetre;

class ImageSegmentation extends Clustering {

	// Constructors

	ImageSegmentation (PointCloud n, PointCloud s, 
			double cr, double ar, double ir, double mr) {
		super (n,s,cr,ar,ir, mr);
	}

	ImageSegmentation (PointCloud n, double bandWidth) {
		super (n, bandWidth);
	} 



	//-------------------------------    
	//--- Test image segmentation ---
	//--- no java code to write, only testing Mean-Shift algorithm on images data
	//-------------------------------



	public static void main(String[ ] args) throws Exception {
		System.out.println("Testing image segmentation: ex 3.1");

		if (args.length < 2) {
			System.out.println("Usage: java ImageSegmentation image bandwidth");
			System.exit(0);
		}

		double bandWidth = Double.parseDouble(args[1]);
		// load input image
		BufferedImage bimg = ImageManipulation.loadImage(args[0]); 
		int dimx = bimg.getWidth();
		int dimy = bimg.getHeight();
		// get array of pixels from image, in L*u*v* space
		WritableRaster raster = bimg.getRaster(); 
		System.out.println("Pixels array created from image");

		// build point cloud from raster
		int dim = 3;
		PointCloud N = ImageManipulation.rasterToPointCloud (raster, dimx, dimy, dim);

		// draw input image and corresponding point cloud
		System.out.println ("dim = " + dim + ", size = " + PointCloud.size(N));
		Draw.draw3D(N);
		Fenetre fInput=new Fenetre(bimg, "input image");

		System.out.print("\nPress <Enter> to start computation...");
//		System.in.read();

		ImageSegmentation msc = new ImageSegmentation (N, bandWidth);
		// 	ImageSegmentation msc = new ImageSegmentation (N, N, 0.001, 1.7, 0.001, 1.6);

		Calendar rightNow = Calendar.getInstance(); // to compute time performances
		long time0 = rightNow.getTimeInMillis();

		Point_D[] clusterCenters = msc.detectClusters();  // performing Mean-Shift algorithm

		rightNow = Calendar.getInstance(); // to compute time performances
		long time = rightNow.getTimeInMillis();
		System.out.println("\n Total time to find clusters: " + (time-time0)/1000 + "s " + (time-time0)%1000 + "ms");

		// Assign colors to clusters
		Color[] couleurs = new Color[clusterCenters.length];
		ImageManipulation.assignColors (clusterCenters.length, clusterCenters, couleurs);
		System.out.println("\nCluster centers: "+clusterCenters.length);

		// move new data into raster, and from there into image
		ImageManipulation.PointCloudToRaster (N, couleurs, raster, dimx, dimy, dim);
		bimg.setData(raster);

		Color[] finalColors=new Color[clusterCenters.length];
		PointCloud t=N;
		int i=0;
		while(t!=null) {
			finalColors[i]=couleurs[t.p.cluster];
			t=t.next;
			i++;
		}

		// draw clusters
		Draw.draw3D(N,finalColors);
		// draw new image (after segmentation)
		Fenetre f=new Fenetre(bimg, "Output image");

		RangeSearch.timePerformance();

		System.out.println("Image Segmentation: end");
	}
}


