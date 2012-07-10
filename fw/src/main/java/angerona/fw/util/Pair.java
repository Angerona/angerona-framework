package angerona.fw.util;

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
	public boolean equals(Object other) {
		if(! (other instanceof Pair)) return false;
		Pair<TF, TS> op = (Pair<TF, TS>)other;
		boolean eqF = (first == null && op.first == null) ||
						first != null && first.equals(op.first);
		boolean eqS = (second == null && op.second == null) ||
						(second != null && second.equals(op.second));
	
		return eqF && eqS;
	}
	
	@Override 
	public int hashCode() {
		return (first != null ? first.hashCode() : 1) + (second != null ? second.hashCode() : 2);
	}
}
