package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.StringReader;
import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;


/**
 * this class models a rule for an extended
 * logic program. a rule is a collection
 * of literals, with separate lists for the
 * head and the body.
 * 
 * @author Thomas Vengels, Tim Janus
 *
 */
public class Rule {

	List<Literal>	head;
	List<Literal>	body;
	
	public Rule() {
		this.head = new LinkedList<Literal>();
		this.body = new LinkedList<Literal>();
	}
	
	public Rule(List<Literal> litsHead, List<Literal> litsBody) {
		this.head = litsHead;
		this.body = litsBody;
	}
	
	public Rule(String ruleexpr) {
		try {
			ELPParser ep = new ELPParser( new StringReader( ruleexpr ));
			Rule r = ep.rule();
			this.head = r.head;
			this.body = r.body;
		} catch (Exception e) {
			System.err.println("Rule: could not parse input!");
			System.err.println(e);
			System.err.println("Input: " + ruleexpr);
		}
	}
	
	public	int		numHead() {
		return (head==null)? 0 : head.size();
	}
	
	public	int		numBody() {
		return (body==null)? 0 : body.size();
	}
	
	public List<Literal>	getHead() {
		return (this.head==null)? Collections.<Literal>emptyList() : this.head;
	}
	
	public List<Literal>	getBody() {
		return (this.body==null)? Collections.<Literal>emptyList() : this.body; 
	}
	
	public List<Literal> getLiterals() {
		List<Literal> reval = new LinkedList<Literal>();
		if(head != null)	reval.addAll(head);
		if(body != null)	reval.addAll(body);
		return reval;
	}
	
	public void		addHead(Literal l) {
		if (this.head == null)
			this.head = new LinkedList<Literal>();
		this.head.add(l);
	}
	
	public void		addBody(Literal l) {
		if (this.body == null)
			this.body = new LinkedList<Literal>();
		this.body.add(l);
	}
	
	public void		addHead(Collection<? extends Literal> l) {
		if (this.head == null)
			this.head = new LinkedList<Literal>();
		this.head.addAll(l);
	}
	
	public void		addBody(Collection<? extends Literal> l) {
		if (this.body == null)
			this.body = new LinkedList<Literal>();
		this.body.addAll(l);
	}
	
	public	boolean		isFact() {
		return (this.numBody() == 0)
			&& !this.isChoice()
			&& (this.numHead() == 1);
	}
	
	public	boolean 	isChoice() {
		return false;
	}
	
	public	boolean		isConstraint() {
		return this.numHead() == 0;
	}
	
	public boolean		isWeakConstraint() {
		return false;
	}
	
	public boolean		isComment() {
		return false;
	}
	
	/**
	 * Proofs if the given rule is safe for use in a solver.
	 * To get a felling when a rule is safe read the following text
	 * from the dlv documentation:
	 * 
	 * A variable X in an aggregate-free rule is safe if at least one of the following conditions is satisfied:
	 * X occurs in a positive standard predicate in the body of the rule;
	 * X occurs in a true negated standard predicate in the body of the rule;
	 * X occurs in the last argument of an arithmetic predicate A and all other arguments of A are safe. (*not supported yet)
	 * A rule is safe if all its variables are safe. However, cyclic dependencies are disallowed, e.g., :- #succ(X,Y), #succ(Y,X) is not safe.
	 * 
	 * @return true if the rule is safe considering the above conditions, false otherwise.
	 */
	public boolean isSafe() {
		Set<Term> variables = new HashSet<Term>();
		Set<Literal> allLit = new HashSet<Literal>();
		allLit.addAll(head);
		allLit.addAll(body);
		
		// TODO: only depth of one... the entire asp-library has major desing issues... best solution: Redesign core interfaces
		// TOTALLY HACKED WILL NOT WOKR FOR EVERYTHING:...
		for(Literal l : allLit) {
			if(!l.isGround()) {
				for(Term t : l.getAtom().getTerms()) {
					if(t instanceof Variable) {
						variables.add((Variable)t);
					} else if(t instanceof Atom) {
						if(!((Atom)t).isGround()) {
							for(Term t2 : ((Atom)t).getTerms()) {
								if(t2 instanceof Variable) {
									variables.add((Variable)t2);
								} else if(t2 instanceof StdTerm) {
									StdTerm st = (StdTerm) t2;
									if(st.get().charAt(0) >= 65 && st.get().charAt(0) <= 90) {
										variables.add(st);
									}
								}
							}
						}
					}
				}
			}
		}
		
		if(variables.size() == 0)
			return true;
		
		for(Term x : variables) {
			boolean safe = false;
			for(Literal l : allLit) {
				if(	(!l.isAggregate() && !l.isArithmetic() && !l.isCondition() && !l.isDefaultNegated() && !l.isTrueNegated() && !l.isRelational() && !l.isWeightLiteral()) ||
					(l.isTrueNegated()) ) {
					for(Term t : l.getAtom().getTerms()) {
						if(t.equals(x)) {
							safe = true;
						}
					}
				}
			}
			
			if(!safe)
				return false;
		}
		
		return true;
	}
	
	@Override
	public String	toString() {
		String ret = "";
		if (this.numHead() > 0) {
			Iterator<Literal> iter = this.head.iterator();
			ret += iter.next();
			while (iter.hasNext())
				ret += " | " + iter.next();			
		}
		if (this.numBody() > 0) {
			ret += ":- ";
			Iterator<Literal> iter = this.body.iterator();
			ret += iter.next();
			while (iter.hasNext())
				ret += ", " + iter.next();
		}
		ret += ".";
		
		return ret;
	}
	
	@Override 
	public boolean equals(Object other) {
		if(!(other instanceof Rule)) 	return false;
		Rule or = (Rule)other;
		
		return this.head.equals(or.head) && this.body.equals(or.body);
	}
}
