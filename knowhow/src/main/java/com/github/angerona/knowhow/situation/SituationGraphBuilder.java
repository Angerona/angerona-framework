package com.github.angerona.knowhow.situation;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import com.github.angerona.knowhow.graph.GraphNode;

public interface SituationGraphBuilder {
	Graph<GraphNode, DefaultEdge> build();
}
