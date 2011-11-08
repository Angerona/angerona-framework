package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

/**
 * The classical disjunction of first-order logic.
 * @author Matthias Thimm
 */
public class Disjunction extends AssociativeFormula{
	
	/**
	 * Creates a new disjunction with the given inner formulas. 
	 * @param formulas a collection of formulas.
	 */
	public Disjunction(Collection<? extends RelationalFormula> formulas){
		super(formulas);
	}
	
	/**
	 * Creates a new (empty) disjunction.
	 */
	public Disjunction(){
		this(new HashSet<RelationalFormula>());
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getDisjunctions()
	 */
	public Set<Disjunction> getDisjunctions(){
		Set<Disjunction> disjuncts = super.getDisjunctions();
		disjuncts.add(this);
		return disjuncts;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isDnf()
	 */
	public boolean isDnf(){
		for(RelationalFormula f: this)
			if(!((FolFormula)f).isDnf() && !(f instanceof Disjunction)) return false;
		return true;
	}
	
	/**
	 * Creates a new disjunction with the two given formulae
	 * @param first a relational formula.
	 * @param second a relational formula.
	 */
	public Disjunction(RelationalFormula first, RelationalFormula second){
		this();
		this.add(first);
		this.add(second);
	}	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toString()
	 */
	public String toString(){
		if(this.isEmpty())
			return FolSignature.TAUTOLOGY;
		String s = "";
		boolean isFirst = true;
		for(RelationalFormula f: this){
			if(isFirst)			
				isFirst = false;
			else
				s  += FolSignature.DISJUNCTION;
			// check if parentheses are needed
			if(f instanceof Disjunction && ((Disjunction)f).size()>1 )
				s += FolSignature.PARENTHESES_LEFT + f.toString() + FolSignature.PARENTHESES_RIGHT;
			else s += f.toString();
		}
		return s;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	public FolFormula substitute(Term v, Term t) throws IllegalArgumentException{
		Set<RelationalFormula> newFormulas = new HashSet<RelationalFormula>();
		for(RelationalFormula f: this)
			newFormulas.add((RelationalFormula)f.substitute(v, t));
		return new Disjunction(newFormulas);
	}
	
	 /*
   * (non-Javadoc)
   * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toNNF()
   */
  @Override
  public FolFormula toNnf() {
    Disjunction d = new Disjunction();
    for(RelationalFormula p : this) {
      if(p instanceof FolFormula)
        d.add( ((FolFormula) p).toNnf() );
      else
        throw new IllegalStateException("Can not convert conjunctions containing non-first-order formulae to NNF.");
    }
    return d;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#collapseAssociativeFormulas()
   */
  @Override
  public FolFormula collapseAssociativeFormulas() {
    if(this.isEmpty())
      return new Contradiction();
    if(this.size() == 1)
      return ((FolFormula)this.iterator().next()).collapseAssociativeFormulas();
    Disjunction newMe = new Disjunction();
    for(RelationalFormula f: this){
      if(! (f instanceof FolFormula)) throw new IllegalStateException("Can not collapse disjunctions containing non-first-order formulae.");
      FolFormula newF = ((FolFormula)f).collapseAssociativeFormulas();
      if(newF instanceof Disjunction)
        newMe.addAll((Disjunction) newF);
      else newMe.add(newF);
    }
    return newMe;
  }
}
