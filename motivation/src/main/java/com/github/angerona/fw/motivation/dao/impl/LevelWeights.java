package com.github.angerona.fw.motivation.dao.impl;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.motivation.data.Maslow;

/**
 * {@link LevelWeights} is a specialized {@link AgentComponent} for {@link Maslow} MotiveLevels
 * 
 * @author Manuel Barbi
 * 
 */
public class LevelWeights extends GenLevelWeights<Maslow> {

	@Override
	protected GenLevelWeights<Maslow> create() {
		return new LevelWeights();
	}

}
