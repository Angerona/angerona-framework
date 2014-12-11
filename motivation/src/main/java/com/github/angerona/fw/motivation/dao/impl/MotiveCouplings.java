package com.github.angerona.fw.motivation.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.MotiveCoupling;
import com.github.angerona.fw.motivation.parser.MotivationParser;
import com.github.angerona.fw.motivation.parser.ParseException;

/**
 * {@link MotiveCouplings} is a specialized AgentComponent for {@link Maslow} MotiveCouplings
 * 
 * @author Manuel Barbi
 * 
 */
public class MotiveCouplings extends GenMotiveCouplings<Maslow> {

	private static final String EXT = ".cpl";

	public MotiveCouplings() {}

	protected MotiveCouplings(Set<MotiveCoupling<Maslow>> couplings) {
		super(couplings);
	}

	@Override
	protected GenMotiveCouplings<Maslow> create() {
		return new MotiveCouplings();
	}

	@Override
	public void loadFromStream(InputStream src) throws IOException {
		try {
			MotivationParser parser = new MotivationParser(src);
			couplings = parser.gatherCouplings();
			report("loaded motive-couplings from file");
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getFileSuffix() {
		return EXT;
	}

}
