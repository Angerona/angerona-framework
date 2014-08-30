package com.github.angerona.fw.motivation.dao;

import net.sf.tweety.Formula;

import com.github.angerona.fw.motivation.MotiveLevel;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 * @param <F>
 */
public interface MotiveState<L extends MotiveLevel, F extends Formula> extends CouplingDao<L, F>, WeightRangeDao<L>, LevelWeightDao<L> {}
