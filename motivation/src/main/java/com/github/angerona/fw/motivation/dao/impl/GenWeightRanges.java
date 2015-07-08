package com.github.angerona.fw.motivation.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.island.components.ParsableComponent;
import com.github.angerona.fw.motivation.dao.WeightRangeDao;
import com.github.angerona.fw.motivation.data.MotiveLevel;
import com.github.angerona.fw.motivation.data.WeightRange;

/**
 * {@link GenWeightRanges} is a generic class delivering access to the LevelWeights of an Agent. LevelWeights can be parsed from file.
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class GenWeightRanges<L extends MotiveLevel> extends ParsableComponent implements WeightRangeDao<L>,
		Iterable<Entry<L, WeightRange>> {

	private static final Logger LOG = LoggerFactory.getLogger(GenWeightRanges.class);

	protected Map<L, WeightRange> ranges;

	public GenWeightRanges() {
		this(new HashMap<L, WeightRange>());
	}

	public GenWeightRanges(Map<L, WeightRange> ranges) {
		if (ranges == null) {
			throw new NullPointerException();
		}

		this.ranges = ranges;

		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	public WeightRange getRange(L key) {
		return ranges.get(key);
	}

	public void putRange(L key, WeightRange value) {
		ranges.put(key, value);
	}

	@Override
	public Iterator<Entry<L, WeightRange>> iterator() {
		return ranges.entrySet().iterator();
	}

	@Override
	public BaseAgentComponent clone() {
		GenWeightRanges<L> cln = create();
		cln.ranges.putAll(this.ranges);
		return cln;
	}

	protected abstract GenWeightRanges<L> create();

	@Override
	public String toString() {
		// this is a unpleasant workaround
		return this.getClass().getSimpleName();
	}

}
