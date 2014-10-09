package com.github.angerona.fw.motivation.functional;

import java.util.Collection;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;

public interface DesireSelection {

	public Collection<Desire> select(BeliefState b, MotStructureDao st);

	public DesireSelection copy();

}
