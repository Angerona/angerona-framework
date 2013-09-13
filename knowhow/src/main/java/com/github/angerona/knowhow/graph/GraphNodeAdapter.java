package com.github.angerona.knowhow.graph;

import java.util.ArrayList;
import java.util.List;


public abstract class GraphNodeAdapter 
	implements GraphNode {

	/** serialVerison */
	private static final long serialVersionUID = 907946462430163415L;
	
	private List<NodeListener> listeners = new ArrayList<>();
	
	protected String name;
	
	public GraphNodeAdapter(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return (getType() == NodeType.NT_PROCESSOR ? "P: " : "S: ") + name;
	}
	
	protected void onStringChange() {
		for(NodeListener listener : listeners) {
			listener.onStringChange(this, this.toString());
		}
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
