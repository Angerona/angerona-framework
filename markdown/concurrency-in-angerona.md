Concurrency in Angerona	{#concurrencyinangerona}
=======================

This article describes how concurrency is implemented in Angerona.

Current Points of Concurrency
-----------------------------

At the moment Angerona implements concurrency between the GUI and
its internal processing, that means the multi agent simulation. We refer to
the thread that executes the SWING events as the *event dispatch thread* and
to the thread that simulate the multi agent system as the *processing thread*.
Because the GUI is implemented in SWING, the concepts described in 
[SWING's concurrency tutorial][1] are used to support concurrency. 

The synchronization point is given in the class 
@ref com.github.angerona.fw.gui.simctrl.SimulationControlModelAdapter. Internally
for every change on the environment that represents the multi agent simulation a 
*processing thread* is generated that processes the initialization or the next
step of the multi agent simulation. Currently a custom thread pool is used for the
*processing thread* but the use of SWING worker threads is part of future
works.

The interactive agent extension is a special case. When an interactive dialog 
is opened it pauses the *processing thread* until the user-input is given.

Future Concurrency
------------------

Angerona is developed with concurrency in mind. The Agent functional component
which contains several @ref com.github.angerona.fw.operators.BaseOperator implementations
strictly divides its functionality from its data. The implementing operator classes are stateless
that means they have no attributes only methods and those methods get all their data by their
parameters. This kind of implementation allows the methods of the operator
implementations to be called by multiple threads without thinking about synchronization because
all the data is given by the caller. In fact to spare memory Angerona only stores one instance
for every implementation of BaseOperator that is used by every agent that needs this implementation. 

In a future version of Angerona the multi agent tick can simulate the agent cylce of every agent in 
parallel because the data is defined by the agent instance and the operators are thread safe as long 
as they fulfill the assumption that they are stateless.



[1]: http://docs.oracle.com/javase/tutorial/uiswing/concurrency "SWING Concurrency Tutorial by Oracle"