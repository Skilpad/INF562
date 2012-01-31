import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import Jcg.geometry.Point_2;
import Jcg.triangulations2D.HalfedgeHandle;
import Jcg.triangulations2D.TriangulationDSFace_2;

public class RayCastWindow extends Canvas {

	private static final long serialVersionUID = 1L;
	private final int ANGULAR_SPEED = 3;  // constant: no need to change
	private double linearSpeed = 0.00625;
	private int textureScale = 10000;
	double sceneZoom = 25;
	private RayCast r;
	private int X,Y;  // view dimensions
	private int horizon;  // height of horizon
	private int horizonShift;  // for walking effect
	private BufferedImage textureImg;  // texture for walls
//	private String textureFileName = "the_wall.jpg";
//	private String textureFileName = "brick_wall.jpg";
	private String textureFileName = "stone_wall.jpg";
	private Map map;  // to update the position on the map in real time
	
	// data for double-buffer
    Graphics bufferGraphics;
    BufferedImage offscreen;
    Dimension dim;
	

    
	public RayCastWindow (RayCast r) throws IOException {
        // wall texture image
//        textureImg = ImageIO.read(new File(textureFileName));  
        // Ray caster
        this.r = r;
        // Create map and compute its content
        this.map = new Map(r);
		Collection<TriangulationDSFace_2<Point_2>> facesDel = r.del.finiteFaces();
		LinkedList<Point_2[]> trianglesDel = new LinkedList<Point_2[]>();
		for (TriangulationDSFace_2<Point_2> f : facesDel)
			trianglesDel.add(new Point_2[]{f.vertex(0).getPoint(), f.vertex(1).getPoint(), f.vertex(2).getPoint()});
		Collection<HalfedgeHandle<Point_2>> cEdgesDel = r.del.constraintEdges();
		LinkedList<Point_2[]> cSegmentsDel = new LinkedList<Point_2[]> ();
		for (HalfedgeHandle<Point_2> e : cEdgesDel)
			cSegmentsDel.add(new Point_2[]{e.getVertex(0).getPoint(), e.getVertex(1).getPoint()});
		map.addTriangles(trianglesDel);
		map.addFatSegments(cSegmentsDel);	

        // Resize Canvas
        X = r.viewSize;
		Y = (int)(r.viewSize * 9.0 / 16.0); // vue en 16/9
		setSize(X, Y);  
		horizon = 2*Y/3;
		horizonShift = 0;
		
		// Create frame and add Canvas to it
		Frame f = new Frame("View");
		f.add(this);
		System.out.println(X + "x" + Y + " -> " + f.getWidth() + "x" + f.getHeight());
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			    System.exit(0);
			}
		});
		
		// Add key events listener
		RayCastKeyAdapter rcka = new RayCastKeyAdapter(r); 
		f.addKeyListener(rcka);
		f.pack();

		
		// double-buffer setup
        dim = getSize();
        System.out.println("dim = " + dim);
        offscreen = (BufferedImage) createImage(dim.width,dim.height);
        bufferGraphics = offscreen.getGraphics(); 


        // display window
		f.setVisible(true);
	}		

	// To avoid flicker
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics graphics) {
		// clear double buffer
		bufferGraphics.clearRect(0,0,dim.width,dim.height);
		// now we draw in double buffer...

		// draw background
    	// sky
    	bufferGraphics.setColor(new Color (200, 200, 255));
    	bufferGraphics.fillRect(0, 0, X, horizon+horizonShift);
		// ground
		bufferGraphics.setColor(new Color(135, 77, 8));
    	bufferGraphics.fillRect(0, horizon+horizonShift, X, Y-horizon-horizonShift);

    	// draw walls
    	bufferGraphics.setColor(Color.gray);
    	r.castRays();
//    	for (int x = 0; x < X; x++) if (r.distancesToObstacles[x] > 0) bufferGraphics.fillRect(x, horizon+horizonShift-(int)(0.66/r.distancesToObstacles[x]), 1, (int)(1/r.distancesToObstacles[x]));
    	for (int x = 0; x < X; x++)
    		if (r.distancesToObstacles[x] > 0) {
    			bufferGraphics.setColor(Color.gray);
    			bufferGraphics.fillRect(x, horizon+horizonShift-(int)(sceneZoom*0.66/r.distancesToObstacles[x]), 1, (int)(sceneZoom/r.distancesToObstacles[x]));
    		} else {
    			bufferGraphics.setColor(Color.red);
    			bufferGraphics.fillRect(x, horizon+horizonShift-3, 1, 6);
    		}
    	
    	// restore default color of graphics
    	bufferGraphics.setColor(Color.black);

    	// get current window's dimensions and map double buffer to window's buffer, with appropriate scaling
    	Dimension d = getSize();
    	graphics.drawImage(offscreen, 0, 0, d.width, d.height, this);
	}


	// subclass to handle key events
	protected class RayCastKeyAdapter extends KeyAdapter {
		private RayCast r;
		private boolean slide = false;
		private int cycle;  // for walking effect
		private final int walkingAmplitude = 3;
		
		public RayCastKeyAdapter(RayCast r) {
			super();
			this.r = r; 
			cycle = 0;
		}

		public void keyTyped (KeyEvent e) {
			switch (e.getKeyChar()) {
			case '*':
				sceneZoom *= 2;
				System.out.println("zoom = " + sceneZoom);
				break;
			case '/':
				sceneZoom /= 2;
				System.out.println("zoom = " + sceneZoom);
				break;
			case '+':
				linearSpeed *= 2;
				System.out.println("linear speed = " + linearSpeed);
				break;
			case '-':
				linearSpeed /= 2;
				System.out.println("linear speed = " + linearSpeed);
				break;
			}
			repaint();
		}

		public void keyReleased (KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ALT:
					slide = false;
					break;
			}
		}

		public void keyPressed (KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE: System.exit(0);
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				if (slide) {
					r.position = new Point_2 (r.position.getX().doubleValue() + 
							linearSpeed/2 * Math.cos(Math.PI/180*(r.orientation+90)),
							r.position.getY().doubleValue() +
							linearSpeed/2 * Math.sin(Math.PI/180*(r.orientation+90)));
					cycle-=60;
					horizonShift = (int) (walkingAmplitude*Math.cos(cycle*Math.PI/180)/2);
				}
				else
					r.orientation += ANGULAR_SPEED;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				if (slide) {
					r.position = new Point_2 (r.position.getX().doubleValue() + 
							linearSpeed/2 * Math.cos(Math.PI/180*(r.orientation-90)),
							r.position.getY().doubleValue() +
							linearSpeed/2 * Math.sin(Math.PI/180*(r.orientation-90)));
					cycle+=60;
					horizonShift = (int) (walkingAmplitude*Math.cos(cycle*Math.PI/180)/2);
				}
				else
					r.orientation -= ANGULAR_SPEED;
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				r.position = new Point_2 (r.position.getX().doubleValue() + 
						linearSpeed * Math.cos(Math.PI/180*r.orientation),
						r.position.getY().doubleValue() +
						linearSpeed * Math.sin(Math.PI/180*r.orientation));
				cycle+=30;
				horizonShift = (int) (walkingAmplitude*Math.cos(cycle*Math.PI/180));
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				r.position = new Point_2 (r.position.getX().doubleValue() - 
						linearSpeed * Math.cos(Math.PI/180*r.orientation),
						r.position.getY().doubleValue() -
						linearSpeed * Math.sin(Math.PI/180*r.orientation));
				cycle-=30;
				horizonShift = (int) (walkingAmplitude*Math.cos(cycle*Math.PI/180));
				break;
			case KeyEvent.VK_ALT:
				slide = true;
			default: return; 
			}
			r.castRays();
			repaint();
			map.repaint();
		}
	}
}
