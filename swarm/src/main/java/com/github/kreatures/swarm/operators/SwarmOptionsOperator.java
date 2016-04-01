package com.github.kreatures.swarm.operators;



import com.github.kreatures.core.logic.Desires;
/**
 * List of Default Desires
 */
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_MOVE;
import static com.github.kreatures.swarm.basic.SwarmDesires.IS_VISIT;
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_LEAVE;
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_VISIT;


import com.github.kreatures.secrecy.operators.BaseGenerateOptionsOperator;
import com.github.kreatures.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.kreatures.swarm.components.StatusAgentComponents;

/**
 * 
 * @author donfack
 *
 */

public class SwarmOptionsOperator extends BaseGenerateOptionsOperator {

	@Override
	protected Integer processImpl(GenerateOptionsParameter preprocessedParameters) {
		Desires desires = preprocessedParameters.getAgent().getComponent(Desires.class);
		StatusAgentComponents statusAgent = preprocessedParameters.getAgent().getComponent(StatusAgentComponents.class);

		if (desires != null && statusAgent != null) {
			desires.clear();

			if (statusAgent.isLeave()) {
				desires.add(WANT_TO_LEAVE);
			}else if (statusAgent.isOnWay()) {
				desires.add(WANT_TO_MOVE);
			}else if (statusAgent.isEnterStation()) {
				desires.add(WANT_TO_VISIT);
			}else			if (statusAgent.isVisit()) {
				desires.add(IS_VISIT);
			}
		}
		return 0;
	}
}
