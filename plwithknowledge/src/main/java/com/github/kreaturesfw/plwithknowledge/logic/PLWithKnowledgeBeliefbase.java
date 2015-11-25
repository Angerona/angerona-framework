package com.github.kreaturesfw.plwithknowledge.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.BaseBeliefbase;
import com.github.kreaturesfw.core.parser.ParseException;

/**
 * A propositional beliefbase containing knowledge and assertions (K,E)
 * 
 * @author Pia Wierzoch
 */
public class PLWithKnowledgeBeliefbase extends BaseBeliefbase{
	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(PLWithKnowledgeBeliefbase.class);
	
	//The set of formulas the Agent get through updates from other Agents
	private LinkedList<PropositionalFormula> assertion = new LinkedList<PropositionalFormula>();
	
	//The set of nonupdatable formulas of the Agent which the Agent always believes
	private Set<PropositionalFormula> knowledge = new HashSet<PropositionalFormula>();
	
	/**
	 * Construct an empty belief
	 */
	public PLWithKnowledgeBeliefbase() {
		super();
	}
	
	/**
	 * CCtor
	 * @param other
	 */
	public PLWithKnowledgeBeliefbase(PLWithKnowledgeBeliefbase other) {
		super(other);
		// TODO: check, if deep cloning is required ...
		this.assertion = new LinkedList<PropositionalFormula>(other.assertion);
		this.knowledge = new HashSet<PropositionalFormula>(other.knowledge);
	}
	
	@Override
	protected void parseImpl(BufferedReader br) throws ParseException,
			IOException {
		log.info("parsing belief base");
		assertion = new LinkedList<PropositionalFormula>();
		knowledge = new HashSet<PropositionalFormula>();
		PlParser parser = new PlParser();
		String line;
		boolean nonUpd = true;
		while( (line = br.readLine()) != null) {
			line = line.trim();
			if(line.equals("")) {
				continue;
			} else if(line.equals(";")){
				nonUpd = false;
				continue;
			}else if(nonUpd) {
				// nonUpdatable Formula
				log.info("parsing knowledge Formulas: {}", line);
				PropositionalFormula formula = (PropositionalFormula) parser.parseFormula(line);
				knowledge.add(formula);
			} else if(!nonUpd){
				// context
				log.info("parsing assertions: {}", line);
				PropositionalFormula formula = (PropositionalFormula) parser.parseFormula(line);
				assertion.add(formula);
			}
		}
		
	}
	
	public LinkedList<PropositionalFormula> getAssertions() {
		return assertion;
	}
	
	public void setAssertions(LinkedList<PropositionalFormula> assertion) {
		this.assertion = assertion;
	}
	
	public Set<PropositionalFormula> getKnowledge() {
		return knowledge;
	}
	
	public void setKnowledge(Set<PropositionalFormula> knowledge) {
		this.knowledge = knowledge;
	}

	/**
	 * Return a list of all conditionals and all propositions from this beliefbase
	 * for debugging purposes.
	 */
	@Override
	public List<String> getAtomsAsStringList() {
		List<String> retval = new LinkedList<String>();
		retval.add("assertion: ");
		for(PropositionalFormula asser : assertion) {
			retval.add(asser.toString());
		}
		retval.add("");
		retval.add("knowledge: ");
		for(PropositionalFormula know : knowledge) {
			retval.add(know.toString());
		}
		
		return retval;
	}

	@Override
	public String getFileEnding() {
		return "upd";
	}

	@Override
	public PLWithKnowledgeBeliefbase clone() {
		return new PLWithKnowledgeBeliefbase(this);
	}

	@Override
	public Signature getSignature() {
		PropositionalSignature signature = new PropositionalSignature();
		
		for(PropositionalFormula asser : assertion) {
			signature.addSignature(asser.getSignature());
		}
		for(PropositionalFormula know : knowledge) {
			signature.addSignature(know.getSignature());
		}
		return signature;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof PLWithKnowledgeBeliefbase))	return false;
		PLWithKnowledgeBeliefbase co = (PLWithKnowledgeBeliefbase)other;
		
		if(!assertion.equals(co.assertion))	return false;
		if(!knowledge.equals(co.knowledge))	return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		return assertion.hashCode() + knowledge.hashCode() + 7;
	}
	
	@Override
	public String toString() {
		String retval = "< {Assertion: ";
		boolean more = false;
		for(PropositionalFormula asser : assertion) {
			if(more)
				retval += ", ";
			else
				more = true;
			retval += asser.toString();
		}
		retval += "}, {Knowledge: ";
		more = false;
		for(PropositionalFormula know : knowledge) {
			if(more)
				retval += ", ";
			else
				more = true;
			retval += know.toString();
		}
		retval += "} >";
		return retval;
	}

}

