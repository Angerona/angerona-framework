package angerona.fw.operators.dummy;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Intention;
import angerona.fw.nacts.NAQuery;
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
	protected List<Intention> processInt(GenerateOptionsParameter param) {
		LOG.info("Generate Options operator");
		
		List<Intention> reval =  new LinkedList<Intention>();
		if(param.getPerception() instanceof NAQuery) {
			reval.add(param.getSkills().get("QueryAnswer"));
		}
		return reval;
	}
}
