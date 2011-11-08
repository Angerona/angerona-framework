package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

/**
 * This abstract class captures the common functionalities of both
 * formulas and terms.
 * @author Matthias Thimm
 */
public abstract class LogicStructure {
	/**
	 * Returns all constants that appear in this structure.
	 * @return all constants that appear in this structure.
	 */
	public abstract Set<Constant> getConstants();
	
	/**
	 * Returns all functors that appear in this structure.
	 * @return all functors that appear in this structure.
	 */
	public abstract Set<Functor> getFunctors();
	
	/**
	 * Returns all variables that appear in this structure.
	 * @return all variables that appear in this structure.
	 */
	public abstract Set<Variable> getVariables();
	
	/**
	 * Returns all functional terms that appear in this structure.
	 * @return all functional terms that appear in this structure.
	 */
	public abstract Set<FunctionalTerm> getFunctionalTerms();
	
	/**
	 * Checks whether this structure contains any functional terms.
	 * @return "true" if this structure contains a functional term.
	 */
	public boolean containsFunctionalTerms(){
		return !this.getFunctionalTerms().isEmpty();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toString()
	 */
	public abstract String toString();
	
}
