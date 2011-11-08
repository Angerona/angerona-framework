package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.StringReader;
import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;


/**
 * this class models a weak constraint. a
 * weak constraint is a soft constraint with
 * some weight and level indicating its
 * priority.
 * 
 * @author Thomas Vengels
 *
 */
public class WeakConstraint extends Rule {

	public WeakConstraint() {
		super();
	}
	
	public WeakConstraint(List<Literal> litsBody) {
		super(null,litsBody);
	}
	
	/*
	public WeakConstraint(String ruleexpr) {
		try {
			ELPParser ep = new ELPParser( new StringReader( ruleexpr ));
			WeakConstraint r = ep.rule();
			this.head = r.head;
			this.body = r.body;
		} catch (Exception e) {
			System.err.println("Rule: could not parse input!");
			System.err.println(e);
			System.err.println("Input: " + ruleexpr);
		}
	}
	*/
	
	@Override
	public void		addHead(Literal l) {
		// not possible
	}
	
	@Override
	public void		addHead(Collection<? extends Literal> l) {
		// not possible
	}
	
	public	boolean		isConstraint() {
		return true;
	}
	
	@Override
	public String	toString() {
		String ret = "";
		if (this.numBody() > 0) {
			ret += ":~ ";
			Iterator<Literal> iter = this.body.iterator();
			ret += iter.next();
			while (iter.hasNext())
				ret += ", " + iter.next();
		}
		ret += ".";
		
		return ret;
	}
}
