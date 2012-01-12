package Jcg.image;

import Jcg.graph.*;

import java.awt.image.Raster;

import Jama.*;
import Jcg.geometry.PointCloud;
import Jcg.geometry.Point_;
import Jcg.geometry.Point_d;

/**
 * Class implementing spectral segmentation (using graph cuts)
 *
 * @author Luca Castelli Aleardi
 */
public class SpectralSegmentation {
	
    /**
     * Initialize the graph corresponding to an image
     * A completer
     */
    public static Graph gridGraphFromImage (float[] pixels, int dimx, int dimy) {
    	Graph result=new AdjacencyGraph(dimx*dimy);
    	
    	for (int j=0; j<dimy; j++) {
    	    for (int i=0; i<dimx; i++) {
    	    }
    	}
    	return result;
	}

    /**
     * Add in graph g the four neighbors of vertex corresponding to pixel (x,y)
     * A completer
     */
    public static void addVertexNeighbors (Graph g, Raster r, int x, int y, int dimx) {
    	int currentVertex=getVertexIndex(x,y,dimx);
    	if(currentVertex<0 || currentVertex>=g.sizeVertices())
    		throw new Error("Vertex index error");
    	for (int j=-1; j<2; j++) {
    	    for (int i=-1; i<2; i++) {
    	    	int[] pix = new int [3];
    	    	r.getPixel(i, j, pix);
    	    }
    	}
	}
    
    /**
     * Return ...
     */
    public static int getVertexIndex(int i, int j, int dimx) {
    	return i+j*dimx;
    }

    /**
     * Return the (geometric grid) position of a given pixel
     */
    public static int[] getPixelPosition(int index, int dimx) {
    	int[] pos=new int[2];
    	pos[0]=index%dimx;
    	pos[1]=index/dimx;
    	return pos;
    }

    /**
     * Return ...
     * A completer
     */
    public static double computeEdgeWeight(float[] pixels, int x, int y) {
    	double result=0.;
    	
    	return result;
	}

}
