package com.github.kreatures.defendingagent.operators.def;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.Desire;
import com.github.kreatures.secrecy.operators.BaseGenerateOptionsOperator;
import com.github.kreatures.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.kreatures.core.comm.Query;
import com.github.kreatures.core.comm.Revision;
import com.github.kreatures.core.comm.Update;
import com.github.kreatures.core.internal.IdGenerator;
import com.github.kreatures.core.logic.Desires;

/**
 * The generate options operator of a defending censor agent expecting either query requests
 * or revision requests.
 * @author Pia Wierzoch, Sebastian Homann
 */
public class GenerateOptionsOperator extends BaseGenerateOptionsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(GenerateOptionsOperator.class);
	
	public static final Predicate prepareQueryProcessing = new Predicate("queryProcessing", 1);
	
	public static final Predicate prepareRevisionProcessing = new Predicate("revisionProcessing", 1);
	
	public static final Predicate prepareUpdateProcessing = new Predicate("updateProcessing", 1);
	
	public static final Predicate prepareScriptingProcessing = new Predicate("scriptingProcessing", 1);
	
	public static final IdGenerator desireIds = new IdGenerator();
	
	@Override
	protected Integer processImpl(GenerateOptionsParameter param) {
		LOG.info("Run Censor-Generate-Options-operator");
		
		FOLAtom ad = null;
		Set<Desire> reval = new HashSet<Desire>();
		if(param.getPerception() instanceof Query) {
			ad = new FOLAtom(prepareQueryProcessing);
		} else if(param.getPerception() instanceof Revision) {
			ad = new FOLAtom(prepareRevisionProcessing);
		}else if(param.getPerception() instanceof Update) {
			ad = new FOLAtom(prepareUpdateProcessing);
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
