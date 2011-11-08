package angerona.fw.operators.dummy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Intention;
import angerona.fw.Skill;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.IntentionUpdateParameter;

/**
 * dummy filter operator: only text output to the console.
 * @author Tim Janus
 */
public class DummyIntentionUpdateOperator extends BaseIntentionUpdateOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DummyIntentionUpdateOperator.class);
	
	@Override
	public Intention process(IntentionUpdateParameter param) {
		LOG.info("Intention-Update-Operator");
		for(int i=0; i<param.getPlan().getNumberOfStacks(); ++i) {
			if(param.getPlan().peekStack(i).isAtomic()) {
				Intention intention = param.getPlan().peekStack(i);
				intention.setRealRun(false);
				intention.run();
				Skill sk = (Skill)intention;
				if(!sk.violates())
					return intention;
			}
		}
		return null;
	}

}
