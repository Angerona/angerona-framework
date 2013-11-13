package com.github.angerona.fw.logic.asp;

import net.sf.tweety.lp.asp.syntax.Program;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.operators.parameter.ChangeBeliefbaseParameter;

/**
 * Simply adds the new rule to the belief base, can make it inconsistent and
 * so on.
 * @author Tim Janus
 */
public class AspExpansion extends BaseChangeBeliefs {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspExpansion.class);
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInternal(
			ChangeBeliefbaseParameter preprocessedParameters) {
		LOG.info("Perform ASPExpansion as change.");
		AspBeliefbase abb = (AspBeliefbase)preprocessedParameters.getSourceBeliefBase();
		Program p = abb.getProgram();
		AspBeliefbase newK = (AspBeliefbase)preprocessedParameters.getNewKnowledge();
		p.add(newK.getProgram());
		return abb;
	}

}
