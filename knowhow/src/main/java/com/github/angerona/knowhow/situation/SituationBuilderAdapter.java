package com.github.angerona.knowhow.situation;

import net.sf.tweety.logicprogramming.asplibrary.parser.ASPParser;
import net.sf.tweety.logicprogramming.asplibrary.parser.ParseException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.knowhow.graph.GraphNode;
import com.github.angerona.knowhow.graph.Selector;

/**
 * 
 * @author Tim Janus
 */
public abstract class SituationBuilderAdapter implements SituationGraphBuilder {
	private static Logger LOG = LoggerFactory.getLogger(SituationBuilderAdapter.class);
	
	protected Graph<GraphNode, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
	
	protected Agent agent;
	
	protected Selector connectedNode;
	
	public SituationBuilderAdapter(Agent agent, Selector connectedNode) {
		this.agent = agent;
		this.connectedNode = connectedNode.clone();
	}
	
	@Override
	public Graph<GraphNode, DefaultEdge> getGraph() {
		return graph;
	}
	
	protected Rule createRule(String str) {
		Rule reval = null;
		try {
			reval = ASPParser.parseRule(str);
		} catch (ParseException e) {
			LOG.warn("Cannot parse the ASP-Fact: '{}'", str);
			e.printStackTrace();
		}
		return reval;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
