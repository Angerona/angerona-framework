package com.github.kreaturesfw.knowhow.graph.complexity;

import com.github.kreaturesfw.knowhow.graph.GraphNode;

/**
 * An interface of a class that calculates the Complexity
 * of {@link GraphNode} instances. 
 * 
 * @author Tim Janus
 */
public interface ComplexityCalculator {
	int getComplexity(GraphNode node);
}
