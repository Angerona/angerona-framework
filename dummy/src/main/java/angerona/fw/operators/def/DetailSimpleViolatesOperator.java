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
 *  This solution isn't working. Fact and negation of fact remain in belief base Perhaps I'm making the rule to add to the program wrong?
 * 
 * 
 * Other change: any information already present in view cannot violate confidentiality
 */
public class DetailSimpleViolatesOperator extends ViolatesOperator {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DetailViolatesOperator.class);
	
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		LOG.info("Run Detail-Simple-ViolatesOperator");
		//JOptionPane.showMessageDialog(null, param.getAction());
		if(param.getAction() instanceof Answer) {
			// only apply violates if confidential knowledge is saved in agent.
			ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
			if(conf == null)
				return new Boolean(false);
			
			Answer a = (Answer) param.getAction();
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			if(views.containsKey(a.getReceiverId())) {
				
				AspBeliefbase view = (AspBeliefbase) views.get(a.getReceiverId()).clone();
				Program prog = view.getProgram();
				
				
				// Check if the information is already present in the view
				//If it is then no violation possible (not implemented yet -- only for secrets now)
				List<Secret> toRemove = new LinkedList<Secret>();
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						if(	view.infere().contains(secret.getInformation()))  {
							toRemove.add(secret);
							LOG.warn("Secret-Knowledge inconsistency found and removed by Violates-Operator.");
						}
					}
				}
				//Should it remove the confidential target or just return FALSE? What if confidential target could be used later?
				for(Secret remove : toRemove) {
					conf.removeConfidentialTarget(remove);
				}
				
				//Adapt the view and check it again
				
				DetailQueryAnswer dqa = ((DetailQueryAnswer) a);
				LOG.info("Make Revision for DetailQueryAnswer: '{}'", dqa.getDetailAnswer());
				view.addNewKnowledge(dqa.getDetailAnswer()); //Should I call addNewKnowledge with a new UpdateType?
				FolFormula newFact = dqa.getDetailAnswer();
				String ruleString = dqa.getDetailAnswer().toString();
				ruleString += "."; //Assume information given is always a fact
				Rule rule = new Rule(ruleString);
				//System.out.println("(Delete) Rule: "+rule.toString());
				//Check program before expansion
				//System.out.println("(Delete) Program before expansion: ");
				//System.out.println(prog.toString());
				Set<FolFormula> oldAns = view.infere();
				System.out.println("(Delete) old answer sets:");
				System.out.println(oldAns.toString());
				prog.add(rule);
				//Check program after expansion
				//System.out.println("(Delete) Program after expansion: ");
				//System.out.println(prog.toString());
				Set<FolFormula> newAns = view.infere();
				
				if(newAns == null)
				{
					return new Boolean(true);
				}
				System.out.println("(Delete) new answer sets:");
				System.out.println(newAns.toString());
				
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
