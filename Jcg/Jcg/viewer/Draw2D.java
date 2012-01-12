package Jcg.viewer;

import java.awt.*;
import java.awt.Graphics;
import java.applet.*;
import java.util.*;
import java.io.*;

import Jcg.geometry.*;

public class Draw2D {
	Graphics g;
	Point_2 a,b;
	int width, height;
	int max_x=300;
	int max_y=300;
	
	boolean[] translate_x, translate_y;
	
	public Draw2D(Graphics g,Point_2 a,Point_2 b,int l, int h) {
		this.g=g; this.a=a; this.b=b; this.width=l; this.height=h;
	}
	
	public Draw2D() {}

    public void fillTriangle(int[] x, int[] y, Color colorTriangle, Color e1, Color e2, Color e3,boolean filled) {
    	g.setColor(colorTriangle);
    	if(filled==true && colorTriangle!=Color.white) g.fillPolygon(x,y,3);
    	
		g.setColor(e1); g.drawLine(x[0],y[0],x[1],y[1]);
		g.setColor(e3); g.drawLine(x[0],y[0],x[2],y[2]);
		g.setColor(e2); g.drawLine(x[1],y[1],x[2],y[2]);
    }

    public void fillTriangle(Point_2 p1, Point_2 p2, Point_2 p3, Color triangle) {
    	//throw new Error("To be completed");  	
    	g.setColor(triangle);

		int[] x=new int[3];
		int[] y=new int[3];
		double denominatorX=b.getX().doubleValue()-a.getX().doubleValue();
		double denominatorY=b.getY().doubleValue()-a.getY().doubleValue();
		
		x[0]= (int) (this.width*( (p1.getX().doubleValue()-a.getX().doubleValue()) / denominatorX ));
		y[0]= this.height - (int) (this.height*( (p1.getY().doubleValue()-a.getY().doubleValue()) / denominatorY ));
		
		x[1]= (int) (this.width*( (p2.getX().doubleValue()-a.getX().doubleValue()) / denominatorX ));
		y[1]= this.height - (int) (this.height*( (p2.getY().doubleValue()-a.getY().doubleValue()) / denominatorY ));

		x[2]= (int) (this.width*( (p3.getX().doubleValue()-a.getX().doubleValue()) / denominatorX ));
		y[2]= this.height - (int) (this.height*( (p3.getY().doubleValue()-a.getY().doubleValue()) / denominatorY ));
    	
    	g.fillPolygon(x,y,3);
    }

    // draw an arrow on the vector v-u (oriented toward v)
    public void drawArrow(Point_2 u, Point_2 v, Color arrowColor) {
    	Point_2 p0, p1, p2, c;
    	
    	double b=0.6;
    	double x_coord=(1-b)*u.getX().doubleValue()+b*v.getX().doubleValue();
    	double y_coord=(1-b)*u.getY().doubleValue()+b*v.getY().doubleValue();
    	c=new Point_2(x_coord, y_coord);

    	double radius=Math.sqrt(u.squareDistance(v).doubleValue());
    	double dxNormalized=(v.getX().doubleValue()-u.getX().doubleValue())/radius;
    	double dyNormalized=(v.getY().doubleValue()-u.getY().doubleValue())/radius;
    	double alpha=Math.acos(dxNormalized);
    	if(dyNormalized<0.) alpha=-1.*alpha;
    	
    	double factor=0.01;
    	p0=new Point_2(c);
    	p0.translateOf(new Vector_2(factor*Math.cos(alpha-Math.PI/2.), factor*Math.sin(alpha-Math.PI/2.)));
    	p1=new Point_2(c);
    	p1.translateOf(new Vector_2(factor*Math.cos(alpha+Math.PI/2.), factor*Math.sin(alpha+Math.PI/2.)));
    	p2=new Point_2(c);
    	p2.translateOf(new Vector_2(3*factor*Math.cos(alpha), 3*factor*Math.sin(alpha)));
    	
    	if(arrowColor!=null)
    		g.setColor(arrowColor);
    	this.fillTriangle(p0, p1, p2, arrowColor);
    	g.setColor(Color.black);
    }

    public void fillTriangle(int[] x, int[] y, Color triangle, Color e1, Color e2, Color e3, double epsilon, boolean filled) {
    	g.setColor(triangle);
    	if(filled==true) g.fillPolygon(x,y,3);
    	
    	int[] v0,v1,v2;
    	v0=this.getLinearCombination(x,y,1.-epsilon,epsilon/2.,epsilon/2.);
    	v1=this.getLinearCombination(x,y,epsilon/2.,1.-epsilon,epsilon/2.);
    	v2=this.getLinearCombination(x,y,epsilon/2.,epsilon/2.,1.-epsilon);
    	
    	int[] edge_x=new int[4], edge_y=new int[4];
		
		edge_x[0]=x[0]; edge_x[1]=x[1]; edge_x[2]=v1[0]; edge_x[3]=v0[0];
		edge_y[0]=y[0]; edge_y[1]=y[1]; edge_y[2]=v1[1]; edge_y[3]=v0[1];
		g.setColor(e1); 
		g.fillPolygon(edge_x,edge_y,4);	
		//
		g.setColor(Color.black); g.drawLine(x[0],y[0],x[1],y[1]);
		

		edge_x[0]=x[0]; edge_x[1]=x[2]; edge_x[2]=v2[0]; edge_x[3]=v0[0];
		edge_y[0]=y[0]; edge_y[1]=y[2]; edge_y[2]=v2[1]; edge_y[3]=v0[1];
		g.setColor(e3); 
		g.fillPolygon(edge_x,edge_y,4);	
		//
		g.setColor(Color.black); g.drawLine(x[0],y[0],x[2],y[2]);

		edge_x[0]=x[1]; edge_x[1]=x[2]; edge_x[2]=v2[0]; edge_x[3]=v1[0];
		edge_y[0]=y[1]; edge_y[1]=y[2]; edge_y[2]=v2[1]; edge_y[3]=v1[1];		
		g.setColor(e2); 
		g.fillPolygon(edge_x,edge_y,4);	
		//
		g.setColor(Color.black); g.drawLine(x[1],y[1],x[2],y[2]);
    }
    
    public int[] getLinearCombination(int[] x, int[] y, double alpha, double beta, double gamma) {
    	// the sum of alpha, beta and gamma must be 1
    	int[] v=new int[2]; // v is a linear combination of the 3 input points
    	
    	v[0]=(int)(alpha*x[0]+beta*x[1]+gamma*x[2]);
    	v[1]=(int)(alpha*y[0]+beta*y[1]+gamma*y[2]);
    	
    	return v;
    }
	
	public void drawSegment(Point_2 p, Point_2 q) {
		Point pI=this.getPoint(p);
		Point qI=this.getPoint(q);
		
		g.setColor(Color.black);
		g.drawLine(pI.x, this.height-pI.y, qI.x, this.height-qI.y);
	}

	public void drawSegment(Point_2 p, Point_2 q, Color c) {
		Point pI=this.getPoint(p);
		Point qI=this.getPoint(q);
		
		if(c!=null)
			g.setColor(c);
		else
			g.setColor(Color.black);
		g.drawLine(pI.x, this.height-pI.y, qI.x, this.height-qI.y);
		g.setColor(Color.black);
	}

	public void drawCircle(Point_2 v) {
		double x=v.getX().doubleValue();
		double y=v.getY().doubleValue();

		int i=(int) (this.width*( (x-a.getX().doubleValue()) / (b.getX().doubleValue()-a.getX().doubleValue()) ));
		int j=(int) (this.height*( (y-a.getY().doubleValue()) / (b.getY().doubleValue()-a.getY().doubleValue()) ));
		
		g.setColor(Color.blue);
		g.fillOval(i-2,this.height-j-2,7,7);
		g.setColor(Color.black);
	}
	
	public void drawLabel(Point_2 v, String label) {
		double x=v.getX().doubleValue();
		double y=v.getY().doubleValue();
		
		int i= (int) (this.width*( (x-a.getX().doubleValue()) / (b.getX().doubleValue()-a.getX().doubleValue()) ));
		int j= (int) (this.height*( (y-a.getY().doubleValue()) / (b.getY().doubleValue()-a.getY().doubleValue()) ));
		
		g.setColor(Color.blue);
		//g.fillOval(n1,h-m1,9,9);
		
		Font font=Font.getFont("SANS_SERIF");
		g.setFont(font);
		g.drawString(label,i+3,this.height-j+7);
		g.setColor(Color.black);
	}

	public Point getPoint(Point_2 v) {
		double x=v.getX().doubleValue();
		double y=v.getY().doubleValue();
		int i= (int) (this.width*( (x-a.getX().doubleValue()) / (b.getX().doubleValue()-a.getX().doubleValue()) ));
		int j= (int) (this.height*( (y-a.getY().doubleValue()) / (b.getY().doubleValue()-a.getY().doubleValue()) ));
		
		return new Point(i,j);
	}
	
}