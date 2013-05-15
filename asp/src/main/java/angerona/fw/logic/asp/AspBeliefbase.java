package angerona.fw.logic.asp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.Signature;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import angerona.fw.BaseBeliefbase;

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
	
	public void setProgram(Program pr) {
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
	public Object clone() {
		return new AspBeliefbase(this);
	}

	@Override
	protected void parseInt(BufferedReader br) throws ParserException, IOException {
		program = Program.loadFrom(br);
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
	public List<String> getAtoms() {
		List<String> reval = new LinkedList<String>();
		
		for(Rule r : program) {
			reval.add(r.toString());
		}
		return reval;
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
