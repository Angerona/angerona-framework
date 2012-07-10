package angerona.fw.aspgraph.util;

import java.util.Collection;
import java.util.HashSet;

import angerona.fw.aspgraph.graphs.EDGVertex;

/**
 * Stores sets of vertices that contradict each other
 * @author ella
 *
 */
public class Contradiction {
	/**
	 * Nodes which represent positive form of a literal
	 */
	private HashSet<EDGVertex> positive;
	
	/**
	 * Nodes which represent negated form of a literal
	 */
	private HashSet<EDGVertex> negative;
	
	/**
	 * Creates a new contradiction
	 * @param p Set of nodes representing positive form of a liter
	 * @param n Set of nodes representing nrgated form of a literal
	 */
	public Contradiction(Collection<EDGVertex> p, Collection<EDGVertex> n){
		positive = new HashSet<EDGVertex>(p);
		negative = new HashSet<EDGVertex>(n);
	}
	
	/**
	 * Returns set of positive nodes
	 * @return Set of positive nodes
	 */
	public HashSet<EDGVertex> getPositive(){
		return positive;
	}
	
	/**
	 * Return set of negative nodes
	 * @return Sewt of negative nodes
	 */
	public HashSet<EDGVertex> getNegative(){
		return negative;
	}
}