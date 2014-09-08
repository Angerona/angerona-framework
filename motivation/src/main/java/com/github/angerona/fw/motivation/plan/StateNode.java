package com.github.angerona.fw.motivation.plan;

import java.util.Iterator;

import net.sf.tweety.Formula;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <F>
 */
public class StateNode<F extends Formula> implements Iterable<ActionEdge<F>> {

	protected String name;
	protected ActionEdge<F>[] actions;

	public StateNode(String name, ActionEdge<F>[] actions) {
		if (name == null) {
			throw new NullPointerException("name must not be null");
		}

		if (actions == null) {
			throw new NullPointerException("actions must not be null");
		}

		this.name = name;
		this.actions = actions;
	}

	@Override
	public Iterator<ActionEdge<F>> iterator() {
		return new Iterator<ActionEdge<F>>() {

			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < actions.length;
			}

			@Override
			public ActionEdge<F> next() {
				return hasNext() ? actions[i++] : null;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("remove operation is not supported");
			}

		};
	}

}
