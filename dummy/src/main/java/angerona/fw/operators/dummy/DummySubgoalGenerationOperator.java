package angerona.fw.operators.dummy;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;

/**
 * dummy subgoal generation: can only generate sub goals for the answering of querys.
 * @author Tim Janus
 */
public class DummySubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DummySubgoalGenerationOperator.class);
	
	@Override
	protected Boolean processInt(SubgoalGenerationParameter pp) {
		LOG.info("Subgoal-Generation: ");
		
		Agent ag = pp.getActualPlan().getAgent();
		Atom wannaAnswer = new Atom(new Predicate("wantsToAnswer"));
		if(ag.getDesires().contains(wannaAnswer)) {
			pp.getActualPlan().newStack(ag.getSkill("QueryAnswer"), pp.getActualPlan().getAgent().getActualPerception());
			ag.getDesires().remove(wannaAnswer);
			return true;
		}
		return false;
	}

}
