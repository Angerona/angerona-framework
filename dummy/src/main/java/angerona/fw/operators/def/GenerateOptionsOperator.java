package angerona.fw.operators.def;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Desire;
import angerona.fw.comm.Query;
import angerona.fw.comm.RevisionRequest;
import angerona.fw.comm.Why;
import angerona.fw.internal.IdGenerator;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.parameter.GenerateOptionsParameter;

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
	
	public static final IdGenerator desireIds = new IdGenerator();
	
	@Override
	protected Integer processInt(GenerateOptionsParameter param) {
		LOG.info("Run Default-Generate-Options-operator");
		
		Atom ad = null;
		Set<Desire> reval = new HashSet<Desire>();
		if(param.getPerception() instanceof Query) {
			ad = new Atom(prepareQueryProcessing);
		} else if(param.getPerception() instanceof RevisionRequest) {
			ad = new Atom(prepareRevisionRequestProcessing);
		} else if(param.getPerception() instanceof Why) {
			ad = new Atom(prepareReasonCalculation);
		}
		if(ad != null) {
			ad.addArgument(new Constant(desireIds.getNextId().toString()));
			reval.add(new Desire(ad, param.getPerception()));
		}
		for(Desire des : reval) {
			param.getAgent().addDesire(des);
		}
		return reval.size();
	}
}
