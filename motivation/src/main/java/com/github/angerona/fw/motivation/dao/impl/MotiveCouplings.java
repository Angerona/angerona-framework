package com.github.angerona.fw.motivation.dao.impl;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.Set;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.MotiveCoupling;

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
	public void loadFromChannel(ReadableByteChannel src) throws IOException {
		report("loaded motive-couplings from file");
	}

	@Override
	public String getFileSuffix() {
		return EXT;
	}

}
