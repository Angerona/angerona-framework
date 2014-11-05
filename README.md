Angerona-Framework
==================

The Angerona-Framework is a plug-in driven 
multi-agent simulation framework. It supports
different knowledge representation mechanisms and
also different agent models.


Features of the current Version
-------------------------------

Angerona's core provides several interfaces that can be 
implemented by a plug-in, such that it is flexible and
extensible. On the conceptual level we differentiate between
plug-ins introducing new knowlege representation mechanisms
and plug-ins that adapt the agent behavior, so that the latter
can be used to adapt or even to instantiate a new agent model.

The current version introduces two knowledge representation
plug-ins. An ASP plug-in that supports the representation of
the agent's beliefs in disjunctive logic programs under the
answer set semantics and an OCF plug-in that uses a set of
conditionals and the c-representation to represent the agent's 
beliefs.

At the moment an extension of the well known BDI agent model, 
called secrecy agent model is implemented as the default agent 
model in the Angerona framework.


Further Documentation
---------------------

This document only gives a short introduction to the Angerona 
framework. For this this section references several readings
with further information about the Angerona framework.

- [Doxygen Code-Documentation](https://marathon.cs.tu-dortmund.de/docs/) - Update every night using current master branch

More external documentation will follow soon.


Contributors
------------

Angerona is collaboratively developed by several contributors. 
Thanks go to Ella Albrecht, Daniel Dilger, Sebastian Homann, 
Tim Janus, Patrick Krümpelmann, Cornelia Tadros and Pia Wierzoch.
