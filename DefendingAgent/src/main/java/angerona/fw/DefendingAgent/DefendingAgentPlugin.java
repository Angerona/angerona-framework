package angerona.fw.DefendingAgent;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.Action;
import angerona.fw.AgentComponent;
import angerona.fw.PlanComponent;
import angerona.fw.DefendingAgent.comm.Revision;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.internal.AgentPlugin;
import angerona.fw.logic.Desires;
import angerona.fw.logic.SecrecyKnowledge;

@PluginImplementation
/**
 * The DefendingAgentPlugin adds a ViewComponent to track conditional beliefs
 * held by the attacker as well as a SpeechAct for revision requests.
 *  
 * @author Pia Wierzoch, Sebastian Homann
 */
public class DefendingAgentPlugin implements AgentPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponents() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(SecrecyKnowledge.class);
		reval.add(PlanComponent.class);
		reval.add(Desires.class);
		reval.add(ViewComponent.class);
		reval.add(CensorComponent.class);
		return reval;
	}

	public List<Class<? extends Action>> getActions() {
		List<Class<? extends Action>> reval = new LinkedList<>();
		reval.add(Query.class);
		reval.add(Answer.class);
		reval.add(Revision.class);
//		reval.add(Justify.class);
//		reval.add(Justification.class);
		return reval;
	}
}
