package com.github.angerona.fw.aspgraph.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;

/**
 * Two valued representation of an answer set
 * @author ella
 *
 */
public class AnswerSetTwoValued {
	/**
	 * Literals that are contained in the answer set
	 */
	private HashSet<String> founded;
	
	/**
	 * Literals that are not contained in the answer set
	 */
	private HashSet<String> unfounded;
	
	/**
	 * Creates a new two valued representation of an answer set
	 * @param founded Set of literals that are contained in the answer set
	 * @param unfounded Set of literals that are not contained in the answer set
	 */
	public AnswerSetTwoValued(HashSet<String> founded, HashSet<String> unfounded){
		this.founded = founded;
		this.unfounded = unfounded;
	}
	
	/**
	 * Creates a new two valued representation of an answer set
	 * @param allLiterals Set of allLiterals that are contained in the logic program
	 * @param as Answer set which should be represented as a two valued answer set
	 */
	public AnswerSetTwoValued(Set<String> allLiterals, AnswerSet as){
		founded = new HashSet<String>();
		unfounded = new HashSet<String>();
		Map<String,Set<Literal>> asLiterals = as.literals;
		Set<String> answerSetLiterals = new HashSet<String>();
		
		/* Get String representation of literals in answer set */
		for (String s : asLiterals.keySet()){
			for (Literal l : asLiterals.get(s)){
				answerSetLiterals.add(l.toString());
			}
		}
		
		/* Divide literals into founded and unfounded set */
		for (String lit : allLiterals){
			if (answerSetLiterals.contains(lit)) founded.add(lit);
			else unfounded.add(lit);
		}
	}
	
	/**
	 * Returns literals that are contained in answer set
	 * @return Literals that are contained in answer set
	 */
	public HashSet<String> getFounded(){
		return founded;
	}
	
	/**
	 * Return literals that are not contained in answer set
	 * @return Literals that are not contained in answer set
	 */
	public HashSet<String> getUnfounded(){
		return unfounded;
	}
	
	
}
