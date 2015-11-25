package com.github.kreatures.core.logic.ocf;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.logics.cl.ClBeliefSet;
import net.sf.tweety.logics.cl.syntax.Conditional;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.parser.ParseException;


/**
 * A beliefbase implementation using ocfs.
 * A ocf beliefbase is a tuple (k,R) where k is an ordinal ocf
 * ranking function (ocf) and R is a set of propositions. In this implementation,
 * the ocf is represented by a set of ocfs of the form (B|A). The ranking
 * function is then taken as the c representation. 
 *  
 * See Gabriele Kern-Isberner. Conditionals in nonmonotonic reasoning and belief revision.
 * Lecture Notes in Computer Science, Volume 2087. 2001.
 * 
 * @author Sebastian Homann, Pia Wierzoch
 */
public class ConditionalBeliefbase extends BaseBeliefbase {
	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(ConditionalBeliefbase.class);
	
	// Set of propositions representing the known facts of this belief base
	private Set<PropositionalFormula> propositions = new HashSet<PropositionalFormula>();
	
	// Set of ocfs representing the ocf of this belief base, which in turn represents
	//  a plausibility ordering of all possible worlds
	private ClBeliefSet ocfs = new ClBeliefSet();
	
	/**
	 * Construct an empty belief base where every possible world is equally (im-)plausible
	 */
	public ConditionalBeliefbase() {
		super();
	}
	
	/**
	 * CCtor
	 * @param other
	 */
	public ConditionalBeliefbase(ConditionalBeliefbase other) {
		super(other);
		// TODO: check, if deep cloning is required ...
		this.propositions = new HashSet<PropositionalFormula>(other.propositions);
		this.ocfs = new ClBeliefSet(other.ocfs);
	}

	/**
	 * Parse ocf belief bases consisting of ocfs of the form (B|A) and propositions.
	 * Each line is interpreted as a separate element.
	 * 
	 * BNF:
	 * BBASE       ::= CONDITIONAL '\n' BBASE | FACT '\n' BBASE
	 * CONDITIONAL ::= '(' PLFORMULA '|' PLFORMULA ')'
	 * FACT        ::= PLLITERAL
	 */
	@Override
	protected void parseImpl(BufferedReader br) throws ParseException,
			IOException {
		log.info("parsing ocf belief base");
		propositions = new HashSet<PropositionalFormula>();
		ocfs = new ClBeliefSet();
		PlParser parser = new PlParser();
		String line;
		while( (line = br.readLine()) != null) {
			line = line.trim();
			if(line.equals("")) {
				continue;
			} else if(line.matches(".*(?<!\\|)\\|(?!\\|).*")) {
				// ocf of the form ( B | A )
				log.info("parsing ocf: {}", line);
				line = line.substring(1, line.length()-1); // remove braces
				
				// caution: PlParser matches '||' as a disjunction, so 
				// negative lookahead and lookbehind are used to parse single '|'.
				String[] ocf = line.split("(?<!\\|)\\|(?!\\|)");
				if(ocf.length != 2) {
					log.warn("could not parse belief item '({})'",line);
					continue;
				}
				PropositionalFormula conclusion = (PropositionalFormula) parser.parseFormula(ocf[0]);
				PropositionalFormula premise = (PropositionalFormula) parser.parseFormula(ocf[1]);
				Conditional newCond = new Conditional(premise, conclusion);
				ocfs.add(newCond);
			} else {
				// proposition
				log.info("parsing proposition: {}", line);
				PropositionalFormula proposition = (PropositionalFormula) parser.parseFormula(line);
				propositions.add(proposition);
			}
		}
	}
	
	/**
	 * Returns the set of propositions of this belief base
	 * @return the set of propositions of this belief base
	 */
	public Set<PropositionalFormula> getPropositions() {
		return propositions;
	}
	
	public void setPropositions(Set<PropositionalFormula> propositions) {
		this.propositions = propositions;
	}
	
	public ClBeliefSet getConditionalBeliefs() {
		return ocfs;
	}
	
	public void setConditionalBeliefs(ClBeliefSet ocfs) {
		this.ocfs = ocfs;
	}

	/**
	 * Return a list of all ocfs and all propositions from this beliefbase
	 * for debugging purposes.
	 */
	@Override
	public List<String> getAtomsAsStringList() {
		List<String> retval = new LinkedList<String>();
		retval.add("propositions: ");
		for(PropositionalFormula proposition : propositions) {
			retval.add(proposition.toString());
		}
		retval.add("");
		retval.add("ocfs: ");
		for(Conditional ocf : ocfs) {
			retval.add(ocf.toString());
		}
		
		return retval;
	}
	
	@Override
	public String getFileEnding() {
		return "ocf";
	}

	@Override
	public ConditionalBeliefbase clone() {
		return new ConditionalBeliefbase(this);
	}

	@Override
	public Signature getSignature() {
		PropositionalSignature signature = new PropositionalSignature();
		
		signature.addSignature(ocfs.getSignature());
		for(PropositionalFormula proposition : propositions) {
			signature.addSignature(proposition.getSignature());
		}
		return signature;
	}

	@Override
	public String toString() {
		String retval = "< {";
		boolean more = false;
		for(Conditional ocf : ocfs) {
			if(more)
				retval += ", ";
			else
				more = true;
			
			retval += ocf.toString();
		}
		retval += "}, {";
		more = false;
		for(PropositionalFormula proposition : propositions) {
			if(more)
				retval += ", ";
			else
				more = true;
			retval += proposition.toString();
		}
		retval += "} >";
		return retval;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof ConditionalBeliefbase))	return false;
		ConditionalBeliefbase co = (ConditionalBeliefbase)other;
		
		if(!propositions.equals(co.propositions))	return false;
		if(!ocfs.equals(co.ocfs))	return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		return propositions.hashCode() + ocfs.hashCode() + 7;
	}

}
