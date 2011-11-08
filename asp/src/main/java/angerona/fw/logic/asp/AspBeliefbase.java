package angerona.fw.logic.asp;

import java.io.BufferedReader;
import java.io.IOException;

import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.Signature;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.base.BaseBeliefbase;

/**
 * A beliefbase implementation containing a logical program. 
 * @author Tim Janus
 */
public class AspBeliefbase extends BaseBeliefbase {

	/** reference to the logical program */
	private Program program;
	
	/** a fol signature describing which predicates are used in the logical program */
	private Signature signature;
	
	/** @return the program used as basic for the beliefbase */
	public Program getProgram() {
		return program;
	}
	
	@Override
	public String getFileEnding() {
		return "asp";
	}

	@Override
	public Object clone() {
		throw new NotImplementedException();
	}

	@Override
	protected void parseInt(BufferedReader br) throws ParserException, IOException {
		program = Program.loadFrom(br);
		
		/*
		signature = new FolSignature();
		for(Rule r : program) {
			List<Literal> literals = new LinkedList<Literal>();
			literals.addAll(r.getBody());
			literals.addAll(r.getHead());
			
			for(Literal l : literals) {
				Predicate p = new Predicate(l.getAtom().getSymbol(), l.getAtom().getArity());
				signature.add(p);
			}
		}
		*/
		signature = program.getSignature();
	}

	@Override
	public Signature getSignature() {
		return signature;
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

}
