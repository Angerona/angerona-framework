package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.*;

/**
 * This class represents a disjunction in propositional logic.
 * 
 * @author Matthias Thimm
 */
public class Disjunction extends AssociativeFormula {
	
	/**
	 * Creates a new disjunction with the given inner formulas. 
	 * @param formulas a collection of formulas.
	 */
	public Disjunction(Collection<? extends PropositionalFormula> formulas){
		super(formulas);
	}
	
	/**
	 * Creates a new (empty) disjunction.
	 */
	public Disjunction(){
		this(new HashSet<PropositionalFormula>());
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#collapseAssociativeFormulas()
	 */
	public PropositionalFormula collapseAssociativeFormulas(){
		if(this.isEmpty())
			return new Contradiction();
		if(this.size() == 1)
			return this.iterator().next().collapseAssociativeFormulas();
		Disjunction newMe = new Disjunction();
		for(PropositionalFormula f: this){
			PropositionalFormula newF = f.collapseAssociativeFormulas();
			if(newF instanceof Disjunction)
				newMe.addAll((Disjunction) newF);
			else newMe.add(newF);
		}
		return newMe;
	}
	
	/**
	 * Creates a new disjunction with the two given formulae
	 * @param first a propositional formula.
	 * @param second a propositional formula.
	 */
	public Disjunction(PropositionalFormula first, PropositionalFormula second){
		this();
		this.add(first);
		this.add(second);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		if(this.isEmpty())
			return PropositionalSignature.TAUTOLOGY;
		String s = "";
		boolean isFirst = true;
		for(PropositionalFormula f: this){
			if(isFirst)			
				isFirst = false;
			else
				s  += PropositionalSignature.DISJUNCTION;
			s += f.toString();
		}
		return s;
	}
	 
  /* (non-Javadoc)
   * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#toNNF()
   */
	@Override
	public PropositionalFormula toNnf() {
	  Disjunction d = new Disjunction();
    for(PropositionalFormula p : this) {
      d.add( p.toNnf() );
    }
    return d;
	}
}
