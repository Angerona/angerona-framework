package com.github.angerona.fw.motivation.island.comp;

import java.io.IOException;
import java.io.InputStream;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.dao.impl.GenTrailBasedPlans;
import com.github.angerona.fw.motivation.island.enums.ActionId;
import com.github.angerona.fw.motivation.parser.MotivationParser;
import com.github.angerona.fw.motivation.parser.ParseException;

public class IslandPlans extends GenTrailBasedPlans<FolFormula> {

	public static final String EXT = ".pln";

	@Override
	protected GenTrailBasedPlans<FolFormula> create() {
		return new IslandPlans();
	}

	@Override
	public void loadFromStream(InputStream src) throws IOException {
		try {
			PlanParser parser = new PlanParser(src);
			this.trails = parser.gatherPlans();
			report("loaded weight-ranges from file");
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getFileSuffix() {
		return EXT;
	}

}
