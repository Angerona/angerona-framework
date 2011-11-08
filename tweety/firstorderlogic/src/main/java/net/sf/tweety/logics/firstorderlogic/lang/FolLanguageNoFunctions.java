package net.sf.tweety.logics.firstorderlogic.lang;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;

/**
 * This class models a first-order language without functions.
 * @author Matthias Thimm
 */
public class FolLanguageNoFunctions extends FolLanguage {
		
	/**
	 * Creates a new language on the given signature.
	 * @param folSignature a signature.
	 */
	public FolLanguageNoFunctions(Signature signature){
		super(signature);
	}	

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.lang.FolLanguage#isRepresentable(net.sf.tweety.kr.Formula)
	 */
	public boolean isRepresentable(Formula formula){
		if(!super.isRepresentable(formula)) return false;
		return !((FolFormula)formula).containsFunctionalTerms();		
	}	
}
