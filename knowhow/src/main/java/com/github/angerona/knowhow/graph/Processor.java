package com.github.angerona.knowhow.graph;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import com.github.angerona.fw.util.Utility;
import com.github.angerona.knowhow.KnowhowStatement;

/**
 * 
 * 
 * @author Tim Janus
 */
public class Processor extends GraphNodeAdapter {	
	/** serial version id */
	private static final long serialVersionUID = -5269593517151014379L;
	
	private KnowhowStatement statement;
	
	public Processor(Processor other) {
		super(other);
		this.statement = other.statement == null ? null : other.statement.clone();
	}
	
	public Processor(String name, Graph<GraphNode, DefaultEdge> graph) {
		super(name, graph);
	}
	
	public Processor(KnowhowStatement stmt, Graph<GraphNode, DefaultEdge> graph) {
		super(stmt.getTarget().toString(), graph);
		this.statement = stmt;
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
			return "P(" + complexity + ") " + name + parameters + 
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

	@Override
	public Processor clone() {
		return new Processor(this);
	}
}
