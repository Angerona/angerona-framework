package angerona.fw.operators.dummy;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.comm.Query;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.parameter.GenerateOptionsParameter;

/**
 * dummy generate options generator: can only generate the Answer option.
 * @author Tim Janus
 */
public class DummyGenerateOptionsOperator extends BaseGenerateOptionsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DummyGenerateOptionsOperator.class);
	
	@Override
	protected Set<Formula> processInt(GenerateOptionsParameter param) {
		LOG.info("Run Example-Generate-Options-operator");
		
		Set<Formula> reval = new HashSet<Formula>();
		if(param.getPerception() instanceof Query) {
			reval.add(new Atom(new Predicate("wantsToAnswer")));
		}
		return reval;
	}
}
