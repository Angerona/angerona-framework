package com.github.angerona.fw.motivation.functional;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;

/**
 * 
 * @author Manuel Barbi
 *
 */
public interface DesireSelection {

	public Desire select(BeliefState b, MotStructureDao st);

	public DesireSelection copy();

}
