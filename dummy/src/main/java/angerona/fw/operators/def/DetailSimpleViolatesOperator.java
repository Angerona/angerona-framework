package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.DetailQueryAnswer;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.operators.parameter.ViolatesParameter;

/**
 * This version of the Violates Operator supports Detail Answers and checks confidentiality violation more simply:
 * it just adds the facts of its answer to the logic program of the view rather than revising the view by the fact.
 * This allows it to check for contradictions because the contradictions won't be revised out.
 * 
 *  Simple solution: cast BaseBeliefBase to AspBeliefBase, get program, add fact to program
 *  More elegant solution is to create a revision operator which does the same thing
 * 
 *  Later *reason* for contradiction should be considered. Distinguish between self-contradiction and other contradiction.
 *  This will have to be incorporated in more elegant solution. 
 *  
 */
public class DetailSimpleViolatesOperator extends ViolatesOperator {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DetailViolatesOperator.class);
	
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		LOG.info("Run Detail-Simple-ViolatesOperator");
		if(param.getAction() instanceof Answer) {
			ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
			if(conf == null)
				return new Boolean(false);
			
			Answer a = (Answer) param.getAction();
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			if(views.containsKey(a.getReceiverId())) {
				
				AspBeliefbase view = (AspBeliefbase) views.get(a.getReceiverId()).clone();
				Program prog = view.getProgram();
				
				
				List<Secret> toRemove = new LinkedList<Secret>();
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						if(	view.infere().contains(secret.getInformation()))  {
							toRemove.add(secret);
							LOG.warn("Secret-Knowledge inconsistency found and removed by Violates-Operator.");
						}
					}
				}
				
				for(Secret remove : toRemove) {
					conf.removeConfidentialTarget(remove);
				}
				
				DetailQueryAnswer dqa = ((DetailQueryAnswer) a);
				LOG.info("Make Revision for DetailQueryAnswer: '{}'", dqa.getDetailAnswer());
				
				String ruleString = dqa.getDetailAnswer().toString();
				ruleString += "."; /* Assume information given is always a fact */
				/* Quick fix to bridge representations of ! and - */
				if(ruleString.startsWith("!"))
				{
					ruleString = "-" + ruleString.substring(1);
				}
				Rule rule = new Rule(ruleString);
				System.out.println(prog.toString());
				/* Check if the information is already present in the view
				If it is then no violation possible */
				prog.add(rule);
				
				/* Check program after expansion */
				Set<FolFormula> newAns = null;
				try
				{
					newAns = view.infere();
				}
				catch (IndexOutOfBoundsException ie)
				{
					
				}
				if (newAns==null)
				{
					report(param.getAgent().getName() + "' creates contradiction by: '" + param.getAction() + "'", view);
					return new Boolean(true);
				}
				
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						if(	view.infere().contains(secret.getInformation()))  {
							report("Confidential-Target: '" + secret + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAction() + "'", view);
							return new Boolean(true);
						}
					}
				}
			}
		}
		report("No violation applying the action: '" + param.getAction() + "'", param.getAgent());
		return new Boolean(false);
	}
}