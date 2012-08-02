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

import angerona.fw.Action;
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
		return false;
		//return super.processInt(param);
	}	private Rule convertToRule(FolFormula f)
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
		List<SecrecyStrengthPair> reval = new LinkedList<SecrecyStrengthPair>();
		for(Secret secret : conf.getTargets()) 
		{
			reval.add(new SecrecyStrengthPair(secret, INFINITY));
		}
		return reval;
	}
	
	
	//Talk about generalizing the cost of weakening secrecy through either some multiplier coefficient,
	//or a non-linear cost calculation through either increasing frequency of belief operators 
	//or cost between the "edges" from one belief operator to another
	private double calculateSecrecyStrength(FolFormula secretInfo, List<AnswerSet> ansSets)
	{
		double numAnsSets = ansSets.size();
		double setsWithSecret = 0.0;
		for(AnswerSet as : ansSets)
		{
			Program p = as.toProgram();
			Rule secretRule = convertToRule(secretInfo);
			System.out.println("(Delete) secretRule: "+secretRule);
			System.out.println("(Delete) answer set program:"+p.toString());
			if(p.hasRule(secretRule))
			{
				setsWithSecret += 1;
			}
		}
		double quotient = setsWithSecret/numAnsSets;
		double strength = 1.0 - quotient;
		System.out.println("(Delete) Quotient: "+quotient+" strength: "+strength);
		return strength;
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
			a.setWeakenings(secretList); //Only works because objects are pass-by-reference, so not the most elegant solution
			
			/* Consider self-repeating answers (and only answers) as bad as revealing all secrets */
			//Should there be a separate repeatsSelf() method, so that the intention update operator can judge the cost of self-repeating?
			//Such a method could also allow for easier changes to the definition of "self-repeating" 
			// e.g. how many new people must be listening for it not to be self-repeating, or how many actions back in history should the function check
			List<Action> actionsHistory = param.getAgent().getActionsHistory();
			System.out.println("(Delete) size of actionsHistory: "+actionsHistory.size());
			for(Action act : actionsHistory)
			{
				if(a.equals(act))
				{
					//Should be replaced with a report once reporting doesn't crash after a certain amount of use
					System.out.println("(Delete) "+param.getAgent().getName() 
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
					//Note: those output lines below have to be called for the program to work, as they're critical to updating the beliefs
					//System.out.println("(Delete) ar.infer view:"+ar.infer(view));
					//System.out.println("(Delete) view.infere:"+view.infere());
					
					//Probably only one of the below statements is necessary
					ar.infer(view);
					view.infere();
					newAnsSets = ar.processAnswerSets();
					prog = view.getProgram(); //Necessary to update according to facts generated by rules
					System.out.println("(Delete) program after expansion:"+prog.toString());
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
					String actString = param.getAction().toString();
					report(param.getAgent().getName() + "' <b> creates contradiction </b> by: '" + actString.substring(0, actString.length()-1) + "'", view);
					secretList = representTotalExposure(conf);
					return secretList;
				}
				for(AnswerSet ans : newAnsSets)
				{
					System.out.println("(Delete) New Answer Set:"+ans.toString());
					
				}
				/* Now the secrecy strengths get added */
				for(Secret secret : conf.getTargets()) 
				{
					FolFormula secretInfo = (FolFormula) secret.getInformation(); //Info stored as a FolFormula in secret, yet access as a Formula...
					Rule secretRule = convertToRule(secretInfo);
					System.out.println("(Delete) information regarding secret: "+secret.toString());
					//better form to use hasRule instead of contains (though technically doesn't matter here)
					//hasRule is a deep comparison while contains is reference-based (for now)
					//if(prog.hasRule(secretRule))
					//if(view.infere().contains(secretInfo)) //Temporary fix until programs updated according to rules
					//but another problem is that the intersection of answer sets is what shows up in infer()
					
					//The below solution is the best I could come up with for rules
					boolean secretContained = false;
					for(AnswerSet ans : newAnsSets)
					{
						//Another temporary string-based solution...
						System.out.println("(Delete) secret info string:"+secretInfo.toString());
						System.out.println("(Delete) ans program:"+ans.toString());
						if(ans.toString().contains(secretInfo.toString()))
						{
							int index = ans.toString().indexOf(secretInfo.toString());
							//I think this will work with a negative secret as well
							//But the string-based solution should still be removed
							if(index == 0 || ans.toString().charAt(index-1) != '-') 
								secretContained = true;
						}
					}
					if(secretContained)
					{
						
						SecrecyStrengthPair sPair = new SecrecyStrengthPair();
						sPair.defineSecret(secret);
						double newStrength = calculateSecrecyStrength(secretInfo, newAnsSets);
						//Note: the line below will crash if the belief operator specified is "DEFAULT"
						double curStrength = Double.parseDouble(secret.getReasonerParameters().get("d"));
						System.out.println("(Delete) curStrength: "+curStrength);
						double degreeOfWeakening = curStrength - newStrength;
						System.out.println("(Delete) degreeOfWeakening: "+degreeOfWeakening);
						/* Revealing a single secret entirely may not necessarily mean infinite cost...look over semantics again
						if(degreeOfWeakening == 1.0) //Represent the total weakening of a secret
							degreeOfWeakening = INFINITY;
							*/ 
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
							report(param.getAgent().getName() + "' <b> weakens no secrets: </b> ' with: '"
									+ actString.substring(0, actString.length()-1) + "'", view);
						}
						//report(param.getAgent().getName() + "' <b> weakens secret: </b> '"+secretInfo.toString()+"' by: '"+degreeOfWeakening);
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
