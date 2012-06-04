package angerona.fw.mary;

import java.util.Comparator;
import java.util.LinkedList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Agent;
import angerona.fw.MasterPlan;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.comm.Query;
import angerona.fw.logic.AnswerValue;
import angerona.fw.operators.def.GenerateOptionsOperator;
//import angerona.fw.operators.def.SubgoalGenerationOperator;
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

		//JOptionPane.showMessageDialog(null, ag.getDesires());
		
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
			/*
			public int compare(Object _f1, Object _f2)
			{
				FolFormula f1 = (FolFormula) _f1;
				FolFormula f2 = (FolFormula) _f2;
				*/
			public int compare(FolFormula f1, FolFormula f2)
			{
				int f1_num = Integer.parseInt(f1.toString().split("_")[2]); 
				int f2_num = Integer.parseInt(f2.toString().split("_")[2]);
				
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
		for (FolFormula d : desires)
		{
			JOptionPane.showMessageDialog(null, d.toString());
		}
		
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
		Context context = ContextFactory.createContext(
				pp.getActualPlan().getAgent().getActualPerception());
		context.set("answer", AnswerValue.AV_TRUE);
		pp.getActualPlan().newStack(qaSkill, context);
		
		context = new Context(context);
		context.set("answer", AnswerValue.AV_FALSE);
		pp.getActualPlan().newStack(qaSkill, context);
		
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
