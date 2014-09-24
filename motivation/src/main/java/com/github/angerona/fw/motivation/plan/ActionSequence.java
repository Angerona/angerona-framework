package com.github.angerona.fw.motivation.plan;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <ID>
 */
public class ActionSequence<ID> implements Iterable<ID>, Comparable<ActionSequence<ID>> {

	protected List<ID> actions = new LinkedList<>();
	protected int safeWindow = 0;

	public int getDuration() {
		return actions.size();
	}

	public ActionSequence<ID> add(ID action) {
		actions.add(action);
		return this;
	}

	public int getSafeWindow() {
		return safeWindow;
	}

	public ActionSequence<ID> setSafeWindow(int safeWindow) {
		this.safeWindow = safeWindow;
		return this;
	}

	@Override
	public String toString() {
		return "[" + actions + ", " + safeWindow + "]";
	}

	@Override
	public Iterator<ID> iterator() {
		return actions.iterator();
	}

	@Override
	public int compareTo(ActionSequence<ID> o) {
		return Integer.compare(this.getDuration(), o.getDuration());
	}

	@Override
	public ActionSequence<ID> clone() {
		ActionSequence<ID> cln = new ActionSequence<>();
		cln.actions.addAll(this.actions);
		cln.safeWindow = this.safeWindow;
		return cln;
	}

}
