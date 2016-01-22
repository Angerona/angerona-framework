KReatures Framework	{#mainpage}
==================
This is the technical documentation of the KReatures framework, beside the code documentation 
it also contains some articles which either help to understand concepts used by the KReatures 
framework or help to get started to extend KReatures at a specific place like adding a new 
knowledge representation mechanism or another agent model.

Features
-------- 
Currently the KReatures framework provided the following list of features.
 
- Flexible and simple plug-in driven multi agent simulation using the
  [Java Simple Plugin Framework](https://code.google.com/p/jspf/).
- Complete decoupling of knowledge representation mechanism from the
  functional component of the agent.
- Defining custom agent models, like the secrecy aware extension of the
  BDI model defined in kreatures.am.secrecy .
- Different EnvironmentBehavior to easily support external simulation software.
- A flexible UI using [Docking Frames](http://dock.javaforge.com/) to give the 
  monitoring of the agent simulation the same features as an IDE.

External Documentation
----------------------

External documentation will be publicized in the near future:
- Manual: Contains information on how to install the KReatures framework. It starts by
  explaining how you can get the binary packages of KReatures to perform experiments and
  also contains installation steps for developers, that want to contribute to the KReatures
  framework.
- Technical Report: The report is divided into two parts, the first part introduces the concept of the secrecy
  agent model that is implemented in the KReatures framework and the second part explains how it
  has been implemented.

 
Articles
--------

- [The Operator Concept](@ref conceptoperators) 
- [Secrecy aware BDI Agent Model](@ref secrecyawarebdiagentmodel) 
- [Concurrency in the KReatures Framework](@ref concurrencyinkreatures)
- [How-to: Implement an GUI-Extension for KReatures](@ref howtoguiextension)

 
