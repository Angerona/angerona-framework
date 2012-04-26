package net.sf.tweety.logicprogramming.asplibrary.solver;

import java.io.File;
import java.util.Iterator;

/**
 * Base class for solver has generic error handling code.
 * @author Tim Janus
 */
public abstract class SolverBase implements Solver {
	
	protected AspInterface ai = new AspInterface();
	
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
	
	/**
	 * default implementation of error checking (should work for dlv package)
	 * @throws SolverException
	 */
	protected void checkErrors() throws SolverException {
		// early exit
		if (ai.getError().size() > 0) {
			Iterator<String> iter = ai.getError().iterator();
			String msg = "";
			while (iter.hasNext()) {
				msg += iter.next();
			}
			if (msg.endsWith("syntax error.")) {
				throw new SolverException(msg, SolverException.SE_SYNTAX_ERROR);
			} else if (msg.endsWith("open input.")) {
				throw new SolverException(msg, SolverException.SE_CANNOT_OPEN_INPUT);
			} else {
				throw new SolverException(msg, SolverException.SE_ERROR);
			}
			
		}
	}
}
