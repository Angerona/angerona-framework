package com.github.angerona.fw.motivation.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.github.angerona.fw.motivation.data.Maslow;
import com.github.angerona.fw.motivation.data.MotiveCoupling;

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
	public String getFileSuffix() {
		return EXT;
	}

	@Override
	public void loadFromStream(InputStream src) throws IOException {
		// TODO Auto-generated method stub

	}

}
