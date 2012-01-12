package Jcg.viewer;

import java.applet.Applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

import Jcg.geometry.Point_3;
import Jcg.geometry.Segment_3;
import Jcg.geometry.Triangle_3;
import Jcg.polyhedron.MeshRepresentation;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.triangulations3D.Delaunay_3;
import Jcg.triangulations3D.FacetHandle;
import Jcg.triangulations3D.TriangulationDSCell_3;
import Jcg.triangulations3D.TriangulationDS_3;
import Jcg.triangulations2D.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;
/*
 * class for rendering 3D triangulated surface meshes
 */
public class MeshViewer extends Applet {

  private static final long serialVersionUID = 1L;

  public ArrayList<Point_3> graphEdges=new ArrayList<Point_3>();
  public Polyhedron_3<Point_3> polyhedron=null;
  public TriangulationDS_3<Point_3> triangulation3D=null;
  public Delaunay_3 del3D=null;
  private boolean wiredDel = false;
  public MeshRepresentation m=null, mTrans=null;

  /**
   * A field in which to store collections of 3D triangulation cells to be shown in the scene.
   */
  public Collection<TriangulationDSCell_3<Point_3>> cellsCollection=null;

  /**
   * A field in which to store collections of 3D triangulation facets to be shown in the scene.
   */
  public Collection<FacetHandle<Point_3>> facetsCollection=null;

  /**
   * A field in which to store collections of triangles to be shown in the scene.
   */
  public Collection<Triangle_3> trianglesCollection=null;
  public boolean drawTriangleEdges = false;

  /**
   * A field in which to store collections of segments to be shown in the scene.
   */
  public Collection<Segment_3> segmentsCollection=null;

  /**
   * A field in which to store collections of points to be shown in the scene.
   */
  public Collection<Point_3> pointsCollection=null;
  public Color[] pointsColor=null;

  public int[][] edges=null;
  public Color[] edgeColors=null;
  public Color[] faceColors=null;
  public Point_3[] points=null;

 
  private SimpleUniverse u = null;
  private BranchGroup scene = null;
  
  public double scaleFactor=0.2;
    
  public BranchGroup createSceneGraph() {
	  
	TransformGroup objTrans=null;
    // Create the root of the branch graph
    BranchGroup objRoot = new BranchGroup();
    objRoot.setCapability(BranchGroup.ALLOW_DETACH);
    
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 30.0);
    
    // Create a Transformgroup to scale all objects so they appear in the scene.
    TransformGroup objScale = new TransformGroup();
    Transform3D t3d = new Transform3D();

    System.out.println("scale factor= "+this.scaleFactor);
    t3d.setScale(this.scaleFactor);
    objScale.setTransform(t3d);
    objRoot.addChild(objScale);

    // This Transformgroup is used by the mouse manipulators to
    // move the mesh.
    objTrans = new TransformGroup();
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    objScale.addChild(objTrans);

    PolygonAttributes attr = new PolygonAttributes();
    attr.setCullFace(PolygonAttributes.CULL_NONE);
    attr.setBackFaceNormalFlip(true);
    Appearance ap = new Appearance();
    Material mat = new Material();
    mat.setLightingEnable(true);
    mat.setDiffuseColor(0.9f, 0.9f, 0.9f);
    mat.setAmbientColor(0.1f, 0.1f, 0.1f);
    ap.setMaterial(mat);
    ap.setPolygonAttributes(attr);
    ap.setPointAttributes(new PointAttributes(6f, true));
    
    Appearance apTrans = new Appearance();
    apTrans.setMaterial(new Material());
    PolygonAttributes attrTrans = new PolygonAttributes();
    attrTrans.setCullFace(PolygonAttributes.CULL_FRONT);
    apTrans.setPolygonAttributes(attrTrans);
    apTrans.setPointAttributes(new PointAttributes(6f, true));
    // setting transparency parameters
    TransparencyAttributes ta = new TransparencyAttributes();
    ta.setTransparencyMode (ta.BLENDED);
    ta.setTransparency (0.3f);
    apTrans.setTransparencyAttributes (ta);

    
    // Because we're about to spin this triangle, be sure to draw
    // backfaces.  If we don't, the back side of the triangle is invisible.
//    Appearance ap2 = new Appearance();
//    PolygonAttributes pa = new PolygonAttributes();
//    pa.setCullFace(PolygonAttributes.CULL_NONE);
//    ap2.setPolygonAttributes(pa);

//----------------------
 
    //this.drawAxis(objTrans);
    
    if(m!=null) {
    	System.out.println("Vizualizing 3D surface mesh (mesh representation)");
    	faceColors=new Color[m.faces.length];
    	for(int i=0;i<m.faces.length;i++)
    	faceColors[i]=new Color(0.9f,0.9f,0.9f);
        // drawing the facets and edges
        addFacets(objTrans,ap,m.points,m.faces,faceColors);
//    	addEdges(objTrans,ap,m.points,m.faces); 
    }
    if(mTrans!=null) {
    	System.out.println("Vizualizing 3D surface mesh (mesh representation)");
    	faceColors=new Color[mTrans.faces.length];
    	for(int i=0;i<mTrans.faces.length;i++)
    	faceColors[i]=new Color(0.5f,0.5f,0.5f);
        // drawing the facets with transparency
        addFacets(objTrans,apTrans,mTrans.points,mTrans.faces,faceColors);
    }
    if(this.triangulation3D!=null) {
    	System.out.println("Vizualizing 3D triangulation (volumic mesh)");
    	Color[] cellColors=new Color[triangulation3D.sizeOfCells()];
    	this.add3DCells(objTrans, ap, triangulation3D.cells, cellColors);
    }
    if(this.del3D!=null) {
    	System.out.println("Vizualizing 3D Delaunay triangulation (volumic mesh)");
    	if (wiredDel)
    		this.addEdges(objTrans, ap, del3D);
    	else
    		this.addFacets(objTrans, ap, del3D);
    		
    }
    if(this.points!=null && this.edges!=null) {
    	System.out.println("Vizualizing edges (graph embedded in 3D)");
    	if(this.edgeColors==null)
    		this.addEdgesOnly(objTrans, ap, this.points, this.edges, Color.green);
    	else
    		this.addColoredEdges(objTrans, ap, this.points, this.edges, this.edgeColors);
    }
    if(this.cellsCollection != null) {
    	Color[] color = new Color[cellsCollection.size()];
    	for (int i=0; i<cellsCollection.size(); i++)
    		color[i] = Color.green;
    	this.add3DCells(objTrans, ap, this.cellsCollection, color);
    }
    if(this.facetsCollection != null) {
    	Color[] color = new Color[facetsCollection.size()];
    	for (int i=0; i<facetsCollection.size(); i++)
    		color[i] = Color.gray;
    	this.addFacets(objTrans, ap, this.facetsCollection, color);
    }
    if(this.trianglesCollection != null) {
    	Color[] color = new Color[trianglesCollection.size()];
    	for (int i=0; i<trianglesCollection.size(); i++)
    		color[i] = Color.green;
    	this.addTriangles(objTrans, ap, this.trianglesCollection, color);
    }

    if(this.segmentsCollection != null) {
    	this.addSegments(objTrans, ap, this.segmentsCollection, Color.white);
    }
    if(this.pointsCollection != null) {
    	this.scaleFactor=computeScaleFactor(pointsCollection);
    	if(pointsColor==null) {
    		pointsColor=new Color[pointsCollection.size()];
    		for (int i=0; i<pointsCollection.size(); i++)
    			pointsColor[i]=Color.red;
    	}
    	this.drawBoundingBox(objTrans, pointsCollection);
    	this.addPoints(objTrans, ap, this.pointsCollection, pointsColor);
    }

//---------------------------------------
    
    // Create the rotate behavior node
    MouseRotate behavior = new MouseRotate(objTrans);
    objTrans.addChild(behavior);
    behavior.setSchedulingBounds(bounds);
    
    // Create the zoom behavior node
    MouseZoom behavior2 = new MouseZoom(objTrans);
    objTrans.addChild(behavior2);
    behavior2.setSchedulingBounds(bounds);
    
    //Shine it with two colored lights.
    Color3f lColor1 = new Color3f(0.9f, 0.9f, 0.9f);
    Color3f lColor2 = new Color3f(0.9f, 0.9f, 0.9f);
    Color3f lColor3 = new Color3f(0.9f, 0.9f, 0.9f);
    Color3f lColor4 = new Color3f(0.9f, 0.9f, 0.9f);
    Vector3f lDir1  = new Vector3f(-1.0f, -1.0f, 1.0f);
    Vector3f lDir2  = new Vector3f(0.0f, 0.0f, -1.0f);
    Vector3f lDir3  = new Vector3f(0.0f, -1.0f, 0.0f);
    Vector3f lDir4  = new Vector3f(-1.0f, 1.0f, 0.0f);
    DirectionalLight lgt1 = new DirectionalLight(lColor1, lDir1);
    DirectionalLight lgt2 = new DirectionalLight(lColor2, lDir2);
    DirectionalLight lgt3 = new DirectionalLight(lColor3, lDir3);
    DirectionalLight lgt4 = new DirectionalLight(lColor4, lDir4);
    lgt1.setInfluencingBounds(bounds);
    lgt2.setInfluencingBounds(bounds);
    lgt3.setInfluencingBounds(bounds);
    lgt4.setInfluencingBounds(bounds);
//    objScale.addChild(lgt1);
    objScale.addChild(lgt2);
//    objScale.addChild(lgt3);
//    objScale.addChild(lgt4);
    
    Color3f lightColor = new Color3f (0.9f, 0.9f, 0.9f);
    AmbientLight ambientLightNode = new AmbientLight (lightColor);
    ambientLightNode.setInfluencingBounds (bounds);
    objRoot.addChild (ambientLightNode);

    // Let Java 3D perform optimizations on this scene graph.
    objRoot.compile();

    return objRoot;
  }
  
	/**
	 * Compute a bounding box of the input points (from an Array)
	 */      
  public double[] boundingBox(Point_3[] points) {
  	double[] box=new double[6];
  	double xmax,xmin,ymax,ymin,zmax,zmin;
  	
  	Point_3 p=points[0];
  	xmax=p.getX().doubleValue(); xmin=p.getX().doubleValue(); 
  	ymax=p.getY().doubleValue(); ymin=p.getY().doubleValue();
  	zmax=p.getZ().doubleValue(); zmin=p.getZ().doubleValue();
  	
  	for(int i=1;i<points.length;i++) {
  		p=points[i];
  		
  		if(p.getX().doubleValue()>xmax) xmax=p.getX().doubleValue();
  		if(p.getX().doubleValue()<xmin) xmin=p.getX().doubleValue();
  		if(p.getY().doubleValue()>ymax) ymax=p.getY().doubleValue();
  		if(p.getY().doubleValue()<ymin) ymin=p.getY().doubleValue();
  		if(p.getZ().doubleValue()>zmax) zmax=p.getZ().doubleValue();
  		if(p.getZ().doubleValue()<zmin) zmin=p.getZ().doubleValue();
  	}
  	
  	box[0]=xmin; box[1]=ymin; box[2]=zmin;
  	box[3]=xmax; box[4]=ymax; box[5]=zmax;
  	return box;
  }

	/**
	 * Compute a bounding box of the input points (from a Collection)
	 */      
  public double[] boundingBox(Collection<Point_3> points) {
	Point_3[] pointsArray=new Point_3[points.size()];
	
	int i=0;
	for(Point_3 p: points) {
		pointsArray[i]=p;
		i++;
	}
	return boundingBox(pointsArray);
  }

	/**
	 * Compute the scale factor in order to adjust the 3D view (from a Collection)
	 */      
  public double computeScaleFactor(Collection<Point_3> points) {
	Point_3[] pointsArray=new Point_3[points.size()];
	double result; 
	int i=0;
	for(Point_3 p: points) {
		pointsArray[i]=p;
		i++;
	}
	result=computeScaleFactor(pointsArray);
	//System.out.println("scale factor computed: "+result);
	return result;
  }

	/**
	 * Compute the scale factor in order to adjust the 3D view
	 */      
  public double computeScaleFactor(Point_3[] points) {
	  double[] box=boundingBox(points);
	  
	  double x=box[3]-box[0];
	  double y=box[4]-box[1];
	  double z=box[5]-box[2];
	  
	  double max=Math.max(x, y);
	  max=Math.max(max, z);
	  double result=0.8/max;
	  return result;
  }
  
	/**
	 * Draw x, y and z axis
	 */    
  public void drawBoundingBox(TransformGroup objTrans, Collection<Point_3> points) {
	  double[] box=boundingBox(points);
	  Point_3[] vertices=new Point_3[8];
	  Color[] colors=new Color[12];
	  for(int i=0;i<12;i++)
		  colors[i]=Color.white;
	  
	  vertices[0]=new Point_3(box[0], box[1], box[2]);
	  vertices[1]=new Point_3(box[3], box[1], box[2]);
	  vertices[2]=new Point_3(box[0], box[4], box[2]);
	  vertices[3]=new Point_3(box[3], box[4], box[2]);
	  vertices[4]=new Point_3(box[0], box[1], box[5]);
	  vertices[5]=new Point_3(box[3], box[1], box[5]);
	  vertices[6]=new Point_3(box[0], box[4], box[5]);
	  vertices[7]=new Point_3(box[3], box[4], box[5]);
	  
	  int[][] lines=new int[12][2];
	  lines[0][0]=0; lines[0][1]=1;
	  lines[1][0]=0; lines[1][1]=2;
	  lines[2][0]=3; lines[2][1]=1;
	  lines[3][0]=3; lines[3][1]=2;
	  lines[4][0]=4; lines[4][1]=5;
	  lines[5][0]=4; lines[5][1]=6;
	  lines[6][0]=7; lines[6][1]=5;
	  lines[7][0]=7; lines[7][1]=6;
	  lines[8][0]=4; lines[8][1]=4;
	  lines[9][0]=1; lines[9][1]=5;
	  lines[10][0]=2; lines[10][1]=6;
	  lines[11][0]=3; lines[11][1]=7;
	  SegmentSoup edges=new SegmentSoup(vertices, lines, colors);
	  objTrans.addChild(edges);
  }

	/**
	 * Draw a triangular face
	 */    
  public void addFacet(TransformGroup objTrans, Appearance ap,Point_3[] points,Color color) {
    Shape3D face=new TriangleFacet(points,color);
    face.setAppearance(ap);
    objTrans.addChild(face);
  }

	/**
	 * Draw a 3D volume mesh (a set of tetrahedra in R3)
	 */  
  public void add3DCells(TransformGroup objTrans, Appearance ap,
		  Collection<TriangulationDSCell_3<Point_3>> cells, Color[] color) {
	  int i=0;
	  //System.out.println("vizualizing 3D cells");
	  for(TriangulationDSCell_3<Point_3> c: cells) {
		  FacetHandle<Point_3> facet;
		  Point_3[] points=new Point_3[3];
		  if(c!=null) {
			  for(int j=0;j<4;j++) {
				  facet=new FacetHandle<Point_3>(c,j);
				  points[0]=facet.vertex(0).getPoint();
				  points[1]=facet.vertex(1).getPoint();
				  points[2]=facet.vertex(2).getPoint();
				  this.addFacet(objTrans, ap, points, color[i]);
			  }
		  }
		  else {
			  System.out.println("cell index error");
		  }
		  i++;
	  }
  }

	/**
	 * Draw a 3D volume mesh (a set of tetrahedra in R3)
	 */  
  public void addEdges(TransformGroup objTrans, Appearance ap, Delaunay_3 del) {
	  //System.out.println("vizualizin 3D cells");
	  Collection<FacetHandle<Point_3>> facets = del.finiteFacets();
	  LinkedHashMap<Point_3, Integer> indices = new LinkedHashMap<Point_3, Integer>();
	  for (FacetHandle<Point_3> f : facets)
		  for (int i=0; i<3; i++) {
			  indices.put(f.vertex(i).getPoint(), -1);
		  }
	  int ind = 0;
	  Set<Point_3> vert = indices.keySet();
	  Point_3[] vertices = new Point_3[vert.size()];
	  for (Point_3 p :vert) {
		  vertices[ind] = p;
		  indices.put(p, ind++);
	  }
	  
	  LinkedHashSet<Integer[]> edgesSet = new LinkedHashSet<Integer[]> (); 
	  for(FacetHandle<Point_3> f: facets) {
		  Integer a = indices.get(f.vertex(0).getPoint());
		  Integer b = indices.get(f.vertex(1).getPoint());
		  Integer c = indices.get(f.vertex(2).getPoint()).intValue();
		  Integer[] ab = new Integer[] {a, b};
		  Integer[] ba = new Integer[] {b, a};
		  Integer[] bc = new Integer[] {b, c};
		  Integer[] cb = new Integer[] {c, b};
		  Integer[] ca = new Integer[] {c, a};
		  Integer[] ac = new Integer[] {a, c};
		  if (!edgesSet.contains(ab) && !edgesSet.contains(ba))
			  edgesSet.add(ab);
		  if (!edgesSet.contains(bc) && !edgesSet.contains(cb))
			  edgesSet.add(bc);
		  if (!edgesSet.contains(ca) && !edgesSet.contains(ac))
			  edgesSet.add(ca);
	  }

	  int[][] edges = new int[edgesSet.size()][2];
	  ind = 0;
	  for (Integer[] e : edgesSet)
		  edges[ind++] = new int[] {e[0].intValue(), e[1].intValue()};

	  addEdgesOnly(objTrans, ap, vertices, edges, Color.blue);
  }

  	/**
	 * Draws the facets of a 3D Delaunay triangulation
	 */  
  public void addFacets(TransformGroup objTrans, Appearance ap,Delaunay_3 del) {
	  ArrayList<FacetHandle<Point_3>> facets = 
		  (ArrayList<FacetHandle<Point_3>>) del.finiteFacets();
	  ArrayList<Triangle_3> triangles = new ArrayList<Triangle_3>();
	  Color[] colors = new Color[facets.size()];
	  int ind = 0;
	  for (FacetHandle<Point_3> f : facets) {
		  triangles.add(new Triangle_3(f.vertex(0).getPoint(), f.vertex(1).getPoint(), f.vertex(2).getPoint()));
		  colors[ind++] = Color.gray;
	  }
	  this.addTriangles(objTrans, ap, triangles, colors);
  }

	/**
	 * Draws a triangle soup in 3D represented as a collection of facet handles in a triangulation
	 */  
  public void addFacets(TransformGroup objTrans, Appearance ap, 
		  Collection<FacetHandle<Point_3>> facets, Color[] colors) {
	  ArrayList<Triangle_3> triangles = new ArrayList<Triangle_3>();
	  for (FacetHandle<Point_3> f : facets)
		  triangles.add(new Triangle_3(f.vertex(0).getPoint(), f.vertex(1).getPoint(), f.vertex(2).getPoint()));
	  this.addTriangles(objTrans, ap, triangles, colors);
  }

  /**
   * Draws a triangle soup in 3D
   */  
  public void addTriangles (TransformGroup objTrans, Appearance ap, 
		  Collection<Triangle_3> triangles, Color[] color) {
	  if (triangles.isEmpty())
		  return;
	Shape3D shape = new TriangleSoup(triangles, color);
    shape.setAppearance(ap);
    objTrans.addChild(shape);
    if (drawTriangleEdges) {  // draw also the edges of the triangles
    	Point_3[] points = new Point_3 [3*triangles.size()];
    	int ind = 0;
    	for (Triangle_3 t : triangles)
    		for (int i=0; i<3; i++)
    			points[ind++] = t.vertex(i);
    	int[][] edges = new int[3*triangles.size()][2];
    	for (int i=0; i<ind;) {
    		edges[i][0] = i;
    		edges[i][1] = i+1;
    		i++;
    		edges[i][0] = i;
    		edges[i][1] = i+1;
    		i++;
    		edges[i][0] = i;
    		edges[i][1] = i-2;
    		i++;
    	}
    	Color[] col = new Color [points.length];
    	for (int i=0; i<points.length; i++)
    		col[i] = Color.black;
        shape = new SegmentSoup (points, edges, col);
        shape.setAppearance(ap);
        objTrans.addChild(shape);
    }
  }


  /**
	 * Draws the set of faces of a (triangulated) polyhedron
	 */  
  public void addFacets(TransformGroup objTrans, Appearance ap,
		  Point_3[] points,int[][] faces,Color[] color) {
	  if (faces.length == 0)
		  return;
	Shape3D shape = new TriangleSoup(points, faces, color);
    shape.setAppearance(ap);
    objTrans.addChild(shape);
  }

//  /**
//	 * Draw the set of edges of a polyhedron (given by face incidence relations)
//	 */  
//  public void addEdges(TransformGroup objTrans, Appearance ap,Point_3[] points,int[][] faces) {
//  	Shape3D[] edges0=new EdgePolyhedron[faces.length];
//    Shape3D[] edges1=new EdgePolyhedron[faces.length];
//    Shape3D[] edges2=new EdgePolyhedron[faces.length];
//    for(int i=0;i<faces.length;i++){
//    	Point_3 p0=points[faces[i][0]];
//    	Point_3 p1=points[faces[i][1]];
//    	Point_3 p2=points[faces[i][2]];
//    	edges0[i]=new EdgePolyhedron(p0,p1,Color.black);
//    	edges1[i]=new EdgePolyhedron(p1,p2,Color.black);
//    	edges2[i]=new EdgePolyhedron(p2,p0,Color.black);
//    	edges0[i].setAppearance(ap); objTrans.addChild(edges0[i]);
//    	edges1[i].setAppearance(ap); objTrans.addChild(edges1[i]);
//    	edges2[i].setAppearance(ap); objTrans.addChild(edges2[i]);
//    }	
//  }

	/**
	 * Draw a set of edges in 3D (for example, as the 1-skeleton of a polyhedron)
	 */
  	public void addEdgesOnly(TransformGroup objTrans, Appearance ap,Point_3[] points,int[][] edges, 
  			Color color) {
  		Color[] colors = new Color[edges.length];
  		for (int i=0; i<edges.length; i++)
  			colors[i] = color;
  		Shape3D edgesToDraw = new SegmentSoup (points, edges, colors);
  		edgesToDraw.setAppearance(ap); 
  		objTrans.addChild(edgesToDraw);
  	}

	/**
	 * Draw a set of colored edges in 3D (for example, as the 1-skeleton of a polyhedron)
	 */
  	public void addColoredEdges(TransformGroup objTrans, Appearance ap,Point_3[] points,int[][] edges, Color[] colors) {
  		Shape3D edgesToDraw = new SegmentSoup (points, edges, colors);
  		edgesToDraw.setAppearance(ap); 
  		objTrans.addChild(edgesToDraw);
  	}

    /**
     * Draws a segment soup in 3D
     */  
    public void addSegments (TransformGroup objTrans, Appearance ap, 
  		  Collection<Segment_3> segments, Color color) {
    	Point_3[] vertices = new Point_3[2*segments.size()];
    	int[][] edges = new int[segments.size()][2];
    	int i = 0;
    	for (Segment_3 s : segments) {
    		vertices[2*i] = (Point_3) s.source();
    		vertices[2*i+1] = (Point_3) s.target();
    		edges[i][0] = 2*i;
    		edges[i][1] = 2*i+1;
    		i++;
    	}
    	addEdgesOnly(objTrans, ap, vertices, edges, color);
    }

    /**
     * Draws a point cloud in 3D
     */  
    public void addPoints (TransformGroup objTrans, Appearance ap, 
  		  Collection<Point_3> points, Color[] color) {
      Shape3D shape = new PointCloud(points, color);
      shape.setAppearance(ap);
      objTrans.addChild(shape);
  	}

    public void init() {
    	setLayout(new BorderLayout());
    	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    	Canvas3D canvas = new Canvas3D(config);
    	add("Center", canvas);
    	
    	// Create a simple scene and attach it to the virtual universe
    	scene = createSceneGraph();
    	u = new SimpleUniverse(canvas);
    	
    	// This will move the ViewPlatform back a bit so the
    	// objects in the scene can be viewed.
    	u.getViewingPlatform().setNominalViewingTransform();
    	
    	u.addBranchGraph(scene);
    }

    public void destroy() {
    	u.cleanup();
    }
  
    public void update() {
    	BranchGroup scene2 = createSceneGraph();
    	u.getLocale().replaceBranchGraph(scene, scene2);
    	scene = scene2;
    }
  
    /**
     * creates a 3D viewer with an empty scene.
     *
     */
    public MeshViewer(){}

    private void createViewer(MeshRepresentation mr, boolean trans) {
    	System.out.println("visualizing polyhedral surface");
    	if (trans)
    		mTrans = mr;
    	else
    		m=mr;
    	new MainFrame(this,400,400);
    }

    /**
     * creates a 3D viewer showing a mesh representation.
     */
    public MeshViewer(MeshRepresentation mr){
    	createViewer(mr, false);
    }
    /**
     * creates a 3D viewer showing a transparent mesh representation.
     */
    public MeshViewer(MeshRepresentation mr, boolean trans){
    	createViewer(mr, trans);
    }
  
    /**
     * creates a 3D viewer showing a polyhedral surface stored in a file.
     */
    public MeshViewer(String filename){
	System.out.println("visualizing polyhedral surface from .off file");
    m=new MeshRepresentation();
    m.readOffFile(filename);
    scaleFactor=this.computeScaleFactor(m.points);
	new MainFrame(this,400,400);
  }

    /**
     * creates a 3D viewer showing a polyhedron.
     */
  public MeshViewer(Polyhedron_3<Point_3> polyhedron){
		System.out.println("visualizing polyhedral surface");
	    m=new MeshRepresentation();
	    m.getFromPolyhedron3D(polyhedron);
	    scaleFactor=this.computeScaleFactor(m.points);
		new MainFrame(this,400,400);
	  }

  /**
   * creates a 3D viewer showing a Triangle mesh.
   */
public MeshViewer(TriangulationDS_2<Point_3> mesh){
		System.out.println("visualizing triangle mesh");
	    m=new MeshRepresentation();
	    m.getFromTriangleMesh(mesh);
	    scaleFactor=this.computeScaleFactor(m.points);
		new MainFrame(this,400,400);
	  }

  /**
   * creates a 3D viewer showing a 3D triangulation.
   */
  public MeshViewer(TriangulationDS_3<Point_3> triangulation3D){
	  System.out.println("visualizing 3D triangulation");
	  this.triangulation3D=triangulation3D;
	  new MainFrame(this,400,400);
  }

  /**
   * creates a 3D viewer showing a 3D Delaunay triangulation: the triangulation is drawn in wireframe 
   * when the flag wired is set to true, and in plain facets otherwise.
   */
  public MeshViewer(Delaunay_3 del, boolean wired){
	  System.out.println("visualizing 3D Delaunay triangulation");
	  this.del3D=del;
	  this.wiredDel = wired;
	  new MainFrame(this,400,400);
  }

  /**
   * creates a 3D viewer showing a collection of edges.
   */
  public MeshViewer(Point_3[] points, int[][] edges){
	  System.out.println("visualizing graph in 3D");
	  this.points=points;
	  this.edges=edges;
	  scaleFactor=this.computeScaleFactor(points);
	  new MainFrame(this,400,400);
  }
  
  /**
   * creates a 3D viewer showing a collection of colored edges.
   */
  public MeshViewer(Point_3[] points, int[][] edges, Color[] colors){
	  System.out.println("visualizing graph in 3D");
	  this.points=points;
	  this.edges=edges;
	  this.edgeColors=colors;
	  scaleFactor=this.computeScaleFactor(points);
	  new MainFrame(this,400,400);
  }

  /**
   * creates a 3D viewer showing a collection of colored edges.
   */
  public MeshViewer(Polyhedron_3<Point_3> mesh, Point_3[] points, int[][] edges, Color[] colors){
	  System.out.println("visualizing graph in 3D");
	  this.points=points;
	  this.edges=edges;
	  this.edgeColors=colors;
	  this.m=new MeshRepresentation();
	  this.m.getFromPolyhedron3D(mesh);

	  scaleFactor=this.computeScaleFactor(points);
	  new MainFrame(this,400,400);
  }


  /**
   * creates a 3D viewer showing a point cloud in 3D.
   */
  public MeshViewer(Collection<Point_3> points){
	  System.out.println("visualizing 3D point cloud");
	  this.pointsCollection=points;
	  new MainFrame(this,400,400);
  }

  /**
   * creates a 3D viewer showing a point cloud (with colors) in 3D.
   */
  public MeshViewer(Collection<Point_3> points, Color[] colors){
	  System.out.println("visualizing 3D point cloud");
	  this.pointsCollection=points;
	  this.pointsColor=colors;
	  new MainFrame(this,400,400);
  }

  public static void main(String argv[]) {    
	Point_3 p00=new Point_3(0.,0.,0.);
	Point_3 p10=new Point_3(1.,0.,0.);
	Point_3 p01=new Point_3(0.,1.,0.);
	Point_3 p001=new Point_3(0.,0.,1.);
	Point_3 p_1=new Point_3(0.,0.,-1.);	
	TriangulationDS_3<Point_3> mesh3D=new TriangulationDS_3<Point_3>(5,5);
	TriangulationDSCell_3<Point_3> c=mesh3D.makeTetrahedron(p00, p10, p01, p001);
	mesh3D.insertOutside(p_1, c, 3);
    
    new MeshViewer(mesh3D);

  }
  
}


  
//-----------------------------------------  
//-----------------------------------------  
//-----------------------------------------  


/*
 * Represents a triangle soup in 3D
 */   
class TriangleSoup extends Shape3D {
    private static final float sqrt3 = (float) Math.sqrt(3.0);

    private TexCoord2f texCoord[] = {
        new TexCoord2f(0.0f, 0.0f), new TexCoord2f(1.0f, 0.0f),
        new TexCoord2f(0.5f, sqrt3 / 2.0f),};
 
    public TriangleSoup(Point_3[] points, int[][] faces, Color[] color) {
    	TriangleArray surface = new TriangleArray(3*faces.length, 
    		TriangleArray.COORDINATES | TriangleArray.NORMALS | TriangleArray.TEXTURE_COORDINATE_2 | TriangleArray.COLOR_3);
    	
    	int ind=0;
    	for (int[] f : faces) {
    		for (int i=0; i<3; i++) {
    			// set vertex coordinates
    			surface.setCoordinates(ind, new double[]{points[f[i]].getX().doubleValue(), 
    					points[f[i]].getY().doubleValue(), points[f[i]].getZ().doubleValue()});
    			// set vertex color
    			Color col = color[ind/3];
    			surface.setColor(ind++, new float[]{(float)col.getRed()/256, (float)col.getGreen()/256, (float)col.getBlue()/256});
    		}
			// set vertices textures
	        for (int i=0; i<3; i++)
	            surface.setTextureCoordinate(0, ind-3+i, texCoord[i%3]);
    		// set triangle normal
    		Vector3f normal = new Vector3f();
    		Vector3f v1 = new Vector3f();
    		Vector3f v2 = new Vector3f();    			
    		Point3f [] pts = new Point3f[3];
    		for (int i=0; i<3; i++) pts[i] = new Point3f();
    	    surface.getCoordinates(ind-3, pts);
    	    v1.sub(pts[1], pts[0]);
    	    v2.sub(pts[2], pts[0]);
    	    normal.cross(v1, v2);
    	    normal.normalize();
    	    for (int i=0; i<3; i++)
    	    	surface.setNormal(ind-3+i, normal);
    	}
	this.setGeometry(surface);
	this.setAppearance(new Appearance());
    }

    public TriangleSoup(Collection<Triangle_3> triangles, Color[] color) {
    	TriangleArray surface = new TriangleArray(3*triangles.size(), 
    		TriangleArray.COORDINATES | TriangleArray.NORMALS | TriangleArray.TEXTURE_COORDINATE_2 | TriangleArray.COLOR_3);
    	
    	int ind=0;
    	for (Triangle_3 t : triangles) {
    		for (int i=0; i<3; i++) {
    			// set vertex coordinates
    			surface.setCoordinates(ind, new double[]{t.vertex(i).getX().doubleValue(), 
    					t.vertex(i).getY().doubleValue(), t.vertex(i).getZ().doubleValue()});
    			// set vertex color
    			Color col = color[ind/3];
    			surface.setColor(ind++, new float[]{(float)col.getRed()/256, (float)col.getGreen()/256, (float)col.getBlue()/256});
    		}
			// set vertices textures
	        for (int i=0; i<3; i++)
	            surface.setTextureCoordinate(0, ind-3+i, texCoord[i%3]);
    		// set triangle normal
    		Vector3f normal = new Vector3f();
    		Vector3f v1 = new Vector3f();
    		Vector3f v2 = new Vector3f();    			
    		Point3f [] pts = new Point3f[3];
    		for (int i=0; i<3; i++) pts[i] = new Point3f();
    	    surface.getCoordinates(ind-3, pts);
    	    v1.sub(pts[1], pts[0]);
    	    v2.sub(pts[2], pts[0]);
    	    normal.cross(v1, v2);
    	    normal.normalize();
    	    for (int i=0; i<3; i++)
    	    	surface.setNormal(ind-3+i, normal);
    	}
	this.setGeometry(surface);
	this.setAppearance(new Appearance());
    }
}

/*
 * Represents a segment soup in 3D
 */   
class SegmentSoup extends Shape3D {
 
    public SegmentSoup(Point_3[] points, int[][] edges, Color[] color) {
    	LineArray surface = new LineArray(2*edges.length, LineArray.COORDINATES | GeometryArray.COLOR_3);
    	
    	int ind=0;
    	for (int i=0; i<edges.length; i++)
    		for (int j=0; j<2; j++) {
    			// set vertex coordinates
    			surface.setCoordinates(ind, new double[]{points[edges[i][j]].getX().doubleValue(), 
    					points[edges[i][j]].getY().doubleValue(), points[edges[i][j]].getZ().doubleValue()});
    			// set vertex color
    			Color col = color[ind/2];
    			surface.setColor(ind++, new float[]{(float)col.getRed()/256, (float)col.getGreen()/256, (float)col.getBlue()/256});
    		}
	this.setGeometry(surface);
	this.setAppearance(new Appearance());
    }
}

/*
 * Represents a point cloud in 3D
 */   
class PointCloud extends Shape3D {
 
    public PointCloud(Collection<Point_3> points, Color[] color) {
    	if(points==null || color==null || points.size()!=color.length) {
    		throw new Error("PointCloud: points or colors non defined, or wrong number of colors");
    	}
    	PointArray surface = new PointArray(points.size(), LineArray.COORDINATES | GeometryArray.COLOR_3);
    	
    	int ind=0;
    	for (Point_3 p : points) {
    			surface.setCoordinates(ind, new double[]{p.getCartesian(0).doubleValue(), 
    					p.getCartesian(1).doubleValue(), p.getCartesian(2).doubleValue()});
    			// set vertex color
    			Color col = color[ind];
    			if(col==null) {
    				//System.out.println("color error: color "+ind+" non defined ");
    				col=Color.white;
    			}
    			surface.setColor(ind, new float[]{(float)col.getRed()/256, (float)col.getGreen()/256, (float)col.getBlue()/256});
    			ind++;
    	}
    	this.setGeometry(surface);
    	this.setAppearance(new Appearance());
    }
}
	

/*
 * Represents a facet of a polyhedron in 3D
 */   
class TriangleFacet extends Shape3D {
    private static final float sqrt3 = (float) Math.sqrt(3.0);
    private static Point3f[] verts;

    private TexCoord2f texCoord[] = {
        new TexCoord2f(0.0f, 0.0f), new TexCoord2f(1.0f, 0.0f),
        new TexCoord2f(0.5f, sqrt3 / 2.0f),};
 
/*
 * Create a face with a given color.
 * Works only for triangular faces.
 */   
    public TriangleFacet(Point_3[] points, Color color) {
    	int i;
    	
    	verts= new Point3f[3];
    	TriangleArray surface = new TriangleArray(3, 
    		TriangleArray.COORDINATES | TriangleArray.NORMALS | TriangleArray.TEXTURE_COORDINATE_2 | TriangleArray.COLOR_3);
    	
    	// initialize vertices and faces
    	Point3f point1, point2, point3;
    	point1=new Point3f( (float)points[0].getX().doubleValue(), 
    						(float)points[0].getY().doubleValue(),
    						(float)points[0].getZ().doubleValue());
    	point2=new Point3f( (float)points[1].getX().doubleValue(), 
    						(float)points[1].getY().doubleValue(),
    						(float)points[1].getZ().doubleValue());
    	point3=new Point3f( (float)points[2].getX().doubleValue(), 
    						(float)points[2].getY().doubleValue(),
    						(float)points[2].getZ().doubleValue());
   		verts[0]=point1;
   		verts[1]=point2;
   		verts[2]=point3;
    	
    	surface.setCoordinates(0, verts);
    	
    	Color3f defaultColor=new Color3f(0.8f,0.8f,0.8f);
    	if(color!=null) { 
    		for(i=0; i<verts.length;i++) {
    			//if(color!=null) 
    				surface.setColor(i,new Color3f(color));
    			//else 
    			//	surface.setColor(i,defaultColor);
    		}
    	}
    	else {
    		for(i=0; i<verts.length;i++) {
    			surface.setColor(i,defaultColor);
    		}
    	}  	
        
        for (i = 0; i < 3; i++) {
            surface.setTextureCoordinate(0, i, texCoord[i%3]);
        }

	Vector3f normal = new Vector3f();
	Vector3f v1 = new Vector3f();
	Vector3f v2 = new Vector3f();
	Point3f [] pts = new Point3f[3];
	for (i = 0; i < 3; i++) pts[i] = new Point3f();

	    surface.getCoordinates(0, pts);
	    v1.sub(pts[1], pts[0]);
	    v2.sub(pts[2], pts[0]);
	    normal.cross(v1, v2);
	    normal.normalize();
	    for (i = 0; i < 3; i++) {
		surface.setNormal(i, normal);
	    }
	    
	this.setGeometry(surface);
	this.setAppearance(new Appearance());
    }


}


class EdgePolyhedron extends Shape3D {

    public EdgePolyhedron(Point_3 p1, Point_3 p2,Color edgeColor) {
    	LineArray segment = new LineArray(2, LineArray.COORDINATES | GeometryArray.COLOR_3);
    	float[] vertices = new float[6];
    	vertices[0]=(float)p1.getX().doubleValue();
    	vertices[1]=(float)p1.getY().doubleValue();
    	vertices[2]=(float)p1.getZ().doubleValue();
    	vertices[3]=(float)p2.getX().doubleValue();
    	vertices[4]=(float)p2.getY().doubleValue();
    	vertices[5]=(float)p2.getZ().doubleValue();
    	
    	segment.setCoordinates(0, vertices);
    	
    	Color3f defaultColor=new Color3f(1.f,1.f,1.f);
    	if(edgeColor!=null)
    		segment.setColor(0, new Color3f(edgeColor));
    	else
    		segment.setColor(0, defaultColor);

    	//this = new Shape3D();
    	this.setGeometry(segment);
    	
    	Appearance lineAppearance=new Appearance();
    	LineAttributes thickLine=new LineAttributes();
    	thickLine.setLineWidth(3.0f);
    	lineAppearance.setLineAttributes(thickLine);
    	this.setAppearance(lineAppearance);
    }

}
