package com.github.angerona.fw.motivation.dao;

import java.util.Collection;
import java.util.List;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.model.MotStrcEntry;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface MotStructureDao {

	public Double getValue(Desire d);

	public List<MotStrcEntry> getOrderedEntries();

	public void putEntries(Collection<MotStrcEntry> entries);

}
