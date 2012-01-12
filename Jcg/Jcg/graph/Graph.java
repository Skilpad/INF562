package Jcg.graph;

/**
 * Define the abstract data type for a graph (undirected, possibly weighted)
 *
 * @author Luca Castelli Aleardi
 */
public interface Graph {
	
    public void addEdge(int d, int a);
    public void addEdge(int d, int a, double weight);
    public void removeEdge(int d, int a);
    
    public boolean adjacent(int d, int a);
    public int degree(int index);
    public int[] neighbors(int index);
    public int[][] getEdges();
    public double getWeight(int d, int a);
    public void setWeight(int d, int a, double weight);
    
    public int sizeVertices();
}
