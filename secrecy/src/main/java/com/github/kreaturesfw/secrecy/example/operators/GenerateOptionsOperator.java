package com.github.kreaturesfw.secrecy.example.operators;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.bdi.operators.BaseGenerateOptionsOperator;
import com.github.kreaturesfw.core.bdi.operators.parameter.GenerateOptionsParameter;
import com.github.kreaturesfw.core.comm.Inform;
import com.github.kreaturesfw.core.comm.Justification;
import com.github.kreaturesfw.core.comm.Justify;
import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.internal.IdGenerator;
import com.github.kreaturesfw.core.legacy.Desire;
import com.github.kreaturesfw.core.logic.Desires;

import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;

/**
 * The default generate options operator. It generates desires to answer to
 * queries, revision-requests and Why questions. It can be used as base class
 * for more specific option generator which want to use the speech acts of Angerona.
 * @author Tim Janus
 */
public class GenerateOptionsOperator extends BaseGenerateOptionsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(GenerateOptionsOperator.class);
	
	public static final Predicate prepareQueryProcessing = new Predicate("queryProcessing", 1);
	
	public static final Predicate prepareRevisionRequestProcessing = new Predicate("revisionRequestProcessing", 1);
	
	public static final Predicate prepareReasonCalculation = new Predicate("reasonProcessing", 1);
	
	public static final Predicate prepareScriptingProcessing = new Predicate("scriptingProcessing", 0);
	
	public static final Predicate prepareJustificationReaction = new Predicate("justificationProcessing", 1);
	
	public static final IdGenerator desireIds = new IdGenerator();
	
	@Override
	protected Integer processImpl(GenerateOptionsParameter param) {
		LOG.info("Run Default-Generate-Options-operator");
		
		FOLAtom ad = null;
		Set<Desire> reval = new HashSet<Desire>();
		if(param.getPerception() instanceof Query) {
			ad = new FOLAtom(prepareQueryProcessing);
		} else if(param.getPerception() instanceof Inform) {
			ad = new FOLAtom(prepareRevisionRequestProcessing);
		} else if(param.getPerception() instanceof Justify) {
			ad = new FOLAtom(prepareReasonCalculation);
		} else if(param.getPerception() instanceof Justification) {
			ad = new FOLAtom(prepareJustificationReaction);
		}
		if(ad != null) {
			ad.addArgument(new NumberTerm(desireIds.getNextId().intValue()));
			reval.add(new Desire(ad, param.getPerception()));
		}
		for(Desire des : reval) {
			param.getAgent().getComponent(Desires.class).add(des);
		}
		return reval.size();
	}
}
