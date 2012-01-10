package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
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
 * @author Thomas Vengels, Tim Janus
 *
 */
public class Program extends ArrayList<Rule> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ElpSignature signature;
	
	private Set<Atom> atoms = new HashSet<Atom>();
	
	public Preamble	preamble = new Preamble();
	
	public Set<Atom> getAtoms() {
		return atoms;
	}
	
	
	
	public void add(Program other) {
		this.addAll( other );
		this.preamble.join( other.preamble );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.size() > 0) {
			sb.append( this.get(0));
		}
		for (int i = 1; i < this.size(); i++) {
			sb.append("\n");
			sb.append(this.get(i));
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
			ret.addAll(lr);
			ret.calcSignature();
		} catch (Exception e) {
			System.err.println("Error while loading program ");
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
		for(Rule r : this) {
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
			for (Rule r : this) {
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
		
		Iterator<Rule> rIter = this.iterator();
		while (rIter.hasNext()) {
			Rule r = rIter.next();
			if (r.isComment())
				continue;
			sb.append(r.toString());
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
		for(Rule origRule : p) {
			Rule defRule = new Rule();
			if(!origRule.isConstraint()) {
				Literal head = origRule.getHead().get(0);
				Neg neg = new Neg(head.getAtom());
				defRule.addBody(origRule.getBody());
				Not defaultificationLit = null;
				if(head.isTrueNegated()) {
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
			reval.add(defRule);
		}
		return reval;
	}
	
	// overload array list methods to keep track of the atoms
	@Override
	public boolean add(Rule r) {
		for(Literal l : r.getLiterals()) {
			atoms.add(l.getAtom()); 
		}
		return super.add(r);
	}
	
	@Override
	public void add(int index, Rule r) {
		for(Literal l : r.getLiterals()) {
			atoms.add(l.getAtom());
		}
		super.add(index, r);
	}
	
	@Override
	public boolean addAll(Collection<? extends Rule> c) {
		for(Rule r : c) {
			for(Literal l : r.getLiterals()) {
				atoms.add(l.getAtom());
			}
		}
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Rule> c) {
		for(Rule r : c) {
			for(Literal l : r.getLiterals()) {
				atoms.add(l.getAtom());
			}
		}
		return super.addAll(index, c);
	}
	
	@Override
	public void clear() {
		atoms.clear();
		super.clear();
	}
	
	@Override
	public Rule remove(int index){		
		Rule reval =  super.remove(index);
		if(reval != null) {
			updateAtoms();
		}
		return reval;
	}
	
	@Override
	public boolean remove(Object o) {
		Rule r = (Rule)o;
		boolean reval = super.remove(r);
		if(reval) {
			updateAtoms();
		}
		return reval;
	}
	
	private void updateAtoms() {
		atoms.clear();
		for(Rule r : this) {
			for(Literal l : r.getLiterals()) {
				atoms.add(l.getAtom());
			}
		}
	}
}
