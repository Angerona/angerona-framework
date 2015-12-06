package com.github.kreaturesfw.asp.logic;

import net.sf.tweety.lp.asp.solver.Solver;

/**
 * A solver wrapper encapsulates a solver like DLV
 * CLASP or Clingo. Every class implementing the
 * interface has to return a Solver instance.
 * 
 * @author Tim Janus
 *
 */
public interface ISolverWrapper {
	Solver getSolver();
	
	InstantiationException getError();
}
