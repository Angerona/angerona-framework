/**
 * 	This package contains important classes to handle a general term
 * 	of operators. An interface BaseOperator defines which methods an
 * 	Operator has to support. The class Operator is an abstract generic 
 * 	implementation of parts of the interface BaseOperator. Operator has
 * 	three type parameters: TCaller defining the type of the caller of the 
 * 	Operator, IN defines the type of the InputParameter which is a class
 * 	implementing OperatorParameter and finally the type OUT defining the
 * 	type of the return value of the Operator.
 * 
 * 	@see angerona.fw.operators.Operator< TCaller extends OperatorCaller, IN extends OperatorParameter, OUT extends Object >
 * 	@author Tim Janus
 */
package angerona.fw.operators;


