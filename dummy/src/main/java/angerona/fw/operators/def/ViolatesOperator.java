package angerona.fw.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.operators.parameter.ViolatesParameter;

/**
 * This class always returns false. (Dummy behavior for testing purposes)
 * @author Tim Janus
 */
public class ViolatesOperator extends BaseViolatesOperator {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ViolatesOperator.class);
	
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		LOG.info("Run Example-ViolatesOperator");
		return new Boolean(false);
	}
}
