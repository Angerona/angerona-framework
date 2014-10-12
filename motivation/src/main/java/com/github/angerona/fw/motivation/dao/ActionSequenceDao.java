package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.ActionSequence;

/**
 * 
 * @author Manuel Barbi
 *
 */
public interface ActionSequenceDao {

	public ActionSequence getSequence(Desire d);

	public void putSequence(Desire d, ActionSequence seq);

	public void clear();

}
