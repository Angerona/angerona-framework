package angerona.fw.dummy;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.internal.OperatorPlugin;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.operators.def.GenerateOptionsOperator;
import angerona.fw.operators.def.IntentionUpdateOperator;
import angerona.fw.operators.def.SubgoalGenerationOperator;
import angerona.fw.operators.def.UpdateBeliefsOperator;
import angerona.fw.operators.def.ViolatesOperator;

/**
 * The dummy operator plugin is an example/test plugin for the Angerona framework.
 * The classes provided by this plugin only implement some dummy functionality to test
 * the program flow of the Angerona framework.
 * @author Tim Janus
 */
@PluginImplementation
public class DummyOperatorPlugin implements OperatorPlugin {

	@Override
	public List<Class<? extends BaseUpdateBeliefsOperator>> getSupportedChangeOperators() {
		List<Class<? extends BaseUpdateBeliefsOperator>> reval = new LinkedList<Class<? extends BaseUpdateBeliefsOperator>>();
		reval.add(UpdateBeliefsOperator.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseIntentionUpdateOperator>> getSupportedFilterOperators() {
		List<Class<? extends BaseIntentionUpdateOperator>> reval = new LinkedList<Class<? extends BaseIntentionUpdateOperator>>();
		reval.add(IntentionUpdateOperator.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseGenerateOptionsOperator>> getSupportedGenerateOptionsOperators() {
		List<Class<? extends BaseGenerateOptionsOperator>> reval = new LinkedList<Class<? extends BaseGenerateOptionsOperator>>();
		reval.add(GenerateOptionsOperator.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseViolatesOperator>> getSupportedViolatesOperators() {
		List<Class<? extends BaseViolatesOperator>> reval = new LinkedList<Class<? extends BaseViolatesOperator>>();
		reval.add(ViolatesOperator.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseSubgoalGenerationOperator>> getSupportedPlaners() {
		List<Class<? extends BaseSubgoalGenerationOperator>> reval = new LinkedList<Class<? extends BaseSubgoalGenerationOperator>>();
		reval.add(SubgoalGenerationOperator.class);
		return reval;
	}

}
