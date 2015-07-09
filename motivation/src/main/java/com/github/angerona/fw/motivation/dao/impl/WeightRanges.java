package com.github.angerona.fw.motivation.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.motivation.data.Maslow;
import com.github.angerona.fw.motivation.data.WeightRange;

/**
 * {@link WeightRange} is a specialized {@link AgentComponent} for {@link Maslow} WeightRanges
 * 
 * @author Manuel Barbi
 * 
 */
public class WeightRanges extends GenWeightRanges<Maslow> {

	private static final String EXT = ".rng";

	public WeightRanges() {}

	public WeightRanges(Map<Maslow, WeightRange> ranges) {
		super(ranges);
	}

	@Override
	protected GenWeightRanges<Maslow> create() {
		return new WeightRanges();
	}

	@Override
	public String getFileSuffix() {
		return EXT;
	}

	@Override
	public void loadFromStream(InputStream src) throws IOException {
		// TODO Auto-generated method stub
	}

}
