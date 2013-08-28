The Concept of Operators	{#conceptoperators}
========================

This documentation page explains the concept of an operator in Angerona and how it is technically implemented.
Angerona uses the term operator to define a piece of functionality. An
operator has an *operation type* which consist of an *input parameter type* and a 
result or *output type* and is identified by a unique name like UpdateBeliefs. 
Given this information the following conceptual hierarchy can be defined:
 
- Abstract base-class for all operators - *parent layer*
- Abstract base-class for an specific operation type *operation declaration layer*
- classes implementing an operation type *implementation layer*
 
The abstract Base-Class
-----------------------

@ref angerona.fw.operators.Operator<TCaller extends OperatorCaller, IN extends OperatorParameter, OUT extends Object> 
defines the *parent layer* of the conceptual 
Operator hierarchy. This class provides the following type parameters:

- *TCaller*, the type of the caller, that has to implement the angerona.fw.OperatorCaller.
  the caller is used to provide state relevant data, that are not in the input parameters
  like a reference to a @ref angerona.fw.operators.OperatorStack
- *IN*, the type of the input parameter for the operator that must implement @ref angerona.fw.operators.parameter.OperatorParameter
- *OUT*, the result type of the operator

The most important implemented method is process(). There are two overloads:
1. Using *IN* as argument type, such that this overload is called from code
2. Using @ref angerona.fw.operators.parameter.GenericOperatorParameter as argument type to call the overload from the @ref ASML scope. 
   Every operator invocation in @ref ASML generates a @ref angerona.fw.operators.parameter.GenericOperatorParameter instance
   that has to be transformed into the type given by *IN*. Therefore the interface @ref angerona.fw.operators.parameter.OperatorParameter,
   that is defined implemented by *IN* defines a method @ref angerona.fw.operators.parameter.OperatorParameter.fromGenericParameter().
process() is defined in the abstract base-class and calls processInternal() that shall be overridden
by sub-classes in the third, the implementation, layer.

On the second, the operation declaration, layer the method getEmptyParameter() and defaultReturn() have
to be implemented. The former generates a parameter data-structure of type IN dynamically, such that it
can be used by the @ref ASML version of the process() method. The latter is used to generate a default
return value that might be used to continue the processing if an error occurs.

When this base-class is inherited, the type parameter are defined and the getEmptyParameter() and 
defaultReturnValue() methods are defined by a plug-in developer then the next layer is reached and a 
base-class for a specific operation type is defined.

A specific Operation-Type: angerona.fw.operators.BaseUpdateBeliefsOperator 
--------------------------------------------------------------------------

The next conceptual level of the hierarchy defines an operation-type, therefore the abstract
base-class is extended and the type parameters are defined. In this how-to we will use the
angerona.fw.operators.BaseUpdateBeliefsOperator as running example.

The update beliefs operator is responsible to translate speech-acts, which are pieces of communication
with a specified semantic, into the correct update-requests on the agents data-components (belief bases). 
Its input parameter is the data-structure @angerona.fw.operators.parameter.EvaluateParameter, that contains
a piece of information like a perception or a speech-act and the agent beliefs. It also contains a reference to the agent who
is the @ref angerona.fw.operators.OperatorCaller for the operator. The output-parameter is a data-structure called 
@ref angerona.fw.logic.Beliefs that contains all the agent's data-components.

The BaseUpdateBeliefsOperator.getEmptyParameter() and the BaseUpdateBeliefsOperator.defaultReturnValue() methods
are implemented, such that the operation-type knows its standard return value and can generate an empty input parameter
data-structure. The latter is used by @ref ASML for dynamic instantiation of parameters.

The Implementation Layer
------------------------


To define a concrete update beliefs operation the BaseUpdateBeliefsOperator class must be inherited. If this
inherited class is not abstract it is on the implementation layer. The @ref angerona.fw.example.operators.UpdateBeliefsOperator 
in the *example project* shows a nice example implementation of an update beliefs operator.


Conclusion
----------

The three abstraction layers of the operators in the Angerona framework provide a flexible approach
to extend the Angerona framework. The first layer defines an abstract base class of an operator that
is used in the second layer to define operation-types, such that the implementation of the operator is
not given in the second layer but its signature. The third layer, referred as implementation layer is
used to provide different implementation of an operation type defined in the second layer.

That means the second layer can be used to generate entirely new agent models by providing new operation-
types and the third layer can be used to adapt an existing agent model.