package com.github.angerona.fw.motivation.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.motivation.dao.LevelWeightDao;
import com.github.angerona.fw.motivation.data.MotiveLevel;

/**
 * {@link GenLevelWeights} is a generic class, delivering access to the LevelWeights of an Agent
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class GenLevelWeights<L extends MotiveLevel> extends BaseAgentComponent implements LevelWeightDao<L>, Iterable<Entry<L, Double>> {

	private static final Logger LOG = LoggerFactory.getLogger(GenLevelWeights.class);

	protected Map<L, Double> weights = new HashMap<>();

	public GenLevelWeights() {
		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	public double getWeight(L key) {
		return weights.get(key);
	}

	@Override
	public void putWeight(L key, double value) {
		weights.put(key, value);
		report("set level-weight " + value + " for motive-level " + key);
	}

	@Override
	public Iterator<Entry<L, Double>> iterator() {
		return weights.entrySet().iterator();
	}

	@Override
	public BaseAgentComponent clone() {
		GenLevelWeights<L> cln = create();
		cln.weights.putAll(this.weights);
		return cln;
	}

	protected abstract GenLevelWeights<L> create();

	@Override
	public String toString() {
		// this is a unpleasant workaround
		return this.getClass().getSimpleName();
	}

}
