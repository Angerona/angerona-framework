package net.sf.tweety.logics.propositionallogic.lang;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.Language;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.propositionallogic.PlBeliefSet;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalSignature;

/**
 * This class models a propositional language for a given signature.
 * @author mthimm
 *
 */
public class PropositionalLanguage extends Language {

	/**
	 * Creates a new language on the given first-order signature.
	 * @param signature a first-order signature.
	 */
	public PropositionalLanguage(Signature signature){
		super(signature);
		if(!(signature instanceof PropositionalSignature))
			throw new IllegalArgumentException("Signatures for propositional languages must be propositional signatures.");
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Language#isRepresentable(net.sf.tweety.kr.Formula)
	 */
	@Override
	public boolean isRepresentable(Formula formula) {
		return formula instanceof PropositionalFormula;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Language#isRepresentable(net.sf.tweety.kr.BeliefBase)
	 */
	@Override
	public boolean isRepresentable(BeliefBase beliefBase) {
		return beliefBase instanceof PlBeliefSet;
	}

}
