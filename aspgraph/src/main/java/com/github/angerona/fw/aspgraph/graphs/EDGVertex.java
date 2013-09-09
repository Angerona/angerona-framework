package com.github.angerona.fw.aspgraph.graphs;

import java.io.Serializable;

/**
 * Represents a vertex in an Extended Dependency Graph
 * @author ella
 *
 */
public class EDGVertex implements Serializable{

	private static final long serialVersionUID = -7777396947071675387L;

	/**
	 * Defines possible colors for vertex:
	 * <ul><li>RED</li><li>GREEN</li></ul>
	 * @author ella
	 *
	 */
	public enum Color{GREEN, RED};
	
	/**
	 * Literal which is represented by vertex
	 */
	private String literal;
	
	/**
	 * Rule number 
	 */
	private int ruleNo;
	
	/**
	 * Color of vertex
	 */
	private Color color;
	
	/**
	 * Index used for tarjan algorithm
	 */
	private int index;
	
	/**
	 * Lowlink used for tarjan algorithm
	 */
	private int lowlink;
	
	/**
	 * Creates new EDGVertex
	 * @param literal Literal which is represented
	 * @param ruleNo Number of rule which is represented
	 */
	public EDGVertex(String literal, int ruleNo){
		this.literal = literal;
		this.ruleNo = ruleNo;
		index = -1;
		lowlink = -1;
	}
	
	/**
	 * Returns index calculated by DFS
	 * @return Index from DFS
	 */
	public int getIndex(){
		return index;
	}
	
	/**
	 * Sets index
	 * @param Index
	 */
	public void setIndex(int index){
		this.index = index;
	}
	
	/**
	 * Return lowlink calculated during Tarjan's algorithm
	 * @return Lowling
	 */
	public int getLowlink(){
		return lowlink;
	}
	
	/**
	 * Sets lowlink
	 * @param Lowlink
	 */
	public void setLowlink(int lowlink){
		this.lowlink = lowlink;
	}
	
	/**
	 * Returns literal this node is representing
	 * @return Literal as a string
	 */
	public String getLiteral(){
		return literal;
	}
	
	/**
	 * Sets literal this node is representing
	 * @param Literal as a string
	 */
	public void setLiteral(String literal){
		this.literal = literal;
	}
	
	/**
	 * Returns the number of the rule, in which the literal of this node is defined.
	 * @return Rule number
	 */
	public int getRuleNo(){
		return ruleNo;
	}
	
	/**
	 * Sets the number of the rule, in which the literal of this node is defined.
	 * @param ruleNo
	 */
	public void setRuleNo(int ruleNo){
		this.ruleNo = ruleNo;
	}
	
	/**
	 * Return the color of the node
	 * @return Color
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * Sets the color of the node
	 * @param Color
	 */
	public void setColor(Color color){
		this.color = color;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof EDGVertex){
			EDGVertex v2 = (EDGVertex) o;
			if (this.literal.equals(v2.getLiteral()) && this.ruleNo == v2.getRuleNo()) return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return 2;
	}
	
	@Override
	public String toString(){
		return "<html>" + literal + "<sup>" + ruleNo + "</sup></html>";
	}
	
	/**
	 * Returns string representation of the node without HTML-formatting
	 * @return
	 */
	public String pureString(){
		return literal + ruleNo;
	}

	/**
	 * Checks, if the node has an active edge
	 * @param edg ExtendedDependencyGraph, that contains the node
	 * @return true, if node has an active edge, otherwise false
	 */
	public boolean hasActiveEdge(ExtendedDependencyGraph edg){
		if (edg == null) return false;
		else{
			for (EDGEdge e : edg.getInEdges(this)){
				if (e.isActive()) return true;
			}
		}
		return false;
	}
	
}
