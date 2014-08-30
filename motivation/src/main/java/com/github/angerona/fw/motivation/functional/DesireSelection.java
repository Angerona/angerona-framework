package com.github.angerona.fw.motivation.functional;

import java.util.Collection;

import net.sf.tweety.Formula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;

public interface DesireSelection<F extends Formula> {

	public Collection<Desire> select(BeliefState<F> b, MotStructureDao st);

	public DesireSelection<F> copy();

}
