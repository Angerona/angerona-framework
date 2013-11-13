package com.github.angerona.fw.example.operators.courtroom;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;
import net.sf.tweety.logics.fol.parser.FolParserB;
import net.sf.tweety.logics.fol.parser.ParseException;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;
import net.sf.tweety.logics.fol.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.comm.Answer;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.example.operators.GenerateOptionsOperator;
import com.github.angerona.fw.example.operators.SubgoalGenerationOperator;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.util.Utility;

/**
* This implementation of a subgoal generation operator allows for the asking of multiple, detail query-type questions. 
* Agents can respond to open queries by considering one of many truthful answers.
* For a closed query they are capable of considering telling the opposite of the truth.
* They can also consider an answer of "I don't know" for any query.
* The expression of ignorance "I don't know" is always marked as a lie by this operator. 
* @author Daniel Dilger, Tim Janus
*/
public class CourtroomPlanner extends SubgoalGenerationOperator {
	private static Logger LOG = LoggerFactory.getLogger(CourtroomPlanner.class);
	
	/** flag indicating if the lies should be generated. */
	private boolean generateLies = false;
	
	@Override
	protected Boolean processInternal(PlanParameter pp) {
		LOG.info("Run Mary-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();
		
		boolean reval = interrogateOtherAgent(pp, ag);


		Desires des = ag.getComponent(Desires.class);
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
				reval = reval || informProcessing(d, pp, ag);
			}
		}
		
		if(!reval)
			pp.report("No new subgoal generated.");
		return reval;
	}
	
	public boolean interrogateOtherAgent(PlanParameter pp, Agent ag) {

		boolean reval = false;
		Desires compDes = ag.getComponent(Desires.class);
		if(compDes == null)
			return false;
		
		/**
		 * Should be way to ensure that what is being parsed is in fact an integer
		 */
		class DesireComp implements Comparator<Desire> {
			
			public int compare(Desire f1, Desire f2)
			{
				if(! (f1.getFormula() instanceof FOLAtom) ) {
					return -1;
				}
				if(! (f2.getFormula() instanceof FOLAtom) ) {
					return -1;
				}
				if(	((FOLAtom)f1.getFormula()).getArguments().size() < 2) {
					return -1;
				} else if( ((FOLAtom)f2.getFormula()).getArguments().size() < 2 ) {
					return 1;
				}
					
				Term<?> t1 = ((FOLAtom)f1.getFormula()).getArguments().get(1);
				Term<?> t2 = ((FOLAtom)f2.getFormula()).getArguments().get(1);
				
				if(! (t1 instanceof NumberTerm)) {
					return -1;
				} else if(! (t2 instanceof NumberTerm)) {
					return 1;
				}
				
				int i1 = ((NumberTerm)t1).get();
				int i2 = ((NumberTerm)t2).get();
				
				return i1-i2;
			}
		}
		
		Desire[] desires = new Desire[compDes.getDesires().size()];
		compDes.getDesires().toArray(desires);
		Arrays.sort(desires, new DesireComp());

		int numDesires = desires.length;
		for(int i=numDesires-1;i>=0;i--)
		{
			Desire des = desires[i];
			if(! (des.getFormula() instanceof FOLAtom) ) {
				continue;
			}
			FOLAtom atom = (FOLAtom) desires[i].getFormula();
			if(atom.getPredicate().getName().startsWith("q_"))
			{
				if(!ag.hasCapability("Query")) {
					LOG.warn("'{}' has not the capability to perform: '{}'.", ag.getName(), "Query");
					continue;
				}
			
				if(atom.getArguments().size()==0) {
					continue;
				}
				
				FolParserB parser = new FolParserB(new StringReader(atom.getArguments().get(0).toString()));
				FolFormula realLiteral;
				try {
					realLiteral = parser.atom(new FolSignature());
				} catch (ParseException e) {
					LOG.warn("The argument in paranthesses can not be parsed: '{}' by FolParser", atom.getArguments().get(0).toString());
					e.printStackTrace();
					continue;
				}
				if(atom.getArguments().size()>=3) {
					Term<?> t = atom.getArguments().get(2);
					if(t instanceof Constant) {
						Constant c = (Constant)t;
						if(c.get().compareToIgnoreCase("neg") == 0) {
							realLiteral = new Negation(realLiteral);
						}
					}
				}
				
				String recvName = atom.getPredicate().getName().substring(2);
				
				Subgoal sg = new Subgoal(ag, des);
				sg.newStack(new Query(ag, recvName, realLiteral));
				ag.getPlanComponent().addPlan(sg);
				reval = true;
				pp.report("Add the new atomic action '"+Query.class.getSimpleName()+"' to the plan, chosen by desire: " + atom.toString(), 
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
		FolFormula expr = new FOLAtom(new Predicate("dontKnow("+question.toString()+")")); 
		return expr;
		
	}
	
	@Override
	protected Boolean answerQuery(Desire des, PlanParameter pp, Agent ag) 
	{
		if(!ag.hasCapability("QueryAnswer")) {
			LOG.warn("Agent '{}' does not have the capability to perform: 'QueryAnswer'", ag.getName());
			return false;
		}
		
		
		Query query = (Query) des.getPerception();
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
						FolFormula simpleLie = Utility.lie(answers.get(i));
						lies.add(simpleLie);
					}
				}
			}
		
		
			Query q = (Query) des.getPerception();
			Subgoal sg = new Subgoal(ag, des);
		
			createSubgoals(answers, sg, q, new Boolean(false), ag);
			createSubgoals(lies, sg, q, new Boolean(true), ag);
			
			Answer dontKnow = new Answer(ag, q.getSenderId(), q.getQuestion(), AnswerValue.AV_UNKNOWN);
			sg.newStack(dontKnow);
			sg.peekStack(sg.getNumberOfStacks()-1).setUserData(new Boolean(true));
			
			ag.getPlanComponent().addPlan(sg);
			return true;
		}
		
		return super.answerQuery(des, pp, ag);
	}
	
	private void createSubgoals(List<FolFormula> answers, Subgoal sg, Query q, Boolean ud, Agent ag) {
		for(int i=0;i<answers.size();i++) {
			Answer a = new Answer(ag, q.getSenderId(), q.getQuestion(), answers.get(i));
			sg.newStack(a);
			sg.peekStack(sg.getNumberOfStacks()-1).setUserData(ud);
		}
	}
	
}
