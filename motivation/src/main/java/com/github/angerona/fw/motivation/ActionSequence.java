package com.github.angerona.fw.motivation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.Action;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ActionSequence implements Comparable<ActionSequence>, Iterable<Action> {

	protected List<Action> actions = new LinkedList<>();

	public int getLength() {
		return actions.size();
	}

	@Override
	public int compareTo(ActionSequence other) {
		return Integer.compare(this.actions.size(), other.actions.size());
	}

	@Override
	public Iterator<Action> iterator() {
		return actions.iterator();
	}

}
