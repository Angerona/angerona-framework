package angerona.fw.aspgraph.util;

import java.util.Collection;
import java.util.HashSet;

import angerona.fw.aspgraph.graphs.EDGVertex;

public class Contradiction {
	private HashSet<EDGVertex> positive;
	private HashSet<EDGVertex> negative;
	
	public Contradiction(Collection<EDGVertex> p, Collection<EDGVertex> n){
		positive = new HashSet<EDGVertex>(p);
		negative = new HashSet<EDGVertex>(n);
	}
	
	public HashSet<EDGVertex> getPositive(){
		return positive;
	}
	
	public HashSet<EDGVertex> getNegative(){
		return negative;
	}
}
