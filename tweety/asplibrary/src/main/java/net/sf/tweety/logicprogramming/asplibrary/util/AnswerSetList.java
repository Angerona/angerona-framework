package net.sf.tweety.logicprogramming.asplibrary.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;

/**
 * this class represents a collection of answer sets and
 * provides some basic reasoning modes.
 * 
 * @author Thomas Vengels, Tim Janus
 *
 */
public class AnswerSetList extends ArrayList<AnswerSet> {
	
	/** kill warning */
	private static final long serialVersionUID = 1130680162671151620L;

	/** constant id for the credolous policy for operations of the AnswerSetList object. */
	static public final int POLICY_CREDOLOUS = 1;
	
	/** constant id for the skeptical policy for operations of the AnswerSetList object. */
	static public final int POLICY_SKEPTICAL = 2;
	

	public Set<Literal> getFactsByName(String name) {
		return getFactsByName(name, POLICY_SKEPTICAL);
	}
	
	/**
	 * Returns all the literals in the answerset with have a given name.
	 * @param name		the name of the literal 'married' as example.
	 * @param policy	The used policy might be skeptical or credolous.
	 * @return			A set of literals which are also in the answerset.
	 */
	public Set<Literal> getFactsByName(String name, int policy) {
		Set<Literal> reval = new HashSet<Literal>();
		boolean first = true;
		for(AnswerSet as : this) {
			if(first == false && policy == POLICY_SKEPTICAL) {
				reval.retainAll(as.getLiteralsBySymbol(name));
			} else {
				reval.addAll(as.getLiteralsBySymbol(name));
			}
			first = false;
		}
		return reval;
	}
	
	/**
	 * this method returns true if at least one
	 * answer set support q.
	 * @param q
	 * @return
	 */
	public boolean	holdsOne( Literal q ) {
		return false;
	}
	
	/**
	 * this method returns ture iff all
	 * answer sets support q.
	 * @param q
	 * @return
	 */
	public boolean	holdsAll( Literal q ) {
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Answer Sets: "+this.size());
		sb.append("\n--------------");
		for (AnswerSet s : this) {
			sb.append("\n");
			sb.append(s);
			sb.append("\n--------------");
		}
		sb.append("\n");
		
		return sb.toString();
	}
}
