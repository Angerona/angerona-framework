package angerona.fw.knowhow;

import java.util.Collection;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Constant;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.syntax.StdTerm;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Term;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.NotImplementedException;
import angerona.fw.util.Pair;

/**
 * Helper class responsible for translating the know-how base into other data-structures like
 * extended logical programs. Only static methods are used.
 * 
 * @author Tim Janus
 */
public class KnowhowBuilder {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(KnowhowBuilder.class);
	
	/**
	 * Creates an extended Logic program from the given KnowhowBase.
	 * @param kb	reference to the knowhow base.
	 * @return		extended logic program representing the given KnowhowBase.
	 */
	public static Program buildKnowhowBaseProgram(KnowhowBase kb, boolean everything) {
		Program p = new Program();
		
		// create facts for Knowhow-Statements
		for(KnowhowStatement ks : kb.getStatements()) {
			// Knowhow Statement
			Rule r = new Rule();
			Atom stAtom = new Atom(ks.name);
			r.addHead(new Atom("khstatement", stAtom, ks.getTarget()));
			p.add(r);
			
			// Subtargets
			int i = 1;
			for(Atom a : ks.getSubTargets()) {
				r = new Rule();
				
				// if the subgoal is an skill then we have to find the parameter mappings
				// and encode them in the logic program:
				if(a.toString().startsWith("s_")) {
					// search parameters:
					int c = 0;
					for(Term t : a.getTerms()) {
						// prepare the four parameters of the action_parameter atom:
						Term skillReference = new Constant(a.getName());
						Term index = new StdTerm(c++);
						
						// create action_parameter atom and add to program:
						p.add(new Atom("skill_parameter", skillReference, index, t));
					}
					
					a = new Atom(a.getName());
				}
				r.addHead(new Atom("khsubgoal", stAtom, new Atom(new Integer(i).toString()), a));
				p.add(r);
				i++;
			}
			
			// Conditions
			for(Atom a : ks.getConditions()) {
				r = new Rule();
				r.addHead(new Atom("khcondition", stAtom, a));
				p.add(r);
			}
		}
		
		if(everything) {
			List<String> holds = kb.getAgent().getBeliefs().getWorldKnowledge().getAtoms();
			p.add(buildHoldsProgram(holds));
			
			//Set<String> atomic_actions = kb.getAgent().getSkills().keySet();
			//p.add(buildAtomicProgram(atomic_actions));

			throw new NotImplementedException("The everything-flag=true is not implemented for KnowhowBuilder.buildKnowhowBaseProgram().");
		}
		return p;
	}
	
	public static Program buildHoldsProgram(Collection<String> literals) {
		Program p = new Program();
		for(String atom : literals) {
			p.add(new Atom("holds", new Atom(atom)));
		}
		return p;
	}
	
	/**
	 * Creates an extended logic program from the given collection of skills
	 * @param skills	all skills which are atomic actions of the agent
	 * @return			An extended logic program containing all is_atomic facts for the agent
	 */
	public static Program buildAtomicProgram(Collection<Pair<String, Integer>> atomic_actions) {
		Program p = new Program();
		
		for(Pair<String, Integer> action : atomic_actions) {
			p.add(new Atom("is_atomic", new Atom(action.first)));
			p.add(new Atom("action_parameters", new Constant(action.second.toString())));
		}
		
		return p;
	}
}
