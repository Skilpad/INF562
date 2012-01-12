package Jcg.image;

import java.applet.Applet;
import java.applet.*;
import java.awt.*;
import java.awt.image.*;

class BilateralFiltering extends Applet {
	static Image img, imgo;
	static int raster []; int rastero [];
	static PixelGrabber pg;
	static int width, height;
	static double sigmas=30.0;
	static double sigmai=1.25;
	static int k=1;
	
	static int [] BilateralFiltering(int [] raster, double sigmas, double sigmai, int k) {
		int [] result=null, rasteri;
		int grey=0, greyc, ngrey,alpha, index, indexc;	
		int i,j, ii, jj,l;
		int bound=(int)(3.0*sigmas);
		double m,p, wg, wr,w;
		
		bound=5;

		for(l=0;l<k;l++) {
			System.out.println("Pass #"+l);
			result=new int[width*height];
			
			for(i=0;i<height;i++)
			for(j=0;j<width;j++) {
				m=p=0.0;
				index=j+i*width;
				//System.out.print("pixel "+index+" bound "+bound);
				alpha=-1;
				
				for(ii=-bound;ii<bound;ii++)
					for(jj=-bound;jj<bound;jj++) {
						if((j+jj>=0) && (i+ii>=0) && (j+jj<width) && (i+ii<height)) {
							indexc=j+jj+(i+ii)*width; // index du pixel voisin
							grey = (raster[index] & 0xFF) ; // couleur pixel courant (a' recalculer)
							greyc= (raster[indexc] & 0xFF); // couleur du pixel voisin
							
							// Remplir ces deux lignes...
							double distance=Math.sqrt((i-ii)*(i-ii)+(j-jj)*(j-jj));
							wg=gaussian(distance,sigmas); 
							//wr=1;
							wr=gaussian(Math.abs(grey-greyc)/20.,sigmai);
							//System.out.println("wg: "+wg+" -  wr: "+wr);
							
							w=wg*wr;
							//w=wg; // normal gaussian 
							m += w*greyc;
							p += w; // for normalization
							//m=grey;
							//p=1.0;
						}
					}
						ngrey=(int)(m/p);
						//System.out.println(" new gray level "+ngrey);
						result [ index ] = ( (alpha << 24) | (ngrey << 16) | (ngrey << 8) | ngrey);	
			}
			raster=result;
		}
		System.out.println("Bilateral filtering computed");
		return result;
	}
	
	public static int gaussian(double d, double sigma) {
		double result=1./(Math.sqrt(2*Math.PI)*sigma);
		result=result*Math.exp(-(d)/(2*sigma*sigma));
		return (int)(result*104);
	}

	public static void printGaussian(double sigma, int bound) {
		double wg;
		for(int ii=-bound;ii<bound;ii++) {
			double distance=ii*ii;
			System.out.print("("+distance+") ");
			wg=gaussian(distance,sigma);
			System.out.println(wg);
		}
	}
	
	public void init() {
	String nameimage="Images/Ferran1.jpg";
	
	// Image image = getImage(getDocumentBase() , nameimage);
    	System.out.print("Opening image from file "+nameimage+" ...");
        img = Toolkit.getDefaultToolkit().getImage(nameimage);
        System.out.println("done");

	pg = new PixelGrabber(img , 0 , 0 , -1 , -1 , true);
	try { pg.grabPixels();} 	catch (InterruptedException e) { }
	height=pg.getHeight(); width=pg.getWidth();
	System.out.println("Image filename:"+nameimage+" "+width+" "+height);
	raster = (int[])pg.getPixels();

	rastero=BilateralFiltering(raster, sigmas, sigmai, 1);
	//printGaussian(0.5, 3);
	
	ImageProducer ip = new MemoryImageSource(width , height ,rastero , 0 , width);
	imgo = createImage(ip);
	this.repaint();
	}
	
	public void paint(Graphics g) {
		g.drawImage(imgo , 0 , 0 , this);
	}
	
public static void main(String[] args) {
// Create the frame this applet will run in
          Frame appletFrame = new Frame("Some applet");

// The frame needs a layout manager, use the GridLayout to maximize
// the applet size to the frame.
          appletFrame.setLayout(new GridLayout(1,0));

// Have to give the frame a size before it is visible
          appletFrame.resize(640, 480);

// Make the frame appear on the screen. You should make the frame appear
// before you call the applet's init method. On some Java implementations,
// some of the graphics information is not available until there is a frame.
// If your applet uses certain graphics functions like getGraphics() in the
// init method, it may fail unless there is a frame already created and
// showing.
          appletFrame.show();

// Create an instance of the applet
          Applet myApplet = new BilateralFiltering();

// Add the applet to the frame
          appletFrame.add(myApplet);

// Initialize and start the applet
          myApplet.init();
          myApplet.start();
          myApplet.repaint();
}

}
