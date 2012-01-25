package Jcg.geometry;

public class Kernel0 extends Kernel {

	/** Creates the null element, available for all kernels **/
	public Kernel0() {}
	
	public int compareTo(Kernel arg0) { throw new Error("Kernel0.compareTo() shall not be called"); }

	public Number getCartesian(int i) { return 0; }

	public void setCartesian(int i, Number x) {}

	public int dimension() { throw new Error("Kernel0.dimension() shall not be called"); }

	public String toString() { return "000"; }

	public boolean equals(Kernel d) { throw new Error("Kernel0.equals() shall not be called"); }

	public void setOrigin() { }

	public void copy(Kernel d) { }

	public Kernel copy() { return new Kernel0(); }

	public void add(Kernel d)        { throw new Error("Kernel0.add() shall not be called"); }
	public void take(Kernel d)       { throw new Error("Kernel0.take() shall not be called"); }
	public void multiplyBy(Number s) { throw new Error("Kernel0.multiplyBy() shall not be called"); }
	public void divideBy(Number s)   { throw new Error("Kernel0.divideBy() shall not be called"); }

	public Kernel plus(Kernel d)         { throw new Error("Kernel0.plus() shall not be called"); }       
	public Kernel minus(Kernel d)        { throw new Error("Kernel0.mminus() shall not be called"); }      
	public Kernel multipliedBy(Number s) { throw new Error("Kernel0.multipliedBy() shall not be called"); }
	public Kernel dividedBy(Number s)    { throw new Error("Kernel0.dividedBy() shall not be called"); }  

	public Number innerProduct(Kernel d) { return 0; }

	public Number norm2() { return 0; }
	public Number norm()  { return 0; }

}
