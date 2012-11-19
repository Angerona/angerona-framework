package angerona.fw.def;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.Action;
import angerona.fw.AgentComponent;
import angerona.fw.PlanComponent;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Justification;
import angerona.fw.comm.Justify;
import angerona.fw.comm.Query;
import angerona.fw.internal.AgentPlugin;
import angerona.fw.logic.SecrecyKnowledge;
import angerona.fw.logic.Desires;

@PluginImplementation
/**
 * The default agent plugin for the Angerona framework defines
 * the confidential Knowledge and is part of the main-framework.
 *  
 * @author Tim Janus
 */
public class DefaultAgentPlugin implements AgentPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponents() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(SecrecyKnowledge.class);
		reval.add(PlanComponent.class);
		reval.add(Desires.class);
		return reval;
	}

	public List<Class<? extends Action>> getActions() {
		List<Class<? extends Action>> reval = new LinkedList<>();
		reval.add(Inform.class);
		reval.add(Query.class);
		reval.add(Answer.class);
		reval.add(Justify.class);
		reval.add(Justification.class);
		return reval;
	}
}
