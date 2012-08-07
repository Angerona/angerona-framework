package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.*;

/**
 * this class models a list term that can be used for
 * dlv complex programs.
 * 
 * @author Thomas Vengels
 *
 */
public class ListTerm implements Term {

	Collection<Term>	listHead;
	Collection<Term>	listTail;
	
	public ListTerm() {
		this.listHead = new LinkedList<Term>();
		this.listTail = new LinkedList<Term>();
	}
	
	/**
	 * constructor for list elements with given [head|tail].
	 * @param head
	 * @param tail
	 */
	public ListTerm(Collection<Term> head, Collection<Term> tail) {
		this.listHead = head;
		this.listTail = tail;
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isAtom() {
		return false;
	}

	@Override
	public boolean isList() {
		return true;
	}

	@Override
	public boolean isSet() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public void set(String value) {
		// not supported
	}

	@Override
	public String get() {
		// not supported
		return null;
	}

	@Override
	public void set(int value) {
		// not supported
	}

	@Override
	public int getInt() {
		// not supported
		return 0;
	}

	@Override
	public String toString() {
		// return list
		String ret = "[";
		if ((this.listHead != null) && (this.listHead.size() > 0)) {
			// draw list, separate head and tail!
			ret += listPrint(this.listHead);
			if (this.listTail != null)
				ret += "|" + listPrint(this.listTail);
		} else {
			// empty list here!
		}
		ret += "]";
		return ret;
	}
	
	protected String listPrint(Collection<Term> tl) {
		String ret = "";
		Iterator<Term> iter = tl.iterator();
		if (iter.hasNext())
			ret += iter.next().toString();
		while (iter.hasNext())
			ret += ", " + iter.next().toString();
		return ret;
	}

	@Override
	public boolean isString() {
		return false;
	}

	@Override
	public TermType type() {
		return TermType.List;
	}
}
