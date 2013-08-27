package angerona.fw.example.operators.scm;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.Subgoal;
import angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Query;
import angerona.fw.example.operators.SubgoalGenerationOperator;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;

/**
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
