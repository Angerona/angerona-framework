package angerona.fw.operators.def;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.DetailQueryAnswer;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.logic.SecrecyStrengthPair;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.logic.asp.AspReasoner;
import angerona.fw.operators.parameter.ViolatesParameter;
/**
 * Extension of my DetailSimpleViolatesOperator which enables one to weaken secrecy
 * @author dilger
 *
 */
public class WeakeningViolatesOperator extends DetailSimpleViolatesOperator {
	static final double INFINITY = 1000.0;
	
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		this.weakenings = processIntAndWeaken(param);
		//Not sure whether or not to call the super.processInt()
		//return false;
		return super.processInt(param);
	}
	private Rule convertToRule(FolFormula f)
	{
		String ruleString = f.toString();
		ruleString += "."; //Assume information given is always a fact
		//Quick fix to bridge representations of ! and -
		if(ruleString.startsWith("!"))
		{
			ruleString = "-" + ruleString.substring(1);
		}
		//TODO: Account for predicates with variables (arity 1+) 
		Rule rule = new Rule(ruleString);
		return rule;
	}
	private List<SecrecyStrengthPair> representTotalExposure(ConfidentialKnowledge conf)
	{
		return null;
	}
	private boolean formulaMatchesLiteral(FolFormula secretInfo, Literal literal)
	{
		return false;
	}
	private double calculateSecrecyStrength(FolFormula secretInfo, List<AnswerSet> ansSets)
	{
		return 0.0;
	}
	protected List<SecrecyStrengthPair> processIntAndWeaken(ViolatesParameter param)
	{
		Logger LOG = LoggerFactory.getLogger(DetailViolatesOperator.class);
		List<SecrecyStrengthPair> secretList = new LinkedList<SecrecyStrengthPair>(); 
		
		/* Check if any confidential knowledge present. If none then no secrecy weakening possible */
		ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
		if(conf == null)
			return secretList;
		
		/* Remaining operations depend on whether the action in question is an answer */
		if(param.getAction() instanceof Answer) 
		{
			Answer a = (Answer) param.getAction();
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			if(views.containsKey(a.getReceiverId())) 
			{
				
				AspBeliefbase view = (AspBeliefbase) views.get(a.getReceiverId()).clone();
				Program prog = view.getProgram();
				
				DetailQueryAnswer dqa = ((DetailQueryAnswer) a);
				LOG.info("Make Revision for DetailQueryAnswer: '{}'", dqa.getDetailAnswer());
				
				FolFormula answerFormula = dqa.getDetailAnswer();
				Rule rule = convertToRule(answerFormula);
				
				prog.add(rule);
				
				/*Check for contradictions. If one is found consider all secrets totally revealed*/ 
				List<AnswerSet> newAnsSets = null; //Should this be a set? Will it pass as a list?
				//This try/catch may be a crude solution but...
				//Actually, is try/catch even necessary in this case?
				try
				{
					//Actually I don't think the solution below was even necessary. It wasn't a bug, just bad output
					//Ensure the reasoning operator's belief base is up to date
					//Ask Tim about this solution?
					AspReasoner ar = (AspReasoner) view.getReasoningOperator();
					//ar.infer(view);
					newAnsSets = ar.processAnswerSets();
					//System.out.println("(Delete) newAnsSets:"+newAnsSets.toString());
				}
				catch (IndexOutOfBoundsException ie)
				{
					//System.out.println("(Delete) IndexOutOfBounds processing answer sets");
				}
				if (newAnsSets==null)
				{
					//There is a bug with contradiction checking
					System.out.println("(Delete) contradiction noted here");
					report(param.getAgent().getName() + "' creates contradiction by: '" + param.getAction() + "'", view);
					secretList = representTotalExposure(conf);
					return secretList;
				}
				
				/* Now the secrecy strengths get added */
				for(Secret secret : conf.getTargets()) 
				{
					FolFormula secretInfo = (FolFormula) secret.getInformation(); //Info stored as a FolFormula in secret, yet access as a Formula...
					Rule secretRule = convertToRule(secretInfo);
					if(prog.contains(secretRule))
					{
						report(param.getAgent().getName() + "' weakens secret by: '" + param.getAction() + "'", view);
						SecrecyStrengthPair sPair = new SecrecyStrengthPair();
						sPair.defineSecret(secret);
						double strength = calculateSecrecyStrength(secretInfo, newAnsSets);
						//Not sure how to access the operator yet
					}
					
				}
			}
		}
		
		return secretList;
	}
}
