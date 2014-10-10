package com.github.angerona.fw.motivation.dao.impl;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.model.WeightRange;

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
	public void loadFromChannel(ReadableByteChannel src) throws IOException {
		report("loaded weight-ranges from file");
	}

	@Override
	public String getFileSuffix() {
		return EXT;
	}

}
