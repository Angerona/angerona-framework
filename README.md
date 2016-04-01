
#KReatures-Framework

The KReatures framework is a framework for the implementation of knowledge-based agents with a strong focus on flexibility, extensibility and compatibility with diverse knowledge representation formalisms.
Its basis is a flexible Java plug-in architecture for the mental state of an agent as well as for the agent's functional component.
Different knowledge representation formalisms can be used within one agent and different agents in the same system can be based on different agent architectures and can use different knowledge representation formalisms.
Partially instantiated plug-ins define sub-frameworks for, e.g., the development of BDI agents and variants thereof.
The knowledge representation plug-ins are based on the [Tweety library](http://tweetyproject.org/) for knowledge representation, which provides various ready-for-use implementations and knowledge representation formalisms and a framework for the implementation of additional ones.
KReatures already contains several partial and complete instantiations that implement several approaches from the literature. 
KReatures also features an environment plug-in for communicating agents and a flexible GUI to monitor the simulation and the agents, particularly including the inspection of the dynamics of their mental states.


##Features of the current Version

The KReatures agent architecture can be freely defined by specifying the types of operators to be used and their order of execution.
This way KReatures allows to easily design different types of agents.
Not only the used language for knowledge representation can differ, but also to which amount an agent's functionality is logic based. It is, for instance, easily possible to realize the agent's deliberation and means-ends reasoning by Java operators and simple data components, or by simple Java operators which make use of logical formalisms, e.g. answer set programming (ASP), ordinal conditional functions (OCF), argumentation formalisms, or propositional logic or horn logic, or any other formalism from the Tweety library.

While the general KReatures framework allows for a high degree of flexibility it also allows to define partially instantiated plug-ins and default agent configurations which represent sub-frameworks with more predefined structure and functionality.
The latter might fix the general agent cycle by specifying the types of operators to be used and provide different implementations for these.
Hence, the sub-frameworks provide more support for easy and rapid development of agents.
We distinguish three different types of users in the KReatures framework.
The *core developer* that uses KReatures as a toolkit to define its own agent types.
The *plug-in developer* that uses provided agent types and instantiates them with given or its own plug-ins.
And the *knowledge engineer* that defines the background and initial knowledge, and all other initial instances of the components of the agents.

KReatures provides default implementations for BDI style agents and diverse extensions that can be modularly used to build agents.
Complete multiagent systems of communicating agents using answer set programming, propositional logic and ordinal conditional functions for knowledge representation, including change operators for these based on belief change theory are implemented and available.
These have been used in the context of secrecy preserving agents for which scenarios and simulations are available.

KReatures also features a plug-in interface for different environments, with a communication environment for communicating agents implemented.

A graphical user interface (GUI) allows the selection, execution, observation, and inspection of multi-agent simulations.
The GUI can be extended by plug-ins to feature displays of specific knowledge representation formalisms, for instance dependency graphs.

##Documentation
The KReatures framework is well documented from the theoretical concept down to the documentation of the source code and documentation of the use of the application.
Here is a list of the primary sources of information, further below follows a list of Publications on, and related to, KReatures.

- [Technical Report](http://sfb876.tu-dortmund.de/PublicPublicationFiles/kruempelmann_janus_2014a.pdf)
  The report is divided into two parts, the first part introduces the general concept of KReatures and the BDI secrecy agent model. The second part describes the implementation of the concepts in KReatures.
- [Manual](documentation/manual.pdf)
  Contains information on how to install the KReatures framework. It starts by explaining how you can get the binary packages of KReatures to perform experiments and also contains installation steps for developers, that want to contribute to the KReatures framework.
- [Doxygen Code-Documentation](https://marathon.cs.tu-dortmund.de/docs/)
  Contains the full documentation of the source code of KReatures. It is updated every night using the current master branch.
- [Track : Issue and Project Management](https://marathon.cs.tu-dortmund.de/trac)
   Issue tracking, project management and a wiki containing e.g. design guidelines, HowTos and an overview of the implemented scenarios.
- [Tweety](http://tweety.sourceforge.net/)
   The Tweety library for knowledge representation project site.


##Contributors

KReatures is collaboratively developed by several contributors. If you could’t find an answer to your questions or problems in any of the listed resources, some of these people might be able to help you:

 + **Patrick Krümpelmann** *(patrick.kruempelmann@udo.edu)*
    Initiator and maintainer of the general KReatures Framework, and the secrecy and ASP instances; supervision of students.
 + **Stefan Rötner** *(stefan.roetner@udo.edu)*
  Student research assistant : In charge of framework maintenance and further development.
 + **Tim Janus** *(tim.janus@udo.edu)*
  Former student research assistant  : Initial main developer of the KReatures Framework, including the secrecy, ASP and know-how implementation.
 + **Cornelia Tadros** *(cornelia.tadros@cs.tu-dortmund.de)*
  Supervision of students, contributions from research on confidentiality preserving agent interactions, especially revisions and updates.
 + **Pia Wierzoch** *(pia.wierzoch@udo.edu)*
   Student research assistant:  Modeling and implementation of a negotiation scenario in an e-market based on revision by means of ordinal conditional functions; Extension of the e-market scenario with PMA updates and negotiation protocols.
 + **Sebastian Homann**
  Former student research assistant  : Modeling and implementation of a negotiation scenario in an e-market based on revision by means of ordinal conditional functions; framework maintenance 

Thanks also go to Ella Albrecht, Manuel Barbi, Benedict Börger and Daniel Dilger for their contributions to KReatures (see Publications and Theses below).

##Publications:
+ Albrecht, E. and Krümpelmann, P. and Kern-Isberner, G. (to appear). "Construction of explanation graphs from extended dependency graphs for answer set programs". In Hanus, M. and Rocha, R., editors, Postproceedings of the 27th Workshop on Functional and Logic Programming (WFLP 2013), Lecture Notes in Computer Science. Springer.
+ Biskup, J. and Tadros, C. (2012). "Revising belief without revealing secrets". In Lukasiewicz, T. and Sali, A., editors, 7th International Symposium on Foundations of Information and Knowledge Systems (FoIKS 2012), volume 7153 of LNCS, pages 51–70. Springer.
+ Biskup, J. and Tadros, C. (2013). "Preserving confidentiality while reacting on iterated queries and belief revisions". Annals of Mathematics and Artificial Intelligence, pages 1–49.
+ Tadros, C. (2014). "Belief Change Operations under Confidentiality Requirements in Multiagent Systems". PhD thesis, Technische Universität Dortmund.
+ Thimm, M. and Krümpelmann, P. (2009). "Know-how for motivated BDI agents (extended version)". Technical Report 822, Technische Universität Dortmund, Department of Computer Science.
+ Krümpelmann, P. and Janus, T. and Kern-Isberner, G. (2014a). "KReatures - a flexible multiagent framework for knowledge-based agents". In Bulling, N., editor, Proceedings of the 12th European Conference on Multi-Agent Systems, volume to appear of Lecture Notes in Artificial Intelligence. Springer.


####Related Theses:
+ Albrecht, E. (2012). "Abhängigkeitsgraphen und Erklärungen für die Antwortmengen Programmierung" (Dependency Graphs and Explanations for Answerset Programming). Bachelor’s Thesis, Technische Universität Dortmund.
+ Barbi, M. (2014). "Implementierung einer Motivationskomponente fü̈r wissensbasierte Agenten" (Implementation of a Motivation Component for Knowledge-based Agents). Bachelor’s Thesis, Technische Universität Dortmund.
+ Böhmer, M. (2011). "Evaluation von Ansätzen zur Wissensänderung von ASP Wissensbasen" (Evaluation of approaches to belief change in ASP knowldege bases). Master’s thesis, Technische Universität Dortmund.
+ Börger, B. (2014). "Modellierung und Evaluation von vertraulichkeitsbewahrenden Agenten in einem E-Markt Szenario mittels Antwortmengenprogrammierung" (Conceptualization and evaluation of secrecy preserving agents in an E-Market scenario with answerset programming). Bachelor’s Thesis, Technische Universität Dortmund.
+ Dilger, D. and Krümpelmann, P. and Tadros, C. (2013). "Preserving confidentiality in multiagent systems - an internship project within the daad rise program". Technical Report, Technische Universität Dortmund.
+ Homann, S. (2013). "Argumentationsbasierte selektive Revision erweiterter logischer Programme" (Argumentation based selective revision of extended logic programs). Master’s thesis, Technische Universität Dortmund.
+ Janus, T. (2013). "Resource-bounded Planning of Communication under Confidentiality Constraints for BDI Agents". Master’s thesis, Technische Universität Dortmund.
+ Wierzoch, P. (2014). "Aktualisierung von Wissen unter Vertraulichkeitsanforderungen". Bachelor’s Thesis, Technische Universität Dortmund, LS 6 - Information Systems and Security.
