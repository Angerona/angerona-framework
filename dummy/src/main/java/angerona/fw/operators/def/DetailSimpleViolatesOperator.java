package angerona.fw.operators.def;

import java.util.HashSet;
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
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.logic.Secret;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.operators.parameter.ViolatesParameter;
import angerona.fw.util.Pair;

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
	protected ViolatesResult processInt(ViolatesParameter param) {
		
		LOG.info("Run Detail-Simple-ViolatesOperator");
		if(param.getAction() instanceof Answer) {
			ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
			if(conf == null)
				return new ViolatesResult();
			
			Answer a = (Answer) param.getAction();
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			if(views.containsKey(a.getReceiverId()) && a.getAnswer().getAnswerValue() == AnswerValue.AV_COMPLEX) {
				
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
				
				// TODO: Merge the violates operators... this one only supports open questions...
				
				Set<FolFormula> answers = new HashSet<>();
				answers = a.getAnswer().getAnswers();
				if(answers.size() > 1) {
					LOG.warn("More than one answer but '" + this.getClass().getSimpleName() + "' only works with one (first).");
				} else if(answers.size() == 0) {
					LOG.warn("No answers given. Might be an error... violates operator doing nothing!");
					return new ViolatesResult();
				}
				FolFormula answer = answers.iterator().next();
				LOG.info("Make Revision for DetailQueryAnswer: '{}'", answer);
				
				Rule rule = new Rule(answer.toString());
				System.out.println(prog.toString());
				/* Check if the information is already present in the view
				If it is then no violation possible */
				prog.add(rule);
				
				/* Check program after expansion */
				Set<FolFormula> newAns = null;
				newAns = view.infere();
					
				if (newAns==null)
				{
					report(param.getAgent().getName() + "' creates contradiction by: '" + param.getAction() + "'", view);
					return new ViolatesResult(false);
				}
				
				List<Pair<Secret, Double>> pairs = new LinkedList<>();
				for(Secret secret : conf.getTargets()) {
					if(secret.getSubjectName().equals(a.getReceiverId())) {
						if(	view.infere().contains(secret.getInformation()))  {
							report("Confidential-Target: '" + secret + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAction() + "'", view);
							pairs.add(new Pair<>(secret, 1.0));
						}
					}
				}
				return new ViolatesResult(pairs);
			}
		}
		report("No violation applying the action: '" + param.getAction() + "'", param.getAgent());
		return new ViolatesResult();
	}
}