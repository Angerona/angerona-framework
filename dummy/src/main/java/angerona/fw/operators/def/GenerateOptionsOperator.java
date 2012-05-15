package angerona.fw.operators.def;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Desire;
import angerona.fw.comm.Query;
import angerona.fw.comm.RevisionRequest;
import angerona.fw.comm.Why;
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
	
	public static final Predicate prepareQueryProcessing = new Predicate("queryProcessing");
	
	public static final Predicate prepareRevisionRequestProcessing = new Predicate("revisionRequestProcessing");
	
	public static final Predicate prepareReasonCalculation = new Predicate("reasonProcessing");
	
	@Override
	protected Set<Desire> processInt(GenerateOptionsParameter param) {
		LOG.info("Run Default-Generate-Options-operator");
		
		Set<Desire> reval = new HashSet<Desire>();
		if(param.getPerception() instanceof Query) {
			reval.add(new Desire(new Atom(prepareQueryProcessing)));
		} else if(param.getPerception() instanceof RevisionRequest) {
			reval.add(new Desire(new Atom(prepareRevisionRequestProcessing)));
		} else if(param.getPerception() instanceof Why) {
			reval.add(new Desire(new Atom(prepareReasonCalculation)));
		}
		return reval;
	}
}
