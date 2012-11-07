package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;

/**
 * this class models a logical program, which is
 * a collection of rules.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class Program {
	
	/** The signature of the logic program */
	private ElpSignature signature;
	
	/** a set of all atoms used in the logic program */
	private Set<Atom> atoms = new HashSet<Atom>();
	
	/** a set of all rules of the logic program */
	private Set<Rule> rules = new HashSet<Rule>();
	
	/** Default Ctor: Does nothing */
	public Program() {}
	
	/** Copy Ctor: Used by clone method */
	public Program(Program other) {
		// TODO: COpy signature
		//this.signature = new ElpSignature(other.signature);
		this.atoms.addAll(other.getAtoms());
		this.rules.addAll(other.getRules()); 
	}
	
	/** @return	the set of atoms */
	public Set<Atom> getAtoms() {
		return atoms;
	}
	
	//Differs from contains in that it does a deep comparision of the rules rather than a reference-based one
	//Could be turned into an override of the ArrayList contains method later
	//--Daniel
	public boolean hasRule(Rule compRule)
	{
		/*
		for(Rule r : this)
		{
			//This solution depends on the rule.equals being a deep comparision
			if(compRule.equals(r))
				return true;
		}
		*/
		if (this.toString().contains(compRule.toString())) //Temporary fix
			return true;
		return false;
	}
	
	/**
	 * Adds the given rule to the program
	 * @param rule	Reference to the rule to add
	 * @return		true if the rule was successful added (not part of the programs
	 * 				rules yet), false otherwise
	 */
	public boolean addRule(Rule rule) {
		boolean reval = rules.add(rule);
		updateAtoms(rule);
		return reval;
	}
	
	public void addAllRules(Collection<Rule> rules) {
		rules.addAll(rules);
		updateAtoms();
	}
	
	/** @return 	An unmodifiable set of the rules of the program */
	public Set<Rule> getRules() {
		return Collections.unmodifiableSet(this.rules);
	}
	
	/**
	 * removes all the given rules from the program 
	 * @param toRemove	collection with rules which has to be remove from the program
	 */
	public void removeAllRules(Collection<Rule> toRemove) {
		rules.removeAll(toRemove);
		updateAtoms();
	}
	
	/** 
	 * empties the program.
	 */
	public void clearRules() {
		rules.clear();
		atoms.clear();
	}
	
	public int size() {
		return rules.size();
	}
	
	/**
	 * Adds another programs content to the content of this program.
	 * @param other	Reference to the other program.
	 */
	public void add(Program other) {
		rules.addAll(other.getRules());
		atoms.addAll(other.getAtoms());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Rule> it = rules.iterator();
		
		if (it.hasNext()) {
			sb.append( it.next() );
		}
		while (it.hasNext()) {
			sb.append("\n" + it.next());
		}
		return sb.toString();
	}
	
	public static Program loadFrom(String file) {
		try {
			return loadFrom(new FileReader( file ));
		} catch (FileNotFoundException e) {
			System.err.println("Error cant find file: " + file);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Program loadFrom(Reader stream) {
		Program ret = null;
		
		try {
			ELPParser ep = new ELPParser( stream );
			List<Rule> lr = ep.program();
			ret = new Program();
			for(Rule r: lr)
				ret.addRule(r);
			ret.calcSignature();
		} catch (Exception e) {
			System.err.println("Error while loading program: " + e.getMessage());
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public Signature getSignature() {
		if(signature == null)
			calcSignature();
		return signature;
	}
	
	private void calcSignature() {
		signature = new ElpSignature();
		for(Rule r : rules) {
			List<Literal> literals = new LinkedList<Literal>();
			literals.addAll(r.getBody());
			literals.addAll(r.getHead());
			
			for(Literal l : literals) {
				signature.add(l);
			}
		}
	}
	
	public void saveTo(String filename) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(filename));
			for (Rule r : rules) {
				w.write(r.toString());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toStringFlat() {
		StringBuilder sb = new StringBuilder();
		
		Iterator<Rule> rIter = rules.iterator();
		while (rIter.hasNext()) {
			Rule r = rIter.next();
			if (r.isComment())
				continue;
			sb.append(r.toString()+"\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Creates the defaultifictation p_d of a given program p.
	 * A defaultificated program p' of p adds for every Rule r in p a modified rule r_d 
	 * of the form: H(r) :- B(r), not -H(r). to the program p'. 
	 * @param p	The program which is not defaultificated yet
	 * @return a program p' which is the defaultificated version of p.
	 */
	public static Program defaultification(Program p) {
		Program reval = new Program();
		for(Rule origRule : p.getRules()) {
			Rule defRule = new Rule();
			if(!origRule.isConstraint()) {
				Literal head = origRule.getHead().get(0);
				Neg neg = new Neg(head.getAtom());
				defRule.addBody(origRule.getBody());
				Not defaultificationLit = null;
				if(head instanceof Neg) {
					defRule.addHead(neg);
					defaultificationLit = new Not(head.getAtom());
				} else {
					defRule.addHead(head);
					defaultificationLit = new Not(neg);
				}
				
				if(defaultificationLit != null && !defRule.getBody().contains(defaultificationLit)) {
					defRule.addBody(defaultificationLit);
				}
			} else {
				defRule.addBody(origRule.getBody());
			}
			reval.addRule(defRule);
		}
		return reval;
	}
	
	/**
	 * Adds the given atom as fact to the logical program.
	 * @param fact	atom representing the fact.
	 * @return
	 */
	public boolean add(Atom fact) {
		Rule r = new Rule();
		r.addHead(fact);
		return addRule(r);
	}
	
	@Override
	public Object clone() {
		return new Program(this);
	}
	
	private void updateAtoms(Rule r) {
		for(Literal l : r.getLiterals()) {
			atoms.add(l.getAtom());
		}
	}
	
	private void updateAtoms() {
		atoms.clear();
		for(Rule r : rules) {
			for(Literal l : r.getLiterals()) {
				atoms.add(l.getAtom());
			}
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Program)) return false;
		Program op = (Program)other;
		boolean eq1 =  op.rules.equals(rules); 
		boolean eq2 =  op.atoms.equals(atoms);
		return eq1 && eq2;
	}
	
	@Override
	public int hashCode() {
		return rules.hashCode() + atoms.hashCode();
	}
}
