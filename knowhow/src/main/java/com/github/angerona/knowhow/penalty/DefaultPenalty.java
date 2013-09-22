package com.github.angerona.knowhow.penalty;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;

public class DefaultPenalty implements PenaltyFunction {

	private int iterations = 0;
	
	public DefaultPenalty() {}
	
	public DefaultPenalty(DefaultPenalty dp) {
		iterations = dp.iterations;
	}
	
	@Override
	public void init(Agent agent) {
		iterations = 0;
	}

	@Override
	public int iterations() {
		return iterations;
	}

	@Override
	public double penalty(Action action) throws IllegalStateException {
		++iterations;
		return 0;
	}

	@Override
	public DefaultPenalty clone() {
		return new DefaultPenalty(this);
	}
}
