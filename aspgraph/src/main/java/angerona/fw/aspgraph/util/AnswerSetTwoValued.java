package angerona.fw.aspgraph.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;

public class AnswerSetTwoValued {
	private HashSet<String> founded;
	private HashSet<String> unfounded;
	
	public AnswerSetTwoValued(HashSet<String> founded, HashSet<String> unfounded){
		this.founded = founded;
		this.unfounded = unfounded;
	}
	
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
	
	public HashSet<String> getFounded(){
		return founded;
	}
	
	public HashSet<String> getUnfounded(){
		return unfounded;
	}
	
	
}
