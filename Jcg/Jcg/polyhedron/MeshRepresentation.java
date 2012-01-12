package Jcg.polyhedron;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

import Jcg.geometry.*;
import Jcg.triangulations2D.TriangulationDSFace_2;
import Jcg.triangulations2D.TriangulationDSVertex_2;
import Jcg.triangulations2D.TriangulationDS_2;



/**
 * A vertex shared representation of a mesh.
 * For each face we store the indices of the incident vertices
 *
 * @author Luca Castelli Aleardi
 *
 */
public class MeshRepresentation {
	    
	public int sizeVertices;
	public int sizeFaces;
	public int sizeHalfedges;
	
    public int[][] faces;
    public int[] faceDegrees;
    public Point_3[] points;

    public Color[] faceColors;
    public Color[] edgesColors;
    
    static final int max_degree=10;

    public void getFromTriangleSoup(Collection<Triangle_3> faces) {
    	this.faces = new int[faces.size()][];
    	faceDegrees = new int[faces.size()];
    	faceColors = new Color[faces.size()];
    	LinkedHashMap<Point_3,Integer> vert = new LinkedHashMap<Point_3,Integer> ();

    	int ind=0;
    	int indF = 0;
    	for (Triangle_3 f : faces) {
    		for (int i=0; i<3; i++)
    			if (!vert.containsKey(f.vertex(i)))
    				vert.put(f.vertex(i), ind++);
    		faceDegrees[indF] = 3;
    		faceColors[indF] = Color.gray;
    		this.faces[indF++] = new int[]{vert.get(f.vertex(0)), vert.get(f.vertex(1)), vert.get(f.vertex(2))};
    	}    	
    	points = vert.keySet().toArray(new Point_3[0]);
    }
    
    
    public void readOffFile(String filename){
    	double x, y, z;
    	File file;
    	FileReader readfic;
    	BufferedReader input;
    	String line;
    	System.out.println("Creating a shared vertex representation of a mesh from OFF file");
    	try{
    		System.out.println("Opening OFF file... "+filename);
    		file = new File(filename);
    		readfic = new FileReader(file);
    		input = new BufferedReader(readfic);
            
            line=input.readLine(); // first line is empty
            line=input.readLine();
            StringTokenizer tok = new StringTokenizer(line);
            sizeVertices=Integer.parseInt(tok.nextToken());
            sizeFaces=Integer.parseInt(tok.nextToken());            
//            size_halfedges=Integer.parseInt(tok.nextToken());
            
            points=new Point_3[sizeVertices];
            faceDegrees=new int[sizeFaces];
            faces=new int[sizeFaces][max_degree];
            
            int i=0;
            Point_3 point;
            System.out.print("Reading vertices...");
            while(i<sizeVertices) {
                line=input.readLine(); tok = new StringTokenizer(line);
                //System.out.println("line "+i+" :"+line);
                x=(new Double(tok.nextToken())).doubleValue();
                y=(new Double(tok.nextToken())).doubleValue();
                z=(new Double(tok.nextToken())).doubleValue();
                
                point=new Point_3(x,y,z);
                points[i]=point;
                i++;
            }
            System.out.println("done "+sizeVertices+" vertices");
            
            line = input.readLine();
            System.out.print("Reading face degrees...");
            i=0;
            while(i<sizeFaces){
            	if((line = input.readLine()) == null) break;
            	tok = new StringTokenizer(line);
                
                faceDegrees[i]=Integer.parseInt(tok.nextToken());
                if(faceDegrees[i]>max_degree) {System.out.println("Error face degree"); return; }
                
                int j=0;
                while(tok.hasMoreTokens()) {
                    faces[i][j]=Integer.parseInt(tok.nextToken());
                    j++;
                    sizeHalfedges++;
                }         
                i++;
            }
            System.out.println("done "+sizeFaces+" faces");
            input.close();
	}
	catch(FileNotFoundException e){
	    //Efichier.erreur(e);
	}
	catch(IOException e){
	    //Efichier.erreur(e);
	}
	System.out.println("Mesh representation created");
	
    }

	public void writeOffFile(String filename) throws IOException {
    	// store vertex indices in map 
    	 BufferedWriter out = new BufferedWriter (new FileWriter(filename));
       	 out.write ("OFF\n");
       	 out.write (sizeVertices + " " + sizeFaces + " 0\n");  
    	 for (Point_3 p : points)
    		 out.write(p.getX() + " " + p.getY() + " " + p.getZ() + "\n");
    	 
    	 for (int i=0; i<sizeFaces; i++) {
    		 out.write(""+faceDegrees[i]);
    		 for (int j=0; j<faceDegrees[i]; j++)
    			 out.write (" " + faces[i][j]);
    			 out.write ("\n");
    	 }
    	 out.close();
	}

    
    public void getFromPolyhedron3D(Polyhedron_3<Point_3> polyhedron){
    	System.out.println(" ---\nCreating Mesh representation from polyhedron");
    	System.out.println("starting encoding a polyhedron...");
    	sizeVertices=polyhedron.sizeOfVertices();
    	sizeFaces=polyhedron.sizeOfFacets();            
    	sizeHalfedges=polyhedron.sizeOfHalfedges();
    	
    	points=new Point_3[sizeVertices];
    	faceDegrees=new int[sizeFaces];
    	faces=new int[sizeFaces][max_degree];
    	
    	for(int i=0;i<sizeVertices;i++) {
    		points[i]=polyhedron.vertices.get(i).getPoint();
    	}
    	
    	for(int i=0;i<sizeFaces;i++){
    		int d=polyhedron.facets.get(i).degree();
    		if(d>max_degree)
    			throw new Error("faces have big huge degree");
    		faceDegrees[i]=d;
    	}
    	
      	for(int i=0;i<sizeFaces;i++){
    		faces[i]=polyhedron.facets.get(i).getVertexIndices(polyhedron);
    	}
    	System.out.println("Mesh representation created \n ---");
  }

    public void getFromTriangleMesh(TriangulationDS_2<Point_3> mesh){
    	System.out.println(" ---\nCreating Mesh representation from Triangle DS...");
    	//System.out.println("starting encoding...");
    	sizeVertices=mesh.vertices.size();
    	sizeFaces=mesh.faces.size();            
    	//sizeHalfedges=polyhedron.sizeOfHalfedges();
    	
    	points=new Point_3[sizeVertices];
    	faceDegrees=new int[sizeFaces];
    	faces=new int[sizeFaces][3];
    	
    	for(int i=0;i<sizeVertices;i++) {
    		points[i]=mesh.vertices.get(i).getPoint();
    	}
    	
    	for(int i=0;i<sizeFaces;i++){
    		faceDegrees[i]=3;
    	}
    	
      	for(int i=0;i<sizeFaces;i++){
      		TriangulationDSFace_2<Point_3> currentFace=mesh.faces.get(i);
    		faces[i][0]=mesh.vertices.indexOf(currentFace.vertex(0));
    		faces[i][1]=mesh.vertices.indexOf(currentFace.vertex(1));
    		faces[i][2]=mesh.vertices.indexOf(currentFace.vertex(2));
    	}
    	System.out.println("done");
    }
    
    public void getFromPlanarTriangleMesh(TriangulationDS_2<Point_2> mesh){
    	System.out.println(" ---\nCreating Mesh representation from Triangle DS...");
    	sizeVertices=mesh.vertices.size();
    	sizeFaces=mesh.faces.size();            
    	
    	points=new Point_3[sizeVertices];
    	faceDegrees=new int[sizeFaces];
    	faces=new int[sizeFaces][3];
    	
    	for(int i=0;i<sizeVertices;i++) {
    		TriangulationDSVertex_2<Point_2> v=mesh.vertices.get(i);
    		v.index=i;
    		Point_2 p=v.getPoint();
    		points[i]=new Point_3(p.getX().doubleValue(), p.getY().doubleValue(), 0.);
    	}
    	
    	for(int i=0;i<sizeFaces;i++){
    		faceDegrees[i]=3;
    	}
    	
      	for(int i=0;i<sizeFaces;i++){
      		TriangulationDSFace_2<Point_2> currentFace=mesh.faces.get(i);
    		faces[i][0]=currentFace.vertex(0).index;
    		faces[i][1]=currentFace.vertex(1).index;
    		faces[i][2]=currentFace.vertex(2).index;
    	}
    	System.out.println("done");
    }


}
