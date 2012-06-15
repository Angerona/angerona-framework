package angerona.fw.logic;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

/**
 * An answer in the Angerona framework. Extends the GenericAnswer to any first order logic formula
 * @author Daniel Dilger
 */

public class AngeronaDetailAnswer extends GenericAnswer<FolFormula>{
	
	public AngeronaDetailAnswer(BeliefBase beliefBase, Formula query) {
		this(beliefBase, query, new Atom(new Predicate("FALSE")));
	}
	
	public AngeronaDetailAnswer(BeliefBase beliefBase, Formula query, FolFormula at) {
		super(beliefBase, query, at);
	}
	
}
