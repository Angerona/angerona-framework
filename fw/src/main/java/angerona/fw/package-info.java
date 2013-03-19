/**
 * @mainpage
 * 
 * Angerona Framework
 * ==================
 * This is the technical documentation of the Angerona framework, beside the code documentation 
 * it also contains some articles which either help to understand concepts used by the Angerona 
 * framework or help to get started to extend Angerona at a specific place like adding a new 
 * knowledge representation mechanism or another agent model.
 * 
 * 
 * Features
 * -------- 
 * Currently the Angerona framework provided the following list of features.
 * 
 * - 	flexible and simple plug-in driven multi agent simulation using the
 * 		[Java Simple Plugin Framework](https://code.google.com/p/jspf/).
 * - 	complete decoupling of knowledge representation mechanism from the
 * 		functional component of the agent.
 * -	Defining custom agent models, like the secrecy aware extension of the
 * 		BDI model defined in angerona.fw.am.secrecy .
 * - 	different EnvironmentBehavior to easily support external simulation software.
 * -	A flexible UI using [Docking Frames](http://dock.javaforge.com/) to give the 
 * 		monitoring of the agent simulation the same features as an IDE.
 * 
 * Articles
 * --------
 * 
 * - @subpage conceptoperators
 * - @subpage secrecyawarebdiagentmodel 
 */



/**
 * This package contains every class which is an 
 * important part of the Angerona Framework. The most
 * important classes are Agent and AngeronaEnvironment.
 * 
 * @author Tim Janus
 */
package angerona.fw;