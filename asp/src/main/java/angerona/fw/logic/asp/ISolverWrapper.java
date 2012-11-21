package angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;

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
}
