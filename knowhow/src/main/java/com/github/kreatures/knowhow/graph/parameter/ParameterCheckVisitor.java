package com.github.kreatures.knowhow.graph.parameter;

import java.util.List;

import com.github.kreatures.knowhow.graph.GraphNode;

public interface ParameterCheckVisitor {
	boolean checkParameterMapping(GraphNode node, List<Parameter> parameters);
}
