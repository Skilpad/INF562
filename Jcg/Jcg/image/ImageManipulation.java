package Jcg.image;

import java.io.*;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.*;
import java.awt.image.*;

import Jcg.*;
import Jcg.geometry.*;

import javax.imageio.*;

/*
 * Class providing primitives for image manipulation
 */
class ImageManipulation {

    static final float Xr = 0.964221f;  // reference white D50
    static final float Yr = 1.0f;
    static final float Zr = 0.825211f;
    
    /**
     * Compute pixel intensity
     */
    public static float computePixelGrayIntensity(float R, float G, float B) {
    	return (float)(0.30*R + 0.59*G + 0.11*B);
	}

    /**
     * Compute pixel intensity
     */
    public static float computePixelGrayIntensity(int[] pixel) {
    	return computePixelGrayIntensity((float)(pixel[0]/256.), (float)(pixel[1]/256.), (float)(pixel[2]/256.));
	}


    /*public static PointCloud rasterToPointCloud (Raster r, int dimx, int dimy, int dim) {
    	PointCloud res = new PointCloud<Point_>(dimx * dimy);
    	for (int j=0; j<dimy; j++)
    	    for (int i=0; i<dimx; i++) {
    		int[] pix = new int [3];
    		r.getPixel(i, j, pix);
//      		System.out.println("pix = [" + pix[0] + " " + pix[1] + " " 
//     				   + pix[2] + "]");
    		// convert pixel to L*u*v*
    		float[] pixluv = new float [3];
    		//ImageManipulation.rgb2luv(pix[0], pix[1], pix[2], pixluv);
    		double[] coord;
    		Point_ newPoint;
    		if (dim == 3) {
    		    coord = new double [3];
    		    coord[0] = pixluv[0];
    		    coord[1] = pixluv[1];
    		    coord[2] = pixluv[2];
    		    newPoint=new Point_d(3);
    		}
    		else if (dim == 5) {
    		    coord = new double [5];
    		    coord[0] = (double)i;
    		    coord[1] = (double)j;
    		    coord[2] = pixluv[0];
    		    coord[3] = pixluv[1];
    		    coord[4] = pixluv[2];
    		    newPoint=new Point_d(5);
    		}		   
    		else {
    		    throw new RuntimeException ("Bad dimension!");
    		}
    		res.add(newPoint);
    	    }
    	return res;
    }*/

    public static float[] rasterToGrayPixels (Raster r, int dimx, int dimy) {
    	float[] result= new float[dimx * dimy];
    	
    	int cont=0;
    	for (int j=0; j<dimy; j++) {
    	    for (int i=0; i<dimx; i++) {
    	    	int[] pix = new int [3];
    	    	r.getPixel(i, j, pix);
    	    	result[cont]=computePixelGrayIntensity(pix);
    	    	
    	    	//System.out.print("Pixel "+cont+" = ");
    	    	//System.out.print("[" + pix[0] + " " + pix[1] + " " + pix[2] + "]");
    	    	//System.out.println(" -> intensity "+result[cont]);
    	    	cont++;
    	    }
    	}
    	return result;
    }

/*    public static void PointCloudToRaster(PointCloud<Point_> n, Color[] cols, WritableRaster r, int dimx, int dimy, int dim) {

    	// points should be taken out in reverse order
    	for (int j=dimy-1; j>=0; j--)
    	    for (int i=dimx-1; i>=0; i--) {
    		// n should not be null
    		if (n == null)
    		    throw new RuntimeException ("Empty point cloud!");
    		if (n.p.cluster < 0)
    		    throw new RuntimeException ("Unclustered point!");
    		
    		int[] pix = {cols[n.p.cluster].getRed(),
    			     cols[n.p.cluster].getGreen(),
    			     cols[n.p.cluster].getBlue()};
    		r.setPixel(i, j, pix);

    		n = n.next;  // move to next point in cloud
    	    }
    }*/

    /*    public static void PointCloudToRaster(PointCloud<Point_> n, Color[] cols, WritableRaster r, int dimx, int dimy, int dim) {

	// points should be taken out in reverse order
	for (int j=dimy-1; j>=0; j--)
	    for (int i=dimx-1; i>=0; i--) {
		// n should not be null
		if (n == null)
		    throw new RuntimeException ("Empty point cloud!");
		if (n.p.cluster < 0)
		    throw new RuntimeException ("Unclustered point!");
		
		int[] pix = {cols[n.p.cluster].getRed(),
			     cols[n.p.cluster].getGreen(),
			     cols[n.p.cluster].getBlue()};
		r.setPixel(i, j, pix);

		n = n.next;  // move to next point in cloud
	    }
}*/

    
    public static void pixelsToRaster(float[] pixels, WritableRaster r, int dimx, int dimy) {
    	// points should be taken out in reverse order
    	for (int j=0; j<dimy; j++)
    		for (int i=0; i<dimx; i++) {
    			// Compute RGB components
    			int pixelIndex=i+j*dimx;
    			Color color=new Color(pixels[pixelIndex], pixels[pixelIndex], pixels[pixelIndex]);
    			int[] pix = {color.getRed(), color.getGreen(), color.getBlue()};
    			r.setPixel(i, j, pix);    			
    			//System.out.print("Pixel: "+pixelIndex+" - Intensity: "+pixels[pixelIndex]+" - Color : "+color);
    			//System.out.println(" - RGB: ("+pix[0]+","+pix[1]+","+pix[2]+")");
	    }
    }

    public static BufferedImage loadImage(String fileName) {  
    	BufferedImage bimg = null;  
    	try {  
    		bimg = ImageIO.read(new File(fileName));  
    	} catch (Exception e) {  
    		e.printStackTrace();  
    		}  
    	return bimg;  
    }  
    
    
    
    //-------------------------------    
    //-- 			Test		   --
    //-------------------------------

    
    
    public static void main(String[] args) {
    	System.out.println("Preliminary tests");    
    	testLoadingImage(args);
    	//testLoadingData(args);
    }  
	public int[] getPixelsFromImage(Image img) {
		PixelGrabber pg = new PixelGrabber(img , 0 , 0 , -1 , -1 , true);
		try { pg.grabPixels();} 	catch (InterruptedException e) { }
		int height=pg.getHeight(); int width=pg.getWidth();
		System.out.println("Image loaded: "+" "+width+" x "+height);
		int[] raster = (int[])pg.getPixels();

		//int[] rastero=BilateralFiltering(raster, sigmas, sigmai, 1);
		//printGaussian(0.5, 3);
		
		ImageProducer ip = new MemoryImageSource(width , height ,raster , 0 , width);
		//Image result = createImage(ip);
		return raster;
		}
    
    public static void testLoadingImage(String[] args) {
    	System.out.println("Loading image data: ex 0");    	    	
    	if (args.length < 1) {
    		System.out.println("Usage: java ImageManipulation image");
    		System.exit(0);
    	}
    	// load input image
    	BufferedImage inputImage = ImageManipulation.loadImage(args[0]); 
    	BufferedImage outputImage = ImageManipulation.loadImage(args[0]); 
    	int dimx = inputImage.getWidth();
    	int dimy = inputImage.getHeight();
    	// get array of pixels from image
    	WritableRaster inputRaster = inputImage.getRaster(); 
    	WritableRaster outputRaster = outputImage.getRaster(); 
    	System.out.println("Pixels array created from image");
    	
    	//PointCloud N = ImageManipulation.rasterToPointCloud (raster, dimx, dimy, dim);
    	float[] pixels=rasterToGrayPixels(inputRaster, dimx, dimy);
    	pixelsToRaster(pixels, outputRaster, dimx, dimy);
    	
    	// draw input image and corresponding point cloud
    	//System.out.println ("dim = " + dim + ", size = " + N.size());
    	Fenetre fInput=new Fenetre(inputImage, "input image"); 	
    	Fenetre fOutput=new Fenetre(outputImage, "output image"); 	
    }

    public static void testLoadingData(String[] args) {
    	System.out.println("Loading point cloud from data: ex 0");    	    	
    	if (args.length < 1) {
    		System.out.println("Usage: java ImageManipulation filename.dat");
    		System.exit(0);
    	}
    	// load point cloud
    	PointCloud N;
    	//N=PointCloud.randomPoints(10000, 3);
    	//N=Clustering.readFile(args[0]);

    	//Draw.draw2D(N, "original point cloud");
    	//Draw.draw3D(N);
    }

}

