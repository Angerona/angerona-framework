package com.github.angerona.fw.motivation.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.LevelWeightDao;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class GenLevelWeights<L extends MotiveLevel> extends BaseAgentComponent implements LevelWeightDao<L> {

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
	}

	@Override
	public BaseAgentComponent clone() {
		GenLevelWeights<L> cln = create();
		cln.weights.putAll(this.weights);
		return cln;
	}

	protected abstract GenLevelWeights<L> create();

}
