package angerona.fw.example.operators;

import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.parser.FolParserB;
import net.sf.tweety.logics.firstorderlogic.parser.ParseException;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Desire;
import angerona.fw.Intention;
import angerona.fw.Subgoal;
import angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.Desires;
import angerona.fw.logic.ScriptingComponent;

/**
 * Default subgoal generation generates the atomic actions need to react on the
 * different speech acts. Subclasses can use the default behavior to react to speech
 * acts.
 * Also implements specialized methods for the simple version of the strike-committee-example.
 * @author Tim Janus
 */
public class SubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(SubgoalGenerationOperator.class);
	
	@Override
	protected Boolean processInternal(PlanParameter pp) {
		LOG.info("Run Default-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();

		boolean reval = processPersuadeOtherAgentsDesires(pp, ag);

		Desires des = ag.getComponent(Desires.class);
		if(des != null) {
			Set<Desire> currentDesires;
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareQueryProcessing);
			for(Desire d : currentDesires) {
				reval = reval || answerQuery(d, pp, ag);
			}
			
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareRevisionRequestProcessing);
			for(Desire d : currentDesires) {
				reval = reval || revisionRequest(d, pp, ag);
			}
			
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareScriptingProcessing);
			pp.report("desires:"+des.getDesires());
			for(Desire d : currentDesires){
				reval = processScripting(d, pp, ag);
			}
		}
		
		if(!reval)
			pp.report("No new subgoal generated.");
		return reval;
	}

	public boolean processScripting(Desire d, PlanParameter pp, Agent ag){
		ScriptingComponent script = ag.getComponent(ScriptingComponent.class);
		Desires desires = ag.getComponent(Desires.class);
		desires.remove(d);
		List<Intention> intentions = script.getIntentions();
		String text = intentions.toString();

		int i = 0;
		for(Intention intention : intentions) {
			Desire des = new Desire(new FOLAtom(new Predicate("script"+ i++)));
			desires.add(des);
			Subgoal sg = new Subgoal(ag, des);
			sg.newStack(intention);
			ag.getPlanComponent().addPlan(sg);
		}

		pp.report("Add the new  actions '" + text + 
				"' to the plan, chosen by desire: " + d.toString(), 
				ag.getPlanComponent());
		return true;
	}
	
	/**
	 * This is a helper method: Which searches for desires starting with the prefix 'v_' or 'q_'.
	 * It creates simple Inform or Query plans for such desires.
	 * @param pp		The data-structure containing parameters for the operator.
	 * @param ag		The agent.
	 * @return			true if a new subgoal was created and added to the master-plan, false otherwise.
	 */
	protected boolean processPersuadeOtherAgentsDesires(
			PlanParameter pp, Agent ag) {
		boolean reval = false;
		Desires desComp = ag.getComponent(Desires.class);
		if(desComp == null)
			return false;

		for(Desire desire : desComp.getDesires()) {
			// only add a plan if no plan for the desire exists.
			if(ag.getPlanComponent().countPlansFor(desire) > 0)
				continue;
			
			FolFormula f = desire.getFormula();
			if(!(f instanceof FOLAtom)) 
				continue;
			
			FOLAtom atom = (FOLAtom)f;
			String predicateName = atom.getPredicate().getName();
			boolean informDesire = predicateName.startsWith("v_");
			boolean queryDesire = predicateName.startsWith("q_");
			
			if(informDesire || queryDesire) {
				int si = predicateName.indexOf("_")+1;
				if(si == -1)
					continue;
				
				String recvName = predicateName.substring(si);
				// check if the receiving agent exists in the simulation:
				if(pp.getAgent().getEnvironment().getAgentByName(recvName) == null) {
					LOG.warn("The agent '{}' referred in desire: '{}' does not exist",
							recvName, desire.toString());
					continue;
				}
				
				if(atom.getArguments().size() < 1)
					continue;
				String content = atom.getArguments().get(0).toString();
				
				// generate information log message:
				StringBuffer buf = new StringBuffer();
				buf.append("'{}' ");
				if(informDesire) {
					buf.append("wants '" + recvName + "' to believe: ");
				} else if(queryDesire) {
					buf.append("asks '" + recvName + "' for: ");
				}
				buf.append("'{}'");
				LOG.info(buf.toString(),  ag.getName(), content);
		
				// create plan...
				Subgoal sg = new Subgoal(ag, desire);
				FolParserB parser = new FolParserB(new StringReader(content));
				FolFormula a = null;
				try {
					a = parser.atom(new FolSignature());
				} catch (ParseException e) {
					System.err.println("parsing: " + content);
					e.printStackTrace();
				}
				
				// check if the negation shall be used:
				if(atom.getArguments().size() >= 3) {
					if(atom.getArguments().get(2).equals(new Constant("neg"))) {
						a = new Negation(a);
					}
				}
				
				// and add action (Inform or Query) to the plan stack:
				Class<?> actionCls = null;
				if(informDesire) {
					sg.newStack( new Inform(ag, recvName, a));
					actionCls = Inform.class;
				} else {
					sg.newStack( new Query(ag, recvName, a));
					actionCls = Query.class;
				}
				// add a report to the simulation about the new plan:
				pp.report("Add the new atomic action '" + actionCls.getSimpleName() + 
						"' to the plan, choosed by desire: " + desire.toString());
				ag.getPlanComponent().addPlan(sg);
				
				
				reval = true;
			}
		}
		return reval;
	}

	/**
	 * Helper method: Reacts on desires which were created by a Query. The default implementation creates two answers for the query:
	 * one with the answer-value true the other with the answer-value false.
	 * When the query is a complex query then the answers and their lies are generated.
	 * @param des	The desire containing the query to answer
	 * @param pp	The data-structure containing parameters for the operator.
	 * @param ag	The agent
	 * @return		true if a new subgoal was created and added to the master-plan, false otherwise.
	 */
	protected Boolean answerQuery(Desire des, PlanParameter pp, Agent ag) {
		Subgoal answer = new Subgoal(ag, des);
		Query q = (Query) des.getPerception();
		
		BaseBeliefbase bb = ag.getBeliefs().getWorldKnowledge();
		AngeronaAnswer aa = bb.reason(q.getQuestion());
		
		boolean generateLies = Boolean.parseBoolean(pp.getSetting("generateLies", String.valueOf(true)));	
		
		Answer honest = null;
		Answer lie = null;
		
		if(aa.getAnswerValue() == AnswerValue.AV_TRUE ||
				aa.getAnswerValue() == AnswerValue.AV_FALSE) {
			// the default behavior on an query is to answer the query
			// truthful. But also an alternative plan is generated which gives
			// the opposite answer (a lie) which can be used if a secret is
			// not safe by using the truthful answer.
			AnswerValue real = aa.getAnswerValue();
			AnswerValue invert = AnswerValue.AV_TRUE;
			if(real == AnswerValue.AV_TRUE) {
				invert = AnswerValue.AV_FALSE;
			}
			
			honest = new Answer(ag, q.getSenderId(), q.getQuestion(), real);
			answer.newStack(honest);
			
			
			if(generateLies) {
				lie = new Answer(ag, q.getSenderId(), q.getQuestion(), invert);
				answer.newStack(lie);
			}
			
		} else if(	aa.getAnswerValue() == AnswerValue.AV_UNKNOWN || 
					aa.getAnswerValue() == AnswerValue.AV_REJECT) {
			// use the answer value returned by reasoner (unknown or rejected).
			honest = new Answer(ag, q.getSenderId(), q.getQuestion(), aa.getAnswerValue());
			answer.newStack(honest);
			
			if(generateLies) {
				// generate alternative plans if a secret is not safe with the
				// real answer (lie by answering the query with true or false).
				answer.newStack(new Answer(ag, q.getSenderId(), 
						q.getQuestion(), AnswerValue.AV_TRUE));
			
				answer.newStack(new Answer(ag, q.getSenderId(), 
						q.getQuestion(), AnswerValue.AV_FALSE));
			}
			
		} else if(aa.getAnswerValue() == AnswerValue.AV_COMPLEX) {
			List<FolFormula> answers = new LinkedList<>(aa.getAnswers());
			
			Collections.sort(answers, new Comparator<FolFormula>() {
				public int compare(FolFormula a1, FolFormula a2) {
					return a1.toString().compareTo(a2.toString());
				}
			}); 
			
			List<FolFormula> lies = new LinkedList<>();
			if(generateLies) {
				// create lieing alternatives:
				for(int i=0; i<answers.size(); i++) {
					//if(isClosedQuery(answers.get(i))) {
					if(answers.get(i).isGround()) {
						FolFormula simpleLie = generateLie(answers.get(i));
						lies.add(simpleLie);
					}
				}
			}
			
			Query query = (Query) des.getPerception();
			Subgoal sg = new Subgoal(ag, des);
			createSubgoals(answers, sg, query, new Boolean(false), ag);
			createSubgoals(lies, sg, query, new Boolean(true), ag);
			ag.getPlanComponent().addPlan(sg);
			return true;
		}
		
		pp.report("Add the honest answer '"+ honest.toString() + "' as alternative plan");
		if(lie != null)
			pp.report("Add the lie '"+ lie.toString() + "' as alternative plan");
		ag.getPlanComponent().addPlan(answer);
		
		return true;
	}
	
	private void createSubgoals(List<FolFormula> answers, Subgoal sg, Query q, Boolean ud, Agent ag) {
		for(int i=0;i<answers.size();i++) {
			Answer a = new Answer(ag, q.getSenderId(), q.getQuestion(), answers.get(i));
			sg.newStack(a);
			sg.peekStack(sg.getNumberOfStacks()-1).setUserData(ud);
		}
	}
		
	protected FolFormula generateLie(FolFormula truth) {
		if(truth instanceof Negation) {
			return ((Negation)truth).getFormula();
		} else {
			return new Negation(truth);
		}
	}
	
	/**
	 * Helper method for handling desires which are created by a revision-request of an other agent.
	 * The default implementation does nothing generic and subclasses are free to implement their own
	 * behavior. Nevertheless the method 'implements' the default behavior for the simple SCM 
	 * scenario: It queries the sender of the revision-request 'excused' for 'attend_scm'.
	 * @param des	The desire created by the revision-request perception
	 * @param pp	The data-structure containing parameters for the operator.
	 * @param ag	The agent.
	 * @return		true if a new subgoal was created and added to the master-plan, false otherwise.
	 */
	protected Boolean revisionRequest(Desire des, PlanParameter pp, Agent ag) {
		// three cases: accept, query (to ensure about something) or deny.
		// in general we will accept all Revision queries but for the scm example
		// it is proofed if the given atom is 'excused' and if this is the case
		// first of all attend_scm is queried.
		if(!(des.getPerception() instanceof Inform))
			return false;
		
		Inform rr = (Inform) des.getPerception();
		if(rr.getSentences().size() == 1) {
			FolFormula ff = rr.getSentences().iterator().next();
			if(	ff instanceof FOLAtom && 
				((FOLAtom)ff).getPredicate().getName().equalsIgnoreCase("ask_for_excuse")) {
				FOLAtom reasonToFire = new FOLAtom(new Predicate("attend_scm"));
				AngeronaAnswer aa = ag.getBeliefs().getWorldKnowledge().reason(reasonToFire);
				if(aa.getAnswerValue() == AnswerValue.AV_UNKNOWN) {
					Subgoal sg = new Subgoal(ag, des);
					sg.newStack(new Query(ag, rr.getSenderId(), reasonToFire));
					pp.report("Add the new action '" + Query.class.getSimpleName() + "' to the plan.");
					ag.getPlanComponent().addPlan(sg);
				} else if(aa.getAnswerValue() == AnswerValue.AV_FALSE) {
					return false;
				}
				return true;
			}
		}
			
		return false;
	}
}
