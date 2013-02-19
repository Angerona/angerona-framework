package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.Collection;

import net.sf.tweety.SetSignature;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.LogicalSymbols;


/**
 * This class captures the signature of a specific
 * propositional language.
 * @author Matthias Thimm, Sebastian Homann
 */
public class PropositionalSignature extends SetSignature<Proposition> implements LogicalSymbols {
	
	/**
	 * Creates a new propositional signature with the given set
	 * of propositions.
	 * @param propositions a set of propositions.
	 */
	public PropositionalSignature(Collection<? extends Proposition> propositions){
		super(propositions);
	}
	
	/**
	 * Creates a new (empty) propositional signature.
	 */
	public PropositionalSignature(){
		super();
	}
	
	/** 
	 * SymbolSet helper methods - basically some glue code between angeronas fol-based internal structure and
	 * propositional signatures
	 */
	

	/**
	 * Populate this propositional signature from another signature which may either be
	 */
	@Override
	public void fromSignature(Signature signature) {
		clear();
		if(signature instanceof PropositionalSignature) {
			this.addSignature(signature);
		} else {
			throw new IllegalArgumentException();
		}
	}
}
