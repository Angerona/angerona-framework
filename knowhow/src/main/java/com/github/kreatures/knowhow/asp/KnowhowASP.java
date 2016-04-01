package com.github.kreatures.knowhow.asp;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;
import net.sf.tweety.logics.fol.parser.FolParserB;
import net.sf.tweety.logics.fol.parser.ParseException;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.lp.asp.solver.SolverException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.Desire;
import com.github.kreatures.core.PlanElement;
import com.github.kreatures.core.Subgoal;
import com.github.kreatures.secrecy.operators.ViolatesResult;
import com.github.kreatures.secrecy.operators.parameter.PlanParameter;
import com.github.kreatures.core.comm.Answer;
import com.github.kreatures.core.comm.Inform;
import com.github.kreatures.core.comm.Justification;
import com.github.kreatures.core.comm.Justify;
import com.github.kreatures.core.comm.Query;
import com.github.kreatures.example.operators.SubgoalGenerationOperator;
import com.github.kreatures.example.operators.ViolatesOperator;
import com.github.kreatures.core.logic.KReaturesAnswer;
import com.github.kreatures.core.logic.AnswerValue;
import com.github.kreatures.core.logic.Desires;
import com.github.kreatures.core.logic.asp.SolverWrapper;
import com.github.kreatures.core.operators.OperatorCallWrapper;
import com.github.kreatures.core.operators.parameter.EvaluateParameter;
import com.github.kreatures.core.util.Pair;
import com.github.kreatures.knowhow.KnowhowBase;

/**
 * Subgoal Generation using Knowhow as basic.
 * @author Tim Janus
 */
public class KnowhowASP extends SubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(KnowhowASP.class);
	
	/** reference to the knowhow-strategy which was used at last */
	private KnowhowASPStrategy lastUsedStrategy;
	
	@Override
	protected Boolean processImpl(PlanParameter param) {
		param.report("Using Knowhow for Subgoal Generation.");
		
		boolean gen  = false;
		for (Desire des : param.getAgent().getComponent(Desires.class).getDesires()) {
			// scenario specific tests:
			boolean revReq = false;
			for(Predicate p : des.getFormula().getPredicates()) {
				if(p.getName().equals("attend_scm")) {
					revReq = true;
					break;
				}
			}
			if(revReq) {
				PlanElement next = nextSafeAction("attend_scm", param, des);
				if(next != null) {
					Subgoal sg = new Subgoal(param.getAgent(), des);
					sg.newStack(next);
					gen = gen || param.getActualPlan().addPlan(sg);
				}
			}	

			gen = gen || informProcessing(des, param, param.getActualPlan().getAgent());
			gen = gen || answerQuery(des, param, param.getActualPlan().getAgent());
			gen = gen || onJustify(des, param);
			gen = gen || onJustification(des, param);
		}
		
		lastUsedStrategy = null;
		return gen;
	}
	
	protected Boolean onJustification(Desire des, PlanParameter pp) {
		if(! (des.getPerception() instanceof Justification)) return false;
		
		// scenario dependent:
		PlanElement next = nextSafeAction("got_justification", pp, des);
		if(next == null)
			return false;
		
		Subgoal sg = new Subgoal(pp.getAgent(), des);
		sg.newStack(next);
		
		return pp.getActualPlan().addPlan(sg);
	}
	
	/**
	 * If a justify request is received this method handles it and calls the knowhow with the correct 
	 * start intention
	 * @param des		The desire to react to the justify.
	 * @param pp		The subgoal generation parameter data structure.
	 * @return			True if a safe action was found, false otherwise.
	 */
	protected Boolean onJustify(Desire des, PlanParameter pp) {
		if(! (des.getPerception() instanceof Justify)) 
			return false;
		
		Justify j = (Justify)des.getPerception();
		PlanElement next = nextSafeAction("justification("+ j.getProposition().toString() + ")", pp, des);
		if(next == null)
			return false;
		Subgoal sg = new Subgoal(pp.getAgent(), des);
		sg.newStack(next);
		boolean reval = pp.getActualPlan().addPlan(sg);
		return reval;
	}
	
	/**
	 *  Adapt the default behavior for strike-committee meeting:
	 *  If the revision-request with atom a is received the knowhow is runned with the target:
	 *  'not_sure(a)'. The actual generation of the plan is handled by the runKnowhow
	 *  method.
	 */
	@Override
	protected Boolean informProcessing(Desire des, PlanParameter pp, Agent ag) {
		if(! (des.getPerception() instanceof Inform))
			return false;
		
		boolean reval = false;
		Inform rr = (Inform) des.getPerception();
		if(rr.getSentences().size() == 0)
			return false;
		
		Set<FolFormula> infered = ag.getBeliefs().getWorldKnowledge().infere();
		
		Iterator<FolFormula> itFormulas = rr.getSentences().iterator();
		while(itFormulas.hasNext()) {
			FolFormula ff = itFormulas.next();
			if(	ff instanceof FOLAtom ) {
				if(infered.contains(ff))
					continue;
				FOLAtom atom = (FOLAtom)ff;
				PlanElement next = nextSafeAction("not_sure("+atom.toString() + ")", pp, des);
				if(next == null) {
					continue;
				}
				Subgoal sg = new Subgoal(ag, des);
				sg.newStack(next);
				reval = reval || pp.getActualPlan().addPlan(sg);
			}
		}
		
		return reval;
	}

	/** 
	 * adapt the default behavior for strike-committee meeting: 
	 * Searches for a valid answer in the knowhow until one answer was
	 * found. If no answer is found no reaction is given by the agent.
	 * @todo Answer with Unknown if no valid answer is found.
	 */
	@Override 
	protected Boolean answerQuery(Desire des, PlanParameter param, Agent ag) {
		if(!(des.getPerception() instanceof Query))
			return false;
		
		// run the answer_query knowhow-statement till no more answers are found or an answer
		// was found which does not violates secrecy.
		PlanElement next = nextSafeAction("answer_query", param, des);
		if(next == null) {
			return false;
		}
		
		Subgoal sg = new Subgoal(ag, des);
		sg.newStack(next);
		return param.getActualPlan().addPlan(sg);
	}
	
	/**
	 * Finds the next safe action by appying different knowhow processes until a action is found which
	 * does not violates secrecy.
	 * @param intention		The intitial intention for searching the next safe action
	 * @param param			The subgoal generation parameter data structrue
	 * @param des			The desire which will be fulfilled by the next safe action.
	 * @return				A plan element containing a safe atomic action.
	 */
	public PlanElement nextSafeAction(String intention, PlanParameter param, Desire des) {
		prepareKnowhow(intention, param, des);
		
		PlanElement candidate = null;
		ViolatesResult res = new ViolatesResult(false);
		while(!res.isAlright()) {
			candidate = iterateKnowhow(param, des);
			if(candidate == null)
				return null;
			if(Boolean.parseBoolean(param.getSetting("allowUnsafe", String.valueOf(false))))
				return candidate;
			
			OperatorCallWrapper vop = param.getAgent().getOperators().getPreferedByType(ViolatesOperator.OPERATION_NAME);
			EvaluateParameter eparam = new EvaluateParameter(param.getAgent(), param.getAgent().getBeliefs(), candidate);
			res = (ViolatesResult) vop.process(eparam);
			if(!res.isAlright())
				lastUsedStrategy.fallback();
		}
		return candidate;
	}
	
	/**
	 * Finds the next action without violation checking.
	 * @param intention		The initial intention for searching the next action.
	 * @param param			The subgoal generation parameter data structure
	 * @param des			The desire which will be fulfilled by the next action.
	 * @return				A plan element containing an atomic action.
	 */
	public PlanElement nextAction(String intention, PlanParameter param, Desire des) {
		prepareKnowhow(intention, param, des);
		return iterateKnowhow(param, des);
	}
	
	/**
	 * Helper method: prepares the knowhow processing: It fetches the data for the creation of
	 * the knowhow-strategy. This method initializes a new knowhow-strategy so after the execution
	 * the strategy is ready for iterateKnowhow.
	 * @param intention		start intention used in the intention-tree
	 * @param param			the subgoal-generation parameter data-structure
	 * @param des			The associated desire.
	 */
	private void prepareKnowhow(String intention, PlanParameter param, Desire des) {
		Agent ag = param.getAgent();
		
		LOG.info("Running Knowhow with intention: '{}' for desire: '{}'.", 
				intention, des.toString());
		
		// Gathering knowhow information
		Set<FolFormula> infered = ag.getBeliefs().getWorldKnowledge().infere();
		
		Collection<String> worldKB = new HashSet<>();
		for(FolFormula f : infered) {
			if(f instanceof Negation) {
				worldKB.add("NEG_"+f.toString().substring(1));
			} else if (f instanceof FOLAtom ){
				worldKB.add(f.toString());
			}
		}
		Collection<String> actions = ag.getCapabilities();

		KnowhowBase kb = (KnowhowBase)ag.getComponent(KnowhowBase.class);
		if(kb == null) {
			throw new RuntimeException("Agent '"+ag.getName()+"' has no KnowhowBase but tries to using '"+this.getClass().getSimpleName()+"'.");
		}
		
		// create and initialize the knowhow strategy
		lastUsedStrategy = new KnowhowASPStrategy(SolverWrapper.DLV_COMPLEX.getSolverPath());
		lastUsedStrategy.init(kb, intention, actions, worldKB);
	}

	/**
	 * Iterates the Knwohow this means the next-action program is called until the
	 * entire knowhow was searched or the first atomic intention was found.
	 * @param param	The subgoal-generation data-structure
	 * @param des	The associated desire
	 * @return		A plan element containing an atomic action.
	 */
	private PlanElement iterateKnowhow(PlanParameter param, Desire des) {
		// iterate knowhow algorithm until a action is found.
		int reval = 0;
		boolean calcKH = true;
		while(calcKH) {
			try {
				reval = lastUsedStrategy.performStep();
			} catch (SolverException e) {
				LOG.error("Knowhow-Base cannot use Solver: '{}'", e.getMessage());
				e.printStackTrace();
				return null;
			}
			
			if(reval != 0) {
				calcKH = false;
			}
		}
		
		// if no action was found return false
		if(reval == -1 || lastUsedStrategy.getAction() == null) {
			param.report("Knowhow was not able to generate atomic action for: '"+lastUsedStrategy.getInitialIntention()+"'");
			return null;
		}
		
		// otherwise update KReatures plan component
		return createAtomicAction(param, des);
	}

	/**
	 * This method creates a Subgoal containing an atomic action by mapping the action found by the knowhow
	 * into the KReatures System.
	 * @todo replace string tests to with something faster to determine the capabilitity (Skill)
	 * @param param		Subgoal-Generation parameter data-structure
	 * @param des		The associated desire.
	 * @return			A Skill context pair representing the last found atomic action or null if an error occurred.
	 */
	private PlanElement createAtomicAction(PlanParameter param, Desire des) {
		// iterate over all actions found by the Knowhow (at the moment this is only one)
		Pair<String, HashMap<Integer, Term<?>>> action = lastUsedStrategy.getAction();
		
		// test if the skill exists
		Agent ag = param.getAgent();
		String skillName = action.first.substring(2);
		if(!ag.hasCapability(skillName)) {
			LOG.warn("Knowhow found Skill '{}' but the Agent '{}' does not support the Skill.", skillName, ag.getName());
			return null;
		}
		
		// create the action using the SkillParameter map and the name of the skill
		Action act = null;
		// String tests are not perfect here.
		if(skillName.equals("Inform")) {
			act = createInform(action.second, param);
		} else if(skillName.equals("Query")) {
			act = createQuery(action.second, param);
		} else if(skillName.equals("QueryAnswer")) {
			act = createQueryAnswer(action.second, (Query)des.getPerception(), param);
		} else if (skillName.equals("Justify")) { 
			act = createJustify(action.second, (Inform)des.getPerception(), param);
		} else if(skillName.equals("Justification")) {
			act = createJustification(action.second, (Justify)des.getPerception(), param);
		} else {
			LOG.error("The parameter mapping for Skill '{}' is not implemented yet.", skillName);
			return null;
		}
		
		if(act == null) {
			LOG.error("The Skill '{}' could not be created, subgoal processing canceled.", skillName);
			return null;
		}
		
		// create the Subgoal which will be returned and report this step to KReatures.
		param.report("Knowhow finds new atomic action '" + act.getClass().getSimpleName() + "'");
		return new PlanElement(act);
	}
	
	/**
	 * Helper method: Creates an instance of the Justification class from the parameter-map given
	 * by the knowhow part of the program.
	 * @param paramMap		Reference to the map representing the parameters
	 * @return				An object of type Justification which represents the KReatures version of the
	 * 						action found by the knowhow.
	 */
	protected Justification createJustification(Map<Integer, Term<?>> paramMap, Justify reason, PlanParameter pp) {
		if(paramMap.size() != 1) {
			LOG.error("Knowhow found Skill '{}' but there are '{}' parameters instead of 2", 
					"Justification", paramMap.size());
			return null;
		}
		
		String var = getVarWithPrefix(0, paramMap);
		FolFormula atom = processVariable(var, pp);
		
		
		return new Justification(reason, atom);
	}
	
	/**
	 * Helper method: Creates an instance of the Justify class from the parameter-map given
	 * by the knowhow part of the program.
	 * @param paramMap		Reference to the map representing the parameters
	 * @return				An object of type Justify which represents the KReatures version of the
	 * 						action found by the knowhow.
	 */
	protected Justify createJustify(Map<Integer, Term<?>> paramMap, Inform reason, PlanParameter pp) {
		if(paramMap.size() != 2) {
			LOG.error("Knowhow found Skill '{}' but there are '{}' parameters instead of 2", 
					"Justify", paramMap.size());
			return null;
		}
		
		String var = getVarWithPrefix(0, paramMap);
		Agent receiver = processVariable(var, pp);
		
		var = getVarWithPrefix(1, paramMap);
		FolFormula atom = processVariable(var, pp);
		
		return new Justify(pp.getAgent(), receiver.getName(), atom);
	}
	
	/**
	 * Helper method: Creates an instance of the Inform class from the parameter-map given
	 * by the knowhow part of the program.
	 * @param paramMap		Reference to the map representing the parameters
	 * @return				An object of type Inform which represents the KReatures version of the
	 * 						action found by the knowhow.
	 */
	protected Inform createInform(Map<Integer, Term<?>> paramMap, PlanParameter pp) {
		if(paramMap.size() != 2) {
			LOG.error("Knowhow found Skill '{}' but there are '{}' parameters instead of 2", "Inform", paramMap.size());
			return null;
		}
		
		String var = getVarWithPrefix(0, paramMap);
		Agent receiver = processVariable(var, pp);
		
		var = getVarWithPrefix(1, paramMap);
		FolFormula atom = processVariable(var, pp);
		
		return new Inform(pp.getAgent(), receiver.getName(), atom);
	}
	
	/**
	 * Helper method: Creates an instance of the Query class from the parameter-map given
	 * by the knowhow part of the program.
	 * @param paramMap		Reference to the map representing the parameters
	 * @return				An object of type Query which represents the KReatures version of the
	 * 						action found by the knowhow.
	 */
	protected Query createQuery(Map<Integer, Term<?>> paramMap, PlanParameter pp) {
		String var = getVarWithPrefix(0, paramMap);
		Agent receiver = processVariable(var, pp);
		
		var = getVarWithPrefix(1, paramMap);
		FolFormula atom = processVariable(var, pp);
		
		return new Query(pp.getAgent(), receiver.getName(), atom);
	}
	
	/**
	 * Helper method: Creates an instance of the QueryAnswer class from the parameter-map given
	 * by the knowhow part of the program.
	 * @todo use lying methods
	 * @param paramMap		Reference to the map representing the parameters
	 * @return				An object of type QueryAnswer which represents the KReatures version of the
	 * 						action found by the knowhow.
	 */
	protected Answer createQueryAnswer(Map<Integer, Term<?>> paramMap, Query reason, PlanParameter pp) {
		String var = getVarWithPrefix(0, paramMap);
		boolean honest = false;
		if(var.equals("p_honest")) {
			honest = true;
		} else if(var.equals("p_lie")) {
			honest = false;
		} else {
			LOG.error("The first parameter of s_Answer has to be: " +
						"'p_honest' or 'p_lie' but '{}' was given.", var);
			return null;
		}
		KReaturesAnswer aa = pp.getAgent().reason(reason.getQuestion());
		if(!honest) {
			// TODO: Move into lying operator... 
			if(aa.getAnswerValue() == AnswerValue.AV_COMPLEX) {
				// TODO: make complex lie
			} else {
				AnswerValue av = aa.getAnswerValue();
				
				if(av == AnswerValue.AV_TRUE) {
					av = AnswerValue.AV_FALSE;
				} else if(av == AnswerValue.AV_FALSE) {
					av = AnswerValue.AV_TRUE;
				}
				
				aa = new KReaturesAnswer(aa.getQueryFOL(), av);
			}
		}
		
		pp.report((honest ? "Honest answer " : "Lie ") + "for: '"+reason.getQuestion().toString()+"' = " + aa.getAnswerValue().toString());

		return new Answer(pp.getAgent(), reason.getSenderId(), reason.getQuestion(), aa);
	}
	
	/**
	 * Helper method: checks if the given index is in the parameter map, if this is not the case some error output
	 * is produced.
	 * @param index		index of the parameter
	 * @param paramMap	the parameter-map 
	 * @return			A String representing the value of the parameter.
	 */
	protected String getVarWithPrefix(int index, Map<Integer, Term<?>> paramMap) {
		String var = paramMap.get(index).toString();
		if(var == null) {
			LOG.error("The mapping of the variables is wrong. We assume index 0-x are used to represent the x variables of the used Skill.");
		}
		return var;
	}
	
	/**
	 * Helper method: checks for prefixes and creates the correct objects from the given parameter/variable.
	 * @param variableWithPrefix	String containing the value
	 * @return	An object of generic Type. Might be an agent or an FOL-Atom or a string.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T processVariable(String variableWithPrefix, PlanParameter pp) {
		if(variableWithPrefix.startsWith("a_")) {
			return (T) this.getAgent(variableWithPrefix.substring(2), pp);
		} else {
			FOLAtom temp =  createAtom(variableWithPrefix);
			List<Term<?>> terms = new LinkedList<>();
			for(Term<?> t : temp.getArguments()) {
				// TODO: Test if that works:
				//if(t.getName().charAt(1) == '_') {
				if(t.toString().charAt(1) == '_') {
					Agent newName = processVariable(t.toString(), pp);
					terms.add(new Constant(newName.getName()));
				} else {
					terms.add(t);
				}
			}
			return (T) new FOLAtom(temp.getPredicate(), terms);
		}
	}
	
	/**
	 * Helper method: Creates a FOL-Atom using the given string
	 * @param formula	String which will be parsed to a FolFormula (Atom).
	 * @return			The FOL-Atom of the given String
	 */
	private FOLAtom createAtom(String formula) {
		FolParserB parser = new FolParserB(new StringReader(formula));
		try {
			FOLAtom reval = parser.atom(new FolSignature());
			return reval;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Helper method: Searchs for the agent with the given name.
	 * @param name		The agent's name
	 * @return			Reference to the Agent with the given name or null if
	 * 					no such Agent exists.
	 */
	private Agent getAgent(String name, PlanParameter pp) {
		if(name.equals("SELF"))
			return pp.getAgent();
		
		Agent reval = pp.getAgent().getEnvironment().getAgentByName(name);
		if(reval == null) {
			LOG.warn("Knowhow tries to find Agent with name '{}' but its not part of the Enviornment", name);
		}
		return reval;
	}
}