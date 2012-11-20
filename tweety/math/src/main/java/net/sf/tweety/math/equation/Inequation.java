package net.sf.tweety.math.equation;

import net.sf.tweety.math.term.*;

/**
 * This class models an inequation of two terms.
 * @author Matthias Thimm
 */
public class Inequation extends Statement{

	public static final int LESS = 0;
	public static final int LESS_EQUAL = 1;
	public static final int GREATER = 2;
	public static final int GREATER_EQUAL = 3;
	public static final int UNEQUAL = 4;
	
	private int type;
	
	/**
	 * Creates a new inequation of the given type with the two terms.
	 * @param leftTerm a term.
	 * @param rightTerm a term.
	 * @param type the type of the inequality, one of Inequation.LESS, Inequation.LESS_EQUAL
	 * 		Inequation.GREATER, Inequation.GREATER_EQUAL, Inequation.UNEQUAL.
	 */
	public Inequation(Term leftTerm, Term rightTerm, int type){
		super(leftTerm,rightTerm);
		if(type < Inequation.LESS || type > Inequation.UNEQUAL)
			throw new IllegalArgumentException("Wrong type for inequation. Expected one of Inequation.LESS, Inequation.LESS_EQUAL, Inequation.GREATER, Inequation.GREATER_EQUAL, Inequation.UNEQUAL.");
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.equation.Statement#replaceTerm(net.sf.tweety.math.term.Term, net.sf.tweety.math.term.Term)
	 */
	public Statement replaceTerm(Term toSubstitute, Term substitution){
		return new Inequation(this.getLeftTerm().replaceTerm(toSubstitute, substitution),this.getRightTerm().replaceTerm(toSubstitute, substitution),this.type);
	}
	
	/**
	 * Returns the type of this inequation.
	 * @return the type of this inequation.
	 */
	public int getType(){
		return this.type;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.equation.Statement#isNormalized()
	 */
	public boolean isNormalized(){
		if(this.type == Inequation.GREATER || this.type == Inequation.GREATER_EQUAL || this.type == Inequation.UNEQUAL)
			if(this.getRightTerm() instanceof Constant){
				if(this.getRightTerm() instanceof FloatConstant){
					if(((FloatConstant)this.getRightTerm()).getValue() == 0)
						return true;
				}
				if(this.getRightTerm() instanceof IntegerConstant){
					if(((IntegerConstant)this.getRightTerm()).getValue() == 0)
						return true;
				}
			}	
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.equation.Statement#toNormalizedForm()
	 */
	public Statement toNormalizedForm(){
		// Check whether it is already normalized
		if(this.isNormalized()) return this;	
		// rearrange the terms
		Term term = this.getLeftTerm().minus(this.getRightTerm());
		
		if(this.type == Inequation.GREATER || this.type == Inequation.GREATER_EQUAL || this.type == Inequation.UNEQUAL)
			return new Inequation(term,new IntegerConstant(0),this.type);			
		else{
			int type = this.type;
			if(type == Inequation.LESS) type = Inequation.GREATER;
			if(type == Inequation.LESS_EQUAL) type = Inequation.GREATER_EQUAL;				
			return new Inequation(term.mult(new IntegerConstant(-1)),new IntegerConstant(0),type);
		}	
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.equation.Statement#toLinearForm()
	 */
	public Statement toLinearForm(){
		Term left = this.getLeftTerm().toLinearForm();
		Term right = (this.isNormalized())?(this.getRightTerm()):(this.getRightTerm().toLinearForm());
		return new Inequation(left,right,this.type);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.equation.Statement#getRelationSymbol()
	 */
	public String getRelationSymbol(){
		if(this.type == Inequation.LESS)
			return "<";
		if(this.type == Inequation.LESS_EQUAL)
			return "<=";
		if(this.type == Inequation.UNEQUAL)
			return "<>";
		if(this.type == Inequation.GREATER)
			return ">";
		if(this.type == Inequation.GREATER_EQUAL)
			return ">=";
		throw new IllegalArgumentException("Inequation is of unrecognized type.");
	}
	
}
