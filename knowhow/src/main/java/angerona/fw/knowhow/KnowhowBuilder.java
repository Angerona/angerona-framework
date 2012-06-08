package angerona.fw.knowhow;

import java.util.Collection;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Skill;

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
	public static Program buildExtendedLogicProgram(KnowhowBase kb) {
		Program p = new Program();
		
		// create facts for Knowhow-Statements
		for(KnowhowStatement ks : kb.getStatements()) {
			// Knowhow Statement
			Rule r = new Rule();
			Atom stAtom = new Atom(ks.name);
			r.addHead(new Atom("khStatement", stAtom, ks.getTarget()));
			p.add(r);
			
			// Subtargets
			int i = 1;
			for(Atom a : ks.getSubTargets()) {
				r = new Rule();
				r.addHead(new Atom("khSubgoal", stAtom, new Atom(new Integer(i).toString()), a));
				p.add(r);
				i++;
			}
			
			// Conditions
			for(Atom a : ks.getConditions()) {
				r = new Rule();
				r.addHead(new Atom("khCondition", stAtom, a));
				p.add(r);
			}
		}
		
		// add is-atomic facts for skills:
		for(String skillName : kb.getAgent().getSkills().keySet()) {
			Rule r = new Rule();
			Atom isAtomic = new Atom("is_atomic", new Atom(skillName));
			r.addHead(isAtomic);
			p.add(r);
		}
		
		// add holds facts for the world knowledge of the agent
		// TODO: react to dynamic changes of the Beliefbase. Implement an iterative 
		// update of the logic program representing the KnowhowBase
		BaseBeliefbase world = kb.getAgent().getBeliefs().getWorldKnowledge();
		for(String atom : world.getAtoms()) {
			Rule r = new Rule();
			Atom holds = new Atom("holds", new Atom(atom));
			r.addHead(holds);
			p.add(r);
		}
		
		return p;
	}
	
	/**
	 * Creates an extended logic program from the given collection of skills
	 * @param skills	all skills which are atomic actions of the agent
	 * @return			An extended logic program containing all is_atomic facts for the agent
	 */
	public static Program buildExtendedLogicProgram(Collection<Skill> skills) {
		Program p = new Program();
		
		for(Skill s : skills) {
			Rule r = new Rule();
			r.addHead(new Atom("is_atomic", new Atom(s.getName())));
		}
		
		return p;
	}
}
