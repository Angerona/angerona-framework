package net.sf.tweety.math.equation;

import java.util.*;

import net.sf.tweety.math.term.*;


/**
 * This class models a mathematical statement, i.e. an equality or an inequality.
 * @author Matthias Thimm
 */
public abstract class Statement {
	
	/**
	 * The left term of this statement.
	 */
	private Term leftTerm;
	
	/**
	 * The right term of this statement.
	 */
	private Term rightTerm;
	
	/**
	 * Creates a new statement with the given terms.
	 * @param leftTerm a term.
	 * @param rightTerm a term.
	 */
	public Statement(Term leftTerm, Term rightTerm){
		this.leftTerm = leftTerm;
		this.rightTerm = rightTerm; 
	}
	
	/**
	 * Returns the left term of this statement.
	 * @return the left term of this statement. 
	 */
	public Term getLeftTerm(){
		return this.leftTerm;
	}
	
	/**
	 * Returns all minimums of this statement.
	 * @return all minimums of this statement.
	 */
	public Set<Minimum> getMinimums(){
		Set<Minimum> minimums = new HashSet<Minimum>();
		minimums.addAll(this.leftTerm.getMinimums());
		minimums.addAll(this.rightTerm.getMinimums());
		return minimums;
	}
	
	/**
	 * Returns all minimums of this statement.
	 * @return all minimums of this statement.
	 */
	public Set<AbsoluteValue> getAbsoluteValues(){
		Set<AbsoluteValue> avs = new HashSet<AbsoluteValue>();
		avs.addAll(this.leftTerm.getAbsoluteValues());
		avs.addAll(this.rightTerm.getAbsoluteValues());
		return avs;
	}
	
	/**
	 * Replaces each occurrence of "toSubstitute" by "substitution" and
	 * return the new statement.
	 * @param toSubstitute the term to be substituted
	 * @param substitution the new term
	 * @return this statement where "toSubstitute" is replaced by "substitution"
	 */
	public abstract Statement replaceTerm(Term toSubstitute, Term substitution);
	
	/**
	 * Replaces terms according to the given map.
	 * @param substitutes a map.
	 * @return a term.
	 */
	public Statement replaceAllTerms(Map<? extends Term,? extends Term> substitutes){
		Statement t = this;
		for(Term s: substitutes.keySet())
			t = t.replaceTerm(s, substitutes.get(s));
		return t;
	}
	
	/**
	 * Checks whether this constraint is of normalized form, i.e.
	 * whether it has the form "T > 0" or "T >= 0", "T = 0" or "T != 0"
	 * @return "true" iff this constraint is normalized.
	 */
	public abstract boolean isNormalized();

	/**
	 * Normalizes this constraint, i.e. brings it into
	 * an equivalent form "T > 0" or "T >= 0", "T = 0" or "T != 0". 
	 * @return a statement.
	 */
	public abstract Statement toNormalizedForm();
	
	/**
	 * Brings both terms into linear form. If this constraint is normalized
	 * (i.e. the right term consists of the constant 0) the right term is not
	 * linearized.
	 * @return a statement.
	 */
	public abstract Statement toLinearForm();
	
	/**
	 * This method collapses all associative operations appearing
	 * in this statement, e.g. every min{min{a,b},c} becomes min{a,b,c}
	 */
	public void collapseAssociativeOperations(){
		this.leftTerm.collapseAssociativeOperations();
		this.rightTerm.collapseAssociativeOperations();
	}
	
	/**
	 * This method expands all associative operations appearing
	 * in this statement, e.g. every min{a,b,c} becomes min{min{a,b},c}.
	 */
	public void expandAssociativeOperations(){
		this.leftTerm.expandAssociativeOperations();
		this.rightTerm.expandAssociativeOperations();
	}
	
	/**
	 * Returns the right term of this statement.
	 * @return the right term of this statement. 
	 */
	public Term getRightTerm(){
		return this.rightTerm;
	}	
	
	/**
	 * Sets the left term of this statement.
	 * @param t a term
	 */
	public void setLeftTerm(Term t){
		this.leftTerm = t;
	}
	
	/**
	 * Sets the right term of this statement.
	 * @param t a term
	 */
	public void setRightTerm(Term t){
		this.rightTerm = t;
	}

	/**
	 * Returns the relation symbol of this statement.
	 * @return the relation symbol of this statement.
	 */
	public abstract String getRelationSymbol();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return this.getLeftTerm().toString() + " " + this.getRelationSymbol() + " " + this.getRightTerm().toString();
	}
}
