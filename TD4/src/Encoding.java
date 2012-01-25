import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Jcg.geometry.*;
import Jcg.graph.*;

import java.util.*;

/**
 * This class provides a set of primitives for dealing with encoding techniques
 *
 * @author Luca Castelli Aleardi
 */
public class Encoding {
	
	public int nVertices;
	public int nEdges;
	public int[] vertexDegree; // vertex degree sequence of the graph (length n)
	public int[] neighborList; // a table containing the concatenation of the adjacency lists
	public int[] sign;
	public int[] differenceEncoding; // the adjacency lists encoded via differences (all integers must non negative)
	
	public Encoding(GeometricGraph g) {
		System.out.print("configuring encoding...");
		this.nVertices=g.sizeVertices();
		this.vertexDegree=new int[this.nVertices];
		nEdges=0;
		
		for(int i=0;i<this.nVertices;i++) {
			vertexDegree[i]=g.getNode(i).degree();
			nEdges=nEdges+vertexDegree[i];
		}
		
		this.sortedNeighborList(g);
		
		this.differenceEncoding=new int[nEdges];
		this.sign=new int[nVertices];
		int edgeCount=0;
		for(int i=0;i<nVertices;i++) {
			GraphNode v=g.getNode(i);
			int difference=this.neighborList[edgeCount]-v.getTag();
			differenceEncoding[edgeCount]=Math.abs(difference);
			if(difference>0)
				this.sign[i]=0;
			else
				this.sign[i]=1;
			edgeCount++;
			for(int j=1;j<v.degree();j++) {
				differenceEncoding[edgeCount]=neighborList[edgeCount]-neighborList[edgeCount-1];
				edgeCount++;
			}
		}
		System.out.println(" done");
	}

	public void sortedNeighborList(GeometricGraph g) {
		this.neighborList=new int[nEdges];
		int edgeCount=0;
		for(int i=0;i<this.nVertices;i++) {
			GraphNode v=g.getNode(i);
			List<GraphNode> vNeighbors=v.neighborsList();
			int[] sortedNeighbors=new int[v.degree()];
			for(int j=0;j<v.degree();j++)
				sortedNeighbors[j]=vNeighbors.get(j).getTag();
			Arrays.sort(sortedNeighbors);
			for(int j=0;j<v.degree();j++) {
				this.neighborList[edgeCount]=sortedNeighbors[j];
				edgeCount++;
			}
		}
	}

	public static double computeEntropy(int[] sequence) {
		throw new Error("a' completer");
	}

	public static double[] computeFrequencies(int[] sequence) {
		throw new Error("a' completer");
	}

	/**
     * Output the connectivity of a graph to a file (without compression)
    */
    public void writeToFile(String filename) {
    	System.out.print("writing graph (not compressed) to file...");
    	try {
    		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
    		out.write (""+this.nVertices+" "+this.nEdges+"\n");
    		
    		for(int i=0;i<this.nVertices;i++)
    			out.write(" "+this.vertexDegree[i]);
    		out.write("\n");

    		for(int i=0;i<this.neighborList.length;i++)
    			out.write(" "+this.neighborList[i]);
    		out.write("\n");
    		
    		//out.write(""+this.sequenceToString(neighborList)+"\n");
    		out.close();
    		} catch (IOException e) {}
    	System.out.println(" done");
    }

	/**
     * Output the encoding to a file (using difference encoding)
    */
    public void encodeToFile(String filename) {
    	System.out.print("encoding graph to file...");
    	try {
    		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
    		out.write (""+this.nVertices+" "+this.nEdges+"\n");

    		for(int i=0;i<this.nVertices;i++)
    			out.write(" "+this.vertexDegree[i]);
    		out.write("\n");

    		for(int i=0;i<this.differenceEncoding.length;i++)
    			out.write(" "+this.differenceEncoding[i]);
    		out.write("\n");

    		for(int i=0;i<this.sign.length;i++)
    			out.write(" "+this.sign[i]);
    		out.write("\n");
    		
    		out.close();
    		} catch (IOException e) {}
    	System.out.println(" done");
    }

    /**
     * Print a sequence of integers
     */    
    public static String sequenceToString(int[] sequence) {
    	if(sequence==null || sequence.length==0)
    		return "sequence not defined";
    	String result="";
    	for(int i=0;i<sequence.length;i++)
    		result=result+" "+sequence[i];
    	return result+"\n";
    }

    /**
     * Print the encoding of the graph
     */    
    public String toString() {
    	//System.out.print("printing encoding...");
    	String result=""+this.nVertices+" "+this.nEdges+"\n";
    	result=result+sequenceToString(this.vertexDegree);
    	//result=result+sequenceToString(this.neighborList);
    	result=result+sequenceToString(this.sign);
    	result=result+sequenceToString(this.differenceEncoding)+"\n";
    	//System.out.print(" done");
    	return result;
    }

}
