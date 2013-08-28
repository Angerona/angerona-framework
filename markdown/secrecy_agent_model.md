Secrecy aware BDI Agent Model	{#secrecyawarebdiagentmodel}
=============================

The secrecy aware BDI agent model is an extension of the BDI agent model,
that allows agents to act in an environment and fulfill confidentiality 
constraints.

This how-to explains how the secrecy agent model is implemented in the 
Angerona framework. For a more conceptualized overview on the agent model
take a look on the technical report of the Angerona framework.

This article assumes that you have read and understand @ref conceptoperators article.

THIS IS CURRENTLY WORK-IN-PROGRESS refer to the technical-report to more information
on the secrecy agent model.

Components in the Secrecy Agent Model
-------------------------------------

The secrecy agent model adds a component @ref angerona.fw.am.secrecy.components.SecrecyKnowledge thats
acts as data-storage for secrets. A secret is a tuple as defined in the technical-report and is represented
by the class @ref angerona.fw.logic.Secret.

Belief Update in the Secrecy Agent Model
----------------------------------------


Operators in the Secrecy Agent Model
------------------------------------

Conclusion
----------