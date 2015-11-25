package com.github.kreatures.example.operators.mates;

import net.sf.tweety.logics.commons.syntax.Predicate;

import com.github.kreatures.core.Agent;
import com.github.kreatures.core.Desire;
import com.github.kreatures.core.Skip;
import com.github.kreatures.core.Subgoal;
import com.github.kreatures.secrecy.operators.parameter.PlanParameter;
import com.github.kreatures.core.comm.Query;
import com.github.kreatures.example.operators.scm.StrikeCommitteePlanner;
import com.github.kreatures.core.logic.KReaturesAnswer;

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
			KReaturesAnswer aa = ag.getBeliefs().getWorldKnowledge().reason(q.getQuestion());
			
			Subgoal answerGoal = generateDefaultAnswerPlan(pp, ag, des, q, aa);
			
			// test if attend_scm is asked
			if(qPred.getName().equals("attend_scm")) {
				// and if this is the case react with an rejection:
				//Answer reject = new Answer(ag, q.getSenderId(), q.getQuestion(), 
				//		new KReaturesAnswer(q.getQuestion(), AnswerValue.AV_REJECT));
				
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
