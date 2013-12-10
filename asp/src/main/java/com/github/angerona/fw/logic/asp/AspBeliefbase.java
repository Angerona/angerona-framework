package com.github.angerona.fw.logic.asp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.lp.asp.parser.ASPParser;
import net.sf.tweety.lp.asp.parser.InstantiateVisitor;
import net.sf.tweety.lp.asp.parser.ParseException;
import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.syntax.Rule;

import com.github.angerona.fw.BaseBeliefbase;

/**
 * A beliefbase implementation containing a logical program. 
 * @author Tim Janus
 */
public class AspBeliefbase extends BaseBeliefbase {

	/** reference to the logical program */
	private Program program = new Program();
	
	/** @return the program used as basic for the beliefbase */
	public Program getProgram() {
		return program;
	}
	
	/**
	 * Changes the ASP program hold by the belief base
	 * @param pr	Reference to the new program, this MUST not be null.
	 */
	public void setProgram(Program pr) {
		if(pr == null)
			throw new IllegalArgumentException();
		program = pr;
	}
	
	@Override
	public String getFileEnding() {
		return "asp";
	}
	
	public AspBeliefbase() {
		super();
	}
	
	public AspBeliefbase(AspBeliefbase other) {
		super(other);
		
		this.program = (Program) other.program.clone();
	}

	@Override
	public AspBeliefbase clone() {
		return new AspBeliefbase(this);
	}

	@Override
	protected void parseImpl(BufferedReader br) throws ParserException, IOException {
		ASPParser parser = new ASPParser(br);
		InstantiateVisitor visitor = new InstantiateVisitor();
		try {
			this.program = visitor.visit(parser.Program(), null);
		} catch (ParseException e) {
			throw new ParserException(e);
		}
	}

	@Override
	public Signature getSignature() {
		return program.getSignature();
	}

	@Override
	public String toString() {
		return program != null ? program.toString() : "empty asp BB";
	}
	
	@Override
	public boolean isFormulaValid(Formula query) {
		if(!super.isFormulaValid(query))
			return false;
		
		String atoms_and_negations = "ASP only supports atoms and negations of atoms yet.";
		
		if(!(query instanceof FOLAtom) && !(query instanceof Negation)) {
			this.reason = atoms_and_negations;
			return false;
		}
		
		if(query instanceof Negation) {
			Negation n = (Negation)query;
			if(!(n.getFormula() instanceof FOLAtom)) {
				this.reason = atoms_and_negations;
				return false;
			}
		}
		
		return true;
	}

	@Override
	public List<String> getAtomsAsStringList() {
		List<String> facts = new LinkedList<String>();
		List<String> rules = new LinkedList<String>();
		
		for(Rule r : program) {
			if(r.isFact())
				facts.add(r.toString());
			else
				rules.add(r.toString());
		}
		
		Collections.sort(facts);
		Collections.sort(rules);
		facts.addAll(rules);
		return facts;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof AspBeliefbase))	return false;
		AspBeliefbase co = (AspBeliefbase)other;
		return co.program.equals(program);
	}

	@Override
	public int hashCode() {
		return program.hashCode();
	}

}
