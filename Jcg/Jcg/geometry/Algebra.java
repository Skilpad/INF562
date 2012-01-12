package Jcg.geometry;

public class Algebra {

    /** 
     * Approximate computation of 2x2 determinant
     */
	public static double det22 (double... m) {
		return m[0]*m[3] - m[1]*m[2];
	}
	
	
    /** 
     * Approximate computation of 3x3 determinant
     */
    public static double det33( double... m ) {
    	double det33 = 0;
    	det33 += m[0]*(m[4]*m[8]-m[5]*m[7]);
    	det33 -= m[1]*(m[3]*m[8]-m[5]*m[6]);
    	det33 += m[2]*(m[3]*m[7]-m[4]*m[6]);
    	return det33;
    }

    /** 
     * Approximate computation of 4x4 determinant
     */
    public static double det44( double... m ) {
    	double det44 = 0;
    	double[] m33=new double[9];
    	double det33;
    	
    	m33[0]=m[5]; m33[1]=m[6]; m33[2]=m[7];
    	m33[3]=m[9]; m33[4]=m[10]; m33[5]=m[11];
    	m33[6]=m[13]; m33[7]=m[14]; m33[8]=m[15]; 	
    	det33=det33(m33);
    	det44 += m[0]*det33;

    	m33[0]=m[4]; m33[1]=m[6]; m33[2]=m[7];
    	m33[3]=m[8]; m33[4]=m[10]; m33[5]=m[11];
    	m33[6]=m[12]; m33[7]=m[14]; m33[8]=m[15]; 	
    	det33=det33(m33);
    	det44 -= m[1]*det33;

    	m33[0]=m[4]; m33[1]=m[5]; m33[2]=m[7];
    	m33[3]=m[8]; m33[4]=m[9]; m33[5]=m[11];
    	m33[6]=m[12]; m33[7]=m[13]; m33[8]=m[15]; 	
    	det33=det33(m33);
    	det44 += m[2]*det33;

    	m33[0]=m[4]; m33[1]=m[5]; m33[2]=m[6];
    	m33[3]=m[8]; m33[4]=m[9]; m33[5]=m[10];
    	m33[6]=m[12]; m33[7]=m[13]; m33[8]=m[14]; 	
    	det33=det33(m33);
    	det44 -= m[3]*det33;

    	return det44;
    }

    /** 
     * Approximate computation of 5x5 determinant
     */
    public static double det55( double... m ) {
    	double det55 = 0;
    	for (int i=0; i<5; i++) {
    		double[] m44=new double[16];
    		m44[0]=m[5+(i+1)%5]; m44[1]=m[5+((i+2)%5)]; m44[2]=m[5+(i+3)%5]; m44[3]=m[5+(i+4)%5];
    		m44[4]=m[10+(i+1)%5]; m44[5]=m[10+((i+2)%5)]; m44[6]=m[10+(i+3)%5]; m44[7]=m[10+(i+4)%5];
    		m44[8]=m[15+(i+1)%5]; m44[9]=m[15+((i+2)%5)]; m44[10]=m[15+(i+3)%5]; m44[11]=m[15+(i+4)%5];
    		m44[12]=m[20+(i+1)%5]; m44[13]=m[20+((i+2)%5)]; m44[14]=m[20+(i+3)%5]; m44[15]=m[20+(i+4)%5];
    		det55 += m[i]*det44(m44);
    	}
    	return det55;
    }

    /** 
     * Approximate computation of nxn determinant (slower than dimension-specific versions)
     */
    public static double detnn(int n,  double... m ) {
    	if (n <= 0)
    		throw new Error ("Empty matrix");
    	if (n == 1)
    		return m[0];
    	double detnn = 0;
		double[] mm = new double[(n-1)*(n-1)];
    	for (int i=0; i<n; i++) {
    		for (int j=0; j<n-1; j++)
    			for (int k=0; k<n-1; k++)
    				mm[j*(n-1) + k] = m[(j+1)*n + (i+1+k)%n];
    		if ((n&1) != 0)
    			detnn += m[i]*detnn(n-1, mm);
    		else
    			detnn += m[i]*detnn(n-1, mm)*(1-2*(i&1));
    	}
    	return detnn;
    }


    public static void main (String args[]) {
    	for(int i=0; i<100000; i++) {
    		double [] m = new double [25];
    		for (int j=0; j<25; j++)
    			m[j] = 1-2*Math.random();
    		if (Math.abs(detnn(2,m)-det22(m)) > 1e-10)
    			System.out.println("det22: " + detnn(2,m) + " != " + det22(m));
    		if (Math.abs(detnn(3,m)-det33(m)) > 1e-10)
    			System.out.println("det33: " + detnn(3,m) + " != " + det33(m));
    		if (Math.abs(detnn(4,m)-det44(m)) > 1e-10)
    			System.out.println("det44: " + detnn(4,m) + " != " + det44(m));
    		if (Math.abs(detnn(5,m)-det55(m)) > 1e-10)
    			System.out.println("det55: " + detnn(5,m) + " != " + det55(m));
    	}
    }
}
