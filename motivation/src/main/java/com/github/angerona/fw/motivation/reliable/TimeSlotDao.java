package com.github.angerona.fw.motivation.reliable;

import java.util.Collection;

/**
 * 
 * @author Manuel Barbi
 *
 */
public interface TimeSlotDao {

	public void addSlot(int s);

	public Collection<Integer> getSlots();

	public Integer getMinSlot();

	public void clear();

}
