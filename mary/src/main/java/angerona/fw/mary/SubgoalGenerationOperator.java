package angerona.fw.mary;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.syntax.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.MasterPlan;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.comm.DetailQuery;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaDetailAnswer;
import angerona.fw.logic.Desires;
import angerona.fw.operators.def.GenerateOptionsOperator;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;

/**
* This implementation of a subgoal generation operator allows for the asking of multiple, detail query-type questions. 
* Agents can respond to open queries by considering one of many truthful answers.
* For a closed query they are capable of considering telling the opposite of the truth.
* They can also consider an answer of "I don't know" for any query.
* The expression of ignorance "I don't know" is always marked as a lie by this operator. 
* @author Daniel Dilger
*/

public class SubgoalGenerationOperator extends
		angerona.fw.operators.def.SubgoalGenerationOperator {
	Logger LOG = LoggerFactory.getLogger(SubgoalGenerationOperator.class);
	
	@Override
	protected Boolean processInt(SubgoalGenerationParameter pp) {
		LOG.info("Run Mary-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();
		
		boolean reval = interrogateOtherAgent(pp, ag);


		Desires des = ag.getDesires();
		if(des != null) {
			Set<Desire> actual;
			actual = des.getDesiresByPredicate(GenerateOptionsOperator.prepareQueryProcessing);
			for(Desire d : actual) {
				MasterPlan p = ag.getComponent(MasterPlan.class);
				boolean exists = false;
				for(Subgoal sg : p.getPlans())
					if(sg.getFulfillsDesire().equals(d))
						exists = true;
				if(exists)
					continue;
				reval = reval || answerQuery(d, pp, ag);
			}
			
			actual = des.getDesiresByPredicate(GenerateOptionsOperator.prepareRevisionRequestProcessing);
			for(Desire d : actual) {
				MasterPlan p = ag.getComponent(MasterPlan.class);
				boolean exists = false;
				for(Subgoal sg : p.getPlans())
					if(sg.getFulfillsDesire().equals(d))
						exists = true;
				if(exists)
					continue;
				reval = reval || revisionRequest(d, pp, ag);
			}
			
			// Todo prepare reason
		}
		
		if(!reval)
			report("No new subgoal generated.", ag);
		return reval;
	}
	public boolean interrogateOtherAgent(SubgoalGenerationParameter pp, Agent ag)
	{

		boolean reval = false;
		if(ag.getDesires() == null)
			return false;
		
		/**
		 * Should be way to ensure that what is being parsed is in fact an integer
		 */
		class DesireComp implements Comparator<Desire>
		{
			
			public int compare(Desire f1, Desire f2)
			{
				String[] f1_s = f1.toString().split("_");
				String[] f2_s = f2.toString().split("_");

				int f1_num = Integer.parseInt(f1_s[f1_s.length-1]); 
				int f2_num = Integer.parseInt(f2_s[f2_s.length-1]);
				
				if (f1_num < f2_num)
				{
					return -1;
				}
				else if (f1_num > f2_num)
				{
					return 1;
				}
				return 0;
			}
		}
		
		Desire[] desires = (Desire[]) ag.getDesires().getDesires().toArray(new Desire[0]);
		Arrays.sort(desires, new DesireComp());

		int numDesires = desires.length;
		for(int i=numDesires-1;i>=0;i--)
		{
			Desire desire = desires[i];
			if(desire.toString().trim().startsWith("q_"))
			{

				int si = desire.toString().indexOf("_")+1;
				int li = desire.toString().indexOf("(", si);
				if(si == -1 || li == -1)
					continue;
				String recvName = desire.toString().substring(si, li);
				
				si = desire.toString().indexOf("(")+1;
				li = desire.toString().lastIndexOf(")");
				if(si == -1 || li == -1)
					continue;
				String content = desire.toString().substring(si,li);
				String predName = content;
				String[] termNames = null;
				if(content.contains("("))
				{
					predName = content.substring(0, content.indexOf("("));
					String termsString = content.substring(content.indexOf("(")+1, content.lastIndexOf(")"));
					termNames = termsString.split(", ");
				}
				
				LinkedList<Term> terms = new LinkedList<Term>();
				if(termNames != null)
				{
					for(String name : termNames)
					{
						if(name.equals(name.toUpperCase()))
						{
							terms.add(new Constant(name)); //Variables not supported right now
						}
						else
						{
							terms.add(new Constant(name));
						}
					}
				}
				
				Skill query = (Skill) ag.getSkill("DetailQuery");
				if(query == null) {
					LOG.warn("'{}' has no Skill: '{}'.", ag.getName(), "DetailQuery");
					continue;
				}
				Subgoal sg = new Subgoal(ag, desire);
				FolFormula f = new Atom(new Predicate(predName, terms.size()), terms);
				if(content.startsWith("-")) {
					content = content.substring(1);
					f = new Negation(new Atom(new Predicate(predName, terms.size()), terms));
				}
				sg.newStack(query, new DetailQuery(ag.getName(), recvName, f).getContext());
				ag.getPlanComponent().addPlan(sg);
				reval = true;
				report("Add the new atomic action '"+query.getName()+"' to the plan, chosen by desire: " + desire.toString(), 
						ag.getPlanComponent());
			}
		}
		
		return reval;
	}
	class AnswerComp implements Comparator<AngeronaDetailAnswer>
	{

		public int compare(AngeronaDetailAnswer a1, AngeronaDetailAnswer a2) {
			return a1.toString().compareTo(a2.toString());
		}
		
	}

	/**
	 * Checks whether the query is a simple true/false type question or not
	 * Ideally it would check if any arguments are variables, but variables aren't supported yet
	 */
	private boolean simpleQuery(AngeronaDetailAnswer queryAnswer)
	{
		Atom a = (Atom) queryAnswer.getAnswerExtended();
		if(a.getPredicate().getArity() > 0)
		{
			List<Term> arguments = a.getArguments();
			String firstArgName = arguments.get(0).getName();
			if(firstArgName.equals(firstArgName.toUpperCase()))
				return true;
			return false;
		}
		return true;
	}

	/**
	 * Expresses ignorance about a certain topic
	 */
	public AngeronaDetailAnswer expressionOfIgnorance(Query query, Agent ag)
		{
			FolFormula question = (FolFormula)query.getQuestion();
			FolFormula expr = new Atom(new Predicate("dontKnow("+question.toString()+")")); 
			return new AngeronaDetailAnswer(ag.getBeliefs().getWorldKnowledge(), question, expr);
			
		}
	@Override
	protected Boolean answerQuery(Desire des, SubgoalGenerationParameter pp, Agent ag) 
	{
		Skill qaSkill = (Skill) ag.getSkill("DetailQueryAnswer");
		if(qaSkill == null) {
			LOG.warn("Agent '{}' does not have Skill: 'DetailQueryAnswer'", ag.getName());
			return false;
		}
		
		
		Query query = (Query) (ag.getActualPerception()); 
		AngeronaDetailAnswer[] trueAnswers = 
				ag.getBeliefs().getWorldKnowledge().allDetailReasons((FolFormula)query.getQuestion()).toArray(new AngeronaDetailAnswer[0]);
		
		Arrays.sort(trueAnswers, new AnswerComp()); 
		
		Context context = ContextFactory.createContext(
				pp.getActualPlan().getAgent().getActualPerception());
		context.set("answer", trueAnswers[0].getAnswerExtended());
		
		LinkedList <AngeronaDetailAnswer> allAnswers = new LinkedList<AngeronaDetailAnswer>();
		for(AngeronaDetailAnswer truth : trueAnswers)
		{
			allAnswers.add(truth);
		}
		
		if(allAnswers.size()>0 && simpleQuery(allAnswers.get(0)))
		{
			AngeronaDetailAnswer simpleLie = new LyingOperator().lie(allAnswers.get(0), ag.getBeliefs().getWorldKnowledge());
			allAnswers.add(simpleLie);
		}
		
		
		AngeronaDetailAnswer ignorance = expressionOfIgnorance(query, ag);
		allAnswers.add(ignorance);
		
		Skill qaSkillLie = (Skill) qaSkill.deepCopy(); 
		qaSkillLie.setHonestyStatus(false); 
		
		for(AngeronaDetailAnswer ans : allAnswers)
		{
			System.out.println("\t"+ans.toString());
		}
		
		Subgoal sg = new Subgoal(ag, des);
		sg.newStack(qaSkill, context);
		
		for(int i=1;i<allAnswers.size();i++)
		{
			context = new Context(context);
			FolFormula answer = allAnswers.get(i).getAnswerExtended();
			context.set("answer", answer);
			if(answer.toString().contains("dontKnow"))
			{
				Skill curLie = qaSkillLie.deepCopy();
				sg.newStack(curLie, context);
			}
			else
			{
				Skill curAns = qaSkill.deepCopy();
				sg.newStack(curAns, context);
			}
		}
		
		ag.getPlanComponent().addPlan(sg);
		return true;
	}
	
}
