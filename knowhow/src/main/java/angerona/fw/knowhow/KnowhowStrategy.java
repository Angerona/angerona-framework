package angerona.fw.knowhow;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logicprogramming.asplibrary.parser.ParseException;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLVComplex;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.ListTerm;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.syntax.StdTerm;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Term;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.util.Pair;

/**
 * The default knowhow strategy.
 * @author Tim Janus
 */
public class KnowhowStrategy {
	
	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(KnowhowBase.class);
	
	private int step;
	
	private String initialIntention;
	
	private KnowhowBase knowhowBase;
	
	/** program contains the world-knowledge of the agent */
	private Program worldKnowledge;
	
	/** program contains atomic actions of the agent */
	private Program atomicActions;
	
	/** the knowhow program containing the knowhow-statement and the rule for nextAction. */
	private Program knowhow;
	
	/** program representing the actual intention tree */
	private Program intentionTree;
	
	private Program visited;
	
	/** name of the actual state of the transition system as string */
	private String stateStr;
	
	/** reference to the used solver */
	private DLVComplex solver;
	
	/** the actions in the correct odering to fullfil the plan */
	private Pair<String, HashMap<Integer, String>> action;
	
	/** saving the last state used for processing */
	private Atom oldState;
	
	private Stack<AnswerSetList> alternatives = new Stack<>();
	
	/**
	 * Ctor: Creates the default Knowhow-Strategy
	 * @param pathtodlv	String with path to the dlv-complex solver
	 */
	public KnowhowStrategy(String solverpath) {
		solver = new DLVComplex(solverpath);
	}
	
	/**
	 * @return the last performed step.
	 */
	public int getStep() {
		return step;
	}
	
	/** @return an unmodifiable version of the action found by the planning yet */
	public Pair<String, HashMap<Integer, String>> getAction() {
		if(action == null)
			return null;
		
		Pair<String, HashMap<Integer, String>> reval = new Pair<>();
		reval.first = action.first;
		reval.second = new HashMap<>(action.second);
		return reval;
	}
	
	/** informs the knowhow-strategy that he agent has performed one action 
	 * 	the knowhow-strategy removes the action from its list of open actions.
	 */
	public void actionDone() {
		// TODO
	}
	
	/**
	 * Initialize the knowhow-strategy to calculate plans for a specific agent
	 * @param kb				Reference to the knowhow-base
	 * @param initialIntention	the name of the intention which should be fulfilled by the plan.
	 * @param atomicActions		collection of strings identifying the atomic actions
	 * @param worldKnowledge	collection of strings identifying the world knowledge of the agent.
	 */
	public void init(KnowhowBase kb, String initialIntention, Collection<String> atomicActions, Collection<String> worldKnowledge) {
		this.knowhowBase = kb;
		stateStr = "intentionAdded";
		intentionTree = new Program();
		this.initialIntention = initialIntention;

		ELPParser parser = new ELPParser(new StringReader("istack(["+initialIntention+"])."));
		Rule r = null;
		try {
			r = parser.rule();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(r != null) {
			intentionTree.add(r); 
		}
		
		action = null;
		visited = new Program();
		oldState = new Atom("khstate", new ListTerm(new LinkedList<Term>(), 
				new LinkedList<Term>()));
		intentionTree.add(oldState);
		intentionTree.add(new Atom("state", new StdTerm(stateStr)));
		Pair<Program, LinkedList<SkillParameter>> reval = null;
		reval = KnowhowBuilder.buildKnowhowbaseProgram(kb, false);
		knowhow = reval.first;
		knowhow.add(kb.getNextActionProgram());
		knowhowBase.setParameters(reval.second);
		this.atomicActions = KnowhowBuilder.buildAtomicProgram(atomicActions);
		// TODO: Find a way to handle negations as holds... perhaps a new atom hold_neg?
		//this.worldKnowledge = KnowhowBuilder.buildHoldsProgram(worldKnowledge);
		this.worldKnowledge = new Program();
		
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
		p.add(visited);
		
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
			return mapParameter(as) ? 1 : 0;
		}
	}

	private boolean mapParameter(AnswerSet as) {
		Set<Literal> act = as.getLiteralsBySymbol("new_act");
		
		for(Literal action : act) {
			int kh_index = -1;
			int subgoal_index = -1;
			
			// find knowhow index:
			Set<Literal> khstatements = as.getLiteralsBySymbol("khstate");
			if(khstatements.size() == 1) {
				ListTerm lst = (ListTerm) khstatements.iterator().next().getAtom().getTerm(0);
				Term t = lst.head();
				int start = t.get().lastIndexOf('_') + 1;
				String toParse = t.get().substring(start);
				kh_index = Integer.parseInt(toParse);
			} else {
				// TODO:
			}
			
			// find subgoal index for parameter:
			Set<Literal> subgoal = as.getLiteralsBySymbol("new_subgoal");
			if(subgoal.size() == 1) {
				subgoal_index = subgoal.iterator().next().getAtom().getTermInt(0);
			} else {
				// TODO:
			}
			
			Atom a = (Atom)action;
			Pair<String, HashMap<Integer, String>> pair = new Pair<>(a.getTerm(0).get(), 
					new HashMap<Integer, String>());
			Set<SkillParameter> parameters = knowhowBase.findParameters(kh_index, subgoal_index);
			for(SkillParameter param : parameters) {
				pair.second.put(param.paramIndex, param.paramValue);
			}
			this.action = pair;
		}
		return this.action != null;
	}
	
	private boolean updateIntentionTree(AnswerSet as) {
		// find new literals for the new intention-tree program:
		Atom new_state = updateAtom(as, "state");
		Atom new_khstate = updateAtom(as, "khstate");
		Atom new_istack = null;
		if(	stateStr.equals("actionPerformed") ||
			stateStr.equals("khAdded")	) {
			new_istack = updateAtom(as, "istack");
		} else {
			Set<Literal> newLits = as.getLiteralsBySymbol("istack");
			if(newLits.size() == 1) {
				new_istack = (Atom)newLits.iterator().next();
			} else {
				return false;
			}
		}
		
		if(	new_state.getTermStr(0).equals(oldState.getTermStr(0))) {
			LOG.error("Old-State and new State are the same: " + new_state.getTermStr(0));
			return false;
		} 
		Program toOutput = new Program();
		toOutput.add(intentionTree);
		// rebuild intention-tree program:
		intentionTree.clear();
		oldState = new Atom("state", new_state.getTerms());
		intentionTree.add(oldState);
		intentionTree.add(new Atom("khstate", new_khstate.getTerms()));
		intentionTree.add(new Atom("istack", new_istack.getTerms()));
		
		LOG.info("\n'{}'\nbecomes\n'{}'", toOutput, intentionTree);
		
		// update state:
		if(new_state != null)
			stateStr = new_state.getTermStr(0);
		
		// proof if a change occurred
		Set<Literal> act = as.getLiteralsBySymbol("new_act");
		return  new_state != null ||
				new_istack != null ||
				new_khstate != null ||
				act.size() > 0;
	}
	
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
	 * Helper method: finds the new version of the atom in the answerset list.
	 * @param asl	Reference to the answer-set list
	 * @param name	The name of the atom like 'state'
	 * @return	the new atom or null if no new atom exists.
	 */
	private Atom updateAtom(AnswerSet asl, String name) {
		// get new state...
		String error = null;
		Set<Literal> lits = asl.getLiteralsBySymbol("new_" + name);
		if(lits.size() == 1) {
			Literal new_literal = lits.iterator().next();
			if(new_literal instanceof Atom) {
				Atom a = (Atom)new_literal;
				Atom state = new Atom(name, a.getTerms());
				return state; 
			} else {
				error = "'" + new_literal.toString() + "' is no atom. new_ literals must be facts.";
			}
		} else if(lits.size() > 1) {
			error = "There are more than one 'new_" + name + "' literals: " + lits.toString();
		}
		if(error != null)
			LOG.error(error);
		
		Set<Literal> newLits = asl.getLiteralsBySymbol(name);
		if(newLits.size() == 0) {
			return null;
		}
		return (Atom)newLits.iterator().next();
	}
	
	public String getInitialIntention() {
		return initialIntention;
	}
}
