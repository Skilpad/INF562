package Jcg.geometry;

import java.math.BigDecimal;

public class AlgebraExact {

    /** 
     * Exact computation of 2x2 determinant
     */
    public static BigDecimal det22( BigDecimal... m ) {
    	return m[0].multiply(m[3]).subtract(m[1].multiply(m[2]));
    }    	

    /** 
     * Exact computation of 3x3 determinant
     */
    public static BigDecimal det33( BigDecimal... m ) {
	BigDecimal det33 = BigDecimal.ZERO;
	det33 = 
	    det33.add(m[0].multiply(m[4].multiply(m[8]).subtract
				    (m[5].multiply(m[7]))));
	det33 = 
	    det33.subtract(m[1].multiply(m[3].multiply(m[8]).subtract
					 (m[5].multiply(m[6]))));
	    det33 = 
		det33.add(m[2].multiply(m[3].multiply(m[7]).subtract
					(m[4].multiply(m[6]))));
	    return det33;
    }

    /** 
     * Exact computation of 4x4 determinant
     */
    public static BigDecimal det44( BigDecimal... m ) {
    	BigDecimal det44 = BigDecimal.ZERO;
    	BigDecimal[] m33=new BigDecimal[9];
    	BigDecimal det33;
    	BigDecimal term;
    	
    	m33[0]=m[5]; m33[1]=m[6]; m33[2]=m[7];
    	m33[3]=m[9]; m33[4]=m[10]; m33[5]=m[11];
    	m33[6]=m[13]; m33[7]=m[14]; m33[8]=m[15]; 	
    	det33=det33(m33);
    	//System.out.println(""+det33);
    	term=m[0].multiply(det33);
    	//System.out.println(""+term);
    	det44=det44.add(term);
    	//System.out.println(""+det44);

    	m33[0]=m[4]; m33[1]=m[6]; m33[2]=m[7];
    	m33[3]=m[8]; m33[4]=m[10]; m33[5]=m[11];
    	m33[6]=m[12]; m33[7]=m[14]; m33[8]=m[15]; 	
    	det33=det33(m33);
    	term=m[1].multiply(det33);
    	det44=det44.subtract(term);

    	m33[0]=m[4]; m33[1]=m[5]; m33[2]=m[7];
    	m33[3]=m[8]; m33[4]=m[9]; m33[5]=m[11];
    	m33[6]=m[12]; m33[7]=m[13]; m33[8]=m[15]; 	
    	det33=det33(m33);
    	term=m[2].multiply(det33);
    	det44=det44.add(term);

    	m33[0]=m[4]; m33[1]=m[5]; m33[2]=m[6];
    	m33[3]=m[8]; m33[4]=m[9]; m33[5]=m[10];
    	m33[6]=m[12]; m33[7]=m[13]; m33[8]=m[14]; 	
    	det33=det33(m33);
    	term=m[3].multiply(det33);
    	det44=det44.subtract(term);
    	
//    	System.out.println(det44);
    	return det44;
    }

    /** 
     * Exact computation of 5x5 determinant
     */
    public static BigDecimal det55( BigDecimal... m ) {
    	BigDecimal det55 = BigDecimal.ZERO;
    	for (int i=0; i<5; i++) {
    		BigDecimal[] m44=new BigDecimal[16];
    		m44[0]=m[5+(i+1)%5]; m44[1]=m[5+((i+2)%5)]; m44[2]=m[5+(i+3)%5]; m44[3]=m[5+(i+4)%5];
    		m44[4]=m[10+(i+1)%5]; m44[5]=m[10+((i+2)%5)]; m44[6]=m[10+(i+3)%5]; m44[7]=m[10+(i+4)%5];
    		m44[8]=m[15+(i+1)%5]; m44[9]=m[15+((i+2)%5)]; m44[10]=m[15+(i+3)%5]; m44[11]=m[15+(i+4)%5];
    		m44[12]=m[20+(i+1)%5]; m44[13]=m[20+((i+2)%5)]; m44[14]=m[20+(i+3)%5]; m44[15]=m[20+(i+4)%5];
    		BigDecimal det44 = det44(m44);
    		//System.out.println(""+det44);
    		det55=det55.add(m[i].multiply(det44));
    		//System.out.println(""+det55);
    	}
    	return det55;
    }

    /** 
     * Exact computation of nxn determinant (slower than dimension-specific versions)
     */
    public static BigDecimal detnn(int n,  BigDecimal... m ) {
    	if (n <= 0)
    		throw new Error ("Empty matrix");
    	if (n == 1)
    		return m[0];
    	BigDecimal detnn = BigDecimal.ZERO;
		BigDecimal[] mm = new BigDecimal[(n-1)*(n-1)];
    	for (int i=0; i<n; i++) {
    		for (int j=0; j<n-1; j++)
    			for (int k=0; k<n-1; k++)
    				mm[j*(n-1) + k] = m[(j+1)*n + (i+1+k)%n];
    		if ((n&1) != 0)
    			detnn=detnn.add(m[i].multiply(detnn(n-1, mm)));
    		else
    			detnn=detnn.add(m[i].multiply(detnn(n-1, mm)).multiply(BigDecimal.valueOf((1-2*(i&1)))));
    	}
    	return detnn;
    }


    public static void main (String args[]) {
    	for(int i=0; i<10000; i++) {
    		BigDecimal [] m = new BigDecimal [25];
    		for (int j=0; j<25; j++)
    			m[j] = BigDecimal.valueOf(1-2*Math.random());
    		if (!AlgebraExact.detnn(2,m).equals(AlgebraExact.det22(m)))
    			System.out.println("det22: " + AlgebraExact.detnn(2,m) + " != " + AlgebraExact.det22(m));
    		if (!AlgebraExact.detnn(3,m).equals(AlgebraExact.det33(m)))
    			System.out.println("det33: " + AlgebraExact.detnn(3,m) + " != " + AlgebraExact.det33(m));
    		if (!AlgebraExact.detnn(4,m).equals(AlgebraExact.det44(m)))
    			System.out.println("det44: " + AlgebraExact.detnn(4,m) + " != " + AlgebraExact.det44(m));
    		if (!AlgebraExact.detnn(5,m).equals(AlgebraExact.det55(m)))
    			System.out.println("det55: " + AlgebraExact.detnn(5,m) + " != " + AlgebraExact.det55(m));
    	}
    }
}
