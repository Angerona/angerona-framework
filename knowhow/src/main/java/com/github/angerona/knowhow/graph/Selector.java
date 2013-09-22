package com.github.angerona.knowhow.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.github.angerona.fw.util.Utility;

/**
 * 
 * @author Tim Janus
 */
public class Selector extends GraphNodeAdapter  {

	/** serial version id */
	private static final long serialVersionUID = -6949598700368734085L;

	public Selector(String name, DirectedGraph<GraphNode, DefaultEdge> graph) {
		super(name, graph);
	}
	
	public Collection<Processor> getProcessors(Collection<Processor> candidates) {
		Set<Processor> reval = new HashSet<>();
		
		String toCompare = name.startsWith("s_") ? name.substring(2) : name;
		for(Processor cur : candidates) {
			if(cur.getName().equals(toCompare)) {
				reval.add(cur);
			}
		}
		return reval;
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
		return Utility.equals(name, pn.name);
	}
	
	@Override
	public int hashCode() {
		return	super.hashCode() * 13;
	}
}
