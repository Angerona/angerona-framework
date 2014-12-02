package com.github.angerona.fw.motivation.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.ActionSequence;
import com.github.angerona.fw.motivation.dao.ActionSequenceDao;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ActionSequences extends BaseAgentComponent implements ActionSequenceDao {

	protected Map<Desire, ActionSequence> sequences = new HashMap<>();

	@Override
	public ActionSequence getSequence(Desire d) {
		return sequences.get(d);
	}

	@Override
	public void putSequence(Desire d, ActionSequence seq) {
		sequences.put(d, seq);
	}

	@Override
	public void clear() {
		sequences.clear();
	}

	@Override
	public BaseAgentComponent clone() {
		ActionSequences cln = new ActionSequences();
		cln.sequences.putAll(this.sequences);
		return cln;
	}

}
