package angerona.fw.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.Skill;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.IntentionUpdateParameter;

/**
 * 
 * @author Tim Janus
 */
public class IntentionUpdateOperator extends BaseIntentionUpdateOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(IntentionUpdateOperator.class);
	
	@Override
	protected Intention processInt(IntentionUpdateParameter param) {
		LOG.info("Run Default-Intention-Update");
		Agent ag = param.getPlan().getAgent();
		for(int i=0; i<param.getPlan().getNumberOfStacks(); ++i) {
			if(param.getPlan().peekStack(i).isAtomic()) {
				Intention intention = param.getPlan().peekStack(i);
				intention.setRealRun(false);
				report("Performing mental-action applying: '"+intention+"'", ag);
				intention.run();
				Skill sk = (Skill)intention;
				if(!sk.violates()) {
					report("Mental action successfull, using '" + sk.getName() + "' as next atomic action.", ag);
					return intention;
				}
			}
		}
		report("No atomic step candidate found.", ag);
		return null;
	}

}
