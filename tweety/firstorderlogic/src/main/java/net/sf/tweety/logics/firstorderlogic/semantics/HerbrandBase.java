package net.sf.tweety.logics.firstorderlogic.semantics;

import java.util.*;

import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.util.*;


/**
 * The Herbrand base is the set of all possible ground atoms of some
 * given first-order logic.
 * <br/>
 * NOTE: We only allow to define a Herbrand base for signatures without
 *   function symbols.
 * @author Matthias Thimm
 */
public class HerbrandBase {

	/**
	 * The atoms of this Herbrand base. 
	 */
	private Set<Atom> atoms;
	
	/**
	 * Creates a new Herbrand base for the given signature.
     * <br/>
     * NOTE: We only allow to define a Herbrand base for signatures without
     *   function symbols.
	 * @param sig the underlying first-order signature for
	 * 	this Herbrand base. There should be no functors defined in "sig"
	 * @throws IllegalArgumentationException if "sig" contains a functor.
	 */
	public HerbrandBase(FolSignature sig) throws IllegalArgumentException{
		if(!sig.getFunctors().isEmpty()) throw new IllegalArgumentException("The Herbrand base is defined only for signatures without functors.");
		this.atoms = new HashSet<Atom>();
		for(Predicate p: sig.getPredicates()){
			if(p.getArity() == 0) this.atoms.add(new Atom(p));
			this.atoms.addAll(this.getAllInstantiations(sig, p, new ArrayList<Term>()));
		}
	}
	
	/**
	 * Computes all instantiations of the predicate "p" relative to the signature "sig"
	 * where "arguments" defines the first arguments of the atoms.
	 * @param sig a signature for which the instantiations should be computed.
	 * @param p the predicate of the atoms.
	 * @param arguments the currently set arguments of the atoms.
	 * @return the complete set of instantiations of "p" relative to "sig" and "arguments".
	 */
	private Set<Atom> getAllInstantiations(FolSignature sig, Predicate p, List<Term> arguments){
		if(p.getArity() == arguments.size()){
			Set<Atom> atoms = new HashSet<Atom>();
			atoms.add(new Atom(p,arguments));
			return atoms;
		}
		Sort currentSort = p.getArguments().get(arguments.size());
		Set<Atom> atoms = new HashSet<Atom>();
		for(Term c: sig.getConstants()){
			if(!c.getSort().equals(currentSort))
				continue;
			List<Term> newArguments = new ArrayList<Term>(arguments);
			newArguments.add(c);
			atoms.addAll(this.getAllInstantiations(sig, p, newArguments));
		}		
		return atoms;
	}
		
	/**
	 * Computes all possible Herbrand interpretations of this Herbrand
	 * base, i.e. all possible subsets of this Herbrand base.
	 * @return all possible Herbrand interpretations of this Herbrand
	 * base, i.e. all possible subsets of this Herbrand base.
	 */
	public Set<HerbrandInterpretation> allHerbrandInterpretations(){
		Set<HerbrandInterpretation> interpretations = new HashSet<HerbrandInterpretation>();
		Set<Set<Atom>> subsets = new SetTools<Atom>().subsets(this.getAtoms());
		for(Set<Atom> atoms: subsets)
			interpretations.add(new HerbrandInterpretation(atoms));		
		return interpretations;
	}
	
	/**
	 * Returns all atoms of this Herbrand base.
	 * @return all atoms of this Herbrand base.
	 */
	public Set<Atom> getAtoms(){
		return new HashSet<Atom>(this.atoms);
	}
}
