package com.github.angerona.fw.motivation.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.motivation.dao.TimeSlotDao;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class TimeSlots extends BaseAgentComponent implements TimeSlotDao, Iterable<Integer> {

	protected Set<Integer> slots = new HashSet<>();

	@Override
	public void addSlot(int s) {
		slots.add(s);
	}

	@Override
	public Collection<Integer> getSlots() {
		return Collections.unmodifiableSet(slots);
	}

	@Override
	public Integer getMinSlot() {
		if (slots.isEmpty()) {
			return null;
		}

		return Collections.min(slots);
	}

	@Override
	public void clear() {
		slots.clear();
	}

	@Override
	public BaseAgentComponent clone() {
		return new TimeSlots();
	}

	@Override
	public Iterator<Integer> iterator() {
		return slots.iterator();
	}
	
	@Override
	public String toString() {
		// this is a unpleasant workaround
		return this.getClass().getSimpleName();
	}

}
