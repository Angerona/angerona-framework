package com.github.angerona.fw.asp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.lp.asp.syntax.DLPAtom;
import net.sf.tweety.lp.asp.syntax.DLPLiteral;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.asp.component.AspMetaKnowledge;
import com.github.angerona.fw.gui.UIPlugin;
import com.github.angerona.fw.gui.asp.AspBeliefbaseView;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.logic.BaseReasoner;
import com.github.angerona.fw.logic.BaseTranslator;
import com.github.angerona.fw.logic.asp.AspBeliefbase;
import com.github.angerona.fw.logic.asp.AspCredulousReasoner;
import com.github.angerona.fw.logic.asp.AspExpansion;
import com.github.angerona.fw.logic.asp.AspIteratorStrategy;
import com.github.angerona.fw.logic.asp.AspReasoner;
import com.github.angerona.fw.logic.asp.AspSkepticalReasoner;
import com.github.angerona.fw.logic.asp.AspTranslator;
import com.github.angerona.fw.logic.asp.MatesTranslate;
import com.github.angerona.fw.logic.asp.MatesUpdateBeliefs;
import com.github.angerona.fw.logic.asp.RevisionBaseRevision;
import com.github.angerona.fw.logic.asp.RevisionConfidentSelectiveLiteral;
import com.github.angerona.fw.logic.asp.RevisionCredibilityPrograms;
import com.github.angerona.fw.logic.asp.RevisionCredibilityProgramsKeepRules;
import com.github.angerona.fw.logic.asp.RevisionPreferenceHandling;
import com.github.angerona.fw.operators.BaseOperator;
import com.github.angerona.fw.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;
import com.github.angerona.serialize.asp.DLPAtomTransform;
import com.github.angerona.serialize.asp.DLPLiteralTransform;

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
		reval.add(RevisionBaseRevision.class);
		reval.add(RevisionCredibilityProgramsKeepRules.class);
		reval.add(RevisionConfidentSelectiveLiteral.class);
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
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new ArrayList<>();
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
