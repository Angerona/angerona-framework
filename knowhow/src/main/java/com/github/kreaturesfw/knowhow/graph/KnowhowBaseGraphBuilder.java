package com.github.kreaturesfw.knowhow.graph;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.lp.asp.syntax.DLPAtom;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.knowhow.KnowhowBase;
import com.github.kreaturesfw.knowhow.KnowhowStatement;
import com.github.kreaturesfw.knowhow.situation.SituationGraphBuilder;

/**
 * Builds a planning graph using a know-how base.
 * Therefore the building process is separated into 
 * these three steps:
 * 1. buildCapabilities() - Generates the leaf nodes representing actions of the agent
 * 2. buildKnowhowbase() - Generates the nodes that are defined by the Know-how base
 * 3. buildEdges() - Generates the edges to connect the generated nodes.
 * 
 * @todo use common base-class or interface for {@link SituationGraphBuilder}
 * 
 * @author Tim Janus
 */
public class KnowhowBaseGraphBuilder {
	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(KnowhowBaseGraphBuilder.class);
	
	private Agent agent;
	
	private KnowhowBase knowhow;
	
	private Graph<GraphNode, DefaultEdge> graph; 
	
	private Set<GraphNode> leafs = new HashSet<>();
	
	public KnowhowBaseGraphBuilder(KnowhowBase base) {
		this(base, base.getAgent());
	}
	
	public KnowhowBaseGraphBuilder(KnowhowBase base, Agent agent)  {
		if(base == null || agent == null)
			throw new IllegalArgumentException();
		this.knowhow = base;
		this.agent = agent;
		graph = new DefaultDirectedGraph<>(DefaultEdge.class);
	}
	
	public Graph<GraphNode, DefaultEdge> getGraph() {
		return graph;
	}
	
	public Set<GraphNode> getLeafs() {
		return leafs;
	}
	
	public void build() {
		buildCapabilities();
		buildKnowhowbase();
		buildEdges();
	}
	
	public void buildCapabilities() {
		LOG.debug("Entering buildCapabilities()");
		for(String cap : agent.getCapabilities()) {
			Processor atomicProcessor = new Processor(cap, graph);
			graph.addVertex(atomicProcessor);
			leafs.add(atomicProcessor);
		}
		LOG.debug("Leaving buildCapaibities() = void)");
	}
	
	public void buildKnowhowbase() {
		LOG.debug("Entering buildKnowhowBase()");
		for(KnowhowStatement ks : knowhow.getStatements()) {
			Processor processor = new Processor(ks, graph);
			graph.addVertex(processor);
			
			for(DLPAtom sub : ks.getSubTargets()) {
				Selector selector = fromSubTarget(sub);
				graph.addVertex(selector);
			}
		}
		LOG.debug("Leaving buildKnowhowBase() = void");
	}

	public void buildEdges() {
		LOG.debug("Entering buildEdges()");
		for(KnowhowStatement ks : knowhow.getStatements()) {
			for(DLPAtom subtarget : ks.getSubTargets()) {
				Selector sel = fromSubTarget(subtarget);
				addEdge(new Processor(ks, graph), sel);
				
				for(Processor p : getConnectableProcessors(sel)) {
					addEdge(sel, p);
				}
			}
		}
		LOG.debug("Leaving buildEdges() = void");;
	}
	
	private Set<Processor> getConnectableProcessors(Selector selector) {
		Set<Processor> reval = new HashSet<>();
		
		String name = selector.getName();
		name = name.startsWith("s_") ? name.substring(2) : name;
		
		for(KnowhowStatement ks : knowhow.getStatements()) {
			Processor cur = new Processor(ks, graph);
			if(cur.getName().equals(name)) {
				reval.add(cur);
			}
		}
		
		for(GraphNode leaf : leafs) {
			if(leaf instanceof Processor) {
				if(leaf.getName().equals(name)) {
					reval.add((Processor) leaf);
				}
			}
		}
		return reval;
	}
	
	private Selector fromSubTarget(DLPAtom subtarget) {
		String str = subtarget.toString();
		if(str.contains("(")) {
			String temp = str.substring(0, str.indexOf('('));
			if(agent.getCapabilities().contains(temp)) {
				subtarget = new DLPAtom(temp);
			}
		}
		
		return new Selector(subtarget, graph);
	}
	
	private boolean addEdge(GraphNode source, GraphNode target) {
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
