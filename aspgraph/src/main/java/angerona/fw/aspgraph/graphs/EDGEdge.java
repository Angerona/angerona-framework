package angerona.fw.aspgraph.graphs;

import java.io.Serializable;


/**
 * Represents an edge in an Extended Dependency Graph
 * @author ella
 *
 */
public class EDGEdge implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6608667047849968395L;

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
	private EDGVertex source;
	
	/**
	 * Target vertex
	 */
	private EDGVertex target;
	
	/**
	 * Label of edge
	 */
	private EdgeType label;
	
	private boolean and;
	private boolean or;
	
	public EDGEdge(EDGVertex source, EDGVertex target, EdgeType label, boolean and, boolean or){
		this.source = source;
		this.target = target;
		this.label = label;
		this.and = and;
		this.or = or;
	}
	
	public EDGVertex getSource(){
		return source;
	}
	
	public EDGVertex getTarget(){
		return target;
	}
	
	public EdgeType getLabel(){
		return label;
	}
	
	public boolean equals(Object o){
		if (o instanceof EDGEdge){
			EDGEdge e2 = (EDGEdge) o;
			if (target.equals(e2.getTarget()) && 
					source.equals(e2.getSource()) && 
					label.equals(e2.getLabel())) return true;
		}	
		return false;
	}
	
	public int hashCode(){
		return source.hashCode() + target.hashCode() + label.hashCode();
	}

	public String toString(){
		switch(label){
			case POS: return "+";
			case NEG: return "-";
			default: return "";
		}
	}
	
	public boolean isActive(){
		// Edge is an AND-edge
		if (and){
			if (source.getColor().equals(EDGVertex.Color.GREEN) && label.equals(EdgeType.NEG)) return true;
			if (!or && source.getColor().equals(EDGVertex.Color.RED) && label.equals(EdgeType.POS)) return false;
		}
		
		// Edge is an OR-edge
		if (or){
			if (source.getColor().equals(EDGVertex.Color.GREEN)) return true;
		}
		
		return false;		
	}
}
