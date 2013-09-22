package com.github.angerona.knowhow.graph;

import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.github.angerona.fw.util.Utility;
import com.github.angerona.knowhow.KnowhowStatement;
import com.github.angerona.knowhow.graph.parameter.Parameter;

/**
 * 
 * 
 * @author Tim Janus
 */
public class Processor extends GraphNodeAdapter {	
	/** serial version id */
	private static final long serialVersionUID = -5269593517151014379L;
	
	private KnowhowStatement statement;
	
	public Processor(String name, DirectedGraph<GraphNode, DefaultEdge> graph) {
		super(name, graph);
	}
	
	public Processor(KnowhowStatement stmt, DirectedGraph<GraphNode, DefaultEdge> graph) {
		super(stmt.getTarget().toString(), graph);
		this.statement = stmt;
	//	parseSubgoals();
	}
	
	public void parseSubgoals() {
		for(DLPAtom subtarget : statement.getSubTargets()) {
			for(Term<?> t : subtarget.getArguments()) {
				parameters.add(new Parameter(t.toString()));
			}
		}
	}
	
	public boolean isAtomic() {
		return statement == null;
	}
	
	public KnowhowStatement getStatement() {
		return statement;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Selector> getChildren() {
		return (List<Selector>)super.getChildren();
	}
	
	@Override
	public NodeType getType() {
		return NodeType.NT_PROCESSOR;
	}
	
	@Override
	public int hashCode() {
		return (super.hashCode() +
				(statement == null ? 0 : statement.hashCode())) * 7;
	}
	
	@Override
	public String toString() {
		if(statement == null)
			return super.toString();
		else 
			return "P(" + complexity + ") #"+statement.getId() + name + parameters + 
					" - (" + statement.getConditions().toString() + ")";
	}
	
	public boolean equals(Object other) {
		if(this == other)	return true;
		if(other == null || getClass() != other.getClass())	return false;
		
		Processor processor = (Processor)other;
		if(!Utility.equals(this.statement, processor.statement))	return false;
		if(this.statement == null) {
			return 	Utility.equals(this.name, processor.name) &&
					Utility.equals(this.parameters, processor.parameters);
		}
		return true;
	}
}
