package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;


/**
 * This class captures the common functionalities of binary formulas like conjunction,
 * disjunction, etc.
 * @author Matthias Thimm
 */
public abstract class AssociativeFormula extends FolFormula implements Collection<RelationalFormula> {
	
	/**
	 * The inner formulas of this formula 
	 */
	private Set<RelationalFormula> formulas;

	/**
	 * Creates a new associative formula with the given inner formulas. 
	 * @param formulas a collection of formulas.
	 */
	public AssociativeFormula(Collection<? extends RelationalFormula> formulas){
		this.formulas = new HashSet<RelationalFormula>(formulas);
	}
	
	/**
	 * Creates a new (empty) associative formula.
	 */
	public AssociativeFormula(){
		this(new HashSet<RelationalFormula>());
	}
	
	/**
	 * Creates a new associative formula with the two given formulae
	 * @param first a relational formula.
	 * @param second a relational formula.
	 */
	public AssociativeFormula(RelationalFormula first, RelationalFormula second){
		this();
		this.add(first);
		this.add(second);
	}
		
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getConstants()
	 */
	public Set<Constant> getConstants(){
		Set<Constant> constants = new HashSet<Constant>();
		for(RelationalFormula f: this)
			constants.addAll(f.getConstants());
		return constants;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getPredicates()
	 */
	public Set<Predicate> getPredicates(){
		Set<Predicate> predicates = new HashSet<Predicate>();
		for(RelationalFormula f: this)
			predicates.addAll(f.getPredicates());
		return predicates;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		Set<Functor> functors = new HashSet<Functor>();
		for(RelationalFormula f: this)
			functors.addAll(f.getFunctors());
		return functors;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getVariables()
	 */
	public Set<Variable> getVariables(){
		Set<Variable> variables = new HashSet<Variable>();
		for(RelationalFormula f: this)
			variables.addAll(f.getVariables());
		return variables;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getDisjunctions()
	 */
	public Set<Disjunction> getDisjunctions(){
		Set<Disjunction> disjuncts = new HashSet<Disjunction>();
		for(RelationalFormula f: this)
			disjuncts.addAll(((FolFormula)f).getDisjunctions());
		return disjuncts;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getConjunctions()
	 */	
	public Set<Conjunction> getConjunctions(){
		Set<Conjunction> conjuncts = new HashSet<Conjunction>();
		for(RelationalFormula f: this)
			conjuncts.addAll(((FolFormula)f).getConjunctions());
		return conjuncts;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getQuantifiedFormulas()
	 */
	public Set<QuantifiedFormula> getQuantifiedFormulas(){
		Set<QuantifiedFormula> quantifiedFormulas = new HashSet<QuantifiedFormula>();
		for(RelationalFormula f: this)
			quantifiedFormulas.addAll(((FolFormula)f).getQuantifiedFormulas());
		return quantifiedFormulas;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getFunctionalTerms()
	 */
	public Set<FunctionalTerm> getFunctionalTerms(){
		Set<FunctionalTerm> terms = new HashSet<FunctionalTerm>();
		for(RelationalFormula f: this)
			terms.addAll(f.getFunctionalTerms());
		return terms;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getAtoms()
	 */
	public Set<Atom> getAtoms(){
		Set<Atom> atoms = new HashSet<Atom>();
		for(RelationalFormula f: this)
			atoms.addAll(f.getAtoms());
		return atoms;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#containsQuantifier()
	 */
	public boolean containsQuantifier(){
		for(RelationalFormula f: this)
			if(f.containsQuantifier()) return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getUnboundVariables()
	 */
	public Set<Variable> getUnboundVariables(){
		Set<Variable> variables = new HashSet<Variable>();
		for(RelationalFormula f: this)
			variables.addAll(f.getUnboundVariables());
		return variables;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed()
	 */
	public boolean isClosed(){
		for(RelationalFormula f: this)
			if(!f.isClosed()) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed(java.util.Set)
	 */
	public boolean isClosed(Set<Variable> boundVariables){
		for(RelationalFormula f: this)
			if(!f.isClosed(boundVariables)) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound()
	 */
	public boolean isWellBound(){
		for(RelationalFormula f: this)
			if(!f.isWellBound()) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound(java.util.Set)
	 */
	public boolean isWellBound(Set<Variable> boundVariables){
		for(RelationalFormula f: this)
			if(!f.isWellBound(boundVariables)) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isLiteral()
	 */
	public boolean isLiteral(){
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(RelationalFormula e) {
		return this.formulas.add(e);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends RelationalFormula> c) {
		return this.formulas.addAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.formulas.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return this.formulas.contains(o);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return this.formulas.containsAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.formulas.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<RelationalFormula> iterator() {
		return this.formulas.iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		return this.formulas.remove(o);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return this.formulas.removeAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return this.formulas.retainAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		return this.formulas.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return this.formulas.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return this.formulas.toArray(a);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((formulas == null) ? 0 : formulas.hashCode());
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
		AssociativeFormula other = (AssociativeFormula) obj;
		if (formulas == null) {
			if (other.formulas != null)
				return false;
		} else if (!formulas.equals(other.formulas))
			return false;
		return true;
	}	
	
}
