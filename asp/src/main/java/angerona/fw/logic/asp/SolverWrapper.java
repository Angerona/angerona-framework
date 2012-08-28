package angerona.fw.logic.asp;

import java.io.FileNotFoundException;

import net.sf.tweety.logicprogramming.asplibrary.solver.Clingo;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLV;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLVComplex;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;

import angerona.fw.Angerona;
import angerona.fw.error.NotImplementedException;
import angerona.fw.serialize.GlobalConfiguration;

/**
 * Wraps the solver type into an enum. It uses the Angerona configuration
 * facility to determine the solvers exectuable name and can also return an
 * instance of the Solver which is capable of processing answer-sets.
 * @author Tim Janus
 */
public enum SolverWrapper {
	CLINGO,
	DLV,
	DLV_COMPLEX;
	
	/** the string representing the path to the solver */
	private String path;
	
	/** Default-Ctor: Calculates the solver-path depending on the enum-value*/
	private SolverWrapper() {
		GlobalConfiguration config = Angerona.getInstance().getConfig();
		String paramName = null;
		
		if(ordinal() == 0) { // if Clingo:
			paramName = ("path-clingo");
		} else if(ordinal() == 1) { // if dlv:
			paramName = ("path-dlv");
		} else if(ordinal() == 2) { // if dlv-complex:
			paramName = ("path-dlv-complex");
		} else {
			throw new NotImplementedException("SolverWrapper has no implementation for ordinal: " + ordinal());
		}
		
		// let the angerona configuration facility handle the postfixs for executables.
		path = config.getAsExecutable(paramName);
	}
	
	/** @return a string representing the path to solver */
	public String getSolverPath() {
		return path;
	}
	
	/** @return an object which can invoke the solver */
	public Solver getSolver() throws FileNotFoundException {
		Solver solver = null;
		if(this == SolverWrapper.CLINGO)
			solver = new Clingo(path);
		else if(this == SolverWrapper.DLV)
			solver = new DLV(path);
		else if(this == SolverWrapper.DLV_COMPLEX)
			solver = new DLVComplex(path);
		else
			throw new NotImplementedException("Solver of this type not supported yet.");
		return solver;
	}
}
