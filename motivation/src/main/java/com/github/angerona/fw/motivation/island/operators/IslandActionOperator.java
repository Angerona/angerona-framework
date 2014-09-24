package com.github.angerona.fw.motivation.island.operators;

import java.util.Set;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.motivation.dao.ActionComponentDao;
import com.github.angerona.fw.motivation.island.IslandAction;
import com.github.angerona.fw.motivation.island.comp.IslandActions;
import com.github.angerona.fw.motivation.island.enums.ActionId;
import com.github.angerona.fw.motivation.operators.ActionSelectionParameter;
import com.github.angerona.fw.motivation.operators.BaseActionSelectionOperator;
import com.github.angerona.fw.motivation.plan.ActionSequence;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandActionOperator extends BaseActionSelectionOperator {

	@Override
	protected Void processImpl(ActionSelectionParameter param) {
		ActionComponentDao<ActionId> actions = param.getAgent().getComponent(IslandActions.class);
		Set<Desire> desires = param.getAgent().getComponent(Desires.class).getDesires();
		Desire selected = null;

		if (desires.size() == 1) {
			selected = desires.iterator().next();
		}

		if (selected != null) {
			ActionSequence<ActionId> seq = actions.get(selected);

			if (seq != null) {
				if (seq.getDuration() > 0) {
					param.getAgent().getEnvironment().sendAction(new IslandAction(param.getAgent(), seq.iterator().next()));
				} else {
					param.getAgent().report("agent decided to wait");
				}
			} else {
				param.getAgent().report("no action available");
			}
		} else {
			param.getAgent().report("no desire available");
		}

		return null;
	}

}
