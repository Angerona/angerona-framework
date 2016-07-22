package com.github.angerona.fw.bargainingAgent.operators;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.am.secrecy.operators.BaseGenerateOptionsOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.angerona.fw.bargainingAgent.comm.Offer;
import com.github.angerona.fw.internal.IdGenerator;
import com.github.angerona.fw.logic.Desires;

/**
 * The generate options operator for the bargaining agent
 * @author Pia Wierzoch
 */
public class GenerateOptionsOperator extends BaseGenerateOptionsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(GenerateOptionsOperator.class);
	
	public static final Predicate prepareCounterOfferProcessing = new Predicate("counterOfferProcessing", 1);
	
	public static final Predicate prepareFirstOfferProcessing = new Predicate("firstOfferProcessing", 1);
	
	public static final IdGenerator desireIds = new IdGenerator();
	
	@Override
	protected Integer processImpl(GenerateOptionsParameter param) {
		LOG.info("Run Bargaining-Generate-Options-operator");
		
		FOLAtom ad = null;
		Set<Desire> reval = new HashSet<Desire>();
		if(param.getPerception() instanceof Offer) {
			ad = new FOLAtom(prepareCounterOfferProcessing);
		}
		if(param.getPerception() == null){
			ad = new FOLAtom(prepareFirstOfferProcessing);
		}
		if(ad != null) {
			ad.addArgument(new Constant(desireIds.getNextId().toString()));
			reval.add(new Desire(ad, param.getPerception()));
		}
		for(Desire des : reval) {
			param.getAgent().getComponent(Desires.class).add(des);
		}
		return reval.size();
	}
}
