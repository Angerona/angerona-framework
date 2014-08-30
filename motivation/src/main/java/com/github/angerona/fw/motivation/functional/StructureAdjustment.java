package com.github.angerona.fw.motivation.functional;

import net.sf.tweety.Formula;

import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.dao.MotiveState;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 * @param <F>
 */
public interface StructureAdjustment<L extends MotiveLevel, F extends Formula> {

	public void adjust(MotiveState<L, F> ms, BeliefState<F> b, MotStructureDao out);

	public StructureAdjustment<L, F> copy();

}
