package net.sf.tweety.logicprogramming.asplibrary.util;

import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.syntax.*;

public class AnswerSet extends BeliefSet {

	public final int level;
	public final int weight;	
	
	public AnswerSet(Collection<Literal> lits, int level, int weight) {

		super(lits);
		this.level = level;
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return super.toString() + " ["+level+","+weight+"]";
	}
}
