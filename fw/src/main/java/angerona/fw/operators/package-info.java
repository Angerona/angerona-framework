/**
 * @page conceptoperators The Operator Concept
 * 
 * This documentation page explains the concept of an operator in Angerona and
 * how it is technically implemented.
 * Angerona uses the term operator to define a piece of functionality. An
 * operator has a operation type, an input parameter type and a result or output
 * type. Given this information the following conceputal hierarchy can be defined:
 * 
 * - Abstract base class for all operators
 * - Abstract base class for an specific operation type.
 * - classes implementing an operation type.
 * 
 * The abstract base class 
 * @link angerona.fw.operators.Operator<TCaller extends OperatorCaller, IN extends OperatorParameter, OUT extends Object> Operator @endlink
 * defines the top of the conceptual Operator hierarchy. The next conceptual level of the hierarchy 
 * is filled with abstract base classes which already define the type templates and the operation 
 * type of the operator but give no implementation of the operation yet. An example is the 
 * @link angerona.fw.operators.BaseUpdateBeliefsOperator BaseUpdateBeliefsOperator @endlink. The last conceptual level are implementing classes like 
 * UpdateBeliefsOperator.
 */

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


