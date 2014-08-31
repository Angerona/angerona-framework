package com.github.angerona.fw.motivation.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.model.MotStrcEntry;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotStructure extends BaseAgentComponent implements MotStructureDao, Iterable<MotStrcEntry> {

	private static final Logger LOG = LoggerFactory.getLogger(MotStructure.class);

	protected List<MotStrcEntry> entries = new LinkedList<>();

	public MotStructure() {
		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	public Double getValue(Desire d) {
		for (MotStrcEntry e : entries) {
			if (e.getDesire().equals(d)) {
				return e.getMotivationalValue();
			}
		}

		return null;
	}

	@Override
	public List<MotStrcEntry> getOrderedEntries() {
		return Collections.unmodifiableList(this.entries);
	}

	@Override
	public void putEntries(Collection<MotStrcEntry> entries) {
		this.entries.clear();
		this.entries.addAll(entries);
		Collections.sort(this.entries);
	}

	@Override
	public Iterator<MotStrcEntry> iterator() {
		return entries.iterator();
	}

	@Override
	public BaseAgentComponent clone() {
		MotStructure cln = new MotStructure();
		cln.entries.addAll(this.entries);
		return cln;
	}

}
