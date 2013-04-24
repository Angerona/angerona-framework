package angerona.fw.example.operators;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Desire;
import angerona.fw.am.secrecy.operators.BaseGenerateOptionsOperator;
import angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Justification;
import angerona.fw.comm.Justify;
import angerona.fw.comm.Query;
import angerona.fw.internal.IdGenerator;
import angerona.fw.logic.Desires;

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
	
	public static final Predicate prepareScriptingProcessing = new Predicate("scriptingProcessing", 1);
	
	public static final Predicate prepareJustificationReaction = new Predicate("justificationProcessing", 1);
	
	public static final IdGenerator desireIds = new IdGenerator();
	
	@Override
	protected Integer processInternal(GenerateOptionsParameter param) {
		LOG.info("Run Default-Generate-Options-operator");
		
		Atom ad = null;
		Set<Desire> reval = new HashSet<Desire>();
		if(param.getPerception() instanceof Query) {
			ad = new Atom(prepareQueryProcessing);
		} else if(param.getPerception() instanceof Inform) {
			ad = new Atom(prepareRevisionRequestProcessing);
		} else if(param.getPerception() instanceof Justify) {
			ad = new Atom(prepareReasonCalculation);
		} else if(param.getPerception() instanceof Justification) {
			ad = new Atom(prepareJustificationReaction);
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
