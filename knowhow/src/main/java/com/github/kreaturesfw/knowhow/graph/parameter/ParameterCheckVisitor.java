package com.github.kreaturesfw.knowhow.graph.parameter;

import java.util.List;

import com.github.kreaturesfw.knowhow.graph.GraphNode;

public interface ParameterCheckVisitor {
	boolean checkParameterMapping(GraphNode node, List<Parameter> parameters);
}
