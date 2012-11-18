package net.sf.tweety.math.opt.solver;

import java.io.*;
import java.util.*;

import net.sf.tweety.math.*;
import net.sf.tweety.math.equation.*;
import net.sf.tweety.math.opt.*;
import net.sf.tweety.math.term.*;

import org.apache.commons.logging.*;


/**
 * This class implements a wrapper for the OpenOpt optimization library.
 * @author Matthias Thimm
 *
 */
public class OpenOptSolver extends Solver {
	
	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(OpenOptSolver.class);
	
	// TODO make the following private and add getter/setter
	public double contol = 1e-8;
	public double ftol = 1e-8;
	public double gtol = 1e-8;
	public double xtol = 1e-8;
	public double maxIter = 1e16;
	public double maxFunEvals = 1e16;
	public String solver = "lincher";
	public boolean ignoreNotFeasibleError = false;
	
	/**
	 * A starting point for the optimization.
	 */
	private Map<Variable,Term> startingPoint;
	
	/**
	 * A map mapping old variables to new variables.
	 */
	private Map<Variable,Variable> oldVars2newVars = new HashMap<Variable,Variable>();
	
	/**
	 * A map mapping old variables to new variables.
	 */
	private Map<Variable,Variable> newVars2oldVars = new HashMap<Variable,Variable>();
	
	/**
	 * A map mapping indices to new variables.
	 */
	private Map<Integer,Variable> idx2newVars = new HashMap<Integer,Variable>();
	
	/**
	 * Creates a new solver for the given problem.
	 * @param problem a csp.
	 */
	public OpenOptSolver(OptimizationProblem problem) {
		this(problem,null);
	}
	
	/**
	 * Creates a new solver for the given problem.
	 * @param problem a csp.
	 * @param a starting point.
	 */
	public OpenOptSolver(OptimizationProblem problem, Map<Variable,Term> startingPoint) {
		super(problem.clone());
		this.startingPoint = startingPoint;
		// do a renaming of variables		
		int idx = 0;
		for(Variable v: this.getProblem().getVariables()){
			Variable newV = new FloatVariable("x[" + idx + "]");
			this.oldVars2newVars.put(v, newV);
			this.newVars2oldVars.put(newV, v);
			this.idx2newVars.put(idx,newV);
			idx++;
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.Solver#solve()
	 */
	@Override
	public Map<Variable, Term> solve() throws GeneralMathException {		
		String output = "";
		String error = "";
		InputStream in = null;
		Process child = null;
		try{
			File ooFile = File.createTempFile("ootmp", null);
			// Delete temp file when program exits.
			ooFile.deleteOnExit();    
			// Write to temp file
			BufferedWriter out = new BufferedWriter(new FileWriter(ooFile));
			this.log.info("Building Python code for OpenOpt.");
			out.write(this.getOpenOptCode());			
			out.close();
			//execute openopt on problem and retrieve console output
			this.log.info("Calling OpenOpt optimization library.");
			child = Runtime.getRuntime().exec("python " + ooFile.getAbsolutePath());
			int c;		
			in = child.getInputStream();
	        while ((c = in.read()) != -1){
	            output += ((char)c);
	        }
			in.close();		        		        
	        in = child.getErrorStream();
	        while ((c = in.read()) != -1)
	            error += (char)c;	        	        
		}catch(IOException e){
			log.error(e.getMessage());
			return null;
		}finally{
			try {
				if(in != null) in.close();
			} catch (IOException e) {
				// ignore
			}
			if(child != null) child.destroy();
		}
		// TODO check error appropriately
		if(output.contains("NO FEASIBLE SOLUTION") && !this.ignoreNotFeasibleError){
			this.log.info("The optimization problem seems to be unfeasible.");
			throw new GeneralMathException("The optimization problem seems to be unfeasible.");
		}
		// parser output
		this.log.info("Parsing solution from OpenOpt.");
		try{
			double[] values = this.parseOutput(output, this.idx2newVars.keySet().size());
			Map<Variable,Term> result = new HashMap<Variable,Term>();
			for(Integer i: this.idx2newVars.keySet())
				result.put(this.newVars2oldVars.get(this.idx2newVars.get(i)), new FloatConstant(values[i]));
			return result;
		}catch(Exception e){
			this.log.error(e.getMessage());
			throw new GeneralMathException(e.getMessage());
		}
	}

	/**
	 * Builds the OpenOpt code for the given problem which can be interpreted
	 * by a python.
	 * @return the python code for the given problem
	 */
	public String getOpenOptCode(){
		OptimizationProblem problem = (OptimizationProblem)this.getProblem();
		// replace vars
		problem.setTargetFunction(problem.getTargetFunction().replaceAllTerms(this.oldVars2newVars));
		// we have to minimize
		if(problem.getType() == OptimizationProblem.MAXIMIZE){
			problem.setTargetFunction(problem.getTargetFunction().mult(new IntegerConstant(-1)));
			problem.setType(OptimizationProblem.MINIMIZE);
		}			
		Set<Statement> constraints = new HashSet<Statement>(problem);
		problem.clear();
		for(Statement s: constraints)
			problem.add(s.replaceAllTerms(this.oldVars2newVars));		
		String code = new String();
		
		//write header
		code += "from FuncDesigner import *\n";
		code += "from openopt import NLP\n";
		code += "from numpy import *\n";
		code += "\n";	
		
		// add some auxiliary functions
		code+= "def log_mod(x):\n";
		code+= "	if x > 0.0:\n";
		code+= "		return log(x)\n";
		code+= "	else:\n";
		code+= "		return 0.0\n\n";
		
		//write objective (replace auxiliary functions)
		code += "objective = lambda x: ";
		
		code += problem.getTargetFunction().toString().replace("log", "log_mod") + "\n\n";
		// write startingpoint
		boolean first = true;
		if(this.startingPoint != null){
			code += "startingpoint = [";			
			for(int i = 0; i < this.idx2newVars.keySet().size(); i++)
				if(first){
					first = false;
					code += startingPoint.get(this.newVars2oldVars.get(this.idx2newVars.get(i))).doubleValue();
				}else code += "," + startingPoint.get(this.newVars2oldVars.get(this.idx2newVars.get(i))).doubleValue();
			code += "]\n\n";
		}
		// add box constraints
		code += "lb = [";
		first = true;
		for(int i = 0; i < this.idx2newVars.keySet().size(); i++)
			if(first){
				first = false;
				code += this.newVars2oldVars.get((this.idx2newVars.get(i))).getLowerBound();
			}else code += "," + this.newVars2oldVars.get((this.idx2newVars.get(i))).getLowerBound();
		code += "]\n";
		code += "ub = [";
		first = true;
		for(int i = 0; i < this.idx2newVars.keySet().size(); i++)
			if(first){
				first = false;
				code += this.newVars2oldVars.get((this.idx2newVars.get(i))).getUpperBound();
			}else code += "," + this.newVars2oldVars.get((this.idx2newVars.get(i))).getUpperBound();
		code += "]\n";
		// specify problem
		if(this.startingPoint != null)
			code += "p = NLP(objective,startingpoint,lb=lb,ub=ub)\n\n";
		else code += "p = NLP(objective,lb=lb,ub=ub)\n\n";
		// add constraints		
		int idx = 0;
		List<String> equalities = new ArrayList<String>();
		List<String> inequalities = new ArrayList<String>();
		for(Statement s: problem){
			if(s instanceof Equation){
				// add equality constraints
				Equation eq = (Equation)s.toNormalizedForm();
				equalities.add("c" + idx);
				code += "c" + idx + " = lambda x: " + eq.getLeftTerm().toString().replace("log", "log_mod") + "\n";				
			}else{
				Inequation ineq = (Inequation) s.toNormalizedForm();
				inequalities.add("i" + idx);
				code += "i" + idx + " = lambda x: " + new FloatConstant(-1).mult(ineq.getLeftTerm()).toString().replace("log", "log_mod") + "\n";
			}
			idx++;
		}
		if(!equalities.isEmpty())
			code += "\np.h = " + equalities + "\n\n";
		if(!inequalities.isEmpty())
			code += "\np.c = " + inequalities + "\n\n";
		// write commands			
		code += "p.contol = " + this.contol + "\n";
		code += "p.ftol = " + this.ftol + "\n";
		code += "p.gtol = " + this.gtol + "\n";
		code += "p.xtol = " + this.xtol + "\n";
		code += "p.maxIter = " + this.maxIter + "\n";
		code += "p.maxFunEvals = " + this.maxFunEvals + "\n";
		code += "r = p.solve('" + this.solver + "')\n";
		code += "print r.xf";
		this.log.trace("Generated the OpenOpt code:\n===BEGIN===\n" + code + "\n===END===");
		return code;
	}
	
	/**
	 * This method parses the output data of an OpenOpt run
	 * @param output a string.
	 * @params length the length of the array to be parsed.
	 * @return an array of double
	 */
	private double[] parseOutput(String output, int length){
		try{
			int valuesBegin = output.lastIndexOf("[");
			int valuesEnd = output.lastIndexOf("]");
			String values = output.substring(valuesBegin+1, valuesEnd);
			String[] tokens = values.split(" ");
			double[] result = new double[length];
			int i = 0;
			for(String token : tokens){
				if(token.trim().equals(""))
					continue;
				result[i] = new Double(token.trim());
				i++;
				if(i==length) break;
			}
			return result;
		}catch(Exception e){
			this.log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
