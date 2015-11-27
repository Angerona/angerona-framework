package com.github.kreaturesfw.secrecy.example.operators.scm;

import com.github.kreaturesfw.core.bdi.Desire;
import com.github.kreaturesfw.core.bdi.Subgoal;
import com.github.kreaturesfw.core.bdi.operators.parameter.PlanParameter;
import com.github.kreaturesfw.core.comm.Inform;
import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.logic.AngeronaAnswer;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.secrecy.example.operators.SubgoalGenerationOperator;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * This implementation of the {@link SubgoalGenerationOperator} handles the
 * reaction to the ask_for_escuse literal by directly asking if the employee
 * wants to attend the strike committee meeting.
 * 
 * The code is fairly simple because the {@link StrikeCommitteePlanner} assumes
 * that only Boss and Employee are agents.
 * 
 * @author Tim Janus
 */
public class StrikeCommitteePlanner extends SubgoalGenerationOperator {
	@Override
	protected Boolean informProcessing(Desire des, PlanParameter pp, Agent ag) {
		if(!(des.getPerception() instanceof Inform))
			return false;
		
		Inform rr = (Inform) des.getPerception();
		if(rr.getSentences().size() == 1) {
			FolFormula ff = rr.getSentences().iterator().next();
			if(	ff instanceof FOLAtom && 
				((FOLAtom)ff).getPredicate().getName().equalsIgnoreCase("ask_for_excuse")) {
				FOLAtom reasonToFire = new FOLAtom(new Predicate("attend_scm"));
				AngeronaAnswer aa = ag.getBeliefs().getWorldKnowledge().reason(reasonToFire);
				if(aa.getAnswerValue() == AnswerValue.AV_UNKNOWN) {
					Subgoal sg = new Subgoal(ag, des);
					sg.newStack(new Query(ag, rr.getSenderId(), reasonToFire));
					pp.report("Add the new action '" + Query.class.getSimpleName() + "' to the plan.");
					ag.getPlanComponent().addPlan(sg);
				} else if(aa.getAnswerValue() == AnswerValue.AV_FALSE) {
					return false;
				}
				return true;
			}
		}
			
		return false;
	}
}
