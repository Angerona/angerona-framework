package com.github.angerona.fw.motivation.plan;

import java.util.Iterator;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class StateNode implements Comparable<StateNode>, Iterable<ActionEdge> {

	protected String name;
	protected ActionEdge[] actions = new ActionEdge[0];

	StateNode(String name) {
		this.setName(name);
	}

	StateNode setName(String name) {
		if (name == null) {
			throw new NullPointerException("name must not be null");
		}

		this.name = name;
		return this;
	}

	StateNode setActions(ActionEdge[] actions) {
		this.actions = actions;
		return this;
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(StateNode o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public Iterator<ActionEdge> iterator() {
		return new Iterator<ActionEdge>() {

			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < actions.length;
			}

			@Override
			public ActionEdge next() {
				return hasNext() ? actions[i++] : null;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("remove operation is not supported");
			}

		};
	}

	@Override
	public String toString() {
		return name;
	}

}
