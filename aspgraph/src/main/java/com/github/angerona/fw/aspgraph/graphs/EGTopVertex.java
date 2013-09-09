package com.github.angerona.fw.aspgraph.graphs;

/**
 * Represents top vertex of Explanation Graph
 * @author ella
 *
 */
public class EGTopVertex extends EGVertex{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7344243387453486485L;
	
	/**
	 * Literal which should be represented as fact
	 */
	private String sourceLiteral;
	
	/**
	 * Creates new top vertex in Explanation Graph
	 * @param literal Literal which should be represented as a fact
	 */
	public EGTopVertex(String literal){
		sourceLiteral = literal;
		index = -1;
		lowlink = -1;
	}
	
	@Override
	public String toString() {
		return "<html>T</html>";
	}

	@Override
	public String pureString() {
		return "T";
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof EGTopVertex){
			EGTopVertex v = (EGTopVertex) o;
			if (v.sourceLiteral.equals(sourceLiteral)) return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return 3;
	}
}
