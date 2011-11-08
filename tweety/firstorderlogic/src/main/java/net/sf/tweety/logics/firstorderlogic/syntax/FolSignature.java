package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.SymbolSet;
import net.sf.tweety.logics.CommonStructure;
import net.sf.tweety.logics.CommonTerm;
import net.sf.tweety.logics.LogicalSymbols;

/**
 * This class captures the signature of a specific
 * first-order language.
 * @author Matthias Thimm
 */
public class FolSignature extends Signature implements LogicalSymbols{
	
	private Set<Term> constants;
	private Set<Sort> sorts;
	private Set<Predicate> predicates;
	private Set<Functor> functors;
	
	/**
	 * Creates an empty signature 
	 */
	public FolSignature(){
		this.constants = new HashSet<Term>();
		this.sorts = new HashSet<Sort>();
		this.predicates = new HashSet<Predicate>();
		this.functors = new HashSet<Functor>();
	}
	
	/**
	 * Creates an FOL Signature from the given SymbolSet
	 * @param symbols	reference to the SymbolSet used for creating this signature.
	 */
	public FolSignature(SymbolSet symbols) {
		this();
		fromSymbolSet(symbols);
	}
	
	/**
	 * Creates a signature with the given objects (should be sorts, constants,
	 * predicates, functors, or formulas).
	 * @param c a collection of items to be added.
	 * @throws IllegalArgumentException if at least one of the given objects is
	 * 	 neither a constant, a sort, a predicate, a functor, or a formula.
	 */
	public FolSignature(Collection<?> c) throws IllegalArgumentException{
		this();
		this.addAll(c);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Signature#isSubSignature(net.sf.tweety.kr.Signature)
	 */
	public boolean isSubSignature(Signature other){
		if(!(other instanceof FolSignature))
			return false;
		FolSignature o = (FolSignature) other;
		if(!o.constants.containsAll(this.constants)) return false;
		if(!o.functors.containsAll(this.functors)) return false;
		if(!o.predicates.containsAll(this.predicates)) return false;
		if(!o.sorts.containsAll(this.sorts)) return false;
		return true;
	}
	
	/**
	 * Adds the given object, either a constant, a sort, a predicate, or a functor,
	 * to this signature. If a constant is added its sort is added as well, the same is
	 * for any sort mentioned in predicates and functors. Alternatively, you can
	 * pass over a formula, then all predicates, sorts, constants, and functors
	 * of this formula are added to the signature.
	 * @param obj the object to be added, either a constant, a sort, a predicate, a functor,
	 *    or a formula.
	 * @throws IllegalArgumentException if the given object is neither a constant, a sort, a
	 *    predicate, a functor, or a formula.
	 */
	public void add(Object obj) throws IllegalArgumentException{
		if(obj instanceof Constant){
			sorts.add(((Constant)obj).getSort());
			constants.add((Constant)obj);
			return;
		}
		if(obj instanceof Sort){
			sorts.add((Sort)obj);
			return;
		}
		if(obj instanceof Predicate){
			predicates.add((Predicate)obj);
			this.addAll(((Predicate)obj).getArguments());
			return;
		}
		if(obj instanceof Functor){
			functors.add((Functor)obj);
			this.addAll(((Functor)obj).getArguments());
			return;
		}
		if(obj instanceof FolFormula){
			this.addAll(((FolFormula)obj).getConstants());
			this.addAll(((FolFormula)obj).getPredicates());
			this.addAll(((FolFormula)obj).getFunctors());
			return;
		}
		throw new IllegalArgumentException("Class " + obj.getClass() + " of parameter is unsupported.");
	}
	
	/**
	 * Adds items of the given collection to this signature. These should be either 
	 * constants, sorts, predicates, or functors. Alternatively, you can pass over formulas,
	 * then all predicates, sorts, constants, and functors of this formula are added t
	 * the signature.
	 * @param c the collection of items to be added
	 * @throws IllegalArgumentException if at least one of the given objects is
	 * 	 neither a constant, a sort, a predicate, a functor, or a formula.
	 */
	public void addAll(Collection<?> c) throws IllegalArgumentException{
		for(Object obj: c)
			this.add(obj);
	}
	
	/**
	 * Checks whether the given formula can be represented by this signature.
	 * @param folFormula A formula to be checked.
	 * @return "true" if the given formula is representable, "false" otherwise.
	 */
	public boolean isRepresentable(FolFormula folFormula){
		if(!this.constants.containsAll(folFormula.getConstants())) return false;
		if(!this.predicates.containsAll(folFormula.getPredicates())) return false;
		if(!this.functors.containsAll(folFormula.getFunctors())) return false;
		return true;
	}

	public Set<Term> getConstants(){
		return this.constants;
	}
	
	public Set<Predicate> getPredicates(){
		return this.predicates;
	}
	
	public Set<Functor> getFunctors(){
		return this.functors;
	}
	
	public Set<Sort> getSorts(){
		return this.sorts;
	}
	
	public Constant getConstant(String s){
		for(Term t: this.constants)
			if(((Constant) t).getName().equals(s))
				return (Constant) t;
		return null;
	}
	
	public Predicate getPredicate(String s){
		for(Predicate p: this.predicates)
			if(p.getName().equals(s))
				return p;
		return null;
	}
	
	public Functor getFunctor(String s){
		for(Functor f: this.functors)
			if(f.getName().equals(s))
				return f;
		return null;
	}
	
	public Sort getSort(String s){
		for(Sort st: this.sorts)
			if(st.getName().equals(s))
				return st;
		return null;
	}
	
	public boolean containsConstant(String s){
		return this.getConstant(s) != null;
	}

	public boolean containsPredicate(String s){
		return this.getPredicate(s) != null;
	}
	
	public boolean containsFunctor(String s){
		return this.getFunctor(s) != null;
	}
	
	public boolean containsSort(String s){
		return this.getSort(s) != null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constants == null) ? 0 : constants.hashCode());
		result = prime * result
				+ ((functors == null) ? 0 : functors.hashCode());
		result = prime * result
				+ ((predicates == null) ? 0 : predicates.hashCode());
		result = prime * result + ((sorts == null) ? 0 : sorts.hashCode());
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
		FolSignature other = (FolSignature) obj;
		if (constants == null) {
			if (other.constants != null)
				return false;
		} else if (!constants.equals(other.constants))
			return false;
		if (functors == null) {
			if (other.functors != null)
				return false;
		} else if (!functors.equals(other.functors))
			return false;
		if (predicates == null) {
			if (other.predicates != null)
				return false;
		} else if (!predicates.equals(other.predicates))
			return false;
		if (sorts == null) {
			if (other.sorts != null)
				return false;
		} else if (!sorts.equals(other.sorts))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.Signature#addSignature(net.sf.tweety.Signature)
	 */
	@Override
	public void addSignature(Signature other) {
		if(!(other instanceof FolSignature))
			return;
		FolSignature folSig = (FolSignature) other;
		this.constants.addAll(folSig.constants);
		this.functors.addAll(folSig.functors);
		this.predicates.addAll(folSig.predicates);
		this.sorts.addAll(folSig.sorts);
		
	}

	@Override
	public void fromSymbolSet(SymbolSet symbols) {
		clear();
		
		for(String constant : symbols.getConstants()) {
			if(symbols.isSorted()) {
				Sort sort = new Sort(symbols.getConstantSort(constant));
				this.add(new Constant(constant, sort));
			} else {
				this.add(new Constant(constant));
			}
		}
		
		for(String symbol : symbols.getSymbols()) {
			int arity = symbols.getArity(symbol);
			if(symbols.isSorted()) {
				List<Sort> sorts = new LinkedList<Sort>();
				for(int i=0; i<arity; ++i) {
					sorts.add(new Sort(symbols.getSymbolSort(symbol, i)));
				}
				this.add(new Predicate(symbol, sorts));
			} else {
				this.add(new Predicate(symbol, arity));
			}
		}
	}

	private void clear() {
		this.constants.clear();
		this.functors.clear();
		this.predicates.clear();
		this.sorts.clear();
	}

	@Override
	public Set<CommonStructure> getCommonStructureElements() {
		Set<CommonStructure> reval = new HashSet<CommonStructure>();
		reval.addAll(predicates);
		reval.addAll(functors);
		return reval;
	}

	@Override
	public Set<CommonTerm> getCommonTermElements() {
		Set<CommonTerm> reval = new HashSet<CommonTerm>();
		reval.addAll(constants);
		return reval;
	}

	@Override
	public void fromSignature(Signature signature) {
		clear();
		if(signature instanceof FolSignature) {
			this.addSignature(signature);
		} else {
			for(CommonStructure cs : signature.getCommonStructureElements()) {
				List<Sort> sorts = new LinkedList<Sort>();
				for(String sort : cs.getSorts()) {
					sorts.add(new Sort(sort));
				}
				if(cs.isPredicate()) {
					add(new Predicate(cs.getName(), sorts));
				} else if(cs.isFunctional()) {
					Sort target = sorts.get(sorts.size()-1);
					sorts.remove(sorts.size()-1);
					add(new Functor(cs.getName(), sorts, target));
				}
			}
			
			for(CommonTerm ct : signature.getCommonTermElements()) {
				if(ct.isConstant()) {
					add(new Constant(ct.getName(), new Sort(ct.getSortName())));
				}
			}
		}
	}
}
