package net.sf.tweety.logicprogramming.asplibrary.revision;

import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;

public interface RevisionApproach {
	Program revision(Program p1, Program p2, Solver solver) throws SolverException;
}
