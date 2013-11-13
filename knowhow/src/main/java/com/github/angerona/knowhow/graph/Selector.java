package com.github.angerona.knowhow.graph;

import java.util.List;

import net.sf.tweety.lp.asp.syntax.DLPAtom;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import com.github.angerona.fw.util.Utility;

/**
 * 
 * @author Tim Janus
 */
public class Selector extends GraphNodeAdapter  {

	/** serial version id */
	private static final long serialVersionUID = -6949598700368734085L;

	private DLPAtom subtarget;
	
	public Selector(Selector other) {
		super(other);
		this.subtarget = other.subtarget.clone();
	}
	
	public Selector(DLPAtom subtarget, Graph<GraphNode, DefaultEdge> graph) {
		super(subtarget.toString(), graph);
		this.subtarget = subtarget;
	}

	public DLPAtom getSubTarget() {
		return subtarget;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Processor> getChildren() {
		return (List<Processor>)super.getChildren();
	}
	
	@Override
	public NodeType getType() {
		return NodeType.NT_SELECTOR;
	}
	
	
	@Override
	public boolean equals(Object other) {
		if(this == other)	return true;
		if(other == null || getClass() != other.getClass())	return false;
		
		Selector pn = (Selector)other;
		return Utility.equals(name, pn.name) && Utility.equals(parameters, pn.parameters);
	}
	
	@Override
	public int hashCode() {
		return	super.hashCode() * 13;
	}
	
	@Override
	public Selector clone() {
		return new Selector(this);
	}
}
