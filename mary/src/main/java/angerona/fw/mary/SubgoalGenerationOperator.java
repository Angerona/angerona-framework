package angerona.fw.mary;

import java.util.Arrays;
import java.util.Collections;
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
import net.sf.tweety.logics.firstorderlogic.syntax.Variable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.PlanComponent;
import angerona.fw.Subgoal;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.Desires;
import angerona.fw.operators.def.GenerateOptionsOperator;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;

/**
* This implementation of a subgoal generation operator allows for the asking of multiple, detail query-type questions. 
* Agents can respond to open queries by considering one of many truthful answers.
* For a closed query they are capable of considering telling the opposite of the truth.
* They can also consider an answer of "I don't know" for any query.
* The expression of ignorance "I don't know" is always marked as a lie by this operator. 
* @author Daniel Dilger, Tim Janus
*/
public class SubgoalGenerationOperator extends
		angerona.fw.operators.def.SubgoalGenerationOperator {
	private static Logger LOG = LoggerFactory.getLogger(SubgoalGenerationOperator.class);
	
	/** flag indicating if the lies should be generated. */
	private boolean generateLies = false;
	
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
				PlanComponent p = ag.getComponent(PlanComponent.class);
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
				PlanComponent p = ag.getComponent(PlanComponent.class);
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
	
	public boolean interrogateOtherAgent(SubgoalGenerationParameter pp, Agent ag) {

		boolean reval = false;
		if(ag.getDesires() == null)
			return false;
		
		/**
		 * Should be way to ensure that what is being parsed is in fact an integer
		 */
		class DesireComp implements Comparator<Desire> {
			
			public int compare(Desire f1, Desire f2)
			{
				String[] f1_s = f1.toString().split("_");
				String[] f2_s = f2.toString().split("_");

				if(f1_s.length == 1) {
					return -1;
				} else if(f2_s.length == 1) {
					return 1;
				}
				
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
							terms.add(new Variable(name)); //Variables not supported right now
						}
						else
						{
							terms.add(new Constant(name));
						}
					}
				}
				
				if(!ag.hasCapability("Query")) {
					LOG.warn("'{}' has no Skill: '{}'.", ag.getName(), "Query");
					continue;
				}
				Subgoal sg = new Subgoal(ag, desire);
				FolFormula f = new Atom(new Predicate(predName, terms.size()), terms);
				if(predName.startsWith("-")) {
					predName = predName.substring(1);
					f = new Negation(new Atom(new Predicate(predName, terms.size()), terms));
				}
				sg.newStack(new Query(ag, recvName, f));
				ag.getPlanComponent().addPlan(sg);
				reval = true;
				report("Add the new atomic action '"+Query.class.getSimpleName()+"' to the plan, chosen by desire: " + desire.toString(), 
						ag.getPlanComponent());
			}
		}
		
		return reval;
	}
	
	class AnswerComp implements Comparator<FolFormula> {
		public int compare(FolFormula a1, FolFormula a2) {
			return a1.toString().compareTo(a2.toString());
		}
	}

	/**
	 * Expresses ignorance about a certain topic
	 */
	public FolFormula expressionOfIgnorance(Query query) {
		FolFormula question = (FolFormula)query.getQuestion();
		FolFormula expr = new Atom(new Predicate("dontKnow("+question.toString()+")")); 
		return expr;
		
	}
	
	@Override
	protected Boolean answerQuery(Desire des, SubgoalGenerationParameter pp, Agent ag) 
	{
		if(!ag.hasCapability("QueryAnswer")) {
			LOG.warn("Agent '{}' does not have Skill: 'QueryAnswer'", ag.getName());
			return false;
		}
		
		
		Query query = (Query) (ag.getActualPerception()); 
		AngeronaAnswer trueAnswer = 
				ag.getBeliefs().getWorldKnowledge().reason((FolFormula)query.getQuestion());
		if(trueAnswer.getAnswerValue() == AnswerValue.AV_COMPLEX) {
			List<FolFormula> answers = new LinkedList<>(trueAnswer.getAnswers());
			Collections.sort(answers, new AnswerComp()); 
			
			List<FolFormula> lies = new LinkedList<>();
			if(generateLies) {
				// create lieing alternatives:
				for(int i=0; i<answers.size(); i++) {
					//if(isClosedQuery(answers.get(i))) {
					if(answers.get(i).isGround()) {
						FolFormula simpleLie = new LyingOperator().lie(answers.get(i));
						lies.add(simpleLie);
					}
				}
			}
		
			// create ignorance alternative:
			FolFormula ignorance = expressionOfIgnorance(query);
			lies.add(ignorance);
			
			Query q = (Query) des.getPerception();
			Subgoal sg = new Subgoal(ag, des);
			createSubgoals(answers, sg, q, new Boolean(false));
			createSubgoals(lies, sg, q, new Boolean(true));
			ag.getPlanComponent().addPlan(sg);
			return true;
		}
		
		return false;
	}
	
	private void createSubgoals(List<FolFormula> answers, Subgoal sg, Query q, Boolean ud) {
		for(int i=0;i<answers.size();i++) {
			Answer a = new Answer(this.getOwner(), q.getSenderId(), q.getQuestion(), answers.get(i));
			sg.newStack(a);
			sg.peekStack(sg.getNumberOfStacks()-1).setUserData(ud);
		}
	}
	
}
