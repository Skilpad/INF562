package Jcg.viewer;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

import Jcg.polyhedron.*;
import Jcg.geometry.*;

/**
 * @author Luca Castelli Aleardi
 * Simple class for visualizing planar graphs and plane objects (points, segments, ...)
 */
public class Canvas2D extends Canvas implements MouseListener, MouseMotionListener {
	public Point_2 p1,p2; // vertices of the bounding box
	int x1,x2,y1,y2; // mouse coordinates
	public int height, width;
	protected Frame frame;
	public Draw2D draw;
	
	private ArrayList<Point_2[]> segments=new ArrayList<Point_2[]>();
	private ArrayList<Color> segmentColors=new ArrayList<Color>();
	private ArrayList<Point_2> points=new ArrayList<Point_2>();
	private ArrayList<String> labels=new ArrayList<String>();
	private ArrayList<Point_2> labelPositions=new ArrayList<Point_2>();
	
	/**
	 * Create a 2D window for drawing 2D objects
	 */
	public Canvas2D() {
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(Color.white);
		
		Dimension d=getSize(); 
		this.height=d.height;
		this.width=d.width;
		
		p1=new Point_2(-5.,-5.);
		p2=new Point_2(5,5);
		
		frame = new Frame("2D viewer");
		frame.add(this);
		frame.setSize(400, 400);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			    System.exit(0);
			}
		    });
		frame.setVisible(true);

	}
  
  	public void setWindows(int pass) {
  		if (pass==-1) {
  			p1.translateOf(new Vector_2(-2.0,2.0));
  			p2.translateOf(new Vector_2(2.0,-2.0));
  		}
  		
  		// bisogna sistemare i limiti di zoom
  		if (pass==1){
  			if(p1.getX().doubleValue()+2.0 < p2.getX().doubleValue()) {
  				p1.translateOf(new Vector_2(2.0,-2.0));
  				p2.translateOf(new Vector_2(-2.0,2.0));
  			}
  		}
  		repaint();
  	}

  	public void mouseClicked(MouseEvent e) {}
  	public void mouseReleased(MouseEvent e) {}
  	public void mouseEntered(MouseEvent e) {}
  	public void mouseExited(MouseEvent e) {}
  	public void mouseMoved(MouseEvent e) {}

  	public void mouseDragged(MouseEvent e) {
  		/*
  		int x3,y3;
  	double delta_x,delta_y;
  	
  	x3=e.getX();
  	y3=e.getY();
  	
  	if(control3.cbg.getCurrent().getLabel()=="trasla finestra")
  	{
  		delta_x=((x1-x3))*(p2.getX()-p1.getX())/200.0;
  		delta_y=((y3-y1))*(p1.getY()-p2.getY())/200.0;
  		
  		p1.translateOf(delta_x,-delta_y);
  		p2.translateOf(delta_x,-delta_y);
  		
  		Dimension d=getSize();
  		altezza=d.height;
  		
  		x1=x3;
  		y1=y3;
  		
  		repaint();
  	}
  	e.consume();
  	repaint(); */
  }

  	public void mousePressed(MouseEvent e) {}

  	public void paint(Graphics g) {
  		Dimension d=getSize(); 
  		this.height=d.height-2; 
  		this.width=d.width-2;  

  		this.draw= new Draw2D(g,p1,p2,height,width);
    	g.setColor(Color.black);
    	
    	
    	// drawing segments
    	int i=0;
    	for(Point_2[] s: this.segments) {
    		Color c=this.segmentColors.get(i);
    		this.draw.drawSegment(s[0], s[1], c);
    		i++;
    	}
    	
    	// drawing points
    	for(Point_2 p: this.points)
    		this.draw.drawCircle(p);
    	
    	// drawing labels
    	i=0;
    	for(String label: this.labels) {
    		Point_2 p=this.labelPositions.get(i);
    		this.draw.drawLabel(p, label);
    		i++;
    	}
    	
    	this.draw.drawArrow(new Point_2(-4., 0.), new Point_2(2., 2.), Color.green);
    	
    }
  	
  	//----------- methods for adding 2D objects to the drawing -----------
  	
  	public void addSegment(Point_2 p, Point_2 q) {
  		if(p==null || q==null) {
  			System.out.println("error null point");
  			return;
  		}
  		Point_2[] s=new Point_2[2];
  		s[0]=p;
  		s[1]=q;
  		this.segments.add(s);
  		this.segmentColors.add(Color.black);
  	}

  	public void addColoredSegment(Point_2 p, Point_2 q, Color c) {
  		if(p==null || q==null)
  			return;
  		Point_2[] s=new Point_2[2];
  		s[0]=p;
  		s[1]=q;
  		this.segments.add(s);
  		this.segmentColors.add(c);
  	}

  	public void addPoint(Point_2 p) {
  		if(p==null)
  			return;
  		this.points.add(p);
  	}
  	
  	public void addLabel(Point_2 p, String label) {
  		if(p==null || label==null)
  			return;
  		this.labels.add(label);
  		this.labelPositions.add(p);
  	}
  	
  	public void addPolyhedronEdges(Polyhedron_3<Point_2> mesh, Color[] edgeColors) {
  		if(mesh==null) return;
  		
  		if(edgeColors==null) {
  			for(Halfedge<Point_2> e: mesh.halfedges) {
  				Point_2 p=e.getVertex().getPoint();
  				Point_2 q=e.getOpposite().getVertex().getPoint();
  				this.addSegment(p, q);
  			}
  		}
  		else {
  			int i=0;
  			for(Halfedge<Point_2> e: mesh.halfedges) {
  				Point_2 p=e.getVertex().getPoint();
  				Point_2 q=e.getOpposite().getVertex().getPoint();
  				this.addColoredSegment(p, q, edgeColors[i]);
  				i++;
  			}
  		}
  	}

  	public void addPolyhedronVertices(Polyhedron_3<Point_2> mesh) {
  		if(mesh==null) return;
  		
  		for(Vertex<Point_2> v: mesh.vertices)
  			this.addPoint(v.getPoint());
  		
  	}

  	public void addPolyhedronVertexLabels(Polyhedron_3<Point_2> mesh, String[] labels) {
  		if(mesh==null || labels==null) return;
  		
  		int i=0;
  		for(Vertex<Point_2> v: mesh.vertices) {
  			if(labels[i]!=null)
  				this.addLabel(v.getPoint(), labels[i]);
  			i++;
  		}
  		
  	}

}
 