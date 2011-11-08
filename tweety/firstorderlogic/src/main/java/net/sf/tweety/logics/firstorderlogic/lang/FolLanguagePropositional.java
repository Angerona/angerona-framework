package net.sf.tweety.logics.firstorderlogic.lang;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;

/**
 * This class represents a classical propositional language, i.e. a language without
 * variables, constants, functors, and predicates of arity greater zero.
 * @author Matthias Thimm
 */
public class FolLanguagePropositional extends FolLanguage {
	/**
	 * Creates a new language on the given signature.
	 * @param folSignature a signature.
	 */
	public FolLanguagePropositional(Signature signature){
		super(signature);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.lang.FolLanguage#isRepresentable(net.sf.tweety.kr.Formula)
	 */
	public boolean isRepresentable(Formula formula){
		if(!super.isRepresentable(formula)) return false;
		// it is sufficient to check whether there are predicates of arity greater zero.
		for(Predicate p: ((FolFormula)formula).getPredicates())
			if(p.getArity() != 0)
				return false;
		return true;
	}
}
