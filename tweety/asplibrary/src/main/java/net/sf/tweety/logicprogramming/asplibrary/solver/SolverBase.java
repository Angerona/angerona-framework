package net.sf.tweety.logicprogramming.asplibrary.solver;

import java.io.File;

/**
 * Base class for solver has generic error handling code.
 * @author Tim Janus
 */
public abstract class SolverBase implements Solver {
	
	/**
	 * proofs if the solver on the given  path is an existing file with
	 * execute permission. If this is not the case a SolverException is
	 * thrown.
	 * 
	 * @param path
	 * @throws SolverException
	 */
	protected void checkSolver(String path) throws SolverException {
		File solverBin = new File(path);
		if(!solverBin.isFile()) {
			throw new SolverException("'" + path + "' is no file.", 
					SolverException.SE_CANNOT_FIND_SOLVER);
		} else if(!solverBin.canExecute()) {
			throw new SolverException("No permission to execute: '" + path + "'", 
					SolverException.SE_PERMISSIONS);
		}
	}
}
