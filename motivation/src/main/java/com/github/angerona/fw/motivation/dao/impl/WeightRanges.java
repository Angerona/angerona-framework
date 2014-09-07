package com.github.angerona.fw.motivation.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.model.WeightRange;
import com.github.angerona.fw.motivation.parser.MotivationParser;
import com.github.angerona.fw.motivation.parser.ParseException;

/**
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
	public void loadFromStream(InputStream src) throws IOException {
		try {
			MotivationParser parser = new MotivationParser(src);
			this.ranges = parser.gatherRanges();
			report("loaded weight-ranges from file");
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getFileSuffix() {
		return EXT;
	}

}
