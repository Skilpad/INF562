import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


import Jcg.Fenetre;


public class Map extends Fenetre {
	private static final long serialVersionUID = 1L;
	private final double pointWidth = 0.04;
	private RayCast r;
	private double oldX, oldY;
	
    public Map (RayCast r) {
		super();
		frame.setTitle("Map");
		frame.pack();
		this.r = r;

    }
	
	// To avoid flicker
	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics graphics) {
		// double-buffer setup
		Dimension dim = getSize();
		BufferedImage offscreen = (BufferedImage) createImage(dim.width,dim.height);
		Graphics2D bufferGraphics = (Graphics2D) offscreen.getGraphics(); 

		// clear double buffer
		bufferGraphics.clearRect(0,0,dim.width,dim.height);

		// now we draw in double buffer...
		
		setTransform(bufferGraphics);
		paintNoTransform(bufferGraphics);
		double oldX = r.position.getX().doubleValue();
		double oldY = r.position.getY().doubleValue();
		Ellipse2D e = new Ellipse2D.Double(oldX-pointWidth/2, oldY-pointWidth/2, 
										   pointWidth, pointWidth);
    	bufferGraphics.setColor(Color.red);
		bufferGraphics.fill(e);
//    	g.setStroke(new BasicStroke((int)(lineThickness)));
    	bufferGraphics.draw(new Line2D.Double
				(oldX, oldY, oldX+10*lineThickness*Math.cos(r.orientation*Math.PI/180), 
						oldY+10*lineThickness*Math.sin(r.orientation*Math.PI/180)));


    	// finally we paste the double buffer into the window
    	graphics.drawImage(offscreen, 0, 0, this);

    }

}
