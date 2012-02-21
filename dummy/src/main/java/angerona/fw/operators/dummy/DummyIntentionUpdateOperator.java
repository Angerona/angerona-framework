package angerona.fw.operators.dummy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Intention;
import angerona.fw.Skill;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.IntentionUpdateParameter;

/**
 * 
 * @author Tim Janus
 */
public class DummyIntentionUpdateOperator extends BaseIntentionUpdateOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DummyIntentionUpdateOperator.class);
	
	@Override
	protected Intention processInt(IntentionUpdateParameter param) {
		LOG.info("Run Example-Intention-Update");
		
		for(int i=0; i<param.getPlan().getNumberOfStacks(); ++i) {
			if(param.getPlan().peekStack(i).isAtomic()) {
				Intention intention = param.getPlan().peekStack(i);
				intention.setRealRun(false);
				report("Performing dry-run of atomic-action: '"+intention+"'");
				intention.run();
				Skill sk = (Skill)intention;
				if(!sk.violates()) {
					report("Next atomic step selected: '" + sk.getName() + "'" );
					return intention;
				}
			}
		}
		report("Cannot find next atomic step.");
		return null;
	}

}
