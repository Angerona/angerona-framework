package net.sf.tweety.math.opt;

import java.util.*;

import net.sf.tweety.math.*;
import net.sf.tweety.math.term.*;


/**
 * Classes which extend this class represent algorithms for
 * finding zero points (roots) of functions. 
 * 
 * @author Matthias Thimm
 */
public abstract class RootFinder {

	/**
	 * The precision for finding roots.
	 */
	public static double PRECISION = 0.00001;
	
	/**
	 * The (multi-dimensional) function of this root finder.
	 */
	private List<Term> functions;

	/**
	 * The starting point of this root finder.
	 */
	private Map<Variable,Term> startingPoint;
	
	/**
	 * Creates a new root finder for the given function.
	 * @param function a term
	 */
	public RootFinder(Term function){
		List<Term> functions = new ArrayList<Term>();
		functions.add(function);
		this.functions = functions;
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given function
	 * @param startingPoint
	 */
	public RootFinder(Term function, Map<Variable,Term> startingPoint){
		this.functions = new LinkedList<Term>();
		this.functions.add(function);
		this.startingPoint = startingPoint;
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given
	 * (multi-dimensional) function
	 * @param startingPoint
	 */
	public RootFinder(List<Term> functions, Map<Variable,Term> startingPoint){
		this.functions = new LinkedList<Term>();
		this.functions.addAll(functions);
		this.startingPoint = startingPoint;
	}
	
	/**
	 * sets the starting point of this root finder.
	 */
	public void setStartingPoint(Map<Variable,Term> startingPoint){
		this.startingPoint = startingPoint;
	}
	
	/**
	 * Returns the starting point of this finder.
	 * @return the starting point of this finder.
	 */
	public Map<Variable,Term> getStartingPoint(){
		return this.startingPoint;
	}
	
	/**
	 * Returns the function of this root finder.
	 * @return the function of this root finder.
	 */
	public List<Term> getFunctions(){
		return this.functions;
	}
	
	/**
	 * Determines the values for the variables appearing in the function such
	 * the function evaluates to zero.
	 * @return a map from variables to terms such that "function" evaluates to zero.
	 * @throws GeneralMathException if something went wrong.
	 */
	public abstract Map<Variable,Term> randomRoot() throws GeneralMathException;	
	
}
