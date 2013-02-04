package angerona.fw.DefendingAgent.operators.def;

import java.io.StringReader;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.parser.FolParserB;
import net.sf.tweety.logics.firstorderlogic.parser.ParseException;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.Subgoal;
import angerona.fw.DefendingAgent.comm.Revision;
import angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.Desires;

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
	protected Boolean processInternal(PlanParameter pp) {
		LOG.info("Run Default-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();
		Desires des = ag.getDesires();
		
		if(des != null) {
			Set<Desire> currentDesires;
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareQueryProcessing);
			for(Desire d : currentDesires) {
				processQuery(d, pp, ag);
			}
			
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareRevisionProcessing);
			for(Desire d : currentDesires) {
				processRevision(d, pp, ag);
			}
			
		}
		return true;
	}
	
	public void processQuery(Desire desire, PlanParameter pp, Agent ag) {
		Censor cexec = new Censor();
		
		Action action = cexec.processQuery(ag, (Query)desire.getPerception());
		Subgoal answer = new Subgoal(ag, desire);
		
		answer.newStack(action);
		
		ag.getPlanComponent().addPlan(answer);
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
	}
	
	public void processRevision(Desire desire, PlanParameter pp, Agent ag) {
		Censor cexec = new Censor();
		Action action = cexec.processRevision(ag, (Revision) desire.getPerception());
		 Subgoal answer = new Subgoal(ag, desire);
			
		answer.newStack(action);
			
		ag.getPlanComponent().addPlan(answer);
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
					"' to the plan", ag.getPlanComponent());
	}

}
