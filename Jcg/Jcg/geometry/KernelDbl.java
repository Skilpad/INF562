package Jcg.geometry;

public abstract class KernelDbl extends Kernel {

	/** To use as constructor for classes extending DataDomain:
	 *  if X extends Kernel, X.constructor() returns new X(),
	 *  assumed to be the element 0 of X  **/
	public static KernelDbl constructor() { return null; }
	
	/** Return the i-th coordinate **/
	public abstract Double getCartesian(int i);  
	
	/** Return an independent copy. **/
	public abstract KernelDbl copy();
	
	/** Return this + d **/
	public abstract KernelDbl plus(Kernel d);
	/** Return this -= d **/
	public abstract KernelDbl minus(Kernel d);
	/** Return this * s **/
	public abstract KernelDbl multipliedBy(Number s);
	/** Return this / s **/
	public abstract KernelDbl dividedBy(Number s);
	/** Return inner product of this and d **/
	public abstract Double innerProduct(Kernel d);
	/** Return squared norm **/
	public abstract Double norm2();
	/** Return norm **/
	public abstract Double norm();
	
}
