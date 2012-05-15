package angerona.fw.operators.def;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.MasterPlan;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.comm.Query;
import angerona.fw.comm.RevisionRequest;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.Desires;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;

/**
 * Default subgoal generation generates the atomic actions need to react on the
 * different speech acts. Subclasses can use the default behavior to react to speech
 * acts.
 * Also implements specialized methods for the simple version of the strike-committee-example.
 * @author Tim Janus
 */
public class SubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(SubgoalGenerationOperator.class);
	
	@Override
	protected Boolean processInt(SubgoalGenerationParameter pp) {
		LOG.info("Run Default-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();

		boolean reval = processPersuadeOtherAgentsDesires(pp, ag);

		Desires des = ag.getDesires();
		if(des != null) {
			Set<Desire> actual;
			actual = des.getDesiresByPredicate(GenerateOptionsOperator.prepareQueryProcessing);
			for(Desire d : actual) {
				reval = reval || answerQuery(d, pp, ag);
			}
			
			actual = des.getDesiresByPredicate(GenerateOptionsOperator.prepareRevisionRequestProcessing);
			for(Desire d : actual) {
				reval = reval || revisionRequest(d, pp, ag);
			}
			
			// Todo prepare reason
		}
		
		if(!reval)
			report("No new subgoal generated.", ag);
		return reval;
	}

	protected boolean processPersuadeOtherAgentsDesires(
			SubgoalGenerationParameter pp, Agent ag) {
		boolean reval = false;
		Set<Atom> toRemove = new HashSet<Atom>();
		if(ag.getDesires() == null)
			return false;
		
		for(Atom desire : ag.getDesires().getTweety()) {
			if(desire.toString().trim().startsWith("v_")) {
				int si = desire.toString().indexOf("_")+1;
				int li = desire.toString().indexOf("(", si);
				if(si == -1 || li == -1)
					continue;
				String recvName = desire.toString().substring(si, li);
				
				si = desire.toString().indexOf("(")+1;
				li = desire.toString().indexOf(")");
				if(si == -1 || li == -1)
					continue;
				String content = desire.toString().substring(si,li);
				
				LOG.info("'{}' wants '"+recvName+"' to believe: '{}'",  ag.getName(), content);
		
				Skill rr = (Skill) ag.getSkill("RevisionRequest");
				if(rr == null) {
					LOG.warn("'{}' has no Skill: '{}'.", ag.getName(), "RevisionRequest");
					continue;
				}
				Subgoal sg = pp.getActualPlan();
				while(!(sg instanceof MasterPlan)) {
					sg = (Subgoal) sg.getParentGoal();
				}
				MasterPlan mp = (MasterPlan)sg;
				mp.newStack(rr, new RevisionRequest(ag.getName(), recvName, new Atom(new Predicate(content))).getContext());
				toRemove.add(desire);
				reval = true;
				
				report("Add the new atomic action '"+rr.getName()+"' to the plan, choosed by desire: " + desire.toString(), mp);
			}
		}
		// TODO: Implement in plan destroying
		for(Atom desire : toRemove) {
			ag.removeDesire(desire);
		//	ag.addDesire(new Atom(new Predicate(desire.toString()+"_wait")));
		}
		return reval;
	}

	protected Boolean answerQuery(Desire des, SubgoalGenerationParameter pp, Agent ag) {
		Skill qaSkill = (Skill) ag.getSkill("QueryAnswer");
		if(qaSkill == null) {
			LOG.warn("Agent '{}' does not have Skill: 'QueryAnswer'", ag.getName());
			return false;
		}
		
		// TODO: create alternative later
		Context context = ContextFactory.createContext(des.getPerception());
		context.set("answer", AnswerValue.AV_TRUE);
		pp.getActualPlan().newStack(qaSkill, context);
		
		context = new Context(context);
		context.set("answer", AnswerValue.AV_FALSE);
		pp.getActualPlan().newStack(qaSkill, context);
		
		// TODO: Find a better place to remove desire again.
		ag.removeDesire(des.getDesire());
		
		Subgoal sg = pp.getActualPlan();
		while(!(sg instanceof MasterPlan)) {
			sg = (Subgoal) sg.getParentGoal();
		}
		MasterPlan mp = (MasterPlan) sg;
		report("Add the new atomic action '"+qaSkill.getName()+"' to the plan", mp);
		return true;
	}
	
	protected Boolean revisionRequest(Desire des, SubgoalGenerationParameter pp, Agent ag) {
		// three cases: accept, query (to ensure about something) or deny.
		// in general we will accept all Revision queries but for the scm example
		// it is proofed if the given atom is 'excused' and if this is the case
		// first of all attend_scm is queried.
		if(!(des.getPerception() instanceof RevisionRequest))
			return false;
		
		RevisionRequest rr = (RevisionRequest) des.getPerception();
		if(rr.getSentences().size() == 1) {
			FolFormula ff = rr.getSentences().iterator().next();
			if(ff.toString().equals("excused")) {
				Atom reasonToFire = new Atom(new Predicate("attend_scm"));
				AngeronaAnswer aa = ag.getBeliefs().getWorldKnowledge().reason(reasonToFire);
				if(aa.getAnswerExtended() == AnswerValue.AV_UNKNOWN) {
					Skill query = (Skill) ag.getSkill("Query");
					pp.getActualPlan().newStack(query, new Query(ag.getName(), rr.getSenderId(), reasonToFire).getContext());
					ag.removeDesire(des);
				} else if(aa.getAnswerExtended() == AnswerValue.AV_FALSE) {
					return false;
				}
				return true;
			}
		}
		
		
		return false;
	}

}
