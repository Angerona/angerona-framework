package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.*;

import net.sf.tweety.logics.commons.LogicalSymbols;

/**
 * This class represents a conjunction in propositional logic.
 * 
 * @author Matthias Thimm
 */
public class Conjunction extends AssociativeFormula {
		
	/**
	 * Creates a new conjunction with the given inner formulas. 
	 * @param formulas a collection of formulas.
	 */
	public Conjunction(Collection<? extends PropositionalFormula> formulas){
		super(formulas);
	}
	
	/**
	 * Creates a new (empty) conjunction.
	 */
	public Conjunction(){
		this(new HashSet<PropositionalFormula>());
	}
	
	/**
	 * Creates a new conjunction with the two given formulae
	 * @param first a propositional formula.
	 * @param second a propositional formula.
	 */
	public Conjunction(PropositionalFormula first, PropositionalFormula second){
		this();
		this.add(first);
		this.add(second);
	}	

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#collapseAssociativeFormulas()
	 */
	public PropositionalFormula collapseAssociativeFormulas(){
		if(this.isEmpty())
			return new Tautology();
		if(this.size() == 1)
			return this.iterator().next().collapseAssociativeFormulas();
		Conjunction newMe = new Conjunction();
		for(PropositionalFormula f: this){
			PropositionalFormula newF = f.collapseAssociativeFormulas();
			if(newF instanceof Conjunction)
				newMe.addAll((Conjunction) newF);
			else newMe.add(newF);
		}
		return newMe;
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		if(this.isEmpty())
			return LogicalSymbols.CONTRADICTION();
		String s = "";
		boolean isFirst = true;
		for(PropositionalFormula f: this){
			if(isFirst)			
				isFirst = false;
			else
				s  += LogicalSymbols.CONJUNCTION();
			// check if parentheses are needed
			if(f instanceof Disjunction && ((Disjunction)f).size()>1 )
				s += LogicalSymbols.PARENTHESES_LEFT() + f.toString() + LogicalSymbols.PARENTHESES_RIGHT();
			else s += f.toString();
		}
		return s;
	}
	
  /* (non-Javadoc)
   * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#toNNF()
   */
	@Override
	public PropositionalFormula toNnf() {
    Conjunction c = new Conjunction();
    for(PropositionalFormula p : this) {
      c.add( p.toNnf() );
    }
    return c;
	}
}
