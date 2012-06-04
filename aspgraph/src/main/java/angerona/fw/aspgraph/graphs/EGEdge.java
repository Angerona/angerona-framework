package angerona.fw.aspgraph.graphs;

/**
 * Represents an edge in an Explanation Graph
 * @author ella
 *
 */
public class EGEdge{
	
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
	
	public boolean equals(EGEdge e2){
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
}
