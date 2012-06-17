package angerona.fw.aspgraph.graphs;

import java.io.Serializable;

/**
 * Represents an edge in an Explanation Graph
 * @author ella
 *
 */
public class EGEdge implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 288152013006799807L;

	/**
	 * Defines two edge types:
	 * <ul>
	 * <li>POS: positive edge</li>
	 * <li>NEG: negative Edge</li>
	 * </ul>
	 * @author ella
	 *
	 */
	public enum EdgeType{POS, NEG}
	
	/**
	 * Source vertex
	 */
	private EGVertex source;
	
	/**
	 * Target vertex
	 */
	private EGVertex target;
	
	/**
	 * Label of edge
	 */
	private EdgeType label;
	
	public EGEdge(EGVertex source, EGVertex target, EdgeType label){
		this.source = source;
		this.target = target;
		this.label = label;
	}
	
	public EGVertex getSource(){
		return source;
	}
	
	public EGVertex getTarget(){
		return target;
	}
	
	public EdgeType getLabel(){
		return label;
	}
	
	public boolean equals(Object o){
		EGEdge e2;
		if (o instanceof EGEdge) e2 = (EGEdge)  o;
		else return false;
		if (target.equals(e2.getTarget()) && 
		    source.equals(e2.getSource()) && 
		    label.equals(e2.getLabel())) return true;
		else return false;
	}

	public String toString(){
		switch(label){
			case POS: return "+";
			case NEG: return "-";
			default: return "";
		}
	}
	
	@Override
	public int hashCode(){
		int h = target.hashCode() + source.hashCode() + toString().hashCode();
		return h;
	}
	
}
