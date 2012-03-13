package angerona.fw.operators.dummy;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.MasterPlan;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;

/**
 * Default subgoal generation generates the atomic actions need to react on the
 * different speech acts. Subclasses can use the default behavior to react to speech
 * acts.
 * @author Tim Janus
 */
public class DummySubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DummySubgoalGenerationOperator.class);
	
	@Override
	protected Boolean processInt(SubgoalGenerationParameter pp) {
		LOG.info("Run Default-Subgoal-Generation");
		
		Agent ag = pp.getActualPlan().getAgent();
		if(ag.getDesires().contains(
				new Atom(DummyGenerateOptionsOperator.prepareQueryProcessing))) {
			return answerQuery(pp, ag);
		} else if(ag.getDesires().contains(
				new Atom(DummyGenerateOptionsOperator.prepareRevisionRequestProcessing))) {
			// TODO Implement.
		} else if(ag.getDesires().contains(
				new Atom(DummyGenerateOptionsOperator.prepareReasonCalculation))) {
			// TODO Implement.
		}
		report("No new subgoal generated.", ag);
		return false;
	}

	private Boolean answerQuery(SubgoalGenerationParameter pp, Agent ag) {
		Skill qaSkill = (Skill) ag.getSkill("QueryAnswer");
		if(qaSkill == null) {
			LOG.warn("Agent '{}' does not have Skill: 'QueryAnswer'", ag.getName());
			return false;
		}
		pp.getActualPlan().newStack(qaSkill, pp.getActualPlan().getAgent().getActualPerception());
		
		// TODO: Find a better place to remove desire again.
		ag.removeDesire(new Atom(DummyGenerateOptionsOperator.prepareQueryProcessing));
		
		Subgoal sg = pp.getActualPlan();
		while(!(sg instanceof MasterPlan)) {
			sg = (Subgoal) sg.getParentGoal();
		}
		MasterPlan mp = (MasterPlan) sg;
		report("Add the new atomic action '"+qaSkill.getName()+"' to the plan", mp);
		return true;
	}

}
