package com.github.angerona.knowhow.graph;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.knowhow.KnowhowBase;

public class KnowhowGraph extends BaseAgentComponent implements PropertyChangeListener {
	ListenableDirectedGraph<GraphNode, DefaultEdge> graph = new ListenableDirectedGraph<>(DefaultEdge.class);
	
	public KnowhowGraph() {}

	@SuppressWarnings("unchecked")
	public KnowhowGraph(KnowhowGraph other) {
		this.graph = (ListenableDirectedGraph<GraphNode, DefaultEdge>) other.graph.clone();
	}
	
	public ListenableDirectedGraph<GraphNode, DefaultEdge> getGraph() {
		return graph;
	}
	
	@Override
	public void init(Map<String, String> additionalData) {
		componentAdded(getAgent().getComponent(KnowhowBase.class));
	}

	@Override
	public void componentAdded(BaseAgentComponent comp) {
		if(comp instanceof KnowhowBase) {
			GraphBuilder.build((KnowhowBase)comp, graph);
			comp.addPropertyChangeListener(this);
		}
	}
	
	@Override
	public BaseAgentComponent clone() {
		return new KnowhowGraph(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		GraphBuilder.build((KnowhowBase)evt.getSource(), graph);
	}
}
