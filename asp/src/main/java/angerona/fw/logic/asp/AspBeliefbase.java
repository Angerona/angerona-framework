package angerona.fw.logic.asp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.Signature;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
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
		
		if(!(query instanceof Atom) && !(query instanceof Negation)) {
			this.reason = atoms_and_negations;
			return false;
		}
		
		if(query instanceof Negation) {
			Negation n = (Negation)query;
			if(!(n.getFormula() instanceof Atom)) {
				this.reason = atoms_and_negations;
				return false;
			}
		}
		
		return true;
	}

	@Override
	public List<String> getAtoms() {
		boolean usedReasoner = false;
		List<String> reval = new LinkedList<String>();
		
		// TODO: Think about caching here...
		if(getReasoningOperator() != null) {
			Set<FolFormula> atoms = getReasoningOperator().infer(this);
			for(FolFormula atom : atoms) {
				reval.add(atom.toString() + ".");
			}
			usedReasoner = true;
		}
		
		for(Rule r : program.getRules()) {
			if(!usedReasoner || r.getBody().size() != 0)
				reval.add(r.toString());
		}
		return reval;
	}

}
