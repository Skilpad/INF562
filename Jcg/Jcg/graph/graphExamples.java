package Jcg.graph;

public class graphExamples {

    public static Graph example12(){
		Graph g=new AdjacencyGraph(12);
		
		// exterior face 0, 1, 11
		g.addEdge(0,1); g.addEdge(0,11); g.addEdge(0,7); g.addEdge(0,8); g.addEdge(0,2);
		g.addEdge(1,11); g.addEdge(1,3); g.addEdge(1,5); g.addEdge(1,2);
		g.addEdge(2,5); g.addEdge(2,10); g.addEdge(2,9); g.addEdge(2,8);
		
		g.addEdge(11,3); g.addEdge(11,4); g.addEdge(11,6); g.addEdge(11,7);
		g.addEdge(3,4); g.addEdge(3,5);
		g.addEdge(4,5); g.addEdge(4,10); g.addEdge(4,7); g.addEdge(4,6);
		g.addEdge(5,10); 
		g.addEdge(6,7);
		g.addEdge(7,8); g.addEdge(7,9); g.addEdge(7,10);
		g.addEdge(8,9);
		g.addEdge(9,10);
    	return g;
    }

    public static Graph example6(){
		Graph g=new AdjacencyGraph(6);
		
		// exterior face 0, 1, 2
		g.addEdge(0,1); g.addEdge(0,3); g.addEdge(0,5); g.addEdge(0,2);
		g.addEdge(1,2); g.addEdge(1,3); g.addEdge(1,4);
		g.addEdge(2,4); g.addEdge(2,5);
		
		g.addEdge(3,4); g.addEdge(3,5);
		g.addEdge(4,5);

    	return g;
    }

    public static Graph example7(){
		Graph g=new AdjacencyGraph(7);
		
		// exterior face 0, 1, 2
		g.addEdge(0,1); g.addEdge(0,3); g.addEdge(0,5); g.addEdge(0,4); g.addEdge(0,2);
		g.addEdge(1,2); g.addEdge(1,3); g.addEdge(1,6);
		g.addEdge(2,5); g.addEdge(2,6);
		
		g.addEdge(3,4); g.addEdge(3,5); g.addEdge(3,6);
		g.addEdge(4,5);
		g.addEdge(5,6);

    	return g;
    }

//---------------------------------------------------    
    
	public static Number[][] getSchnyderCoefficient12(int n, int k) {
		Number[][] coeff=new Double[n][k];
		for(int i=0;i<n;i++) {
			for(int j=0;j<k;j++)
				coeff[i][j]=1.;
		}
		coeff[3][0]=2.; coeff[3][1]=15.; coeff[3][2]=2.;
		coeff[4][0]=5.; coeff[4][1]=11.; coeff[4][2]=3.;
		coeff[5][0]=1.; coeff[5][1]=13.; coeff[5][2]=5.;
		coeff[6][0]=7.; coeff[6][1]=8.; coeff[6][2]=4.;
		coeff[7][0]=6.; coeff[7][1]=6.; coeff[7][2]=7.;
		coeff[8][0]=10.; coeff[8][1]=1.; coeff[8][2]=8.;
		coeff[9][0]=2.; coeff[9][1]=2.; coeff[9][2]=15.;
		coeff[10][0]=2.; coeff[10][1]=3.; coeff[10][2]=14.;
		coeff[11][0]=11.; coeff[11][1]=7.; coeff[11][2]=1.;

		for(int i=0;i<n;i++) {
			for(int j=0;j<k;j++)
				coeff[i][j]=(Number)(coeff[i][j].doubleValue()/(2*n-5.));
		}

		return coeff;
	}
	
	public static Number[][] getSchnyderCoefficient6(int n, int k) {
		Number[][] coeff=new Double[n][k];
		for(int i=0;i<n;i++) {
			for(int j=0;j<k;j++)
				coeff[i][j]=(double)(2.*n-5);
		}
		coeff[3][0]=4.; coeff[3][1]=2.; coeff[3][2]=1.;
		coeff[4][0]=1.; coeff[4][1]=4.; coeff[4][2]=2.;
		coeff[5][0]=2.; coeff[5][1]=1.; coeff[5][2]=4.;

		//coeff[3][0]=4.; coeff[3][1]=-2.; coeff[3][2]=-1.;
		//coeff[4][0]=-1.; coeff[4][1]=4.; coeff[4][2]=-2.;
		//coeff[5][0]=-2.; coeff[5][1]=-1.; coeff[5][2]=4.;
		
		System.out.println("computing coefficients");
		for(int i=k;i<n;i++) {
			System.out.print("vertex "+i+": ");
			for(int j=0;j<k;j++) {
				coeff[i][j]=(Number)(coeff[i][j].doubleValue()/(2.*n-5.));
				System.out.print(coeff[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("barycentric coefficients computed");

		return coeff;
	}

	public static Number[][] getSchnyderCoefficient7(int n, int k) {
		Number[][] coeff=new Double[n][k];
		for(int i=0;i<n;i++) {
			for(int j=0;j<k;j++)
				coeff[i][j]=(double)(2.*n-5);
		}
		// coeff[3][0]=2.; coeff[3][1]=6.; coeff[3][2]=1.;
		// coeff[4][0]=5.; coeff[4][1]=2.; coeff[4][2]=2.;
		// coeff[5][0]=4.; coeff[5][1]=1.; coeff[5][2]=4.;
		// coeff[6][0]=1.; coeff[6][1]=2.; coeff[6][2]=6.;

		coeff[3][0]=-2.; coeff[3][1]=4.; coeff[3][2]=-1.;
		coeff[4][0]=5.; coeff[4][1]=-2.; coeff[4][2]=-2.;
		coeff[5][0]=6.; coeff[5][1]=-1.; coeff[5][2]=-4.;
		coeff[6][0]=-1.; coeff[6][1]=-2.; coeff[6][2]=4.;
		
		System.out.println("computing coefficients");
		for(int i=k;i<n;i++) {
			System.out.print("vertex "+i+": ");
			for(int j=0;j<k;j++) {
				//coeff[i][j]=(Number)(coeff[i][j].doubleValue()/(2.*n-5.));
				System.out.print(coeff[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("barycentric coefficients computed");

		return coeff;
	}

	//---------------------------------------------------    

	public static void printCoefficients(Number[][] coeff) {
		for(int i=0;i<coeff.length;i++) {
			for(int j=0;j<coeff[0].length;j++) {
				double v;
				v=(Double)coeff[i][j];
				System.out.print(v+" ");
			}
			System.out.println();
		}
	}
	

	
}
