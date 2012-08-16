package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.DetailQueryAnswer;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.SecrecyStrengthPair;
import angerona.fw.logic.Secret;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.logic.asp.AspReasoner;
import angerona.fw.operators.parameter.ViolatesParameter;
/**
 * Extension of my DetailSimpleViolatesOperator which enables one to weaken secrecy.
 * The operator determines which secrets would be affected by an action.
 * It returns a list of pairs containing the secret and the degree by which it would be weakened. 
 * @author dilger
 *
 */
public class WeakeningViolatesOperator extends DetailSimpleViolatesOperator {
	static final double INFINITY = 1000.0;
	
	/**
	 * Does not call super().processInt
	 */
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		this.weakenings = processIntAndWeaken(param);
		return false;
	}	
	/**
	 * Assumes information given is always a fact
	 */
	private Rule convertToRule(FolFormula f)
	{
		String ruleString = f.toString();
		ruleString += "."; 
		/* Quick fix to bridge representations of ! and - */
		if(ruleString.startsWith("!"))
		{
			ruleString = "-" + ruleString.substring(1);
		} 
		Rule rule = new Rule(ruleString);
		return rule;
	}
	
	/**
	 * 
	 */
	private List<SecrecyStrengthPair> representTotalExposure(ConfidentialKnowledge conf)
	{
		List<SecrecyStrengthPair> reval = new LinkedList<SecrecyStrengthPair>();
		for(Secret secret : conf.getTargets()) 
		{
			reval.add(new SecrecyStrengthPair(secret, INFINITY));
		}
		return reval;
	}
	
	
	/**
	 * 
	 */
	private double calculateSecrecyStrength(FolFormula secretInfo, List<AnswerSet> ansSets)
	{
		double numAnsSets = ansSets.size();
		double setsWithSecret = 0.0;
		for(AnswerSet as : ansSets)
		{
			Program p = as.toProgram();
			Rule secretRule = convertToRule(secretInfo);
			if(p.hasRule(secretRule))
			{
				setsWithSecret += 1;
			}
		}
		double quotient = setsWithSecret/numAnsSets;
		double strength = 1.0 - quotient;
		return strength;
	}
	
	/**
	 * 
	 */
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
			a.setWeakenings(secretList); //Only works because objects are pass-by-reference, so not the most elegant solution
			
			/* Consider self-repeating answers (and only answers) as bad as revealing all secrets */
			List<Action> actionsHistory = param.getAgent().getActionsHistory();
			for(Action act : actionsHistory)
			{
				if(a.equals(act))
				{
					report(param.getAgent().getName() 
							+ "' <b> self-repeats </b> with: '" + param.getAction() + "'"); 
					secretList = representTotalExposure(conf);
					return secretList;
				}
			}
			
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
				List<AnswerSet> newAnsSets = null;
				try
				{
					AspReasoner ar = (AspReasoner) view.getReasoningOperator();
					ar.infer(view);
					view.infere();
					newAnsSets = ar.processAnswerSets();
					prog = view.getProgram();
				}
				catch (IndexOutOfBoundsException ie)
				{
					
				}
				if (newAnsSets==null)
				{
					String actString = param.getAction().toString();
					report(param.getAgent().getName() + "' <b> creates contradiction </b> by: '" + actString.substring(0, actString.length()-1) + "'", view);
					secretList = representTotalExposure(conf);
					return secretList;
				}
				
				/* Now the secrecy strengths get added */
				for(Secret secret : conf.getTargets()) 
				{
					FolFormula secretInfo = (FolFormula) secret.getInformation(); 
					// not used: (kill warning TJ)
					//	Rule secretRule = convertToRule(secretInfo);
					
					boolean secretContained = false;
					for(AnswerSet ans : newAnsSets)
					{
						if(ans.toString().contains(secretInfo.toString()))
						{
							int index = ans.toString().indexOf(secretInfo.toString());
							if(index == 0 || ans.toString().charAt(index-1) != '-') 
								secretContained = true;
						}
					}
					if(secretContained)
					{
						
						SecrecyStrengthPair sPair = new SecrecyStrengthPair();
						sPair.defineSecret(secret);
						double newStrength = calculateSecrecyStrength(secretInfo, newAnsSets);
						
						// TODO: Find default policy like a default parameter value if not set yet.
						String d = secret.getReasonerParameters().get("d");
						double curStrength = 0;
						if(d == null) {
							curStrength = 1;
						} else {
							curStrength = Double.parseDouble(d);
						}
						double degreeOfWeakening = curStrength - newStrength;
						sPair.defineDegreeOfWeakening(degreeOfWeakening);
						secretList.add(sPair);
						String actString = param.getAction().toString();
						if(degreeOfWeakening > 0)
						{
							report(param.getAgent().getName() + "' <b> weakens secret: </b> '"+secretInfo.toString()+"' by: '"+degreeOfWeakening+"' with: '"
						+ actString.substring(0, actString.length()-1) + "'", view);
						}
						else
						{
							report(param.getAgent().getName() + "' <b> weakens no secrets </b> ' with: '"
									+ actString.substring(0, actString.length()-1) + "'", view);
						}
					}
					else
					{
						String actString = param.getAction().toString();
						report(param.getAgent().getName() + "' <b> weakens no secrets: </b> ' with: '"
								+ actString.substring(0, actString.length()-1) + "'", view);
					}
					
				}
			}
		}
		
		return secretList;
	}
}
