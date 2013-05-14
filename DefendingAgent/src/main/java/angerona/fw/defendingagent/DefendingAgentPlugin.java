package angerona.fw.defendingagent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.AngeronaPluginAdapter;
import angerona.fw.defendingagent.gui.ViewView;
import angerona.fw.defendingagent.operators.def.GenerateOptionsOperator;
import angerona.fw.defendingagent.operators.def.SubgoalGenerationOperator;
import angerona.fw.defendingagent.operators.def.UpdateBeliefsOperator;
import angerona.fw.gui.UIPlugin;
import angerona.fw.operators.BaseOperator;

@PluginImplementation
/**
 * The DefendingAgentPlugin adds a ViewComponent to track conditional beliefs
 * held by the attacker as well as a SpeechAct for revision requests.
 *  
 * @author Pia Wierzoch, Sebastian Homann
 */
public class DefendingAgentPlugin extends AngeronaPluginAdapter implements UIPlugin {

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new LinkedList<>();
		reval.add(GenerateOptionsOperator.class);
		reval.add(UpdateBeliefsOperator.class);
		reval.add(SubgoalGenerationOperator.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();

		reval.add(CensorComponent.class);
		reval.add(ViewComponent.class);
		return reval;
	}

	@Override
	public Map<String, Class<? extends angerona.fw.gui.base.ViewComponent>> getUIComponents() {
		Map<String, Class<? extends angerona.fw.gui.base.ViewComponent>> reval = new HashMap<String, Class<? extends angerona.fw.gui.base.ViewComponent>>();
		reval.put("Views", ViewView.class);
		return reval;
	}

}
