package com.github.kreatures.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreatures.core.AgentComponent;
import com.github.kreatures.core.KReaturesPluginAdapter;
import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.example.components.CommunicationHistory;
import com.github.kreatures.example.components.MatesAttackerModelling;
import com.github.kreatures.example.gui.ExampleBeliefbaseComponent;
import com.github.kreatures.example.logic.ExampleBeliefbase;
import com.github.kreatures.example.logic.ExampleExpansion;
import com.github.kreatures.example.logic.ExampleReasoner;
import com.github.kreatures.example.logic.ExampleTranslator;
import com.github.kreatures.example.operators.CautiousUpdateBeliefs;
import com.github.kreatures.example.operators.GenerateOptionsOperator;
import com.github.kreatures.example.operators.IntentionUpdateOperator;
import com.github.kreatures.example.operators.SubgoalGenerationOperator;
import com.github.kreatures.example.operators.ViolatesOperator;
import com.github.kreatures.example.operators.courtroom.CourtroomIntentionUpdate;
import com.github.kreatures.example.operators.courtroom.CourtroomPlanner;
import com.github.kreatures.example.operators.courtroom.CourtroomViolates;
import com.github.kreatures.example.operators.mates.MatesPlanner;
import com.github.kreatures.example.operators.scm.StrikeCommitteePlanner;
import com.github.kreatures.gui.UIPlugin;
import com.github.kreatures.gui.base.ViewComponent;
import com.github.kreatures.core.logic.BaseChangeBeliefs;
import com.github.kreatures.core.logic.BaseReasoner;
import com.github.kreatures.core.logic.BaseTranslator;
import com.github.kreatures.core.operators.BaseOperator;
import com.github.kreatures.core.operators.UpdateBeliefsOperator;

/**
 * The example plug-in acts as example/default plug-in for the secrecy agent model in the KReatures framework.
 * The belief bases implementations given by the plug-in is very rudimentary it 
 * only saves a set of FOL literals. But it shows a simple implementation of a
 * knowledge representation mechanism. A knowledge representation method is divided
 * into four parts in KReatures. Most importantly the belief base implementation given
 * by extending BaseBeliefbase but also operations on the belief base can be implemented
 * by extending the BaseReasoner, BaseChangeBeliefs, BaseTranslator classes.
 * 
 * The example plug-in also implements the secrecy aware BDI agent cycle described in 
 * Krümpelmann et al. 2012. There it uses the GenerateOptionsOperator, 
 * the IntentionUpdateOperator, the SubgoalGenerationOperator, the ViolatesOperator and
 * a default version of the UpdateBeliefsOperator
 * 
 * @author Tim Janus
 */
@PluginImplementation
public class ExamplePlugin extends KReaturesPluginAdapter
	implements UIPlugin {
	@Override
	public List<Class<? extends BaseBeliefbase>> getBeliefbaseImpl() {
		List<Class<? extends BaseBeliefbase>> reval = new ArrayList<Class<? extends BaseBeliefbase>>();
		reval.add(ExampleBeliefbase.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseReasoner>> getReasonerImpl() {
		List<Class<? extends BaseReasoner>> reval = new ArrayList<Class<? extends BaseReasoner>>();
		reval.add(ExampleReasoner.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getChangeImpl() {
		List<Class<? extends BaseChangeBeliefs>> reval = new ArrayList<Class<? extends BaseChangeBeliefs>>();
		reval.add(ExampleExpansion.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		List<Class<? extends BaseTranslator>> reval = new ArrayList<Class<? extends BaseTranslator>>();
		reval.add(ExampleTranslator.class);
		return reval;
	}
	
	@Override 
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new ArrayList<>();
		// general basis operators:
		reval.add(GenerateOptionsOperator.class);
		reval.add(SubgoalGenerationOperator.class);
		reval.add(IntentionUpdateOperator.class);
		reval.add(ViolatesOperator.class);
		reval.add(UpdateBeliefsOperator.class);
		reval.add(CautiousUpdateBeliefs.class);
		
		// courtroom scenario operators:
		reval.add(CourtroomPlanner.class);
		reval.add(CourtroomViolates.class);
		reval.add(CourtroomIntentionUpdate.class);
		
		// strike committee meeting operators:
		reval.add(StrikeCommitteePlanner.class);
		
		// mates
		reval.add(MatesPlanner.class);
		
		return reval;
	}
	
	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Example Beliefbase Extension", ExampleBeliefbaseComponent.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new ArrayList<>();
		reval.add(CommunicationHistory.class);
		reval.add(MatesAttackerModelling.class);
		return reval;
	}
}
