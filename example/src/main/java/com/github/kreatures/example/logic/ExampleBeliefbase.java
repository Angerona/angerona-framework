package com.github.kreatures.example.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.Formula;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.parser.FolParserB;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.parser.ParseException;

/**
 * Just a dummy belief base for testing purposes.
 * It implements a set of literals using tweety-fol.
 *
 * @author Tim Janus
 */
public class ExampleBeliefbase extends BaseBeliefbase {

	/** the beliefset of the beliefbase */
	FolBeliefSet fbs = new FolBeliefSet();
	
	/** Default Ctor: Needed for dynamic instantiation */
	public ExampleBeliefbase() {	
	}
	
	/** Copy-Ctor: Copies a Dummy-Beliefbase, the copied object has the
	 * 	same object id like the given parameter.
	 * 	@param other
	 */
	public ExampleBeliefbase(ExampleBeliefbase other) {
		super(other);
		fbs.addAll(other.fbs);
	}
	
	@Override
	public Signature getSignature() {
		return fbs.getSignature();
	}
	
	public FolBeliefSet getBeliefSet() {
		return fbs;
	}

	@Override
	public String toString() {
		String reval = "";
		
		for(Formula f : fbs)
			reval += f.toString()+"\n";
		
		return reval;
	}

	@Override
	public ExampleBeliefbase clone() {
		return new ExampleBeliefbase(this);
	}

	// Belief base is a simple FOL Beliefbase.
	@Override
	protected void parseImpl(BufferedReader br) throws ParseException, IOException {
		FolParserB parser = new FolParserB(br);
		try {
			fbs = parser.KB();
		} catch (net.sf.tweety.logics.fol.parser.ParseException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage());
		}
	}

	@Override
	public String getFileEnding() {
		return "dum";
	}

	@Override
	public List<String> getAtomsAsStringList() {
		List<String> reval = new LinkedList<String>();
		for(FolFormula ff : fbs)
			reval.add(ff.toString());
		return reval;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof ExampleBeliefbase)) return false;
		ExampleBeliefbase co = (ExampleBeliefbase)other;
		
		return fbs.equals(co.fbs);
	}

	@Override
	public int hashCode() {
		return fbs.hashCode();
	}
}
