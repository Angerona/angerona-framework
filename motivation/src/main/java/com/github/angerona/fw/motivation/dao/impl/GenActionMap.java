package com.github.angerona.fw.motivation.dao.impl;

import static com.github.angerona.fw.motivation.utils.FormulaUtils.desireToString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.ActionComponentDao;
import com.github.angerona.fw.motivation.plan.ActionSequence;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class GenActionMap<T extends Comparable<T>> extends BaseAgentComponent implements ActionComponentDao<T>, Iterable<String> {

	protected Map<String, ActionSequence<T>> actions = new HashMap<>();

	@Override
	public ActionSequence<T> get(Desire d) {
		return get(desireToString(d));
	}

	@Override
	public ActionSequence<T> get(String key) {
		if (key != null) {
			return actions.get(key);
		}

		return null;
	}

	@Override
	public void put(Desire d, ActionSequence<T> seq) {
		String key = desireToString(d);

		if (key != null) {
			if (seq != null) {
				actions.put(key, seq);
				getAgent().report("action-sequence for " + key + ": " + seq);
			} else {
				actions.remove(key);
				getAgent().report("no action-sequence for " + key);
			}
		}
	}

	@Override
	public BaseAgentComponent clone() {
		GenActionMap<T> cln = new GenActionMap<T>();
		cln.actions.putAll(this.actions);
		return cln;
	}

	@Override
	public Iterator<String> iterator() {
		return actions.keySet().iterator();
	}

}
