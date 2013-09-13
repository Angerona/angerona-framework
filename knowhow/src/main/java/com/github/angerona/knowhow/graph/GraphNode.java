package com.github.angerona.knowhow.graph;

import java.io.Serializable;


public interface GraphNode extends Serializable{
	public static enum NodeType {
		NT_PROCESSOR,
		NT_SELECTOR
	}
	
	NodeType getType();
	
	String toString();
	
	void addListener(NodeListener listener);
	
	void removeListener(NodeListener listener);
	
	void removeAllListener();
}
