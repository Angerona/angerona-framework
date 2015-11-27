package com.github.kreaturesfw.asp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.lp.asp.syntax.DLPAtom;
import net.sf.tweety.lp.asp.syntax.DLPLiteral;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreaturesfw.asp.component.AspMetaKnowledge;
import com.github.kreaturesfw.asp.gui.AspBeliefbaseView;
import com.github.kreaturesfw.asp.logic.AspBeliefbase;
import com.github.kreaturesfw.asp.logic.AspCredulousReasoner;
import com.github.kreaturesfw.asp.logic.AspExpansion;
import com.github.kreaturesfw.asp.logic.AspIteratorStrategy;
import com.github.kreaturesfw.asp.logic.AspReasoner;
import com.github.kreaturesfw.asp.logic.AspSkepticalReasoner;
import com.github.kreaturesfw.asp.logic.AspTranslator;
import com.github.kreaturesfw.asp.logic.MatesTranslate;
import com.github.kreaturesfw.asp.logic.MatesUpdateBeliefs;
import com.github.kreaturesfw.asp.logic.RevisionCredibilityPrograms;
import com.github.kreaturesfw.asp.logic.RevisionPreferenceHandling;
import com.github.kreaturesfw.asp.serialize.DLPAtomTransform;
import com.github.kreaturesfw.asp.serialize.DLPLiteralTransform;
import com.github.kreaturesfw.core.AngeronaPluginAdapter;
import com.github.kreaturesfw.core.legacy.AgentComponent;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.legacy.Operator;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.logic.BaseReasoner;
import com.github.kreaturesfw.core.logic.BaseTranslator;
import com.github.kreaturesfw.core.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;
import com.github.kreaturesfw.gui.UIPlugin;
import com.github.kreaturesfw.gui.base.ViewComponent;

/**
 * The ASP plugin implements a belief base plugin which provides an ASP
 * belief base and operators for changing the belief base (like preference
 * handling or expansion). It also provides a reasoning operator and a Translator.
 * The ASP plugin also implements a ui plugin for providing a specialized belief
 * base view for ASP.
 * 
 * @author Tim Janus
 */
@PluginImplementation
public class AspPlugin extends AngeronaPluginAdapter 
	implements UIPlugin {

	@Override
	public void onLoading() {
		addTransformMapping(DLPAtom.class, DLPAtomTransform.class);
		addTransformMapping(DLPLiteral.class, DLPLiteralTransform.class);
	}
	
	@Override
	public List<Class<? extends BaseBeliefbase>> getBeliefbaseImpl() {
		List<Class<? extends BaseBeliefbase>> reval = new LinkedList<Class<? extends BaseBeliefbase>>();
		reval.add(AspBeliefbase.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseReasoner>> getReasonerImpl() {
		List<Class<? extends BaseReasoner>> reval = new LinkedList<Class<? extends BaseReasoner>>();
		reval.add(AspReasoner.class);
		reval.add(AspSkepticalReasoner.class);
		reval.add(AspCredulousReasoner.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getChangeImpl() {
		List<Class<? extends BaseChangeBeliefs>> reval = new LinkedList<Class<? extends BaseChangeBeliefs>>();
		reval.add(RevisionPreferenceHandling.class);
		reval.add(RevisionCredibilityPrograms.class);
		reval.add(AspExpansion.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		List<Class<? extends BaseTranslator>> reval = new LinkedList<Class<? extends BaseTranslator>>();
		reval.add(AspTranslator.class);
		reval.add(MatesTranslate.class);
		return reval;
	}


	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("todo", AspBeliefbaseView.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends ContinuousBeliefOperatorFamilyIteratorStrategy>> getBeliefOperatorFamilyIteratorStrategies() {
		List<Class<? extends ContinuousBeliefOperatorFamilyIteratorStrategy>> reval = new LinkedList<>();
		reval.add(AspIteratorStrategy.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends Operator>> getOperators() {
		List<Class<? extends Operator>> reval = new ArrayList<>();
		reval.add(MatesUpdateBeliefs.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new ArrayList<>();
		reval.add(AspMetaKnowledge.class);
		return reval;
	}
}
