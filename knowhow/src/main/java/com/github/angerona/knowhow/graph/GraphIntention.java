package com.github.angerona.knowhow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * 
 * @author Tim Janus
 */
public class GraphIntention {
	private static final GraphIntention TBD = new GraphIntention();
	
	private Selector selector;
	
	private Processor intention;
	
	private GraphIntention parent;
	
	/** the list of sub-intentions */
	private List<GraphIntention> subIntentions = new ArrayList<>();
	
	/** Default Ctor: Generates the TBD GraphIntention */
	private GraphIntention() {}
	
	public GraphIntention(Processor processor, Selector precessor, GraphIntention parent) {
		this.intention = processor;
		this.selector = precessor;
		this.parent = parent;
		
		for(int i=0; i<this.intention.getChildren().size(); ++i) {
			subIntentions.add(TBD);
		}
	}
	
	public GraphIntention getParent() {
		return parent;
	}
	
	public Selector getSelector() {
		return selector;
	}
	
	/**
	 * Replaces the sub intention on the specified index by the given intention
	 * @param index		The index of the sub-intention that shall be replaced
	 * @param intention	The new sub-intention
	 */
	public void replaceSubIntention(int index, GraphIntention intention) {
		if(index < 0 || index >= subIntentions.size())
			throw new IndexOutOfBoundsException();
		if(intention == null)
			throw new IllegalArgumentException();
		
		subIntentions.set(index, intention);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getComplexity() {
		int complexity = 0;
		
		if(isComplete()) {
			for(GraphIntention gi : subIntentions) {
				complexity += gi.getComplexity();	
			}
		} else {
			complexity = intention.getComplexity();
		}
		
		return complexity;
	}
	
	/**
	 * Recursively counts the actions stored in this intention
	 * @return	the number of actions stored in this intention
	 */
	public int countActions() {
		int actions = 0;
		for(GraphIntention gi : subIntentions) {
			if(gi.isAtomic())
				actions += 1;
			else
				actions += gi.countActions();
		}
		return actions;
	}
	
	public List<GraphIntention> getSubIntentions() {
		return Collections.unmodifiableList(subIntentions);
	}
	
	/** @return	true if this GraphIntention is atomic and therefore represents an Action, false otherwise */
	public boolean isAtomic() {
		return intention != null && subIntentions.isEmpty();
	}
	
	/** @return true if this GraphIntention represents a sequence of actions (a complete plan), false otherwise */
	public boolean isSequence() {
		return intention == null && !subIntentions.isEmpty();
	}
	
	/** @return true if this GraphIntention represents a complex intention that needs further processing */
	public boolean isComplex() {
		return intention != null && !subIntentions.isEmpty();
	}
	
	/** @return true if this GraphIntention represents a complex intention that direct childs are already processed */
	public boolean isComplete() {
		return isComplex() && !subIntentions.contains(TBD);
	}
	
	public Processor getNode() {
		return intention;
	}
	
	@Override
	public String toString() {
		if(intention == null)
			return "TBD";
		return intention.toString() + subIntentions.toString();
	}
}
