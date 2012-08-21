package angerona.fw.util;

import java.util.HashMap;

/** 
 * Pair adaption near at c## pair.
 * @author Tim Janus
 *
 * @param <TF>
 * @param <TS>
 */
public class Pair<TF, TS> {
	public TF first;
	
	public TS second;
	
	public Pair() {}
	
	public Pair(TF first, TS second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public String toString() {
		return first == null ? "null" : first.toString() + ";" 
				+ second == null ? "null" : second.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if(! (other instanceof Pair)) return false;
		@SuppressWarnings("unchecked")
		Pair<TF, TS> op = (Pair<TF, TS>)other;
		boolean eqF = first == null ? op.first == null : first.equals(op.first);
		boolean eqS = second == null ? op.second == null : second.equals(op.second);
	
		return eqF && eqS;
	}
	
	@Override 
	public int hashCode() {
		return (first != null ? first.hashCode() : 1) + (second != null ? second.hashCode() : 2);
	}
	
	public static void main(String [] args) {
		Pair<String, HashMap<String, String>> p1 = new Pair<>();
		p1.first = "AspReasoner";
		p1.second = new HashMap<>();
		p1.second.put("d", "0.0");
		
		Pair<String, HashMap<String, String>> p2 = new Pair<>();
		p2.first = "AspReasoner";
		p2.second = new HashMap<>();
		p2.second.put("d", "0.0");
		
		
		int h1 = p1.hashCode();
		int h2 = p2.hashCode();
		
		System.out.println(h1 + "=" + h2);
		System.out.println(p1.equals(p2));
	}
}
