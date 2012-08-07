package angerona.fw.knowhow;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

/**
 * Helper class responsible for translating the know-how base into other data-structures like
 * extended logical programs. Only static methods are used.
 * 
 * @author Tim Janus
 */
public class KnowhowBuilder {
	
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
			
			Set<String> atomic_actions = kb.getAgent().getSkills().keySet();
			p.add(buildAtomicProgram(atomic_actions));
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
	public static Program buildAtomicProgram(Collection<String> atomic_actions) {
		Program p = new Program();
		
		for(String action : atomic_actions) {
			p.add(new Atom("is_atomic", new Atom(action)));
		}
		
		return p;
	}
}
