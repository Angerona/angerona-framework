package com.github.angerona.fw.motivation.dao;

import java.util.List;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 *
 */
public interface ActionSequenceDao {

	public List<Action> getSequence(Desire d);

	public void putSequence(Desire d, List<Action> seq);

	public void clear();

}
