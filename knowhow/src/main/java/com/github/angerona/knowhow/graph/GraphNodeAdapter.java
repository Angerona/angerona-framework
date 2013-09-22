package com.github.angerona.knowhow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.knowhow.graph.complexity.ComplexityCalculator;
import com.github.angerona.knowhow.graph.parameter.Parameter;
import com.github.angerona.knowhow.graph.parameter.ParameterCheckVisitor;

/**
 * Abstract base class for every node in the planning graph.
 * Implements functionality to allow parameter forwarding.
 * 
 * @author Tim Janus
 */
public abstract class GraphNodeAdapter
	implements GraphNode {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(GraphNodeAdapter.class);
	
	/** serialVerison */
	private static final long serialVersionUID = 907946462430163415L;
	
	/** the list of node listeners */
	private List<NodeListener> listeners = new ArrayList<>();
	
	/** the graph the node belongs to */
	private DirectedGraph<GraphNode, DefaultEdge> graph;
	
	/** the complexity of the node */
	protected int complexity;
	
	/** the parameters of the node stored as Strings in a list */
	protected List<Parameter> parameters = new ArrayList<>();
	
	/** the name of the node */
	protected String name;
	
	/** 
	 * Default Ctor
	 * @param name	The name of the node
	 * @param graph	The parent graph of the node
	 */
	public GraphNodeAdapter(String name, DirectedGraph<GraphNode, DefaultEdge> graph) {
		this.graph = graph;		
		parseName(name);
	}

	/**
	 * Parses the given string as name by using the string before '(' as name and parsing
	 * a list of '(param1,param2,...)' as parameters.
	 * @param name	The name that maybe contains a parameter list
	 */
	private void parseName(String name) {
		int paranthessStack = 0;
		boolean closed = false;
		String parameter = "";
		String end = "";
		this.name = "";
		
		for(int index=0; index<name.length(); ++index) {
			switch(name.charAt(index)) {
			case '(':
				if(paranthessStack >= 1)
					parameter += '(';
				paranthessStack += 1;
				break;
				
			case ')':
				paranthessStack -= 1;
				if(paranthessStack == 0) {
					parameters.add(new Parameter(parameter));
					parameter = "";
					closed = true;
				} else {
					parameter += ')';
				}
				break;
				
			case ',':
				parameters.add(new Parameter(parameter));
				parameter = "";
				break;
				
			default:
				if(closed) {
					end += name.charAt(index);
				} else if(paranthessStack == 0) {
					this.name += name.charAt(index);
				} else {
					parameter += name.charAt(index);
				}
			}
		}
		
		if(!end.isEmpty())
			LOG.warn("The name's '{}' end: '{}' is skipped by the parsing", name, end);
	}
	
	/**
	 * @return A list of GraphNodes that represent the children of this node
	 */
	@Override
	public List<? extends GraphNode> getChildren() {
		List<GraphNode> reval = new ArrayList<>();
		for(DefaultEdge edge : graph.edgesOf(this)) {
			GraphNode node = graph.getEdgeTarget(edge);
			if(node != this) {
				reval.add(node);
			}
		}
		return reval;
	}
	
	@Override
	public int visitComplexityCalculation(ComplexityCalculator calculator) {
		return complexity = calculator.getComplexity(this);
	}
	
	@Override
	public boolean visitParameterCheck(ParameterCheckVisitor parameterChecker) {
		return parameterChecker.checkParameterMapping(this, new ArrayList<Parameter>());
	}
	
	@Override
	public int getComplexity() {
		return complexity;
	}
	
	@Override
	public String toString() {
		return (getType() == NodeType.NT_PROCESSOR ? "P" : "S") + "(" + complexity + ")" +  
				name + (parameters.isEmpty()?"":parameters.toString());
	}
	
	@Override
	public int hashCode() {
		return ((name == null ? 0 : name.hashCode()) + 
			   (parameters == null ? 0 : parameters.hashCode())) * 3;
	}
	
	protected void onStringChange() {
		for(NodeListener listener : listeners) {
			listener.onStringChange(this, this.toString());
		}
	}
	

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Graph<GraphNode, DefaultEdge> getGraph() {
		return graph;
	}
	
	@Override
	public List<Parameter> getParameters() {
		return Collections.unmodifiableList(parameters);
	}
	
	@Override
	public void addListener(NodeListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(NodeListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void removeAllListener() {
		listeners.clear();
	}
}
