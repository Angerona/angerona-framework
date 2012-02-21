package angerona.fw.operators.dummy;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.MasterPlan;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
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
		LOG.info("Run Example-Subgoal-Generation");
		
		Agent ag = pp.getActualPlan().getAgent();
		Atom wannaAnswer = new Atom(new Predicate("wantsToAnswer"));
		if(ag.getDesires().contains(wannaAnswer)) {
			Skill qaSkill = (Skill) ag.getSkill("QueryAnswer");
			if(qaSkill == null) {
				LOG.warn("Agent '{}' does not have Skill: 'QueryAnswer'", ag.getName());
				return false;
			}
			pp.getActualPlan().newStack(qaSkill, pp.getActualPlan().getAgent().getActualPerception());
			
			// TODO: Find a better place to remove desire again.
			ag.removeDesire(wannaAnswer);
			
			Subgoal sg = pp.getActualPlan();
			while(!(sg instanceof MasterPlan)) {
				sg = (Subgoal) sg.getParentGoal();
			}
			MasterPlan mp = (MasterPlan) sg;
			report("Add the new atomic action '"+qaSkill.getName()+"' to the plan", mp);
			return true;
		}
		report("No new subgoal generated.", ag);
		return false;
	}

}
