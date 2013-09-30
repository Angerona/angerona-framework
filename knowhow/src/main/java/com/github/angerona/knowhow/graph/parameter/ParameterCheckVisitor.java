package com.github.angerona.knowhow.graph.parameter;

import java.util.List;

import com.github.angerona.knowhow.graph.GraphNode;

public interface ParameterCheckVisitor {
	boolean checkParameterMapping(GraphNode node, List<Parameter> parameters);
}
