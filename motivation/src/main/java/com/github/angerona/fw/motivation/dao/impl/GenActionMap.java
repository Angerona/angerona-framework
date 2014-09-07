package com.github.angerona.fw.motivation.dao.impl;

import static com.github.angerona.fw.motivation.utils.FormulaUtils.desireToString;

import java.util.HashMap;
import java.util.Map;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.ActionComponentDao;
import com.github.angerona.fw.motivation.model.ActionNode;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class GenActionMap<T extends Comparable<T>> extends BaseAgentComponent implements ActionComponentDao<T> {

	protected Map<String, ActionNode<T>> actions = new HashMap<>();

	@Override
	public ActionNode<T> get(Desire d) {
		String key = desireToString(d);

		if (key != null) {
			return actions.get(key);
		}

		return null;
	}

	@Override
	public void put(Desire d, ActionNode<T> node) {
		String key = desireToString(d);

		if (key != null) {
			actions.put(key, node);
		}
	}

	@Override
	public BaseAgentComponent clone() {
		GenActionMap<T> cln = new GenActionMap<T>();
		cln.actions.putAll(this.actions);
		return cln;
	}

}
