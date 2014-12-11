package com.github.angerona.fw.motivation.plans.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.behavior.ParsableComponent;
import com.github.angerona.fw.motivation.plans.PlanComponentDao;
import com.github.angerona.fw.motivation.plans.PlanFactory;
import com.github.angerona.fw.motivation.plans.StateNode;
import com.github.angerona.fw.motivation.plans.parser.ParseException;
import com.github.angerona.fw.motivation.plans.parser.PlanParser;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class TrailBasedPlans extends ParsableComponent implements PlanComponentDao, Iterable<String> {

	public static final String EXT = ".pln";

	protected Map<String, Collection<StateNode>> trails = new HashMap<>();

	@Override
	public Collection<StateNode> getPlan(Desire d) {
		return getPlan(d);
	}

	@Override
	public Collection<StateNode> getPlan(String key) {
		Collection<StateNode> list = trails.get(key);

		if (list != null) {
			return Collections.unmodifiableCollection(list);
		}

		return null;
	}

	@Override
	public void loadFromStream(InputStream src) throws IOException {
		try {
			PlanParser parser = new PlanParser(src);
			this.trails = PlanFactory.assemble(parser.gatherPlans());
			report("loaded action-plans from file");
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getFileSuffix() {
		return EXT;
	}

	@Override
	public BaseAgentComponent clone() {
		TrailBasedPlans cln = new TrailBasedPlans();
		cln.trails.putAll(this.trails);
		return cln;
	}

	@Override
	public Iterator<String> iterator() {
		return trails.keySet().iterator();
	}

}
