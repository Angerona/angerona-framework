package com.github.angerona.fw.motivation.plans;

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

	public int getEgdeCount() {
		return actions.length;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateNode other = (StateNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
