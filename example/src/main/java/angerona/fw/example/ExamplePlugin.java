package angerona.fw.example;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import angerona.fw.AngeronaPluginAdapter;
import angerona.fw.BaseBeliefbase;
import angerona.fw.example.gui.ExampleBeliefbaseComponent;
import angerona.fw.example.logic.ExampleBeliefbase;
import angerona.fw.example.logic.ExampleExpansion;
import angerona.fw.example.logic.ExampleReasoner;
import angerona.fw.example.logic.ExampleTranslator;
import angerona.fw.example.operators.GenerateOptionsOperator;
import angerona.fw.example.operators.IntentionUpdateOperator;
import angerona.fw.example.operators.SubgoalGenerationOperator;
import angerona.fw.example.operators.UpdateBeliefsOperator;
import angerona.fw.example.operators.ViolatesOperator;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.base.ViewComponent;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.operators.BaseOperator;

/**
 * The example plugin acts as example/default plugin for the Angerona framework.
 * The belief bases implementations given by the plugin is very rudimentary it 
 * only saves a set of FOL literals. But it shows a simple implementation of a
 * knowledge representation mechanism. A knowledge representation method is divided
 * into four parts in Angerona. Most importantly the belief base implementation given
 * by extending BaseBeliefbase but also operations on the belief base can be implemented
 * by extending the BaseReasoner, BaseChangeBeliefs, BaseTranslator classes.
 * 
 * The example plugin also implements the secrecy aware BDI agent cycle described in 
 * Krümpelmann et al. 2012. There it uses the GenerateOptionsOperator, 
 * the IntentionUpdateOperator, the SubgoalGenerationOperator, the ViolatesOperator and
 * a default version of the UpdateBeliefsOperator
 * 
 * @todo Move UpdateBeliefsOperator to fw or am.secrecy
 * @author Tim Janus
 */
/**
 * A dummy belief base plugin. It is used as an example and for testing purposes.
 * It has to work with the Angerona framework. And should show how to decouple the
 * knowledge representation from the rest of the framework.
 * @author Tim Janus
 */
@PluginImplementation
public class ExamplePlugin extends AngeronaPluginAdapter
	implements UIPlugin {
	@Override
	public List<Class<? extends BaseBeliefbase>> getBeliefbaseImpl() {
		List<Class<? extends BaseBeliefbase>> reval = new LinkedList<Class<? extends BaseBeliefbase>>();
		reval.add(ExampleBeliefbase.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseReasoner>> getReasonerImpl() {
		List<Class<? extends BaseReasoner>> reval = new LinkedList<Class<? extends BaseReasoner>>();
		reval.add(ExampleReasoner.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getChangeImpl() {
		List<Class<? extends BaseChangeBeliefs>> reval = new LinkedList<Class<? extends BaseChangeBeliefs>>();
		reval.add(ExampleExpansion.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		List<Class<? extends BaseTranslator>> reval = new LinkedList<Class<? extends BaseTranslator>>();
		reval.add(ExampleTranslator.class);
		return reval;
	}
	
	@Override 
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new LinkedList<>();
		reval.add(GenerateOptionsOperator.class);
		reval.add(SubgoalGenerationOperator.class);
		reval.add(IntentionUpdateOperator.class);
		reval.add(ViolatesOperator.class);
		reval.add(UpdateBeliefsOperator.class);
		return reval;
	}
	
	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Example Beliefbase Extension", ExampleBeliefbaseComponent.class);
		return reval;
	}
}
