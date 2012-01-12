package Jcg.polyhedron;

import java.util.*;

public class Decorator<K,V> {
	
	HashMap<K,V> table;
	
	public Decorator() { 
		table=new HashMap<K,V>();
	}
	
	public Decorator(int n) {
		table=new HashMap<K,V>(n);
	}
	
	public boolean isDecorated(K key) { 
		return table.containsKey(key);
	}
	
	public void setDecoration(K key, V value) {
		table.put(key,value);
	}

	public void removeDecoration(K key) {
		table.remove(key);
	}
	
	public void clear() {
		table.clear();
	}
}
