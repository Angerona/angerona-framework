package net.sf.tweety.logics.conditionallogic.syntax;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;
import net.sf.tweety.util.*;
import net.sf.tweety.util.rules.*;

/**
 * This class represents a basic conditional (B|A) with formulas A,B.
 * @author Matthias Thimm
 */
public class Conditional implements ClassicalFormula, Rule {
	
	/**
	 * The premise of this conditional. 
	 */
	private PropositionalFormula premise;
	
	/**
	 * The conclusion of this conditional.
	 */
	private PropositionalFormula conclusion;
	
	/**
	 * Creates a new conditional with a tautological premise
	 * and given conclusion.
	 * @param conclusion the conclusion (a formula) of this conditional.
	 */
	public Conditional(PropositionalFormula conclusion){
		this.premise = new Tautology();
		this.conclusion = conclusion;
	}
	
	/**
	 * Creates a new conditional with the given premise
	 * and conclusion.
	 * @param premise the premise (a formula) of this conditional.
	 * @param conclusion the conclusion (a formula) of this conditional.
	 */
	public Conditional(PropositionalFormula premise, PropositionalFormula conclusion){
		this.premise = premise;
		this.conclusion = conclusion;
	}
	
	/**
	 * CopyCTor
	 * @param other
	 */
	public Conditional(Conditional other) {
		this.premise = other.premise;
		this.conclusion = other.conclusion;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.util.rules.Rule#getPremise()
	 */
	public Collection<PropositionalFormula> getPremise(){
		HashSet<PropositionalFormula> premiseSet = new HashSet<PropositionalFormula>();
		premiseSet.add(this.premise);
		return premiseSet;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.util.rules.Rule#getConclusion()
	 */
	public PropositionalFormula getConclusion(){
		return this.conclusion;
	}
	
	/**
	 * Returns this conditional's probability in the uniform distribution. 
	 * @return this conditional's probability in the uniform distribution.
	 */
	public Probability getUniformProbability(){
		Double n = ((PropositionalFormula)this.conclusion.combineWithAnd(this.premise)).getUniformProbability().getValue();
		Double d = this.premise.getUniformProbability().getValue();
		if(d == 0)
			return new Probability(0d);
		return new Probability(n/d);
	}
		
	/**
	 * Checks whether this conditional is a fact, i.e.
	 * has a tautological premise.
	 * @return "true" iff this conditional is a fact.
	 */
	public boolean isFact(){
		return (this.premise instanceof Tautology);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Formula#getSignature()
	 */
	public Signature getSignature(){		
		return this.premise.combineWithAnd(this.conclusion).getSignature();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "(" + conclusion + "|" + premise + ")";
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithAnd(net.sf.tweety.kr.Formula)
	 */
	public Conditional combineWithAnd(ClassicalFormula f){		
		throw new UnsupportedOperationException("Conditionals cannot be combined by 'AND'");		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithOr(net.sf.tweety.kr.ClassicalFormula)
	 */
	public Conditional combineWithOr(ClassicalFormula f){
		throw new UnsupportedOperationException("Conditionals cannot be combined by 'OR'");
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#complement()
	 */
	public Conditional complement(){
		return new Conditional(this.premise,(PropositionalFormula)this.conclusion.complement());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((conclusion == null) ? 0 : conclusion.hashCode());
		result = prime * result + ((premise == null) ? 0 : premise.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conditional other = (Conditional) obj;
		if (conclusion == null) {
			if (other.conclusion != null)
				return false;
		} else if (!conclusion.equals(other.conclusion))
			return false;
		if (premise == null) {
			if (other.premise != null)
				return false;
		} else if (!premise.equals(other.premise))
			return false;
		return true;
	}
	
}
