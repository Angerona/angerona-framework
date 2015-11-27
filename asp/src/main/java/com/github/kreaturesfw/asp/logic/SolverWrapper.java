package com.github.kreaturesfw.asp.logic;

import java.io.File;

import com.github.kreaturesfw.core.KReatures;
import com.github.kreaturesfw.core.serialize.GlobalConfiguration;

import net.sf.tweety.lp.asp.solver.Clingo;
import net.sf.tweety.lp.asp.solver.DLV;
import net.sf.tweety.lp.asp.solver.DLVComplex;
import net.sf.tweety.lp.asp.solver.Solver;

/**
 * Wraps the solver type into an enum. It uses the Angerona configuration
 * facility to determine the solvers exectuable name and can also return an
 * instance of the Solver which is capable of processing answer-sets.
 * @author Tim Janus
 */
public enum SolverWrapper implements ISolverWrapper {
	CLINGO,
	DLV,
	DLV_COMPLEX;
	
	/** the string representing the path to the solver */
	private String path = "";

	private String paramName = null;
	
	/** Default-Ctor: Calculates the solver-path depending on the enum-value
	 * @throws InstantiationException */
	private SolverWrapper() {
		GlobalConfiguration config = KReatures.getInstance().getConfig();

		if(ordinal() == 0) { // if Clingo:
			paramName = ("path-clingo");
		} else if(ordinal() == 1) { // if dlv:
			paramName = ("path-dlv");
		} else if(ordinal() == 2) { // if dlv-complex:
			paramName = ("path-dlv-complex");
		} else {
			throw new UnsupportedOperationException("SolverWrapper has no implementation for ordinal: " + ordinal());
		}
		
		if(config != null) {
			// let the angerona configuration facility handle the postfixs for executables.
			path = config.getAsExecutable(paramName);
		}
	}
	
	/** @return a string representing the path to solver */
	public String getSolverPath() {
		return path;
	}
	
	public InstantiationException getError() {
		if(path == null) {
			GlobalConfiguration config = KReatures.getInstance().getConfig();
			
			return new InstantiationException("The path '" + config.getParameters().get(paramName) + 
					"' is not the path to the '" + this.toString() + "' ASP solver.");
		}
		
		File testFile = new File(path);
		if(!testFile.canExecute()) {
			return new InstantiationException("The Solver on path '" + path + "' has no execute permission.");
		}
		
		return null;
	}
	
	/** @return an object which can invoke the solver 
	 * @throws InstantiationException */
	public Solver getSolver() {
		Solver solver = null;
		if(this == SolverWrapper.CLINGO)
			solver = new Clingo(path);
		else if(this == SolverWrapper.DLV)
			solver = new DLV(path);
		else if(this == SolverWrapper.DLV_COMPLEX)
			solver = new DLVComplex(path);
		else
			throw new UnsupportedOperationException("Solver of this type not supported yet.");
		return solver;
	}
}
