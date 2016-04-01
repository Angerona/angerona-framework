package com.github.kreatures.knowhow.graph;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.AgentComponent;
import com.github.kreatures.core.BaseAgentComponent;
import com.github.kreatures.knowhow.KnowhowBase;
import com.github.kreatures.knowhow.graph.complexity.ComplexityCalculator;
import com.github.kreatures.knowhow.graph.complexity.MaxSelectorSumProcessorComplexity;
import com.github.kreatures.knowhow.graph.parameter.DefaultParameterCheck;
import com.github.kreatures.knowhow.situation.Situation;
import com.github.kreatures.knowhow.situation.SituationGraphBuilder;
import com.github.kreatures.knowhow.situation.SituationGraphBuilderFactory;
import com.github.kreatures.knowhow.situation.SituationStorage;

/**
 * The KnowhowGraph is an agent component that depends on the {@link KnowhowBase}
 * of an agent and on it's {@link SituationStorage}. Those components are used
 * to generate a reachable Planning Graph as described in the Diploma Thesis
 * "Resource-bounded Planning of Communication under Confidentiality Constraints
 * for BDI Agents".
 * 
 * @todo move build() method into external GraphBuilder class
 * 
 * @author Tim Janus
 */
public class KnowhowGraph extends BaseAgentComponent implements PropertyChangeListener {
	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(KnowhowGraph.class);
	
	/** the graph as JGraphT instance */
	ListenableDirectedGraph<GraphNode, DefaultEdge> graph = new ListenableDirectedGraph<>(DefaultEdge.class);
	
	private KnowhowBase depKnowhowBase;
	
	private SituationStorage depSituationStorage;
	
	public KnowhowGraph() {}

	public KnowhowGraph(KnowhowGraph other) {
		super(other);
		this.graph = new ListenableDirectedGraph<>(DefaultEdge.class);
		addToGraph(this.graph, other.graph);
	}
	
	public ListenableDirectedGraph<GraphNode, DefaultEdge> getGraph() {
		return graph;
	}
	
	@Override
	public void init(Map<String, String> parameters) {
		if(initalized)
			return;
		
		if(depKnowhowBase != null && depSituationStorage != null) {
			build();
			super.init(parameters);
		} 
	}
	
	@Override
	public void componentInitialized(AgentComponent comp) {
		if(comp instanceof KnowhowBase) {
			depKnowhowBase = (KnowhowBase)comp;
			comp.addPropertyChangeListener(this);
			init(null);
		} else if(comp instanceof SituationStorage) {
			depSituationStorage = (SituationStorage)comp;
			comp.addPropertyChangeListener(this);
			init(null);
		}
	}
	
	@Override
	public BaseAgentComponent clone() {
		return new KnowhowGraph(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		build();
	}
	
	private void build() {	
		KnowhowBase knowhow = getAgent().getComponent(KnowhowBase.class);
		SituationStorage ss = getAgent().getComponent(SituationStorage.class);
		if(knowhow == null || ss == null) {
			return;
		}
		
		report("Building Know-how-Graph");
		graph = new ListenableDirectedGraph<>(DefaultEdge.class);
		KnowhowBaseGraphBuilder kbBuilder = new KnowhowBaseGraphBuilder(knowhow);
		kbBuilder.build();
		addToGraph(graph, kbBuilder.getGraph());
		report("Build Graph from Know-how Base", this);
		
		checkValidGraph();
		
		// find graph nodes that have no outgoing edges:
		List<GraphNode> leafNodes = new ArrayList<>();
		DepthFirstIterator<GraphNode, DefaultEdge> it = new DepthFirstIterator<>(graph);
		while(it.hasNext()) {
			boolean leaf = true;
			GraphNode cur = it.next();
			for(DefaultEdge edge : graph.edgesOf(cur)) {
				if(graph.getEdgeSource(edge).equals(cur)) {
					leaf = false;
					break;
				}
			}
			
			if(leaf) {
				leafNodes.add(cur);
			}
		}
		
		for(GraphNode leaf : leafNodes) {
			if(!kbBuilder.getLeafs().contains(leaf)) {
				// check if the leaf node is in the graph (it might be removed by a previous iteration)
				if(!graph.containsVertex(leaf))
					continue;
					
				// TODO: Try to find situation to generate graph, otherwise remove from graph
				if(leaf instanceof Selector) {
					Selector lSel = (Selector)leaf;
					
					Situation sit = null;
					if(ss != null) {
						sit = ss.getSituation(lSel.getName());
						
						if(sit == null) {
							LOG.info("Cannot find a situation in SituatioStorage for goal '{}'", lSel.getName());
						}
					} else {
						LOG.info("No situation storage, therefore no situation can be found for goal '{}'", lSel.getName());
					}
					
					if(sit != null) {
						SituationGraphBuilder builder = SituationGraphBuilderFactory.createGraphBuilder(
								sit, getAgent());
						builder.build();
						Graph<GraphNode, DefaultEdge> situationGraph = builder.getGraph();
						
						// add situation subgraph to planning graph:
						addToGraph(graph, situationGraph);
					} else {
						bottomUpRemove(leaf);
					}
				}
			}
		}
		report("Optimize Graph by using the situation driven plan generation", this);
		checkValidGraph();
		
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

	private void checkValidGraph() {
		// check validy:
		String err = "";
		for(GraphNode node : graph.vertexSet()) {
			if(node.getGraph() != graph) {
				err += "The node '" + node.toString() + "' is not linked to correct graph\n";
			}
		}
		
		for(DefaultEdge edge : graph.edgeSet()) {
			if(graph.getEdgeSource(edge).getGraph() != graph) {
				err += "The edge '" + edge + "' source is not linked to correct graph\n";
			}
			if(graph.getEdgeTarget(edge).getGraph() != graph) {
				err += "The edge '" + edge + "' target is not linked to correct graph\n";
			}
		}
		
		if(!err.isEmpty()) {
			throw new IllegalStateException(err);
		}
	}
	
	private void addToGraph(Graph<GraphNode, DefaultEdge> dst, Graph<GraphNode, DefaultEdge> source) {
		for(GraphNode node : source.vertexSet()) {
			GraphNode toAdd = node.clone();
			toAdd.setGraph(dst);
			dst.addVertex(toAdd);
		}
		
		for(DefaultEdge edge : source.edgeSet()) {
			GraphNode src = graph.getEdgeSource(edge).clone();
			GraphNode target = graph.getEdgeTarget(edge).clone();
			
			for(GraphNode cur : dst.vertexSet()) {
				if(cur.equals(src)) {
					src = cur;
				} else if(cur.equals(target)) {
					target = cur;
				}
			}
			
			dst.addEdge(src, target);
		}
	}
	
	private void bottomUpRemove(GraphNode node) {
		if(! ((node instanceof Selector) && node.getChildren().size() == 0)) {
			throw new IllegalArgumentException("bottomUpRemove has to be called " +
					"on selector node that does not have children");
		}
		
		Set<GraphNode> ntr = new HashSet<>();
		bottomUpRemove(node, ntr);
		
		LOG.info("Removed the vertices: '{}' to generate reachable planning graph", ntr);
	}
	
	private void bottomUpRemove(GraphNode node, Set<GraphNode> nodesRemoved) {
		// check if the given node has to be removed to have a reachable graph:
		boolean remove = false;
		if(node instanceof Selector) {
			if(node.getChildren().size() == 0) {
				remove = true;
			}
		} else if(node instanceof Processor) {
			Processor pro = (Processor) node;
			int subTargets = pro.getStatement().getSubTargets().size();
			int children = pro.getChildren().size();
			if(children < subTargets) {
				remove = true;
			}
		}
		
		if(remove) {
			// first find parent nodes:
			List<? extends GraphNode> children = node.getChildren();
			Set<GraphNode> parents = new HashSet<>();
			for(DefaultEdge edge : node.getGraph().edgesOf(node)) {
				// if the edge goes to a parent:
				if(node.getGraph().getEdgeTarget(edge).equals(node)) {
					parents.add(node.getGraph().getEdgeSource(edge));
				}
			}
			
			// remove this node:
			boolean res = graph.removeVertex(node);
			if(!res) {
				LOG.warn("Cannot remove vertex: '{}' from Graph.", node.toString());
			}
			nodesRemoved.add(node);
						
			
			// recursively delete all children:
			for(GraphNode child : children) {
				topDownDelete(child, nodesRemoved);
			}
			
			// recursively call all parents:
			for(GraphNode parent : parents) {
				bottomUpRemove(parent, nodesRemoved);
			}
		}
	}
	
	private void topDownDelete(GraphNode node, Set<GraphNode> nodesRemoved) {
		// only delete if there is no parent anymore:
		boolean parent = false;
		for(DefaultEdge edge : graph.edgesOf(node)) {
			if(graph.getEdgeTarget(edge).equals(node)) {
				parent = true;
			}
		}
		
		if(!parent) {
			List<? extends GraphNode> children = node.getChildren();
			
			nodesRemoved.add(node);
			graph.removeVertex(node);
			
			for(GraphNode child : children) {
				topDownDelete(child, nodesRemoved);
			}
		}
	}
}
