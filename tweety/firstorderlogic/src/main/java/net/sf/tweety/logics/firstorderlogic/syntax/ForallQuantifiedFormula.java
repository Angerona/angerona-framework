package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

import net.sf.tweety.logics.commons.LogicalSymbols;

/**
 * For-All quantified formula.
 * @author Matthias Thimm
 */
public class ForallQuantifiedFormula extends QuantifiedFormula{
	
	/**
	 * Creates a new for-all-quantified formula with the given formula and variables.
	 * @param folFormula the formula this for-all-quantified formula ranges over.
	 * @param variables the variables of this for-all-quantified formula.
	 */
	public ForallQuantifiedFormula(RelationalFormula folFormula, Set<Variable> variables){
		super(folFormula,variables);		
	}
	
	/**
	 * Creates a new for-all-quantified formula with the given formula and variable.
	 * @param folFormula the formula this for-all-quantified formula ranges over.
	 * @param variables the variable of this for-all-quantified formula.
	 */
	public ForallQuantifiedFormula(FolFormula folFormula, Variable variable){
		super(folFormula,variable);
	} 
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	public RelationalFormula substitute(Term v, Term t) throws IllegalArgumentException{
		if(this.getQuantifierVariables().contains(v))
			return new ForallQuantifiedFormula(this.getFormula(),this.getQuantifierVariables());
		return new ForallQuantifiedFormula(this.getFormula().substitute(v, t),this.getQuantifierVariables());
	}
	
	
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#collapseAssociativeFormulas()
   */
  @Override
  public FolFormula collapseAssociativeFormulas() {
    return new ForallQuantifiedFormula( this.getFormula().collapseAssociativeFormulas(), this.getQuantifierVariables() );
  }
  
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toNNF()
	 */
	@Override
	public FolFormula toNnf() {
	  return new ForallQuantifiedFormula( getFormula().toNnf(), getQuantifierVariables() );
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toString()
	 */
	public String toString(){
		String s = LogicalSymbols.FORALLQUANTIFIER() + " ";
		Iterator<Variable> it = this.getQuantifierVariables().iterator();
		if(it.hasNext())
			s += it.next();
		while(it.hasNext())
			s += "," + it.next();
		s += ":" + this.getFormula();
		return s;
	}
}
