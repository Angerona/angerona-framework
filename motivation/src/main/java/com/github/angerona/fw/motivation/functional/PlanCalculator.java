package com.github.angerona.fw.motivation.functional;

import com.github.angerona.fw.Desire;

public interface PlanCalculator<ID extends Comparable<ID>> {

	public ID next(Desire d);
	
}
