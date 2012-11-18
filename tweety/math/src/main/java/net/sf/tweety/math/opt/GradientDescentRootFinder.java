package net.sf.tweety.math.opt;

import java.util.List;
import java.util.Map;

import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.opt.solver.GradientDescent;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the gradient descent method to find zeros of a (multi-dimensional)
 * function.
 * 
 * @author Matthias Thimm
 *
 */
public class GradientDescentRootFinder extends OptimizationRootFinder {
	
	/**
	 * Logger.
	 */
	static private Logger log = LoggerFactory.getLogger(GradientDescentRootFinder.class);	
	
	/**
	 * The precision of the approximation.
	 * The actual used precision depends on the number of variables. 
	 */
	public double precision = 0.00001;
	
	/**
	 * Creates a new root finder for the given starting point and the given function
	 * @param startingPoint
	 */
	public GradientDescentRootFinder(Term function, Map<Variable,Term> startingPoint){
		super(function,startingPoint);
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given
	 * (multi-dimensional) function
	 * @param startingPoint
	 */
	public GradientDescentRootFinder(List<Term> functions, Map<Variable,Term> startingPoint){
		super(functions,startingPoint);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.RootFinder#randomRoot()
	 */
	@Override
	public Map<Variable, Term> randomRoot() throws GeneralMathException {		
		GradientDescentRootFinder.log.trace("Determining a random root of the function '" + this.getFunctions() + "' using the gradient descent root finder.");
		GradientDescent solver = new GradientDescent(this.buildOptimizationProblem(),this.getStartingPoint());
		solver.precision = this.precision;
		return solver.solve();
	}

}
