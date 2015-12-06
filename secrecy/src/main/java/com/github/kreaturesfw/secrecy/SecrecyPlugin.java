package com.github.kreaturesfw.secrecy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.kreaturesfw.core.KReaturesPluginAdapter;
import com.github.kreaturesfw.core.basic.AgentComponent;
import com.github.kreaturesfw.core.basic.Operator;
import com.github.kreaturesfw.core.bdi.components.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.logic.BaseReasoner;
import com.github.kreaturesfw.core.logic.BaseTranslator;
import com.github.kreaturesfw.core.operators.UpdateBeliefsOperator;
import com.github.kreaturesfw.gui.UIPlugin;
import com.github.kreaturesfw.gui.base.ViewComponent;
import com.github.kreaturesfw.secrecy.components.SecrecyKnowledge;
import com.github.kreaturesfw.secrecy.example.components.CommunicationHistory;
import com.github.kreaturesfw.secrecy.example.components.MatesAttackerModelling;
import com.github.kreaturesfw.secrecy.example.gui.ExampleBeliefbaseComponent;
import com.github.kreaturesfw.secrecy.example.logic.ExampleBeliefbase;
import com.github.kreaturesfw.secrecy.example.logic.ExampleExpansion;
import com.github.kreaturesfw.secrecy.example.logic.ExampleReasoner;
import com.github.kreaturesfw.secrecy.example.logic.ExampleTranslator;
import com.github.kreaturesfw.secrecy.example.operators.CautiousUpdateBeliefs;
import com.github.kreaturesfw.secrecy.example.operators.GenerateOptionsOperator;
import com.github.kreaturesfw.secrecy.example.operators.IntentionUpdateOperator;
import com.github.kreaturesfw.secrecy.example.operators.SubgoalGenerationOperator;
import com.github.kreaturesfw.secrecy.example.operators.ViolatesOperator;
import com.github.kreaturesfw.secrecy.example.operators.courtroom.CourtroomIntentionUpdate;
import com.github.kreaturesfw.secrecy.example.operators.courtroom.CourtroomPlanner;
import com.github.kreaturesfw.secrecy.example.operators.courtroom.CourtroomViolates;
import com.github.kreaturesfw.secrecy.example.operators.mates.MatesPlanner;
import com.github.kreaturesfw.secrecy.example.operators.scm.StrikeCommitteePlanner;
import com.github.kreaturesfw.secrecy.gui.SecrecyView;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * Provides AgentComponent implementations like the SecrecyKnowledge and a UI View
 * for the SecrecyKnowledge
 * 
 * The example plug-in acts as example/default plug-in for the secrecy agent model in the Angerona framework.
 * The belief bases implementations given by the plug-in is very rudimentary it 
 * only saves a set of FOL literals. But it shows a simple implementation of a
 * knowledge representation mechanism. A knowledge representation method is divided
 * into four parts in Angerona. Most importantly the belief base implementation given
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
public class SecrecyPlugin extends KReaturesPluginAdapter
	implements 
	UIPlugin {
	
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
	public List<Class<? extends Operator>> getOperators() {
		List<Class<? extends Operator>> reval = new ArrayList<>();
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
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<>();
		reval.add(SecrecyKnowledge.class);
		reval.add(CommunicationHistory.class);
		reval.add(MatesAttackerModelling.class);
		return reval;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Confidential-Knowledge", SecrecyView.class);
		reval.put("Example Beliefbase Extension", ExampleBeliefbaseComponent.class);
		return reval;
	}

}
