package com.github.angerona.fw.motivation.dao.impl;

import com.github.angerona.fw.motivation.Maslow;

/**
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
