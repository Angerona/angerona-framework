package angerona.fw.operators.def;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Desire;
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
		if(ag.getDesires() == null)
			return false;
		
		for(Desire desire : ag.getDesires().getDesires()) {
			Atom atom = desire.getAtom();
			if(atom.toString().trim().startsWith("v_")) {
				int si = atom.toString().indexOf("_")+1;
				int li = atom.toString().indexOf("(", si);
				if(si == -1 || li == -1)
					continue;
				String recvName = atom.toString().substring(si, li);
				
				si = atom.toString().indexOf("(")+1;
				li = atom.toString().indexOf(")");
				if(si == -1 || li == -1)
					continue;
				String content = atom.toString().substring(si,li);
				
				LOG.info("'{}' wants '"+recvName+"' to believe: '{}'",  ag.getName(), content);
		
				Skill rr = (Skill) ag.getSkill("RevisionRequest");
				if(rr == null) {
					LOG.warn("'{}' has no Skill: '{}'.", ag.getName(), "RevisionRequest");
					continue;
				}
				Subgoal sg = new Subgoal(ag, desire);
				sg.newStack(rr, new RevisionRequest(ag.getName(), recvName, new Atom(new Predicate(content))).getContext());
				ag.getPlanComponent().addPlan(sg);
				report("Add the new atomic action '"+rr.getName()+"' to the plan, choosed by desire: " + desire.toString(), ag.getPlanComponent());
				reval = true;
			}
		}
		return reval;
	}

	protected Boolean answerQuery(Desire des, SubgoalGenerationParameter pp, Agent ag) {
		Skill qaSkill = (Skill) ag.getSkill("QueryAnswer");
		if(qaSkill == null) {
			LOG.warn("Agent '{}' does not have Skill: 'QueryAnswer'", ag.getName());
			return false;
		}
		
		Subgoal answer = new Subgoal(ag, des);
		// TODO: create alternative later
		Context context = ContextFactory.createContext(des.getPerception());
		context.set("answer", AnswerValue.AV_TRUE);
		answer.newStack(qaSkill, context);
		
		context = new Context(context);
		context.set("answer", AnswerValue.AV_FALSE);
		answer.newStack(qaSkill, context);
		ag.getPlanComponent().addPlan(answer);
		report("Add the new atomic action '"+qaSkill.getName()+"' to the plan", ag.getPlanComponent());
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
				if(aa.getAnswerValue() == AnswerValue.AV_UNKNOWN) {
					Skill query = (Skill) ag.getSkill("Query");
					Subgoal sg = new Subgoal(ag, des);
					sg.newStack(query, new Query(ag.getName(), rr.getSenderId(), reasonToFire).getContext());
					ag.getPlanComponent().addPlan(sg);
					report("Add the new atomic action '" + query.getName() + "' to the plan.", ag.getPlanComponent());
				} else if(aa.getAnswerValue() == AnswerValue.AV_FALSE) {
					return false;
				}
				return true;
			}
		}
		
		
		return false;
	}

}
