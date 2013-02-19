package net.sf.tweety.math.opt.solver.lbfgs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.opt.ConstraintSatisfactionProblem;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.Solver;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.IntegerConstant;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;
import net.sf.tweety.util.VectorTools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class implements a wrapper for L-BFGS.
 * 
 * @author Matthias Thimm
 */
public class LbfgsSolver extends Solver {
	
	/**
	 * Logger.
	 */
	private Logger log = LoggerFactory.getLogger(LbfgsSolver.class);
	
	/**
	 * The starting point for the solver.
	 */
	private Map<Variable,Term> startingPoint;
	
	public LbfgsSolver(ConstraintSatisfactionProblem problem, Map<Variable,Term> startingPoint) {
		super(problem);		
		if(problem.size() > 0)
			throw new IllegalArgumentException("The gradient descent method works only for optimization problems without constraints.");
		this.startingPoint = startingPoint;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.Solver#solve()
	 */
	@Override
	public Map<Variable, Term> solve() throws GeneralMathException {
		this.log.trace("Solving the following optimization problem using L-BFGS:\n===BEGIN===\n" + this.getProblem() + "\n===END===");
		Term func = ((OptimizationProblem)this.getProblem()).getTargetFunction();
		if(((OptimizationProblem)this.getProblem()).getType() == OptimizationProblem.MAXIMIZE)
			func = new IntegerConstant(-1).mult(func);	
		// variables need to be ordered
		List<Variable> variables = new ArrayList<Variable>(func.getVariables());
		List<Term> gradient = new LinkedList<Term>();		
		for(Variable v: variables)
			gradient.add(func.derive(v).simplify());
		Map<Variable,Term> currentGuess = this.startingPoint;
		// set parameters for L-BFGS
		int n = variables.size();
		int m = 1000;		
		double[] x = new double[n];
		for(int i = 0; i < n; i++)
			x[i] = currentGuess.get(variables.get(i)).doubleValue();
		double f = func.replaceAllTerms(currentGuess).doubleValue();
		double[] g = new double[n];
		for(int i = 0; i < n; i++)
			g[i] = gradient.get(i).replaceAllTerms(currentGuess).doubleValue();
		boolean diagco = false;
		double[] diag = new double[n];
		int[] iprint = new int[2];
		iprint[0] = -1;
		iprint[1] = 3;
		double eps = 0.00001;
		double xtol = 10e-16;
		int[] iflag = new int[1];
		iflag[0] = 0;
		this.log.trace("Starting optimization.");
		while(iflag[0] >= 0){
			try{
				Lbfgs.lbfgs(n, m, x, f, g, diagco, diag, iprint, eps, xtol, iflag);
				this.log.trace("Current manhattan distance of gradient to zero: " + VectorTools.manhattanDistanceToZero(g));
			}catch(Exception e){
				throw new GeneralMathException("Call to L-BFGS failed.");
			}
			if(iflag[0] == 0){
				break;
			}else if(iflag[0] == 1){				
				int i = 0;
				for(Variable v: variables){
					/*
					// if the variable should be positive, make some corrections
					// NOTE: this is a workaround.
					if(v.isPositive() && x[i]<0){
						currentGuess.put(v, new FloatConstant(-x[i]/2));
						x[i] = -x[i]/2;
						// restart optimization
						iflag[0] = 0;						
					}else*/ 
					currentGuess.put(v, new FloatConstant(x[i]));
					i++;
				}
				f = func.replaceAllTerms(currentGuess).doubleValue();
				for(i = 0; i < n; i++)
					g[i] = gradient.get(i).replaceAllTerms(currentGuess).doubleValue();					
			}
		}
		this.log.trace("Optimum found: " + currentGuess);
		return currentGuess;
	}	
}
