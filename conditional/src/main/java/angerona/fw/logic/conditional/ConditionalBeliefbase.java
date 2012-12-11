package angerona.fw.logic.conditional;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.logics.conditionallogic.ClBeliefSet;
import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.propositionallogic.parser.PlParser;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.parser.ParseException;

/**
 * A beliefbase implementation using conditionals.
 * A conditional beliefbase is a tuple (k,R) where k is an ordinal conditional
 * ranking function (ocf) and R is a set of propositions. In this implementation,
 * the ocf is represented by a set of conditionals of the form (B|A). The ranking
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
	
	// Set of conditionals representing the ocf of this belief base, which in turn represents
	//  a plausibility ordering of all possible worlds
	private ClBeliefSet conditionals = new ClBeliefSet();
	
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
		this.conditionals = new ClBeliefSet(other.conditionals);
	}

	/**
	 * Parse conditional belief bases consisting of conditionals of the form (B|A) and propositions.
	 * Each line is interpreted as a separate element.
	 * 
	 * BNF:
	 * BBASE       ::= CONDITIONAL '\n' BBASE | FACT '\n' BBASE
	 * CONDITIONAL ::= '(' PLFORMULA '|' PLFORMULA ')'
	 * FACT        ::= PLLITERAL
	 */
	@Override
	protected void parseInt(BufferedReader br) throws ParseException,
			IOException {
		log.info("parsing conditional belief base");
		propositions = new HashSet<PropositionalFormula>();
		conditionals = new ClBeliefSet();
		PlParser parser = new PlParser();
		String line;
		while( (line = br.readLine()) != null) {
			line = line.trim();
			if(line.contains("|")) {
				// conditional of the form ( B | A )
				log.info("parsing conditional: {}", line);
				line = line.substring(1, line.length()-1); // remove braces
				
				// caution: PlParser matches '||' as a disjunction, so 
				// negative lookahead and lookbehind are used to parse single '|'.
				String[] conditional = line.split("(?<!\\|)\\|(?!\\|)");
				if(conditional.length != 2) {
					log.warn("could not parse belief item '({})'",line);
					continue;
				}
				PropositionalFormula conclusion = (PropositionalFormula) parser.parseFormula(conditional[0]);
				PropositionalFormula premise = (PropositionalFormula) parser.parseFormula(conditional[1]);
				Conditional newCond = new Conditional(premise, conclusion);
				conditionals.add(newCond);
			} else {
				// proposition
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
		return conditionals;
	}
	
	public void setConditionalBeliefs(ClBeliefSet conditionals) {
		this.conditionals = conditionals;
	}

	/**
	 * Return a list of all conditionals and all propositions from this beliefbase
	 * for debugging purposes.
	 */
	@Override
	public List<String> getAtoms() {
		List<String> retval = new LinkedList<String>();
		for(PropositionalFormula proposition : propositions) {
			retval.add(proposition.toString());
		}
		for(Conditional conditional : conditionals) {
			retval.add(conditional.toString());
		}
		
		return retval;
	}
	
	@Override
	public String getFileEnding() {
		return "ocf";
	}

	@Override
	public Object clone() {
		return new ConditionalBeliefbase(this);
	}

	@Override
	public Signature getSignature() {
		PropositionalSignature signature = new PropositionalSignature();
		
		signature.addSignature(conditionals.getSignature());
		for(PropositionalFormula proposition : propositions) {
			signature.addSignature(proposition.getSignature());
		}
		return signature;
	}

	@Override
	public String toString() {
		String retval = "< {";
		boolean more = false;
		for(Conditional conditional : conditionals) {
			if(more)
				retval += ", ";
			else
				more = true;
			
			retval += conditional.toString();
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

}
