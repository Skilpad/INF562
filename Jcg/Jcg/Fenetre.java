package Jcg;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.Collection;

import Jcg.geometry.Point_2;
import Jcg.geometry.Point_;
import Jcg.polyhedron.Halfedge;
import Jcg.polyhedron.Polyhedron_3;

/**
 *  2D window allowing to draw line segments and points
 */
public class Fenetre extends Canvas {

	private static final long serialVersionUID = 1L;
	private BufferedImage backgroudImage=null;
	protected Frame frame;
	
    // line thickness
    public final float r = 0.02f;
    public float lineThickness=0.001f;
    double boundaryThickness=0.5;

    // lists of points and lines to draw
    Vector<Point2D.Double> pl = new Vector<Point2D.Double>();
    Vector<Line2D.Double>  ll = new Vector<Line2D.Double>();
    Vector<Line2D.Double>  fatSegmentsList = new Vector<Line2D.Double>();
    
    // list of colored segments to draw
    Vector<Line2D.Double> coloredEdgeList=new Vector<Line2D.Double>();
    Vector<Color> colorList=new Vector<Color>();
    
    Vector<String> labels=new Vector<String>();
    Vector<Point2D.Double> labelCoordinates=new Vector<Point2D.Double>();

    // coordinates of the bounding box
    protected double xmin=Double.MAX_VALUE, xmax=Double.MIN_VALUE, 
	ymin=Double.MAX_VALUE, ymax=Double.MIN_VALUE;

    //----------------------------------------
    //-------------   Methods   --------------
    //----------------------------------------
    
    /**
     * Update of the bounding box
     */    
    protected void update(double x, double y) {
    	if (x<xmin)
    		xmin = x-boundaryThickness;
    	if (x>xmax)
    		xmax = x+boundaryThickness;
    	if (y<ymin)
    		ymin = y-boundaryThickness;
    	if (y>ymax)
    		ymax = y+boundaryThickness;
    }
    
    /**
     * Get bounding box coordinates
     */    
    public double[] boundingBox() {
    	return new double[] {xmin, xmax, ymin, ymax};
    }

    /**
     * Remove all points and segments from the window
     */    
    public void clearWindow() {
        pl = new Vector<Point2D.Double>();
        ll = new Vector<Line2D.Double>();
        fatSegmentsList = new Vector<Line2D.Double>();
        
        coloredEdgeList=new Vector<Line2D.Double>();
        colorList=new Vector<Color>();
        
        // update bounding box
        xmin=Double.MAX_VALUE; xmax=Double.MIN_VALUE; 
    	ymin=Double.MAX_VALUE; ymax=Double.MIN_VALUE;
    	
    }

    // ajout de point ou de segment ne sera visible
    // qu'apres avoir appele repaint()
    public void ajoutePoint(double x, double y) {
	pl.add(new Point2D.Double(x,y));
	update(x,y);
    }

    /**
     * Draw a text label at a given position
     */    
    public void addLabel(String label, Point_2 p) {
    	this.labels.add(label);
    	double x=p.getX().doubleValue();
    	double y=p.getY().doubleValue();
    	this.labelCoordinates.add(new Point2D.Double(x, y));
    	update(x,y);
    }

    public void addPoints(Point_2[] points) {
    	for(int i=0;i<points.length;i++)
    		ajoutePoint(
    			points[i].getX().doubleValue(), points[i].getY().doubleValue());
    }

    /**
     * Add a 2D point to the window
     */    
    public void addPoint(Point_2 point) {
    	ajoutePoint(point.getX().doubleValue(), point.getY().doubleValue());
    }

    /**
     * Add a collection of 2D points
     */    
    public void addPoints(Collection<Point_2> points) {
    	for(Point_2 p : points)
    		ajoutePoint(p.getX().doubleValue(), p.getY().doubleValue());
    }

    /**
     * Add a collection of (generic) points
     */    
    public void addGenericPoints(Collection<Point_<?>> points) {
    	for(Point_<?> p : points)
    		ajoutePoint(p.getCartesian(0).doubleValue(), p.getCartesian(1).doubleValue());
    }

    public void addSegments(java.util.Collection<Point_2[]> segments) {
    	for(Point_2[] p : segments) {
    		Point_2 p1=p[0];
    		Point_2 p2=p[1];
    		ajouteSegment(
    			p1.getX().doubleValue(), p1.getY().doubleValue(),
    			p2.getX().doubleValue(), p2.getY().doubleValue()
    			);
    	}
    }

    public void addFatSegments (java.util.Collection<Point_2[]> segments) {
    	for(Point_2[] p : segments) {
    		Point_2 p1=p[0];
    		Point_2 p2=p[1];
    		addFatSegment(
    			p1.getX().doubleValue(), p1.getY().doubleValue(),
    			p2.getX().doubleValue(), p2.getY().doubleValue()
    			);
    	}
    }

    /**
     * Add the three segments of a triangle
     */    
    public void addTriangle(Point_2[] points) {
    	for(int i=0;i<points.length-1;i++) {
    		Point_2 p1=points[i];
    		Point_2 p2=points[i+1];
    		ajouteSegment(
    			p1.getX().doubleValue(), p1.getY().doubleValue(),
    			p2.getX().doubleValue(), p2.getY().doubleValue()
    			);
    	}
		Point_2 p1=points[0];
		Point_2 p2=points[points.length-1];
		ajouteSegment(
			p1.getX().doubleValue(), p1.getY().doubleValue(),
			p2.getX().doubleValue(), p2.getY().doubleValue()
			);
    }

    /**
     * Add a list of triangles
     */    
    public void addTriangles(Collection<Point_2[]> triangles) {
    	for(Point_2[] t : triangles) {
    		addTriangle(t);
    	}
    }    

    /**
     * Add a segment
     */    
    public void addSegment(Point_2 p1, Point_2 p2) {
    	ajouteSegment(p1.getX().doubleValue(), p1.getY().doubleValue(),
    				  p2.getX().doubleValue(), p2.getY().doubleValue()
    				 );
    }

    /**
     * Draw a segment with a given color
     */    
    public void addColoredSegment(Point_2 p1, Point_2 p2, Color c) {
    	if(c==null)
    		throw new Error("null color");
    	if(p1==null || p2==null)
    		throw new Error("points non defined");
    	addColoredSegment(p1.getX().doubleValue(), p1.getY().doubleValue(),
    				  p2.getX().doubleValue(), p2.getY().doubleValue(), c
    				 );
    }

    public void addFatSegment(Point_2 p1, Point_2 p2) {
    	addFatSegment(p1.getX().doubleValue(), p1.getY().doubleValue(),
    				  p2.getX().doubleValue(), p2.getY().doubleValue()
    				 );
    }
    
    /**
     * Draw all halfedges of a planar Polyhedron (half-edge DS)
     */    
    public void addPolyhedronEdges(Polyhedron_3<Point_2> polyhedron){
    	this.clearWindow();
    	Point_2 p1, p2;
    	for(Halfedge<Point_2> e: polyhedron.halfedges) {
    		if(e==null || e.opposite==null) continue;
    		p1=(Point_2)e.getVertex().getPoint();
    		p2=(Point_2)e.getOpposite().getVertex().getPoint();
    		addSegment(p1, p2);
    	}
    }

    public void addPolyhedronColoredEdges(Polyhedron_3<Point_2> polyhedron, Color[] colors){
    	if(colors==null)
    		throw new Error("edge colors are not defined");
    	if(polyhedron.halfedges.size()!=colors.length)
    		throw new Error("wrong size of colors list");
    	
    	this.clearWindow();
    	Point_2 p1, p2;
    	int i=0;
    	for(Halfedge<Point_2> e: polyhedron.halfedges) {
    		if(e==null || e.opposite==null) continue;
    		p1=(Point_2)e.getVertex().getPoint();
    		p2=(Point_2)e.getOpposite().getVertex().getPoint();
    		if(colors[i]!=null)
    			this.addColoredSegment(p1, p2, colors[i]);
    		else
    			System.out.println("null color "+i);
    		i++;
    	}
    }
    
    public void addColoredSegments(Collection<Point_2[]> segments, Color[] colors) {
    	int i=0;
    	for(Point_2[] p : segments) {
    		Point_2 p1=p[0];
    		Point_2 p2=p[1];
    		this.addColoredSegment(p1, p2, colors[i]);
    		i++;
    	}
    }

    public void ajouteSegment(double x1, double y1, double x2, double y2) {
	ll.add(new Line2D.Double(x1,y1,x2,y2));
	update(x1,y1);
	update(x2,y2);
    }
    
    public void addColoredSegment(double x1, double y1, double x2, double y2, Color c) {
    	this.coloredEdgeList.add(new Line2D.Double(x1, y1, x2, y2));
    	this.colorList.add(c);
    	update(x1,y1);
    	update(x2,y2);
    }

    public void addFatSegment(double x1, double y1, double x2, double y2) {
	fatSegmentsList.add(new Line2D.Double(x1,y1,x2,y2));
	update(x1,y1);
	update(x2,y2);
    }

    /**
     * Draw a point on the canvas
     */    
    void paint(Graphics2D g, Point2D p, Color c) {
    	Ellipse2D e = new Ellipse2D.Double(p.getX()-3*lineThickness, p.getY()-3*lineThickness, 5*lineThickness, 5*lineThickness);
    	g.setColor(c);
    	g.fill(e);
    	g.setColor(Color.black);
    	//g.draw(e);
    }
	
    protected void setTransform(Graphics2D g) {
    	double sx = getWidth()/(xmax-xmin);
    	double sy = getHeight()/(ymax-ymin);
    	double s  = Math.min(sx, sy);
    	g.scale(s,-s);
    	g.translate(-(double)xmin,-(double)ymax);
    	lineThickness=(float)1.f/(float)s;
    	g.setStroke(new BasicStroke(lineThickness));
    }
	
    protected void paintNoTransform (Graphics2D g) {
    	g.setColor(Color.black);
    	for (Line2D l:  ll)
    	    g.draw(l);
    	for (Point2D p: pl)
    	    paint(g, p, Color.red);

    	g.setStroke(new BasicStroke(lineThickness*3));
    	g.setColor(Color.blue);
    	for (Line2D l:  fatSegmentsList) {
    		g.draw(l);
    	}
    	
    	g.setStroke(new BasicStroke(lineThickness*2));
    	int i=0;
    	for(Line2D l: this.coloredEdgeList) {
    		Color c=this.colorList.get(i);
    		if(c!=null) g.setColor(c);
    		else g.setColor(Color.black);
    		g.draw(l);
    		i++;
    	}
       	g.setColor(Color.black);
    }
    
    public void paint(Graphics graphics) {
    	Graphics2D g = (Graphics2D)graphics;
    	if(this.backgroudImage!=null) {
    		displayImage(g, this.backgroudImage, 0, 0);
    		return;
    	}
    	setTransform(g);
    	paintNoTransform(g);
    }

    // Display image
    public void displayImage (Graphics2D g, BufferedImage bimg, int x, int y) {
    	g.drawImage(bimg, null, x, y);
    }

    /**
     * Creates a new Fenetre and shows it immediately.
     *
     */
    public Fenetre() {
	frame = new Frame("Fenetre");
	frame.add(this);
	frame.setSize(400, 400);
	frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit(0);
		}
	    });
	frame.setVisible(true);
    }

    /**
     * Creates a new Fenetre and shows it only if setVisible == true (recommended when one
     * wants to update the Fenetre's data structures before showing it).
     */
    public Fenetre(boolean setVisible) {
    	frame = new Frame("Fenetre");
    	frame.add(this);
    	frame.setSize(400, 400);
    	frame.addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {
    		    System.exit(0);
    		}
    	    });
    	frame.setVisible(setVisible);
        }

    /**
     * Shows the Fenetre.
     *
     */
    public void setVisible() {
    	frame.setVisible(true);
    }
    
    /**
     * Creates a new Fenetre with a given title.
     */
    public Fenetre(String title) {
    	frame = new Frame(title);
    	frame.add(this);
    	frame.setSize(400, 400);
    	frame.addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {
    		    System.exit(0);
    		}
    	    });
    	frame.setVisible(true);
    }

    /**
     * Creates a new Fenetre with a given title and background image.
     */
    public Fenetre(BufferedImage bimg, String title) {
    	this.backgroudImage=bimg;
    	frame = new Frame(title);
    	frame.add(this);
    	frame.setSize(bimg.getWidth(), bimg.getHeight());
    	frame.addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {
    		    System.exit(0);
    		}
    	    });
    	frame.setVisible(true);
        }

    // petit test
    public static void main(String args[]) {
	Fenetre f = new Fenetre();
	f.ajoutePoint(100,0);
	f.ajoutePoint(110,0);
	f.ajoutePoint(110,5);
	f.ajouteSegment(100,0,110,5);
    }
}
    
