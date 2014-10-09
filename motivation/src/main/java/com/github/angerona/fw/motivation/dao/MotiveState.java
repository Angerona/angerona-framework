package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.motivation.MotiveLevel;

/**
 * {@link MotiveState} is an implementation of different DAOs and therefore {@link AgentComponent}s bundling access for usage in functional
 * components
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public interface MotiveState<L extends MotiveLevel> extends CouplingDao<L>, WeightRangeDao<L>, LevelWeightDao<L> {}
