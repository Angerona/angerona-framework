package com.github.angerona.fw.motivation.island.comp;

import static com.github.angerona.fw.motivation.utils.FormulaUtils.desireToString;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.PlanComponentDao;
import com.github.angerona.fw.motivation.dao.impl.ParsableComponent;
import com.github.angerona.fw.motivation.plan.PlanFactory;
import com.github.angerona.fw.motivation.plan.StateNode;
import com.github.angerona.fw.motivation.plan.parser.ParseException;
import com.github.angerona.fw.motivation.plan.parser.PlanParser;

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
		String key = desireToString(d);

		if (key != null) {
			return getPlan(key);
		}

		return null;
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
