package net.sf.tweety.logicprogramming.asplibrary.util;

import java.util.*;
import net.sf.tweety.logicprogramming.asplibrary.syntax.*;

/**
 * this class represents a collection of answer sets and
 * provides some basic reasoning modes.
 * 
 * @author Thomas Vengels
 *
 */
public class AnswerSetList extends ArrayList<AnswerSet> {

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
