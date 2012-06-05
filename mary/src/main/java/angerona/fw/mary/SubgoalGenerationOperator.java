package angerona.fw.mary;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JOptionPane;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.MasterPlan;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.operators.def.GenerateOptionsOperator;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;


public class SubgoalGenerationOperator extends
		angerona.fw.operators.def.SubgoalGenerationOperator {
	Logger LOG = LoggerFactory.getLogger(SubgoalGenerationOperator.class);
	
	@Override
	protected Boolean processInt(SubgoalGenerationParameter pp) {
		LOG.info("Run Default-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();
		
		boolean reval = interrogateOtherAgent(pp, ag);


		//Changing this if statement to a while loop has absolutely no effect
		//while(ag.getDesires() != null && ag.getDesires().getTweety().size() > 0) 
		if(ag.getDesires() != null)
		{
			Set<FolFormula> desires = ag.getDesires().getTweety();
			if(desires.contains(
					new Atom(GenerateOptionsOperator.prepareQueryProcessing))) 
			{
				//JOptionPane.showMessageDialog(null, "Answer Query called");
				reval = reval || answerQuery(pp, ag);
			} 
			else if(desires.contains(
					new Atom(GenerateOptionsOperator.prepareRevisionRequestProcessing))) 
			{
				reval = reval || revisionRequest(pp, ag);
			} 
			else if(desires.contains(
					new Atom(GenerateOptionsOperator.prepareReasonCalculation))) {
				// TODO Implement.
			}
		}
		
		if(!reval)
			report("No new subgoal generated.", ag);
		return reval;
	}
	public boolean interrogateOtherAgent(SubgoalGenerationParameter pp, Agent ag)
	{

		boolean reval = false;
//		Set<FolFormula> toRemove = new HashSet<FolFormula>();
		LinkedList<FolFormula> toRemove = new LinkedList<FolFormula>();
		if(ag.getDesires() == null)
			return false;
		
		//Sort the desires before using. It would be more modular to have a function
		//within the Agent class which returns a sorted array
		class DesireComp implements Comparator<FolFormula>
		{
			public int compare(FolFormula f1, FolFormula f2)
			{
				String[] f1_s = f1.toString().split("_");
				String[] f2_s = f2.toString().split("_");
				//Should be some way to ensure that what's being parsed
				//is in fact an integer
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
		//FolFormula[] desires = (FolFormula[]) ag.getDesires().getTweety().toArray(new FolFormula[0]);
		FolFormula[] desires = (FolFormula[]) ag.getDesires().getTweety().toArray(new FolFormula[0]);
		Arrays.sort(desires, new DesireComp());
		/*for (FolFormula d : desires)
		{
			JOptionPane.showMessageDialog(null, d.toString());
		}*/
		
		for(FolFormula desire : desires)
		{
			if(desire.toString().trim().startsWith("q_"))
			{
				//Should the snippet below be put in its own subroutine?
				int si = desire.toString().indexOf("_")+1;
				int li = desire.toString().indexOf("(", si);
				if(si == -1 || li == -1)
					continue;
				String recvName = desire.toString().substring(si, li);
				
				si = desire.toString().indexOf("(")+1;
				li = desire.toString().indexOf(")");
				if(si == -1 || li == -1)
					continue;
				String content = desire.toString().substring(si,li);
				//Should the snippet above be put in its own subroutine?
				
				Skill query = (Skill) ag.getSkill("Query");
				if(query == null) {
					LOG.warn("'{}' has no Skill: '{}'.", ag.getName(), "Query");
					continue;
				}
				Subgoal sg = pp.getActualPlan();
				while(!(sg instanceof MasterPlan)) {
					sg = (Subgoal) sg.getParentGoal();
				}
				Atom question = new Atom(new Predicate(content));
				MasterPlan mp = (MasterPlan)sg;
				mp.newStack(query, new Query(ag.getName(), recvName, question).getContext());
				toRemove.add(desire);
				reval = true;
				report("Add the new atomic action '"+query.getName()+"' to the plan, chosen by desire: " + desire.toString(), mp);
			}
		}
		
		for(FolFormula desire : toRemove) {
			ag.removeDesire(desire);
		}
		return reval;
	}
	
	//What currently needs work. The program is easily able to ask multiple questions,
	//but it can't return multiple answers (it just answers its first answer over and over)
	@Override
	protected Boolean answerQuery(SubgoalGenerationParameter pp, Agent ag) 
	{
		Skill qaSkill = (Skill) ag.getSkill("QueryAnswer");
		if(qaSkill == null) {
			LOG.warn("Agent '{}' does not have Skill: 'QueryAnswer'", ag.getName());
			return false;
		}
		
		/* Here, rather than hardwire the answer, the answer should be read from the
		 * belief base of the answering agent, based on the question that was asked. 
		 * */
		//That presupposes that you know what question was asked
		//Unfortunately, the agent does not even have access to the question
		
		//JOptionPane.showMessageDialog(null, ag.getActualPerception().toString());
		Query query = (Query) (ag.getActualPerception()); //How it knows what question was asked
		AngeronaAnswer ans = ag.getBeliefs().getWorldKnowledge().reason((FolFormula)query.getQuestion()); //How it refers to the belief base
		//JOptionPane.showMessageDialog(null, ans.getAnswerExtended());
		
		Context context = ContextFactory.createContext(
				pp.getActualPlan().getAgent().getActualPerception());
		context.set("answer", ans.getAnswerExtended());
		pp.getActualPlan().newStack(qaSkill, context);
		
		/*
		context = new Context(context);
		context.set("answer", AnswerValue.AV_REJECT);
		pp.getActualPlan().newStack(qaSkill, context);
		*/
		// TODO: Find a better place to remove desire again.
		ag.removeDesire(new Atom(GenerateOptionsOperator.prepareQueryProcessing));
		
		Subgoal sg = pp.getActualPlan();
		while(!(sg instanceof MasterPlan)) {
			sg = (Subgoal) sg.getParentGoal();
		}
		MasterPlan mp = (MasterPlan) sg;
		report("Add the new atomic action '"+qaSkill.getName()+"' to the plan", mp);
		return true;
	}
	
}
