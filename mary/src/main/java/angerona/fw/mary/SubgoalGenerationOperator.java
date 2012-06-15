package angerona.fw.mary;

import java.util.Arrays;
import java.util.Comparator;
//import java.util.LinkedList;
import java.util.Set;

//import javax.swing.JOptionPane;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.MasterPlan;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.Desires;
//import angerona.fw.logic.asp.AspReasoner;
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
		
		//Sort the desires before using. It would be more modular to have a function
		//within the Agent class which returns a sorted array
		
		class DesireComp implements Comparator<Desire>
		{
			
			public int compare(Desire f1, Desire f2)
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
		
		Desire[] desires = (Desire[]) ag.getDesires().getDesires().toArray(new Desire[0]);
		Arrays.sort(desires, new DesireComp());
		/*
		for (Desire d : desires)
		{
			JOptionPane.showMessageDialog(null, d.toString());
		}
		*/
		//for(Desire desire : ag.getDesires().getDesires())
		for(Desire desire: desires)
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
				li = desire.toString().lastIndexOf(")");
				if(si == -1 || li == -1)
					continue;
				String content = desire.toString().substring(si,li);
				//Should the snippet above be put in its own subroutine?
				
				Skill query = (Skill) ag.getSkill("DetailQuery");
				if(query == null) {
					LOG.warn("'{}' has no Skill: '{}'.", ag.getName(), "DetailQuery");
					continue;
				}
				Subgoal sg = new Subgoal(ag, desire);
				sg.newStack(query, new Query(ag.getName(), recvName, new Atom(new Predicate(content))).getContext());
				ag.getPlanComponent().addPlan(sg);
				reval = true;
				report("Add the new atomic action '"+query.getName()+"' to the plan, chosen by desire: " + desire.toString(), 
						ag.getPlanComponent());
			}
		}
		
		return reval;
	}
	
	//The most basic form of the lying operator
	protected AnswerValue lie(AngeronaAnswer truth)
	{
		if(truth.getAnswerExtended() == AnswerValue.AV_TRUE)
			return AnswerValue.AV_FALSE;
		else if(truth.getAnswerExtended() == AnswerValue.AV_FALSE)
			return AnswerValue.AV_TRUE;
		return AnswerValue.AV_UNKNOWN;
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
		AngeronaAnswer ans = ag.getBeliefs().getWorldKnowledge().reason((FolFormula)query.getQuestion());
		
		AnswerValue lie = lie(ans);
		
		Context context = ContextFactory.createContext(
				pp.getActualPlan().getAgent().getActualPerception());
		context.set("answer", ans.getAnswerExtended());
		
		Subgoal sg = new Subgoal(ag, des);
		sg.newStack(qaSkill, context);
		
		context = new Context(context);
		context.set("answer", lie);
		sg.newStack(qaSkill, context);
		
		ag.getPlanComponent().addPlan(sg);
		
	
		report("Add the new atomic action '"+qaSkill.getName()+"' to the plan", ag.getPlanComponent());
		return true;
	}
	
}
