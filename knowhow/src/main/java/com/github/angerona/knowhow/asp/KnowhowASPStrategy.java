package com.github.angerona.knowhow.asp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.sf.tweety.logicprogramming.asplibrary.parser.ASPParser;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLVComplex;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logicprogramming.asplibrary.syntax.ListTerm;
import net.sf.tweety.logicprogramming.asplibrary.syntax.ListTermValue;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.commons.syntax.StringTerm;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.util.Pair;
import com.github.angerona.knowhow.KnowhowBase;
import com.github.angerona.knowhow.parameter.SkillParameter;

/**
 * The default knowhow strategy.
 * @author Tim Janus
 */
public class KnowhowASPStrategy {
	
	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(KnowhowBase.class);
	
	/** the internal step-counter */
	private int step;
	
	/** the name of the initial intention */
	private String initialIntention;

	/**
	 * the program responsible to calculate the next action, nextAction4 of
	 * Regina Fritsch was used as basic
	 */
	private Program nextAction;
	
	/** program contains the world-knowledge of the agent */
	private Program worldKnowledge;
	
	/** program contains atomic actions of the agent */
	private Program atomicActions;
	
	/** the knowhow program containing the knowhow-statement and the rule for nextAction. */
	private Program knowhow;
	
	/** program representing the actual intention tree */
	private Program intentionTree;
	
	/** name of the actual state of the transition system as string */
	private String stateStr;
	
	/** reference to the used solver */
	private DLVComplex solver;
	
	/** the actions in the correct odering to fullfil the plan */
	private Pair<String, HashMap<Integer, Term<?>>> action;
	
	/** saving the last state used for processing */
	private DLPAtom oldState;
	
	/** a stack of answerset-lists which represents the options for backtracking */
	private Stack<AnswerSetList> alternatives = new Stack<>();
	
	/**
	 * a list of skill-parameters helping to map atomic actions in knowhow to
	 * map to the correct Action in Angerona
	 */
	private List<SkillParameter> parameters = new LinkedList<>();
	
	/**
	 * @return unmodifiable list of all SkillParameters used for mapping between
	 *         Knowhow and Angerona
	 */
	public List<SkillParameter> getParameters() {
		return Collections.unmodifiableList(parameters);
	}
	
	/**
	 * Ctor: Creates the default Knowhow-Strategy
	 * @param solverpath	String with path to the dlv-complex solver
	 */
	public KnowhowASPStrategy(String solverpath) {
		solver = new DLVComplex(solverpath);
		
		String programPath = "programs/NextAction.asp";
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(programPath);
		if (is != null) {
			try {
				nextAction = ASPParser.parseProgram(new InputStreamReader(is));
			} catch (net.sf.tweety.logicprogramming.asplibrary.parser.ParseException e) {
				nextAction = null;
				LOG.error("Cannot load the 'nextAction' program: '{}'",
						e.getMessage());
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			LOG.error("Cannot found resource: '{}'", programPath);
		}
	}
	
	/**
	 * @return the number of the last performed step.
	 */
	public int getStep() {
		return step;
	}
	
	/** @return an unmodifiable version of the action found by the planning yet */
	public Pair<String, HashMap<Integer, Term<?>>> getAction() {
		if(action == null)
			return null;
		
		Pair<String, HashMap<Integer, Term<?>>> reval = new Pair<>();
		reval.first = action.first;
		reval.second = new HashMap<>(action.second);
		return reval;
	}
	
	/**
	 * Initialize the knowhow-strategy to calculate plans for a specific agent
	 * @todo handle negative literals with nholds(lit) for example.
	 * @param kb				Reference to the knowhow-base
	 * @param initialIntention	the name of the intention which should be fulfilled by the plan.
	 * @param atomicActions		collection of strings identifying the atomic actions
	 * @param worldKnowledge	collection of strings identifying the world knowledge of the agent.
	 */
	public void init(KnowhowBase kb, String initialIntention, Collection<String> atomicActions, Collection<String> worldKnowledge) {
		stateStr = "intentionAdded";
		intentionTree = new Program();
		this.initialIntention = initialIntention;

		List<Term<?>> terms = new LinkedList<>();
		terms.add(new Constant(initialIntention));
		ListTerm term = new ListTerm(new ListTermValue(terms));
		intentionTree.addFact(new DLPAtom("istack", term)); 
		
		action = null;
		oldState = new DLPAtom("khstate", new ListTerm());
		intentionTree.addFact(oldState);
		intentionTree.addFact(new DLPAtom("state", new Constant(stateStr)));
		Pair<Program, LinkedList<SkillParameter>> reval = null;
		reval = DLPBuilder.buildKnowhowbaseProgram(kb);
		knowhow = reval.first;
		knowhow.add(nextAction);
		setParameters(reval.second);
		this.atomicActions = DLPBuilder.buildAtomicProgram(atomicActions);
		// TODO: Find a way to handle negations as holds... perhaps a new atom hold_neg?
		this.worldKnowledge = DLPBuilder.buildHoldsProgram(worldKnowledge);
		//this.worldKnowledge = new Program();
		
		step = 0;
	}
	
	/**
	 * Performs one step in the context of the knowhow. This means the dlv-complex program
	 * is called once.
	 * @return	 0 	if the processing must be continued
	 * 			-1	if the plan can not be created (no plans to fullfil the intention)
	 * 			>0	the count of actions which must be processed.
	 * @throws SolverException
	 */
	public int performStep() throws SolverException {
		step+=1;
		
		// make union of program parts:
		Program p = new Program();
		p.add(intentionTree);
		p.add(worldKnowledge);
		p.add(atomicActions);
		p.add(knowhow);
		
		LOG.info(p.toString());
		
		// calculate answer sets using dlv-complex:
		AnswerSetList asl = solver.computeModels(p, 10);
		if(asl.size() == 0) {
			return -1;
		}
		
		LOG.trace("AnswerSets:\n'{}'\nfor IntentionTree: '{}'", asl, intentionTree.toString());
		
		// save the answer set as alternative for backtracking
		AnswerSet as = asl.get(0);
		if(asl.size() > 1) {
			asl.remove(0);
			alternatives.push(asl);
		}
				
		// update the intention tree
		if(!updateIntentionTree(as)) {
			return -1;
		} else {
			// mapParameter if action was found
			// return 0 if no action was found yet.
			return mapAction(as) ? 1 : 0;
		}
	}
	
	/**
	 * Searches for an alternative way in the intention tree. 
	 * If an alternative is found the intention tree is prepared
	 * using the alternative
	 * @return	true if an alternative was found, false otherwise.
	 */
	public boolean fallback() {
		action = null;
		if(alternatives.size() == 0)
			return false;
		
		AnswerSetList asl = alternatives.peek();
		AnswerSet as = asl.get(0);
		LOG.info("Fallback to AnswerSet: '{}'", as);
		updateIntentionTree(as);
		asl.remove(0);
		if(asl.size() == 0)
			alternatives.pop();
		return true;
	}

	/**
	 * Helper method: Checks if an action is saved in the given answer-set. 
	 * Then it Maps parameters to the action using the prefix-syntax in the 
	 * knowhow representation.
	 * @param as	Reference to the actual answer-set
	 * @return		true if an action was found and mapped successful, false otherwise.
	 */
	private boolean mapAction(AnswerSet as) {
		Set<DLPLiteral> act = as.getLiteralsWithName("new_act");
		
		for(DLPLiteral action : act) {
			int kh_index = -1;
			int subgoal_index = -1;
			
			// find knowhow index:
			Set<DLPLiteral> khstatements = as.getLiteralsWithName("khstate");
			if(khstatements.size() == 1) {
				ListTerm lst = (ListTerm) khstatements.iterator().next().getAtom().getTerm(0);
				Constant constant = (Constant)lst.get().head();
				int start = constant.get().lastIndexOf('_') + 1;
				String toParse = constant.get().substring(start);
				kh_index = Integer.parseInt(toParse);
			} else {
				// TODO:
			}
			
			// find subgoal index for parameter:
			Set<DLPLiteral> subgoal = as.getLiteralsWithName("new_subgoal");
			if(subgoal.size() == 1) {
				subgoal_index = ((NumberTerm)subgoal.iterator().next().getAtom().getTerm(0)).get();
			} else {
				// TODO:
			}
			
			DLPAtom a = (DLPAtom)action;
			Pair<String, HashMap<Integer, Term<?>>> pair = new Pair<>(((StringTerm)a.getTerm(0)).get(), 
					new HashMap<Integer, Term<?>>());
			Set<SkillParameter> parameters = findParameters(kh_index, subgoal_index);
			for(SkillParameter param : parameters) {
				pair.second.put(param.getParamIndex(), param.getParamValue());
			}
			this.action = pair;
		}
		return this.action != null;
	}
	
	/**
	 * Searches the given answer set for the new_* facts and uses them
	 * to create the intention tree for the next iteration.
	 * @param as The actual answer-set.
	 * @return		true if the given answer-set is valid and the intention tree
	 * 				was created successful, false otherwise.
	 */
	private boolean updateIntentionTree(AnswerSet as) {
		// find new literals for the new intention-tree program:
		DLPAtom new_state = updateAtom(as, "state");
		DLPAtom new_khstate = updateAtom(as, "khstate");
		DLPAtom new_istack = null;
		if(	stateStr.equals("actionPerformed") ||
			stateStr.equals("khAdded")	) {
			new_istack = updateAtom(as, "istack");
		} else {
			Set<DLPLiteral> newLits = as.getLiteralsWithName("istack");
			if(newLits.size() == 1) {
				new_istack = (DLPAtom)newLits.iterator().next();
			} else {
				return false;
			}
		}
		
		if(new_state == null || oldState == null) {
			LOG.error("new_state or old_state must not be null...");
			return false;
		}
		
		if(	new_state.getTerm(0).equals(oldState.getTerm(0))) {
			LOG.error("Old-State and new State are the same: " + new_state.getTerm(0));
			return false;
		} 
		Program toOutput = new Program();
		toOutput.add(intentionTree);
		// rebuild intention-tree program:
		intentionTree.clear();
		oldState = new DLPAtom("state", new_state.getArguments());
		intentionTree.addFact(oldState);
		intentionTree.addFact(new DLPAtom("khstate", new_khstate.getArguments()));
		intentionTree.addFact(new DLPAtom("istack", new_istack.getArguments()));
		
		LOG.info("\n'{}'\nbecomes\n'{}'", toOutput, intentionTree);
		
		// update state:
		if(new_state != null)
			stateStr = ((StringTerm)new_state.getTerm(0)).get();
		
		// proof if a change occurred
		Set<DLPLiteral> act = as.getLiteralsWithName("new_act");
		return  new_state != null ||
				new_istack != null ||
				new_khstate != null ||
				act.size() > 0;
	}
	
	/**
	 * changes the skill-parameters (by reference).
	 * 
	 * @param parameters
	 *            the new list of SkillParameter
	 */
	public void setParameters(List<SkillParameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * finds the Skill-parameter for a specific knowhow-statements subgoal.
	 * 
	 * @param kh_index
	 *            the index (id) of the knowhow-statement
	 * @param subgoal_index
	 *            the index of the subgoal.
	 * @return All Skill-Parameters for subtargets of the knowhow-statement and
	 *         subgoal index.
	 */
	public Set<SkillParameter> findParameters(int kh_index, int subgoal_index) {
		Set<SkillParameter> reval = new HashSet<>();
		for (SkillParameter sp : parameters) {
			if (sp.getKnowhowStatementId() == kh_index
					&& sp.getSubgoalIndex() == subgoal_index) {
				reval.add(sp);
			}
		}
		return reval;
	}

	
	/**
	 * Helper method: finds the new version of the atom in the answer set list.
	 * @param asl	Reference to the answer-set list
	 * @param name	The name of the atom like 'state'
	 * @return	the new atom or null if no new atom exists.
	 */
	private DLPAtom updateAtom(AnswerSet asl, String name) {
		// get new state...
		String error = null;
		Set<DLPLiteral> lits = asl.getLiteralsWithName("new_" + name);
		if(lits.size() == 1) {
			DLPLiteral new_literal = lits.iterator().next();
			if(new_literal instanceof DLPAtom) {
				DLPAtom a = (DLPAtom)new_literal;
				DLPAtom state = new DLPAtom(name, a.getArguments());
				return state; 
			} else {
				error = "'" + new_literal.toString() + "' is no atom. new_ literals must be facts.";
			}
		} else if(lits.size() > 1) {
			error = "There are more than one 'new_" + name + "' literals: " + lits.toString();
		}
		if(error != null)
			LOG.error(error);
		
		Set<DLPLiteral> newLits = asl.getLiteralsWithName(name);
		if(newLits.size() == 0) {
			return null;
		}
		return (DLPAtom)newLits.iterator().next();
	}
	
	public String getInitialIntention() {
		return initialIntention;
	}
}
