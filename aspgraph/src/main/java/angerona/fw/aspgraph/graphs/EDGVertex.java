package angerona.fw.aspgraph.graphs;

import java.io.Serializable;

public class EDGVertex implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7777396947071675387L;

	public enum Color{GREEN, RED};
	
	private String literal;
	private int ruleNo;
	private Color color;
	private int index;
	private int lowlink;
	
	public EDGVertex(String literal, int ruleNo){
		this.literal = literal;
		this.ruleNo = ruleNo;
		index = -1;
		lowlink = -1;
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public int getLowlink(){
		return lowlink;
	}
	
	public void setLowlink(int lowlink){
		this.lowlink = lowlink;
	}
	
	public String getLiteral(){
		return literal;
	}
	
	public void setLiteral(String literal){
		this.literal = literal;
	}
	
	public int getRuleNo(){
		return ruleNo;
	}
	
	public void setRuleNo(int ruleNo){
		this.ruleNo = ruleNo;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public boolean equals(EDGVertex v2){
		if (this.literal.equals(v2.getLiteral()) && this.ruleNo == v2.getRuleNo()) return true;
		else return false;
	}
	
	public String toString(){
		return "<html>" + literal + "<sup>" + ruleNo + "</sup></html>";
	}
	
	public String pureString(){
		return literal + ruleNo;
	}
	
}
