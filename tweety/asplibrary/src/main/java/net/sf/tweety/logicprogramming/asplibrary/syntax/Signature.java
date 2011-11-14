package net.sf.tweety.logicprogramming.asplibrary.syntax;

import net.sf.tweety.logicprogramming.asplibrary.factory.TermDescriptor;
import java.util.*;

/**
 * this interface models a factory for common atom and
 * term instantiation, based upon functor (string) pooling
 * and custom class loaders for specialized atoms.
 *
 * in formal languages, a signature is usually used to define
 * all symbols, functions, predicates and variables. in this
 * implementation, a signature is used to track all predicates
 * to allow functor pooling and custom class instantiation.
 * 
 * @author Thomas Vengels
 *
 */
public interface Signature {

	public	boolean	registerAtom(String symbol, String className);
	
	public	boolean	registerTerm(String className, TermDescriptor tl);
	
	public	Literal	instantiate(String symbol, String ... terms);
	
	public	Literal	instantiate(String symbol, Collection<Term> terms);	
}
