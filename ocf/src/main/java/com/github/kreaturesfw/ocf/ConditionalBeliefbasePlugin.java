package com.github.kreaturesfw.ocf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreaturesfw.core.KReaturesPluginAdapter;
import com.github.kreaturesfw.core.bdi.components.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.logic.BaseReasoner;
import com.github.kreaturesfw.core.logic.BaseTranslator;
import com.github.kreaturesfw.gui.UIPlugin;
import com.github.kreaturesfw.gui.base.ViewComponent;
import com.github.kreaturesfw.ocf.gui.OCFMVPComponent;
import com.github.kreaturesfw.ocf.logic.ConditionalBeliefbase;
import com.github.kreaturesfw.ocf.logic.ConditionalExpansion;
import com.github.kreaturesfw.ocf.logic.ConditionalReasoner;
import com.github.kreaturesfw.ocf.logic.ConditionalRevision;
import com.github.kreaturesfw.ocf.logic.ConditionalTranslator;

@PluginImplementation
public class ConditionalBeliefbasePlugin extends KReaturesPluginAdapter implements UIPlugin {

	@Override
	public List<Class<? extends BaseBeliefbase>> getBeliefbaseImpl() {
		List<Class<? extends BaseBeliefbase>> reval = new LinkedList<Class<? extends BaseBeliefbase>>();
		reval.add(ConditionalBeliefbase.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseReasoner>> getReasonerImpl() {
		List<Class<? extends BaseReasoner>> reval = new LinkedList<Class<? extends BaseReasoner>>();
		reval.add(ConditionalReasoner.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getChangeImpl() {
		List<Class<? extends BaseChangeBeliefs>> reval = new LinkedList<Class<? extends BaseChangeBeliefs>>();
		reval.add(ConditionalExpansion.class);
		reval.add(ConditionalRevision.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		List<Class<? extends BaseTranslator>> reval = new LinkedList<Class<? extends BaseTranslator>>();
		reval.add(ConditionalTranslator.class);
		return reval;
	}
	
	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("OCF-Calculator", OCFMVPComponent.class);
		return reval;
	}

}
