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

- [Technical Report](http://sfb876.tu-dortmund.de/PublicPublicationFiles/kruempelmann_janus_2014a.pdf): The report is divided into two parts, the first part introduces the concept of the secrecy agent model that is implemented in the Angerona framework and the second part explains how it has been implemented.
- [Manual] : Contains information on how to install the Angerona framework. It starts by explaining how you can get the binary packages of Angerona to perform experiments and also contains installation steps for developers, that want to contribute to the Angerona framework.
- [Doxygen Code-Documentation](https://marathon.cs.tu-dortmund.de/docs/) - Update every night using current master branch
- [Track : Issue and Project Management](https://marathon.cs.tu-dortmund.de/trac) - Issue tracking, project management and a wiki containing e.g. design guidelines, HowTos and an overview of the implemented scenarios
- Related Theses:
		[1] Ella Albrecht. Dependency graphs and explanations for answerset programming. Master’s thesis, Technische Universita ̈t Dortmund, 2012.
		[2] Manuel Barbi. Implementierung einer motivationskomponente fu ̈r wissensbasierte agenten. Master’s thesis, Technische Universita ̈t Dortmund, 2014.
		[3] Mirja Bo ̈hmer. Evaluation of approaches to belief change in asp knowldege bases. Master’s thesis, Technische Universita ̈t Dortmund, 2011.
		[4] Benedict Bo ̈rger. Modellierung und evaluation von vertraulichkeits- bewahrenden agenten in einem e-markt szenario mittels antwort- mengenprogrammierung. Master’s thesis, Technische Universita ̈t Dortmund, 2014.
		[5] Daniel Dilger, Patrick Kru ̈mpelmann, and Cornelia Tadros. Pre- serving confidentiality in multiagent systems - an internship project within the daad rise program. Technical Report 1, Technische Univer- sita ̈t Dortmund, 2013.
		[6] Sebastian Homann. Argumentation based selective revision of ex- tended logic programs. Master’s thesis, Technische Universita ̈t Dort- mund, 2013.
		[7] Tim Janus. Student research project: Planning communication under confidentiality consideration. Master’s thesis, Technische Universita ̈t Dortmund, 2012.
		[8] Matthias Thimm and Patrick Kru ̈mpelmann. Know-how for moti- vated BDI agents (extended version). Technical Report 822, Technis- che Universita ̈t Dortmund, Department of Computer Science, Febru- ary 2009.

More external documentation will follow soon.


Contributors
------------

Angerona is collaboratively developed by several contributors. 
Thanks go to Ella Albrecht, Daniel Dilger, Sebastian Homann, 
Tim Janus, Patrick Krümpelmann, Cornelia Tadros and Pia Wierzoch.
