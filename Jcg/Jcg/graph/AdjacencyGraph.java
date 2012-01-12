package Jcg.graph;

import Jcg.polyhedron.*;

/**
 * An implementation of a generic (undirected, possibly weighted) graph, using an adjacency matrix
 *
 * @author Luca Castelli Aleardi
 */
public class AdjacencyGraph implements Graph{

    int n=0; // number of vertices
    int[][] adjacency;
    double[][] weight;

    public AdjacencyGraph() { }

    public AdjacencyGraph(int n) {
    	this.n  = n;
    	this.adjacency=new int[n][n];
    	this.weight=new double[n][n];
    }

    public AdjacencyGraph(int[][] adjacency) {
    	this.adjacency=adjacency;
    	this.n=adjacency[0].length;
    	this.weight=new double[n][n];
    }

    public AdjacencyGraph(int[][] adjacency, double[][] weight) {
    	this.adjacency=adjacency;
    	this.weight=weight;
    	this.n=adjacency[0].length;
    }

    /**
     * add an edge between vertices d and a
     * By default, the edge weight is set to 1
     */
    public void addEdge(int d, int a) {
		if(d<0 || a<0) throw new Error("addEdge: vertex index error");
		adjacency[d][a]=1;
		adjacency[a][d]=1;
		weight[d][a]=1.;
		weight[a][d]=1.;
    }

    /**
     * add an edge with a given weight
     */
    public void addEdge(int d, int a, double weight) {
		if(d<0 || a<0) throw new Error("addEdge: vertex index error");
		adjacency[d][a]=1;
		adjacency[a][d]=1;
		this.weight[d][a]=weight;
		this.weight[a][d]=weight;
    }

    /**
     * add an edge with a given weight
     */
    public void setWeight(int d, int a, double weight) {
		if(d<0 || a<0) throw new Error("setWeight: vertex index error");
		this.weight[d][a]=weight;
		this.weight[a][d]=weight;
    }

    /**
     * remove an edge between vertices d and a
     */
    public void removeEdge(int d, int a) {
		if(d<0 || a<0) throw new Error("removeEdge: vertex index error");
		adjacency[d][a]=0;
		adjacency[a][d]=0;
		weight[d][a]=0.;
		weight[a][d]=0.;
    }
    
    /**
     * test whether two vertices are adjacent in the graph
     */
    public boolean adjacent(int d, int a) {
		if(d<0 || a<0) throw new Error("adjacent: vertex index error");
		if(adjacency[d][a]==1 && adjacency[a][d]==1)
			return true;
		else if(adjacency[d][a]==0 && adjacency[a][d]==0)
			return false;
		else throw new Error("adjacent vertices: error");
    }

    /**
     * return the weight of edge (d,a)
     */
    public double getWeight(int d, int a) {
		if(d<0 || a<0) throw new Error("getWeight: vertex index error");
		if(weight==null) throw new Error("weight non defined");
		return this.weight[d][a];
    }

    /**
     * return the number of vertices of the graph
     */
    public int sizeVertices() {
    	return adjacency[0].length;
    }

    /**
     * return the degree of a node
     */
    public int degree(int index) {
    	int d=0;
    	for(int i=0;i<this.n;i++)
    		if(i!=index && adjacency[i][index]!=0) d++;
    	return d;
    }

    /**
     * return the vertices adjacent to a given vertex
     */
    public int[] neighbors(int index) {
    	int[] result=new int[degree(index)];
    	int compt=0;
    	for(int i=0;i<this.n;i++) {
    		if(i!=index && adjacency[i][index]!=0) {
    			result[compt]=i;
    			compt++;
    		}
    	}
    	return result;
    }
 
    /**
     * return the list of edges (pair of vertices)
     */
    public int[][] getEdges() {
    	int numberOfEdges=0;
    	for(int i=0;i<this.n;i++)
    		numberOfEdges=numberOfEdges+degree(i);
    	numberOfEdges=numberOfEdges/2;
    	
    	int[][] result=new int[numberOfEdges][2];
    	int compt=0;
    	for(int i=0;i<this.n;i++) {
    		for(int j=i+1;j<this.n;j++){ 
    			if(adjacency[i][j]!=0) {
    				result[compt][0]=i;
    				result[compt][1]=j;
    				compt++;
    			}
    		}
    	}
    	return result;
    }

    public String toString() {
   		String result="adjacency matrix\n";
   		for(int i=0;i<sizeVertices();i++) {
   			for(int j=0;j<sizeVertices();j++)
   				result=result+" "+this.adjacency[i][j];
   			result=result+"\n";
   		}
   		result=result+"weights matrix\n";
   		for(int i=0;i<sizeVertices();i++) {
   			for(int j=0;j<sizeVertices();j++)
   				result=result+" "+this.weight[i][j];
   			result=result+"\n";
   		}
   		return result;
    }

    /**
     * construct the graph corresponding to the 1-skeleton of a 3D cube
     */    
    public static Graph constructCube(){
		Graph g=new AdjacencyGraph(8);
		g.addEdge(0,1); g.addEdge(1,2); g.addEdge(2,3); g.addEdge(3,0);
		g.addEdge(4,5); g.addEdge(5,6); g.addEdge(6,7); g.addEdge(7,4);
		g.addEdge(0,4); g.addEdge(1,5); g.addEdge(2,6); g.addEdge(3,7);
    	return g;
    }

    /**
     * construct the complete graph K4 (1-skeleton of a tetrahedron)
     */    
    public static Graph constructK4(){
		Graph g=new AdjacencyGraph(4);
		g.addEdge(0,1); g.addEdge(0,2); g.addEdge(0,3); 
		g.addEdge(1,2); g.addEdge(1,3); g.addEdge(2,3);
    	return g;
    }

    /**
     * construct the 1-skeleton of a dodecahedron
     */    
    public static Graph constructDodecahedron(){
		Graph g=new AdjacencyGraph(20);
		g.addEdge(0,1); g.addEdge(1,2); g.addEdge(2,3); g.addEdge(3,4); g.addEdge(4,0); 

		g.addEdge(5,6); g.addEdge(6,7); g.addEdge(7,8); g.addEdge(8,9); g.addEdge(9,10); 
		g.addEdge(10,11); g.addEdge(11,12); g.addEdge(12,13); g.addEdge(13,14); g.addEdge(14,5); 

		g.addEdge(15,16); g.addEdge(16,17); g.addEdge(17,18); g.addEdge(18,19); g.addEdge(19,15);
		
		g.addEdge(0,5); g.addEdge(1,7); g.addEdge(2,9); g.addEdge(3,11); g.addEdge(4, 13); 

		g.addEdge(6,15); g.addEdge(8,16); g.addEdge(10,17); g.addEdge(12,18); g.addEdge(14,19); 

		return g;
    }

    /**
     * Construct the graph corresponding to the 1-skeleton of a 3D surface.
     * Vertices are reordered according to the choice of the outer face.
     * The vertices of the outer face must appear as the first vertices in the graph
     */    
    public static Graph constructFromPolyhedron(Polyhedron_3 polyhedron, int face){
    	int size=polyhedron.sizeOfVertices();
    	Graph g=new AdjacencyGraph(size);
    	int[] newOrder=new int[size];
    	int[] permutation=new int[size];
    	
    	if(face<0 || face>=polyhedron.sizeOfFacets()) throw new Error("error face index");
    	Face outerFace=(Face)polyhedron.facets.get(face);
    	int[] faceVertexIndices=outerFace.getVertexIndices(polyhedron);
    	int faceDegree=outerFace.degree();
    	
    	for(int i=0;i<faceDegree;i++) {
    		newOrder[i]=faceVertexIndices[i];
    		permutation[faceVertexIndices[i]]=i;
    	}
    	
    	int compt=faceDegree;
    	for(int i=0;i<size;i++) {
    		boolean incidentToOuterFace=false;
    		int j=0;
    		while(j<faceDegree && incidentToOuterFace==false) {
    			if(i==newOrder[j])
    				incidentToOuterFace=true;
    			j++;
    		}
    		if(incidentToOuterFace==false) {
    			newOrder[compt]=i;
    			permutation[i]=compt;
    			compt++;
    		}
    	}
    	
    	for(int i=0;i<polyhedron.sizeOfFacets();i++) {
    		Face f=(Face)polyhedron.facets.get(i);
    		int[] indices=f.getVertexIndices(polyhedron);
    		int d=f.degree();
    		for(int j=0;j<d-1;j++) {
    			g.addEdge(permutation[indices[j]], permutation[indices[j+1]]);
    		}
    		g.addEdge(permutation[d-1], permutation[0]);
    	}
    	
    	return g;
    }
                
}

