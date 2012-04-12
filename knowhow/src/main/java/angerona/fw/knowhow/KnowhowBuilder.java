package angerona.fw.knowhow;

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
	public static Program buildExtendedLogicProgram(KnowhowBase kb) {
		Program p = new Program();
		
		for(KnowhowStatement ks : kb.getStatements()) {
			// Knowhow Statement
			Rule r = new Rule();
			Atom stAtom = new Atom(ks.name);
			r.addHead(new Atom("khStatement", stAtom));
			p.add(r);
			
			// Subtargets
			int i = 1;
			for(Atom a : ks.getSubTargets()) {
				r = new Rule();
				r.addHead(new Atom("khSubgoal", stAtom, new Atom(new Integer(i).toString()), a));
				p.add(r);
			}
			
			// Conditions
			for(Atom a : ks.getConditions()) {
				r = new Rule();
				r.addHead(new Atom("khCondition", stAtom, a));
				p.add(r);
			}
		}
		
		return p;
	}
}
