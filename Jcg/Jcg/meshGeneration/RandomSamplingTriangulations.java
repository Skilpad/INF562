package Jcg.meshGeneration;

import java.util.*;

import Jcg.geometry.*;
import Jcg.polyhedron.*;
import Jcg.graph.*;
//import Jcg.graphDrawing.*;

public class RandomSamplingTriangulations {

	boolean debug = false;
	int n;
	int[] word;
	public Polyhedron_3<Point_2> poly; // Stores the generated triangulation.
	Halfedge<Point_2> rootEdge; // An edge to the root of the tree.
	List<Vertex<Point_2>> outerFace; // The vertices on outer face.
	
	public RandomSamplingTriangulations(boolean debug) {
	    this.debug = debug;
    }
	
	static boolean isValid(int[] word) {
		// Every proper prefix must satisfy 3|u|_1 - |u|_0 > -2
		int count = 0;
	    for (int i = 0; i < word.length - 1; i++) { // Up to word.length - 1 because it's a proper prefix.
	        if (word[i] == 1) {
	        	count += 3;
	        }
	        else {
	        	count -= 1;
	        }
	        if (count <= -2) {
	            return false;
	        }
	    }
	    return true;
	}
	
	int[] generateWord(int n) {
		this.n = n;
		int[] word = new int[4 * n - 2];
		Random random = new Random();
		for (int k = 0; k < n - 1; k++) {
			int i;
			do {
				i = random.nextInt(4 * n - 2);
			} while (word[i] == 1);
			word[i] = 1;
		}
		this.word = word;
		return word;
	}
	
	int[] validateWord() {
		// Find valid rotation.
		int count = 0;
	    int countMin = 0;
	    int j = 0;
	    for (int i = 0; i < 4 * n - 2; i++) {
	        if (word[i] == 1) {
	        	count += 3;
	        }
	        else {
	        	count -= 1;
	        }
	        if (count < countMin) {
	            countMin = count;
	            j = i + 1;
	        }
	    }
	    if (word[j  % (4 * n - 2)] == 1) {
	    	// Always start with 0, 1, ...
	    	j = (j - 1) % (4 * n - 2);
	    }
	    
	    int[] validWord = new int[4 * n - 2];
	    for (int i = 0; i < 4 * n - 2; i++) {
	    	validWord[i] = word[(j + i) % (4 * n - 2)];
	    }
	    assert isValid(word);
	    this.word = validWord;
	    return validWord;
	}
		
	
	Vertex<Point_2> newVertex() {
		Vertex<Point_2> v = new Vertex<Point_2>(new Point_2());
		poly.vertices.add(v);
		return v;
	}
	
	Halfedge<Point_2> newHalfedge() {
		Halfedge<Point_2> h = new Halfedge<Point_2>();
		poly.halfedges.add(h);
		return h;
	}
	
	Face<Point_2> newFace() {
		Face<Point_2> f = new Face<Point_2>();
		poly.facets.add(f);
		return f;
	}
	
	int decodeSubtree(Halfedge<Point_2> nodeEdge, int i) {
		int leaves = 0;
	    while (i < word.length) {
	        if (word[i] == 1) {
	        	Halfedge<Point_2> childEdge = addLeaf(nodeEdge, newVertex());
	            i = decodeSubtree(childEdge, i + 1);
	            assert word[i] == 0;
	            nodeEdge = childEdge.opposite;
	        }
	        else {
	            if (leaves == 2) {
	            	break;
	            }
	            leaves += 1;
	            Halfedge<Point_2> childEdge = addLeaf(nodeEdge, null);
	            nodeEdge = childEdge.opposite;
	        }
	        i += 1;
	    }
        return i;
	}
	
	Halfedge<Point_2> addLeaf(Halfedge<Point_2> nodeEdge,
									 Vertex<Point_2> leaf) {
		Halfedge<Point_2> nextEdge = nodeEdge.next;
		Halfedge<Point_2> g = newHalfedge();
		Halfedge<Point_2> h = newHalfedge();
		g.vertex = leaf;
		h.vertex = nodeEdge.vertex;
		g.opposite = h;
		h.opposite = g;
		g.next = h;
		h.prev = g;
		g.prev = nodeEdge;
		h.next = nextEdge;
		nodeEdge.next = g; 
		nextEdge.prev = h;
		if (leaf != null) {
			leaf.setEdge(g);
		}
		return g;
	}
	
	Halfedge<Point_2> addRoot() {
		Vertex<Point_2> root = newVertex();
		Halfedge<Point_2> g = newHalfedge();
		Halfedge<Point_2> h = newHalfedge();
		root.setEdge(g);
		g.vertex = root;
		g.opposite = h;
		h.opposite = g;
		g.next = h;
		h.prev = g;
		g.prev = h;
		h.next = g;
		return g;
	}
	
	void decodeTree() {
		poly = new Polyhedron_3<Point_2>();
		rootEdge = addRoot();
		int i = decodeSubtree(rootEdge, 1);
        assert i == word.length;
	}
	
	static boolean isInternal(Halfedge<Point_2> h) {
		return h.vertex != null && h.opposite.vertex != null;
	}
	
	static boolean isExternal(Halfedge<Point_2> h) {
		return !isInternal(h);
	}
	
	void close(Halfedge<Point_2> g) {
		Halfedge<Point_2> a = g.prev.prev.prev;
		Halfedge<Point_2> b = g.prev.prev;
		Halfedge<Point_2> c = g.prev;
		Halfedge<Point_2> h = g.next;
		g.vertex = a.vertex;
		g.next = b;
		b.prev = g;
		h.prev = a;
		a.next = h;
		Face<Point_2> f = newFace();
		f.setEdge(g);
		g.face = f;
		b.face = f;
		c.face = f;
	}
	
	void computeClosure() {
		int n = word.length;
		
		// Partial closure.
		Halfedge<Point_2> h = rootEdge;
		for (int i = 0; i < 12 * n; i++) {
			if (isExternal(h) && isInternal(h.prev) && isInternal(h.prev.prev)) {
				close(h);
				h = h.opposite;
			}
			h = h.next;
		}
		
		// Complete closure.
		while (!(h.vertex == null && h.prev.prev.vertex == null)) {
			// Find special vertex.
			h = h.next;
		}
		outerFace = new ArrayList<Vertex<Point_2>>(3);
		outerFace.add(newVertex());
		outerFace.add(newVertex());
		for (int i = 0; i < 2; i++) {
			h.vertex = outerFace.get(i);
			outerFace.get(i).setEdge(h);
			h = h.next.next.next;
			while (h.vertex == null) {
				close(h);
				h = h.opposite.next.next;
			}
			h = h.prev;
		}
		outerFace.add(h.next.vertex);
		h = addLeaf(h, null);
		close(h);
		;
	}
	
	public void generateTriangulation(int n) {
		generateWord(n);
		if (debug) System.out.println(Arrays.toString(word));
		validateWord();
		if (debug) System.out.println(Arrays.toString(word));
		decodeTree();
		computeClosure();
		if (debug) System.out.println(poly.isValid(false));
	}
	
	public Graph getGraph() {
		// Put outer vertices at the beginning
		for (int i = 0; i < 3; i++) {
			int j = poly.vertices.indexOf(outerFace.get(i));
			poly.vertices.set(j, poly.vertices.get(i));
			poly.vertices.set(i, outerFace.get(i));
		}
		
		// Assign indices
		Map<Vertex<Point_2>, Integer> indices =
			new HashMap<Vertex<Point_2>, Integer>();
		for (int i = 0; i < poly.vertices.size(); i++) {
			indices.put(poly.vertices.get(i), i);
		}

		// Construct graph
		Graph graph = new AdjacencyGraph(n + 2);
		for (Halfedge<Point_2> h : poly.halfedges) {
			Vertex<Point_2> v = h.vertex;
			Vertex<Point_2> u = h.opposite.vertex;
			graph.addEdge(indices.get(u), indices.get(v));
		}
		return graph;
	}
	
	public void drawGraph() {
		Graph graph = getGraph();
		Point_2[] triangle = new Point_2[3];
		triangle[0] = new Point_2(-0.5, 0);
		triangle[1] = new Point_2(0.5, 0);
		triangle[2] = new Point_2(0, 1);
		

// TODO: Caution! Code in next comment was deleted because there is no Jcg.graphDrawing package
// TODO: It has never been replaced
/*		IterativeTutteDrawing<Point_2> drawing =
			new IterativeTutteDrawing<Point_2>(graph, triangle);
		drawing.computeDrawing();
		drawing.draw2D();
 */
		
//		graph = getGraph();
//		SpectralDrawing_2<Point_2> spectral =
//			new SpectralDrawing_2<Point_2>(graph);
//		spectral.computeDrawing();
//		spectral.draw2D();
//		
//		graph = getGraph();
//		SpringDrawing_2<Point_2> spring =
//			new SpringDrawing_2<Point_2>(graph);
//		spring.computeDrawing();
//		spring.draw2D();
	}

    public static RandomSamplingTriangulations randomTriangulation(int n, boolean debug) {
        RandomSamplingTriangulations s = new RandomSamplingTriangulations(debug);
		s.generateTriangulation(n);
		return s;
	}
	
	public static void drawRandomTriangulation(int n) {
		RandomSamplingTriangulations s = randomTriangulation(n, false);
		System.out.println("graph generated: "+n+" vertices");
		System.out.print("Computing drawing...");
		s.drawGraph();
		System.out.println("done");
	}
	
	public static void benchmark() {
		System.out.println("size\ttime (microseconds)");
		for (int n = 100000; n <= 500000; n += 100000) {
			int number = 1;
			long start = System.nanoTime();
			for (int i = 0; i < number; i++) {
				randomTriangulation(n, false);
			}
			long end = System.nanoTime();
			System.out.println(n + "\t" + ((end - start) / (number * 1000)));
		}
	}
	
	public static void main(String[] args) {
	    if (args[0].equals("-b")) {
	        benchmark();
	    }
	    else if (args[0].equals("-t")) {
	    	// It works fine for triangulations having up to 1M vertices
	        int n = Integer.valueOf(args[1]).intValue();
	        System.out.print("Generating random planar triangulation of size "+n+"...");
	        randomTriangulation(n, false);
	        System.out.println("done");
        }
        else {
            int n = Integer.valueOf(args[0]).intValue();
	        drawRandomTriangulation(n);
	   }
	}
}
