package com.github.angerona.fw.aspgraph.util;

import com.github.angerona.fw.aspgraph.graphs.EGEdge;
import com.github.angerona.fw.aspgraph.graphs.EGLiteralVertex;
import com.github.angerona.fw.aspgraph.graphs.EGVertex;
import com.github.angerona.fw.aspgraph.graphs.ExplanationGraph;

public class SubEG {
	private ExplanationGraph eg;
	private EGEdge.EdgeType connectingEdgeType;
	private String rootLiteral;
	private String cycleTarget;
	
	public SubEG(ExplanationGraph eg, EGEdge.EdgeType type, String rootLiteral, String cycleTarget){
		this.eg = eg;
		this.connectingEdgeType = type;
		this.rootLiteral = rootLiteral;
		this.cycleTarget = cycleTarget;
	}
	
	public SubEG(ExplanationGraph eg, String cycleTarget){
		this.eg = eg;
		this.cycleTarget = cycleTarget;
	}
	
	public ExplanationGraph getEG(){
		return eg;
	}
	
	public EGEdge.EdgeType getConnectingEdgeType(){
		return connectingEdgeType;
	}
	
	public String getRootLiteral(){
		return rootLiteral;
	}
	
	public boolean isIncomplete(String literal){
		if (cycleTarget == null || literal.equals(cycleTarget)) return false;
		return true;
	}
	
	public String getCycleTarget(){
		return cycleTarget;
	}
}
