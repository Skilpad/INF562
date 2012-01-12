package Jcg.io;

import tc.TC;

/**
 * @author amturing
 * This class provides basic methods for easy input/output operations on text file
 */
public class IO {

	// --- Methods for writing data to text file ---
	public static void writeNewTextFile(String filename) { TC.ecritureDansNouveauFichier(filename); }
	public static void appendFile(String filename) { TC.ecritureEnFinDeFichier(filename); }
	public static void writeStandardOutput() { TC.ecritureSortieStandard(); }

	// --- Methods for reading data from text file ---
	public static void readTextFile(String filename) { TC.lectureDansFichier(filename); }
	public static void readStandardInput() { TC.lectureEntreeStandard(); }
	public static boolean endInput() { return TC.finEntree(); }
	
	public static String readLine() { return TC.lireLigne(); }
	public static String[] wordsFromString(String s) { return TC.motsDeChaine(s); }
	
	// --- Methods for writing data into a file ---
	public static void print(int n) { TC.print(n); }
	public static void print(char c) { TC.print(c); }
	public static void print(String s) { TC.print(s); }
	public static void print(double d) { TC.print(d); }
	public static void print(char[] t) { TC.print(t); }
	public static void print(Object o) { TC.print(o); }

	public static void println(int n) { TC.println(n); }
	public static void println(char c) { TC.println(c); }
	public static void println(String s) { TC.println(s); }
	public static void println(double d) { TC.println(d); }
	public static void println(char[] t) { TC.println(t); }
	public static void println(Object o) { TC.println(o); }
}
