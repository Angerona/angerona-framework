package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.SetSignature;
import net.sf.tweety.Signature;
import net.sf.tweety.SymbolSet;
import net.sf.tweety.logics.CommonStructure;
import net.sf.tweety.logics.CommonTerm;
import net.sf.tweety.logics.LogicalSymbols;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;


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
	 * Angerona translates predicates into symbols, which we are going to interpret as propositions
	 */
	@Override
	public void fromSymbolSet(SymbolSet symbols) {
		this.clear();
		for(String symbol : symbols.getSymbols()) {
			this.add(new Proposition(symbol));
		}
	}

	/**
	 * In first order logic signatures, common structure elements are predicates and functors.
	 * In the propositional case, propositions are identified with predicates, so this method
	 * returns the set of propositions, translated to predicates
	 */
	@Override
	public Set<CommonStructure> getCommonStructureElements() {
		Set<CommonStructure> retval = new HashSet<CommonStructure>();
		for(Proposition proposition : this) {
			Predicate predicate = new Predicate(proposition.getName());
			retval.add(predicate);
		}
		
		return retval;
	}

	/**
	 * In first order logics, term elements are constants. There is no equivalent to constants
	 * in propositional languages, so the return value of this method is always an empty set.
	 * 
	 * @return empty set
	 */
	@Override
	public Set<CommonTerm> getCommonTermElements() {
		return new HashSet<CommonTerm>();
	}

	/**
	 * Populate this propositional signature from another signature which may either be
	 */
	@Override
	public void fromSignature(Signature signature) {
		clear();
		if(signature instanceof PropositionalSignature) {
			this.addSignature(signature);
		} else {
			for(CommonStructure cs : signature.getCommonStructureElements()) {
				if(cs instanceof Predicate) {
					Predicate predicate = (Predicate) cs;
					this.add(new Proposition(predicate.getName()));
				}
			}
		}
	}
}
