package Jcg.geometry;

import java.math.BigDecimal;

public class GeometricOperations_3 {

	// Bounds for filters depend on the degree of the predicate
	private static final double epsilon3 = 1e-12;
	private static final double epsilon4 = 1e-9;
//	private static final double epsilon3 = Double.MAX_VALUE;
//	private static final double epsilon4 = Double.MAX_VALUE;
	
	
	 /**
	 * Returns the square of the distance between two 3D points
     * with exact computations (slow but more reliable)
     *
     * @param p1,p2 the two 3D points
     * @return dist the square of the distance
     */
    public static BigDecimal squaredistanceExact(BigDecimal px, BigDecimal py, BigDecimal pz, BigDecimal qx, BigDecimal qy, BigDecimal qz) {
    	BigDecimal dx = qx.subtract(px);
    	BigDecimal dy = qy.subtract(py);
    	BigDecimal dz = qz.subtract(pz);
    	return dx.multiply(dx).add(dy.multiply(dy).add(dz.multiply(dz)));
    }
    

    /**
     * Tests whether segment s intersects triangle t (test is filtered)
     *
     */
    public static boolean doIntersect(Segment_3 s, Triangle_3 t) {
    	Point_3 p = (Point_3) s.source(), q = (Point_3) s.target();
    	Point_3 a = t.vertex(0), b = t.vertex(1), c = t.vertex(2);
    	int orientabcp = orientation(a,b,c,p);
    	int orientabcq = orientation(a,b,c,q);
    	int orientabpq = orientation(a,b,p,q);
    	int orientbcpq = orientation(b,c,p,q);
    	int orientcapq = orientation(c,a,p,q);
    	
    	// We are redundent between ab, bc and ca to avoid problems when line pq intersects one of the edges
    	return orientabcp*orientabcq <= 0 &&
    	orientabpq*orientbcpq >= 0 && orientbcpq*orientcapq >= 0 && orientcapq*orientabpq >= 0; 
    }
 

    /**
     * Test whether four 3D points are coplanar (test is filtered)
     */
    public static boolean coplanarExact(Point_3 a, Point_3 b, Point_3 c, Point_3 d) {
    	return orientation(a,b,c,d) == 0;
    }
    
    /** 
     * Returns orientation of tetrahedron (a, b, c, d): +1 if orientation is direct, -1 if orientation
     * is indirect, 0 if points are coplanar (test is filtered)
     */
    public static int orientation(Point_3 a, Point_3 b, Point_3 c, Point_3 d) {
    	// Note: we multiply det by -1 because positive orientation <=> negative determinant in odd dimensions
    	double det = -Algebra.det44(new double[] {a.x, a.y, a.z, 1, b.x, b.y, b.z, 1, c.x, c.y, c.z, 1, d.x, d.y, d.z, 1});
    	if (det > epsilon3)
    		return 1;
    	else if (det < -epsilon3)
    		return -1;
    	
    	// else perform exact computation
    	BigDecimal ax = BigDecimal.valueOf(a.x);
    	BigDecimal ay = BigDecimal.valueOf(a.y);
    	BigDecimal az = BigDecimal.valueOf(a.z);
    	BigDecimal bx = BigDecimal.valueOf(b.x);
    	BigDecimal by = BigDecimal.valueOf(b.y);
    	BigDecimal bz = BigDecimal.valueOf(b.z);
    	BigDecimal cx = BigDecimal.valueOf(c.x);
    	BigDecimal cy = BigDecimal.valueOf(c.y);
    	BigDecimal cz = BigDecimal.valueOf(c.z);
    	BigDecimal dx = BigDecimal.valueOf(d.x);
    	BigDecimal dy = BigDecimal.valueOf(d.y);
    	BigDecimal dz = BigDecimal.valueOf(d.z);
    	
    	BigDecimal[] m=new BigDecimal[16];
    	m[0]=ax; m[1]=ay; m[2]=az; m[3]=BigDecimal.valueOf(1.0);
    	m[4]=bx; m[5]=by; m[6]=bz; m[7]=BigDecimal.valueOf(1.0);
    	m[8]=cx; m[9]=cy; m[10]=cz; m[11]=BigDecimal.valueOf(1.0);
    	m[12]=dx; m[13]=dy; m[14]=dz; m[15]=BigDecimal.valueOf(1.0);

    	// Note: we multiply det by -1 because positive orientation <=> negative determinant in odd dimensions
    	return AlgebraExact.det44(m).multiply(BigDecimal.valueOf(-1)).compareTo(BigDecimal.ZERO);
    }

    /** 
     * Returns the position of p with respect to the circumsphere of tetrahedron (a, b, c, d): +1 if 
     * p lies outside the sphere, -1 if p lies inside the sphere, 0 if p lies on the sphere
     * with exact computations (slow but more reliable)
     */
     public static int sideOfSphere(Point_3 p, Point_3 a, Point_3 b, Point_3 c, Point_3 d) {
     	double det = Algebra.det55(new double[] {p.x, p.y, p.z, p.x*p.x+p.y*p.y+p.z*p.z, 1,
     			a.x, a.y, a.z, a.x*a.x+a.y*a.y+a.z*a.z, 1, 
     			b.x, b.y, b.z, b.x*b.x+b.y*b.y+b.z*b.z, 1, 
     			c.x, c.y, c.z, c.x*c.x+c.y*c.y+c.z*c.z, 1, 
     			d.x, d.y, d.z, d.x*d.x+d.y*d.y+d.z*d.z, 1});
//     	System.out.println("det=" + det);
     	if (det > epsilon4)
    		return orientation(a,b,c,d);
    	else if (det < -epsilon4)
    		return -orientation(a,b,c,d);

    	
    	// else perform exact computation
     	BigDecimal px = BigDecimal.valueOf(p.x);
    	BigDecimal py = BigDecimal.valueOf(p.y);
    	BigDecimal pz = BigDecimal.valueOf(p.z);
    	BigDecimal pt = (px.multiply(px)).add(py.multiply(py)).add(pz.multiply(pz));
    	BigDecimal ax = BigDecimal.valueOf(a.x);
    	BigDecimal ay = BigDecimal.valueOf(a.y);
    	BigDecimal az = BigDecimal.valueOf(a.z);
    	BigDecimal at = (ax.multiply(ax)).add(ay.multiply(ay)).add(az.multiply(az));
    	BigDecimal bx = BigDecimal.valueOf(b.x);
    	BigDecimal by = BigDecimal.valueOf(b.y);
    	BigDecimal bz = BigDecimal.valueOf(b.z);
    	BigDecimal bt = (bx.multiply(bx)).add(by.multiply(by)).add(bz.multiply(bz));
    	BigDecimal cx = BigDecimal.valueOf(c.x);
    	BigDecimal cy = BigDecimal.valueOf(c.y);
    	BigDecimal cz = BigDecimal.valueOf(c.z);
    	BigDecimal ct = (cx.multiply(cx)).add(cy.multiply(cy)).add(cz.multiply(cz));
    	BigDecimal dx = BigDecimal.valueOf(d.x);
    	BigDecimal dy = BigDecimal.valueOf(d.y);
    	BigDecimal dz = BigDecimal.valueOf(d.z);
    	BigDecimal dt = (dx.multiply(dx)).add(dy.multiply(dy)).add(dz.multiply(dz));

    	BigDecimal[] m=new BigDecimal[25];
    	m[0]=px; m[1]=py; m[2]=pz; m[3]=pt; m[4]=BigDecimal.valueOf(1.0);
    	m[5]=ax; m[6]=ay; m[7]=az; m[8]=at; m[9]=BigDecimal.valueOf(1.0);
    	m[10]=bx; m[11]=by; m[12]=bz; m[13]=bt; m[14]=BigDecimal.valueOf(1.0);
    	m[15]=cx; m[16]=cy; m[17]=cz; m[18]=ct; m[19]=BigDecimal.valueOf(1.0);
    	m[20]=dx; m[21]=dy; m[22]=dz; m[23]=dt; m[24]=BigDecimal.valueOf(1.0);

//     	System.out.println("detExact=" + AlgebraExact.det55(m).multiply(BigDecimal.valueOf(orientation(a,b,c,d))));

     	return AlgebraExact.det55(m).multiply(BigDecimal.valueOf(orientation(a,b,c,d))).compareTo(BigDecimal.ZERO);
     }

     public static Point_3 circumCenter (Point_3 p, Point_3 q, Point_3 r, Point_3 s) {
         // Translate p to origin to simplify the expression.
         double qpx = q.getX().doubleValue()-p.getX().doubleValue();
         double qpy = q.getY().doubleValue()-p.getY().doubleValue();
         double qpz = q.getZ().doubleValue()-p.getZ().doubleValue();
         double qp2 = qpx*qpx + qpy*qpy + qpz*qpz;
         double rpx = r.getX().doubleValue()-p.getX().doubleValue();
         double rpy = r.getY().doubleValue()-p.getY().doubleValue();
         double rpz = r.getZ().doubleValue()-p.getZ().doubleValue();
         double rp2 = rpx*rpx + rpy*rpy + rpz*rpz;
         double spx = s.getX().doubleValue()-p.getX().doubleValue();
         double spy = s.getY().doubleValue()-p.getY().doubleValue();
         double spz = s.getZ().doubleValue()-p.getZ().doubleValue();
         double sp2 = spx*spx + spy*spy + spz*spz;

         double num_x = Algebra.det33 (new double[] {qpy,qpz,qp2,
        		 rpy,rpz,rp2, spy,spz,sp2});
         double num_y = Algebra.det33 (new double[] {qpx,qpz,qp2,
        		 rpx,rpz,rp2, spx,spz,sp2});
         double num_z = Algebra.det33 (new double[] {qpx,qpy,qp2,
        		 rpx,rpy,rp2, spx,spy,sp2});
         double den = Algebra.det33 (new double[] {qpx,qpy,qpz,
        		 rpx,rpy,rpz, spx,spy,spz});
         double inv = 1.0 / (2.0 * den);

         double x = p.getX().doubleValue() + num_x*inv;
         double y = p.getY().doubleValue() - num_y*inv;
         double z = p.getZ().doubleValue() + num_z*inv;
         return new Point_3 (x, y, z);

     }
    
 	/**
      * determines on which side of the tetrahedron t point p lies: -1 means inside, 0 means on the boundary,
      * +1 means outside 
      */     
     public static int sideOfTetrahedron (Point_3 p, Point_3 [] t) {
     	int[] res = new int[4];
     	for (int i=0; i<4; i++)
     		// compute orientation of tetrahedron, signed with orientation of facet
     		res[i] = (1-2*(i&1)) * orientation(t[(i+1)&3], t[(i+2)&3], t[(i+3)&3], p);
     	// introduce some redundence in the tests, in order to be more robust to degeneracies
     	for (int i=0; i<4; i++)
     		for (int j=i+1; j<4; j++)
     			if (res[i] * res[j] < 0)
     				return 1;
     	for (int i=0; i<4; i++)
     		if (res[i] == 0)
     			return 0;
     	return -1;
     }


    public static void main(String[] args){

    	Point_3 a = new Point_3(-2,2,-2);
    	Point_3 b = new Point_3(2,-2,-2);
    	Point_3 c = new Point_3(-2,-2,2);
    	Point_3 d = new Point_3(2,-2,2);
//    	Point_3 a = new Point_3(0,0,0);
//    	Point_3 b = new Point_3(2,0,0);
//    	Point_3 c = new Point_3(0,2,0);
//    	Point_3 d = new Point_3(0,0,2);
    	System.out.println("orientation("+a+","+b+","+c+","+d+")=" + orientation(a,b,c,d));
    	
    	System.out.println("" + sideOfSphere(new Point_3(0,0,0),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(1,1,1),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(0.5,-0.5,0.5),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(2,0,0),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(2,-1e-10,0),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(2,1e-10,0),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(0,-2,0),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(2*Math.sqrt(3), 0,0),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(2*Math.sqrt(3)+1e-10, 0, 0),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(2*Math.sqrt(2), 2,0),a,b,c,d));
    	System.out.println("" + sideOfSphere(new Point_3(2*Math.sqrt(2)-1e-10, 0, 2),a,b,c,d));
    	}
}
