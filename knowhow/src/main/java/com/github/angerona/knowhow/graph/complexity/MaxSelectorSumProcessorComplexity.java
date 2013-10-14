package com.github.angerona.knowhow.graph.complexity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(MaxSelectorSumProcessorComplexity.class);
	
	@Override
	public int getComplexity(GraphNode node) {
		int reval = 0;
		LOG.debug("Entering getComplexity({})", node.toString());
		if(node instanceof Processor) {
			Processor pro = (Processor)node;
			List<Selector> children = pro.getChildren();
			if(children.size() == 0) {
				reval = 1;
			} else {
				for(Selector child : children) {
					reval += child.visitComplexityCalculation(this);
				}
				reval += 1;
			}
		} else if(node instanceof Selector) {
			List<Processor> children = ((Selector)node).getChildren();
			reval = -Integer.MAX_VALUE;
			for(Processor child : children) {
				int childComplexity = child.visitComplexityCalculation(this);
				if(childComplexity > reval) {
					reval = childComplexity;
				}
			}
//			reval += 1;
		} else {
			throw new IllegalStateException();
		}
		LOG.debug("Leaving getComplexity() = {}", reval);
		return reval;
	}

}
