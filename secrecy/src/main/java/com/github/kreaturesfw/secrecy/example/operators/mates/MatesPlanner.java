package com.github.kreaturesfw.secrecy.example.operators.mates;

import com.github.kreaturesfw.core.bdi.Desire;
import com.github.kreaturesfw.core.bdi.Subgoal;
import com.github.kreaturesfw.core.bdi.operators.parameter.PlanParameter;
import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.legacy.Skip;
import com.github.kreaturesfw.core.logic.AngeronaAnswer;
import com.github.kreaturesfw.secrecy.example.operators.scm.StrikeCommitteePlanner;

import net.sf.tweety.logics.commons.syntax.Predicate;

/**
 * This {@link MatesPlanner} extends the {@link StrikeCommitteePlanner} by a
 * reaction to the question "attend_scm". If the Employee is asked this questions
 * he refuses an answer.
 * 
 * This operator is fairly simple and only implements the behavior of the
 * Employee on top of the {@link StrikeCommitteePlanner}.
 * 
 * @author Tim Janus
 */
public class MatesPlanner extends StrikeCommitteePlanner {
	@Override
	protected Boolean answerQuery(Desire des, PlanParameter pp, Agent ag) {
		if(des.getPerception() instanceof Query) {
			Query q = (Query)des.getPerception();
			Predicate qPred = q.getQuestion().getPredicates().iterator().next();
			AngeronaAnswer aa = ag.getBeliefs().getWorldKnowledge().reason(q.getQuestion());
			
			Subgoal answerGoal = generateDefaultAnswerPlan(pp, ag, des, q, aa);
			
			// test if attend_scm is asked
			if(qPred.getName().equals("attend_scm")) {
				// and if this is the case react with an rejection:
				//Answer reject = new Answer(ag, q.getSenderId(), q.getQuestion(), 
				//		new AngeronaAnswer(q.getQuestion(), AnswerValue.AV_REJECT));
				
				Subgoal sg = new Subgoal(ag, des);
				sg.newStack(new Skip(ag));
				sg.newStack(answerGoal.getStack(0).get(0));
				sg.newStack(answerGoal.getStack(1).get(0));
				
				pp.getActualPlan().addPlan(sg);
				return true;
			}
		}
		
		
		return super.answerQuery(des, pp, ag);
	}
}
