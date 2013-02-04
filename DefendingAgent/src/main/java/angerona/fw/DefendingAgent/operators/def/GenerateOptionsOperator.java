package angerona.fw.DefendingAgent.operators.def;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Desire;
import angerona.fw.DefendingAgent.comm.Revision;
import angerona.fw.am.secrecy.operators.BaseGenerateOptionsOperator;
import angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Justification;
import angerona.fw.comm.Justify;
import angerona.fw.comm.Query;
import angerona.fw.internal.IdGenerator;

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
	
	public static final IdGenerator desireIds = new IdGenerator();
	
	@Override
	protected Integer processInternal(GenerateOptionsParameter param) {
		LOG.info("Run Censor-Generate-Options-operator");
		
		Atom ad = null;
		Set<Desire> reval = new HashSet<Desire>();
		if(param.getPerception() instanceof Query) {
			ad = new Atom(prepareQueryProcessing);
		} else if(param.getPerception() instanceof Revision) {
			ad = new Atom(prepareRevisionProcessing);
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
