package angerona.fw.def;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math.ode.sampling.StepInterpolator;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.ActionHistory;
import angerona.fw.AgentComponent;
import angerona.fw.AngeronaPluginAdapter;
import angerona.fw.EnvironmentBehavior;
import angerona.fw.PlanComponent;
import angerona.fw.logic.Desires;
import angerona.fw.logic.ScriptingComponent;
import angerona.fw.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;
import angerona.fw.operators.StepIteratorStrategy;

/**
 * The default agent plugin for the Angerona framework defines
 * the confidential Knowledge and is part of the main framework.
 *  
 * @author Tim Janus
 */
@PluginImplementation
public class FrameworkPlugin extends AngeronaPluginAdapter {

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
