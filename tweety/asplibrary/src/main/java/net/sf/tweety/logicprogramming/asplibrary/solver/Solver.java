package net.sf.tweety.logicprogramming.asplibrary.solver;

import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.util.*;
import net.sf.tweety.logicprogramming.asplibrary.syntax.*;

/**
 * this interface models common calls to a solver.
 * 
 * @author Thomas Vengels
 *
 */
public interface Solver {

	/**
	 * this method computes at most maxModels answer sets for a given program.
	 * 
	 * @param p
	 * @param maxModels
	 * @return
	 * @throws SolverException
	 */
	public AnswerSetList	computeModels(Program p, int maxModels) throws SolverException;
	
	/**
	 * this method computes at most maxModels answer sets for a given program as a flat string.
	 * @param s
	 * @param maxModels
	 * @return
	 * @throws SolverException
	 */
	public AnswerSetList	computeModels(String s, int maxModels ) throws SolverException;

	/**
	 * this method computes at most maxModels for a given program, a collection of facts,
	 * and an arbitrary number of additional programs as a file resource.
	 * 
	 * @param p
	 * @param facts
	 * @param files
	 * @param maxModels
	 * @return
	 * @throws SolverException
	 */
	public AnswerSetList	computeModels(List<String> files, int maxModels) throws SolverException;
	
}
