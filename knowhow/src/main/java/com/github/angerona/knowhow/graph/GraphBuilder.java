package com.github.angerona.knowhow.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.knowhow.KnowhowBase;
import com.github.angerona.knowhow.KnowhowStatement;
import com.github.angerona.knowhow.graph.complexity.ComplexityCalculator;
import com.github.angerona.knowhow.graph.complexity.MaxSelectorSumProcessorComplexity;
import com.github.angerona.knowhow.graph.parameter.DefaultParameterCheck;

/**
 * Builds the planning graph from a given know-how base.
 * @author Tim Janus
 */
public class GraphBuilder {

	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(GraphBuilder.class);
	
	public static void build(KnowhowBase knowhow, DirectedGraph<GraphNode, DefaultEdge> graph) {	
		Set<Processor> candidates = new HashSet<>();
		
		// generate processor nodes:
		for(String cap : knowhow.getAgent().getCapabilities()) {
			Processor atomicProcessor = new Processor(cap, graph);
			graph.addVertex(atomicProcessor);
			candidates.add(atomicProcessor);
		}
		
		for(KnowhowStatement ks : knowhow.getStatements()) {
			Processor processor = new Processor(ks, graph);
			graph.addVertex(processor);
			candidates.add(processor);
		}
		
		// generate selector nodes:
		for(KnowhowStatement ks : knowhow.getStatements()) {
			Processor target = new Processor(ks, graph);
			for(DLPAtom sub : ks.getSubTargets()) {
				String str = sub.toString();
				if(str.contains("(")) {
					String temp = str.substring(0, str.indexOf('('));
					if(knowhow.getAgent().getCapabilities().contains(temp)) {
						str = temp;
					}
				}
				
				Selector selector = new Selector(str, graph);
				graph.addVertex(selector);
				addEdge(graph, target, selector);
				
				for(Processor p : selector.getProcessors(candidates)) {
					addEdge(graph, selector, p);
				}
			}
		}
		
		// find graph nodes that have no incoming edges
		List<GraphNode> nodesWithoutParent = new ArrayList<>(graph.vertexSet());
		for(DefaultEdge edge : graph.edgeSet()) {
			nodesWithoutParent.remove(graph.getEdgeTarget(edge));
		}
		
		// calculate the complexity of those nodes that have no parent (will traverse the graph)
		boolean validMapping = true;
		ComplexityCalculator cc = new MaxSelectorSumProcessorComplexity();
		DefaultParameterCheck check = new DefaultParameterCheck();
		for(GraphNode node : nodesWithoutParent) {
			node.visitComplexityCalculation(cc);
			validMapping = validMapping && node.visitParameterCheck(check);
		}
		
		if(!validMapping) {
			LOG.error(check.getError());
		}
	}
	
	private static boolean addEdge(DirectedGraph<GraphNode, DefaultEdge> graph, GraphNode source, GraphNode target) {
		LOG.info("Generating Edge: '{}' --> '{}'",
				source.toString(), target.toString());
		DefaultEdge res = graph.addEdge(source, target);
		if(res != null) {
			return true;
		} else {
			LOG.warn("Edge: '{}' --> '{}' already exists.",
					source.toString(), target.toString());
			return false;
		}
	}

}
