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
}
