package com.github.angerona.knowhow.graph;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.knowhow.KnowhowBase;
import com.github.angerona.knowhow.KnowhowStatement;

/**
 * 
 * @author Tim Janus
 */
public class GraphBuilder {

	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(GraphBuilder.class);
	
	public static void build(KnowhowBase knowhow, DirectedGraph<GraphNode, DefaultEdge> graph) {	
		Set<Processor> candidates = new HashSet<>();
		
		// generate processor nodes:
		for(String cap : knowhow.getAgent().getCapabilities()) {
			Processor atomicProcessor = new Processor(cap);
			graph.addVertex(atomicProcessor);
			candidates.add(atomicProcessor);
		}
		
		for(KnowhowStatement ks : knowhow.getStatements()) {
			Processor processor = new Processor(ks);
			graph.addVertex(processor);
			candidates.add(processor);
		}
		
		// generate selector nodes:
		for(KnowhowStatement ks : knowhow.getStatements()) {
			Processor target = new Processor(ks);
			for(DLPAtom sub : ks.getSubTargets()) {
				String str = sub.toString();
				if(str.contains("(")) {
					String temp = str.substring(0, str.indexOf('('));
					if(knowhow.getAgent().getCapabilities().contains(temp)) {
						str = temp;
					}
				}
				
				Selector selector = new Selector(str);
				graph.addVertex(selector);
				addEdge(graph, target, selector);
				
				for(Processor p : selector.getProcessors(candidates)) {
					addEdge(graph, selector, p);
				}
			}
		}
		
	}
	
	private static boolean addEdge(DirectedGraph<GraphNode, DefaultEdge> graph, GraphNode source, GraphNode target) {
		DefaultEdge res = graph.addEdge(source, target);
		if(res != null) {
			LOG.info("Generate Edge: '{}' --> '{}'",
					source.toString(), target.toString());
			return true;
		} else {
			LOG.warn("Edge: '{}' --> '{}' already exists.",
					source.toString(), target.toString());
			return false;
		}
	}

}
