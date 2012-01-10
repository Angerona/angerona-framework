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
 * @author Thomas Vengels
 *
 */
public class Rule {

	List<Literal>	head;
	List<Literal>	body;
	
	public Rule() {
		this.head = null;
		this.body = null;
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
