package com.github.angerona.fw.motivation.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.ActionComponentDao;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class GenActionMap<T extends Comparable<T>> extends BaseAgentComponent implements ActionComponentDao<T> {

	protected Map<String, T> actions = new HashMap<>();

	@Override
	public T get(Desire d) {
		String key = desireToString(d);

		if (key != null) {
			return actions.get(key);
		}

		return null;
	}

	@Override
	public boolean exists(Desire d) {
		return get(d) != null;
	}

	@Override
	public void put(Desire d, T actionId) {
		String key = desireToString(d);

		if (key != null) {
			if (actionId != null) {
				actions.put(key, actionId);
			} else {
				actions.remove(key);
			}
		}
	}

	@Override
	public BaseAgentComponent clone() {
		GenActionMap<T> cln = new GenActionMap<T>();
		return cln;
	}

	private String desireToString(Desire d) {
		try {
			return d.toString();
		} catch (Exception e) {
			// ignore
		}

		return null;
	}

}
