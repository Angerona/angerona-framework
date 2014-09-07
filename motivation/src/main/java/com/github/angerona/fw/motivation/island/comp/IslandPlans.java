package com.github.angerona.fw.motivation.island.comp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.impl.GenTrailBasedPlans;
import com.github.angerona.fw.motivation.island.enums.ActionId;
import com.github.angerona.fw.motivation.plan.StateNode;

public class IslandPlans extends GenTrailBasedPlans<ActionId, FolFormula> {

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
		return null;
	}

	@Override
	public Collection<StateNode<ActionId, FolFormula>> getPlan(Desire d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FolFormula getRelCondition(Desire d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FolFormula getRelAlternatives(Desire d) {
		// TODO Auto-generated method stub
		return null;
	}

}
