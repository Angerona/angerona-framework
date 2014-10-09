package com.github.angerona.fw.motivation.dao;

import java.util.Map.Entry;
import java.util.Set;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;

/**
 * {@link MotStructureDao} is a generic class, that delivers access to the motivational-structure of an {@link Agent}.
 * 
 * @author Manuel Barbi
 * 
 */
public interface MotStructureDao {

	public Double getValue(Desire d);

	public Set<Entry<Desire, Double>> getEntries();

	public void putEntry(Desire d, Double mu);

	public void clear();

}
