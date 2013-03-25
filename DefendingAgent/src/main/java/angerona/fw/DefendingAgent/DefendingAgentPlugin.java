package angerona.fw.DefendingAgent;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.AngeronaPluginAdapter;
import angerona.fw.DefendingAgent.operators.def.GenerateOptionsOperator;
import angerona.fw.DefendingAgent.operators.def.SubgoalGenerationOperator;
import angerona.fw.DefendingAgent.operators.def.UpdateBeliefsOperator;
import angerona.fw.operators.BaseOperator;

@PluginImplementation
/**
 * The DefendingAgentPlugin adds a ViewComponent to track conditional beliefs
 * held by the attacker as well as a SpeechAct for revision requests.
 *  
 * @author Pia Wierzoch, Sebastian Homann
 */
public class DefendingAgentPlugin extends AngeronaPluginAdapter {

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
		
		// TJ: the following three class description are already in FrameworkPlugin or SecrecyPlugin
		/*
		reval.add(SecrecyKnowledge.class);
		reval.add(PlanComponent.class);
		reval.add(Desires.class);
		*/
		reval.add(ViewComponent.class);
		reval.add(CensorComponent.class);
		return reval;
	}

	/* TJ: DEPRECATED this method is removed now 
	public List<Class<? extends Action>> getActions() {
		List<Class<? extends Action>> reval = new LinkedList<>();
		reval.add(Query.class);
		reval.add(Answer.class);
		reval.add(Revision.class);
//		reval.add(Justify.class);
//		reval.add(Justification.class);
		return reval;
	}
	*/
}
