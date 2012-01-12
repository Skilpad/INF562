package Jcg.image;

import java.util.ArrayList;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jcg.geometry.Point_2;
import Jcg.graph.AdjacencyGraph;
import Jcg.graph.Graph;
//import Jcg.graphDrawing.SpectralDrawing_2;

/**
 * Provides methods for drawing graphs in 2D using spectral based methods
 *
 * @author Luca Castelli Aleardi
 */
class GraphPartitioning {

	Matrix laplacian;
	int n;

	public GraphPartitioning(int n) {
		this.n=n;
	}

	
	/**
	 * computes and returns the adjacency matrix of a graph
	 */
	public Matrix AdjacencyMatrix() {
		double[][] m=new double[n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				//if(this.g.adjacent(i,j)==true)
				//	m[i][j]=1.;
				//else m[i][j]=0.;
				double value=(int)(Math.random()*1.2);
				m[i][j]=value;
				m[j][i]=value;
			}
	   	}
	   	return new Matrix(m);
	}

	/**
	 * computes and returns the laplacian matrix of a graph
	 */	
/*	public Matrix LaplacianMatrix() {
		double[][] m=new double[this.g.sizeVertices()][this.g.sizeVertices()];
		for(int i=0;i<this.g.sizeVertices();i++) {
			for(int j=0;j<this.g.sizeVertices();j++)
				if(i==j) m[i][j]=this.g.degree(i);
				else if(this.g.adjacent(i,j)==true)
					m[i][j]=-1.;
				else m[i][j]=0.;
	   	}
	   	return new Matrix(m);
	}*/
	
	public double[] Eigenvalues(Matrix m){
		EigenvalueDecomposition decomposition=m.eig();   	  					
		double[] eigenvalues=decomposition.getRealEigenvalues();

//		System.out.println("eigenvalues: ");
//		for(int i=0;i<eigenvalues.length;i++)
//			System.out.println(""+eigenvalues[i]);
	   	  		
		return eigenvalues;
	}

	public double[][] Eigenvectors(Matrix m){
		EigenvalueDecomposition decomposition=m.eig();   	  					
	   	Matrix eigenVectorsMatrix=decomposition.getV();
	   	double[][] eigenvectors=eigenVectorsMatrix.getArray();
	   	
//	   	System.out.println("eigenvectors matrix");
//	   	eigenVectorsMatrix.print(2,2);
	   	
	   	return eigenvectors;
	}
	
	public Point_2 computeCoordinates_2(int i,double[] eigenvalues, double[][] eigenvectors) {
		double x,y;
		x=eigenvectors[i][1]/Math.sqrt(eigenvalues[1]);
		y=eigenvectors[i][2]/Math.sqrt(eigenvalues[2]);
		return new Point_2(x,y);
	}
	

	/**
	 * Test SpectralDrawing methods
	 */
	public static void main(String[] args) {
		System.out.println("Spectral Graph Partitioning");		
		//Graph g=AdjacencyGraph.constructCube();
		System.out.println("graph initialized");
		
		GraphPartitioning cut=new GraphPartitioning(1200);
		Matrix a=cut.AdjacencyMatrix();		
		System.out.println("adjacency matrix computed");
		
		double[][] v=cut.Eigenvectors(a);
		System.out.println("eigenvectors computed");
	}

}


