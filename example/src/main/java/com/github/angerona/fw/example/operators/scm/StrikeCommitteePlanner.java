package com.github.angerona.fw.example.operators.scm;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.comm.Inform;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.example.operators.SubgoalGenerationOperator;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;

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
