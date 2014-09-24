package com.github.angerona.fw.motivation.island.operators;

import java.util.Collection;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.dao.ActionComponentDao;
import com.github.angerona.fw.motivation.dao.CouplingDao;
import com.github.angerona.fw.motivation.dao.PlanComponentDao;
import com.github.angerona.fw.motivation.dao.PlanParam;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.PlanParamImpl;
import com.github.angerona.fw.motivation.functional.PlanCalculator;
import com.github.angerona.fw.motivation.functional.ReliabilityChecker;
import com.github.angerona.fw.motivation.functional.impl.PlanCalculatorImpl;
import com.github.angerona.fw.motivation.functional.impl.ReliabilityCheckerImpl;
import com.github.angerona.fw.motivation.island.comp.Area;
import com.github.angerona.fw.motivation.island.comp.IslandActions;
import com.github.angerona.fw.motivation.island.comp.TrailBasedPlans;
import com.github.angerona.fw.motivation.island.enums.ActionId;
import com.github.angerona.fw.motivation.operators.GenMotOperatorParameter;
import com.github.angerona.fw.motivation.operators.MotOperatorParameter;
import com.github.angerona.fw.motivation.operators.MotivationOperator;
import com.github.angerona.fw.motivation.plan.StateNode;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandMotOperator extends MotivationOperator {

	private PlanCalculator<ActionId> calculator = new PlanCalculatorImpl();

	@Override
	protected Integer processImpl(GenerateOptionsParameter param) {
		ActionComponentDao<ActionId> actions = param.getAgent().getComponent(IslandActions.class);
		PlanComponentDao plans = param.getAgent().getComponent(TrailBasedPlans.class);
		CouplingDao<Maslow, FolFormula> couplings = param.getAgent().getComponent(MotiveCouplings.class);
		Area area = param.getAgent().getComponent(Area.class);

		Collection<StateNode> nodes;
		PlanParam pp;

		for (Desire d : couplings.getDesires()) {
			// prepare parameter
			nodes = plans.getPlan(d);
			pp = new PlanParamImpl(nodes, area.getLocation(), area.getWeather(), param.getAgent().getBeliefs());

			// calculate sequence for desire d
			actions.put(d, calculator.calc(pp));
		}

		return super.processImpl(param);
	}

	@Override
	protected GenMotOperatorParameter<Maslow, FolFormula> getEmptyParameter() {
		return new MotOperatorParameter() {

			@Override
			protected ReliabilityChecker checker() {
				return new ReliabilityCheckerImpl(getAgent());
			}

		};
	}

}
