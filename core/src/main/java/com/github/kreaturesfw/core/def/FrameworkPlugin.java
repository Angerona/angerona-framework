package com.github.kreaturesfw.core.def;

import java.util.LinkedList;
import java.util.List;

import com.github.kreaturesfw.core.AngeronaPluginAdapter;
import com.github.kreaturesfw.core.legacy.ActionHistory;
import com.github.kreaturesfw.core.legacy.AgentComponent;
import com.github.kreaturesfw.core.legacy.EnvironmentBehavior;
import com.github.kreaturesfw.core.legacy.PlanComponent;
import com.github.kreaturesfw.core.logic.Desires;
import com.github.kreaturesfw.core.logic.ScriptingComponent;
import com.github.kreaturesfw.core.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;
import com.github.kreaturesfw.core.operators.StepIteratorStrategy;
import com.github.kreaturesfw.core.reflection.BooleanExpression;
import com.github.kreaturesfw.core.reflection.Condition;
import com.github.kreaturesfw.core.reflection.FolFormulaVariable;
import com.github.kreaturesfw.core.reflection.Value;
import com.github.kreaturesfw.core.serialize.transform.ConditionTransform;
import com.github.kreaturesfw.core.serialize.transform.FolAtomTransform;
import com.github.kreaturesfw.core.serialize.transform.FolFormulaTransform;
import com.github.kreaturesfw.core.serialize.transform.ValueTransform;
import com.github.kreaturesfw.core.serialize.transform.VariableTransform;

import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * The default agent plug-in for the Angerona framework defines
 * the confidential Knowledge and is part of the main framework.
 *  
 * @author Tim Janus
 */
@PluginImplementation
public class FrameworkPlugin extends AngeronaPluginAdapter {

	public static class FOLVariableTransform extends VariableTransform<FolFormulaVariable> {
		@Override
		protected Class<FolFormulaVariable> getCls() {
			return FolFormulaVariable.class;
		}
	}
	
	@Override
	public void onLoading() {
		addTransformMapping(FolFormula.class, FolFormulaTransform.class);
		addTransformMapping(FOLAtom.class, FolAtomTransform.class);
		addTransformMapping(FolFormulaVariable.class, FOLVariableTransform.class);
		addTransformMapping(Value.class, ValueTransform.class);
		addTransformMapping(BooleanExpression.class, ConditionTransform.class);
		addTransformMapping(Condition.class, ConditionTransform.class);
	}
	
	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(PlanComponent.class);
		reval.add(Desires.class);
		reval.add(ScriptingComponent.class);
		reval.add(ActionHistory.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors() {
		List<Class<? extends EnvironmentBehavior>> reval = new LinkedList<>();
		reval.add(DefaultBehavior.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends ContinuousBeliefOperatorFamilyIteratorStrategy>> getBeliefOperatorFamilyIteratorStrategies() {
		List<Class<? extends ContinuousBeliefOperatorFamilyIteratorStrategy>> reval = new LinkedList<>();
		reval.add(StepIteratorStrategy.class);
		return reval;
	}
}
