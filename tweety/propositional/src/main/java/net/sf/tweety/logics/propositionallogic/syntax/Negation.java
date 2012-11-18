package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.*;

/**
 * This class models classical negation of propositional logic.
 * 
 * @author Matthias Thimm
 */
public class Negation extends PropositionalFormula {

	/**
	 * The formula within this negation.
	 */
	private PropositionalFormula formula;
	
	/**
	 * Creates a new negation with the given formula.
	 * @param formula the formula within the negation.
	 */
	public Negation(PropositionalFormula formula){
		this.formula = formula;	
	}
	
	/**
	 * Returns the formula within this negation.
	 * @return the formula within this negation.
	 */
	public PropositionalFormula getFormula(){
		return this.formula;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#collapseAssociativeFormulas()
	 */
	public PropositionalFormula collapseAssociativeFormulas(){
		return new Negation(this.formula.collapseAssociativeFormulas());
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#hasLowerBindingPriority(net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula)
	 */
	public boolean hasLowerBindingPriority(PropositionalFormula other){
		// negations have the highest binding priority
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#getPropositions()
	 */
	public Set<Proposition> getPropositions(){
		return this.formula.getPropositions();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		if(this.formula instanceof AssociativeFormula || this.formula instanceof Negation)			
			return PropositionalSignature.CLASSICAL_NEGATION + "(" + this.formula + ")";
		return PropositionalSignature.CLASSICAL_NEGATION + this.formula;
	}
	
  /* (non-Javadoc)
   * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#toNNF()
   */
	@Override
	public PropositionalFormula toNnf() {
    // remove double negation    
    if(formula instanceof Negation)
      return ((Negation)formula).formula.toNnf();

     // Distribute negation inside conjunctions or disjunctions according to deMorgan's laws:
     // -(p & q)  = -p || -q
     // -(p || q) = -p & -q
    if(formula instanceof Conjunction) {
      Conjunction c = (Conjunction)formula;
      Disjunction d = new Disjunction();
      
      for(PropositionalFormula p : c) {
        d.add( new Negation( p ).toNnf() );
      }
      return d;
    }
    
    if(formula instanceof Disjunction) {
       Disjunction d = (Disjunction)formula;
       Conjunction c = new Conjunction();
       
       for(PropositionalFormula p : d) {
         c.add( new Negation( p ).toNnf() );
       }
       return c;
    }
    return this;
	}
}
