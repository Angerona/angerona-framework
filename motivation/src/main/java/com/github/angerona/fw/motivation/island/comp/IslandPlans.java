package com.github.angerona.fw.motivation.island.comp;

import java.io.IOException;
import java.io.InputStream;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.dao.impl.GenTrailBasedPlans;
import com.github.angerona.fw.motivation.island.enums.ActionId;

public class IslandPlans extends GenTrailBasedPlans<ActionId, FolFormula> {

	public static final String EXT = ".pln";

	@Override
	protected GenTrailBasedPlans<ActionId, FolFormula> create() {
		return new IslandPlans();
	}

	@Override
	public void loadFromStream(InputStream src) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public String getFileSuffix() {
		// TODO Auto-generated method stub
		return EXT;
	}

}
