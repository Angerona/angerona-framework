package com.github.angerona.fw.def;

import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.angerona.fw.ActionHistory;
import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.EnvironmentBehavior;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.logic.ScriptingComponent;
import com.github.angerona.fw.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;
import com.github.angerona.fw.operators.StepIteratorStrategy;
import com.github.angerona.fw.reflection.BooleanExpression;
import com.github.angerona.fw.reflection.Condition;
import com.github.angerona.fw.reflection.FolFormulaVariable;
import com.github.angerona.fw.reflection.Value;
import com.github.angerona.fw.serialize.transform.ConditionTransform;
import com.github.angerona.fw.serialize.transform.FolAtomTransform;
import com.github.angerona.fw.serialize.transform.FolFormulaTransform;
import com.github.angerona.fw.serialize.transform.ValueTransform;
import com.github.angerona.fw.serialize.transform.VariableTransform;

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
