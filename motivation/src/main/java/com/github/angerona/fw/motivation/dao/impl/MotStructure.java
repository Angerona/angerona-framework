package com.github.angerona.fw.motivation.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.MotStructureDao;

/**
 * {@link MotStructure} is an {@link AgentComponent} representing the motivational-structure of a motivated BDI-Agent
 * 
 * @author Manuel Barbi
 * 
 */
public class MotStructure extends BaseAgentComponent implements MotStructureDao, Iterable<Entry<Desire, Double>> {

	private static final Logger LOG = LoggerFactory.getLogger(MotStructure.class);

	protected Map<Desire, Double> entries = new HashMap<>();

	public MotStructure() {
		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	public Double getValue(Desire d) {
		return entries.get(d);
	}

	@Override
	public Set<Entry<Desire, Double>> getEntries() {
		return Collections.unmodifiableSet(entries.entrySet());
	}

	@Override
	public Iterator<Entry<Desire, Double>> iterator() {
		return entries.entrySet().iterator();
	}

	@Override
	public BaseAgentComponent clone() {
		MotStructure cln = new MotStructure();
		cln.entries.putAll(this.entries);
		return cln;
	}

	@Override
	public void putEntry(Desire d, Double mu) {
		this.entries.put(d, mu);
		report("set motivational-value " + mu + " for desire " + d);
	}

	@Override
	public void clear() {
		this.entries.clear();
	}

	@Override
	public String toString() {
		// this is a unpleasant workaround
		return this.getClass().getSimpleName();
	}

}
