package com.github.kreaturesfw.knowhow.situation;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import com.github.kreaturesfw.knowhow.graph.GraphNode;
import com.github.kreaturesfw.knowhow.graph.KnowhowBaseGraphBuilder;

/**
 * @todo use common base class / interface with {@link KnowhowBaseGraphBuilder}
 * 
 * @author Tim Janus
 */
public interface SituationGraphBuilder {
	
	Graph<GraphNode, DefaultEdge> getGraph();
	
	void build();
}
