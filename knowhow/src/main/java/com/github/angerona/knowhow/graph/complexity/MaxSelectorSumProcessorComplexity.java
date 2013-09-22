package com.github.angerona.knowhow.graph.complexity;

import java.util.List;

import com.github.angerona.knowhow.graph.GraphNode;
import com.github.angerona.knowhow.graph.Processor;
import com.github.angerona.knowhow.graph.Selector;

/**
 * This class calculates the complexity of the nodes of a given graph by using
 * the maximum of the Selector nodes complexities and the sum of the Processor
 * Node complexities plus one.
 * 
 * @author Tim Janus
 */
public class MaxSelectorSumProcessorComplexity implements ComplexityCalculator {
	@Override
	public int getComplexity(GraphNode node) {
		if(node instanceof Processor) {
			Processor pro = (Processor)node;
			List<Selector> children = pro.getChildren();
			if(children.size() == 0)
				return 1;
			
			int reval = 0;
			for(Selector child : children) {
				reval += child.visitComplexityCalculation(this);
			}
			return reval + 1;
		} else if(node instanceof Selector) {
			List<Processor> children = ((Selector)node).getChildren();
			int reval = -Integer.MAX_VALUE;
			for(Processor child : children) {
				int childComplexity = child.visitComplexityCalculation(this);
				if(childComplexity > reval) {
					reval = childComplexity;
				}
			}
			return reval + 1;
		} else {
			throw new IllegalStateException();
		}
	}

}
