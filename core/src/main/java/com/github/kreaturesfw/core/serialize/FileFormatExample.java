package com.github.kreaturesfw.core.serialize;

import java.io.File;
import java.util.LinkedList;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;

import org.simpleframework.xml.core.PersistenceException;

import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.legacy.Agent;

/**
 * Writes xml examples of the current file format to the console.
 * @author Tim Janus
 */
public class FileFormatExample {

	public static void main(String[] args) {
		// Create example simulation configuration file:
		SimulationConfiguration conf = new SimulationConfiguration();
		conf.name = "SCM";
		conf.behaviorCls = "net.sf.Behavior";
		AgentInstance agent = getTestAgentInstance();
		conf.agents.add(agent);
		SerializeHelper sh = SerializeHelper.get();
		
		Agent sender = new Agent("Boss", null);
		conf.perceptions.add(new Query(sender, "Employee", new FOLAtom(new Predicate("attend_scm"))));
		
		try {
			conf.validate();
		} catch (PersistenceException e) {
			System.out.println("Validation Error: " + e.getMessage());
		} catch (Exception e) {
			
		}
		// Output example simulation configuration file:
		System.out.println("Simulation - Configuration - An example XML:");
		sh.outputXml(conf, System.out);
		System.out.println("\n\n");
		
		
		// Create example belief base configuration file:
		// But first prepare a typical operator set shared for all operators:
		OperationSetConfigReal opc = new OperationSetConfigReal();
		opc.defaultClassName = "classNameOfDefaultOperator";
		opc.operatorClassNames.add("0-to-many");
		opc.operatorClassNames.add("alternative-classes");
		
		// Finally create the config:
		BeliefbaseConfigReal bbcr = new BeliefbaseConfigReal();
		bbcr.name = "example-belief-base-config";
		bbcr.beliefbaseClassName = "fully.qualified.beliefbasecls";
		bbcr.changeOperators = bbcr.reasonerOperators = bbcr.translators = opc;
		
		// Output example belief base configuration file:
		System.out.println("Beliefbase - Configuration - An example XML:");
		sh.outputXml(bbcr, System.out);
		System.out.println("\n\n");
		
		opc.operationType = "GenerateOptions";
		// Create example agent configuration file:
		AgentConfigReal acr = new AgentConfigReal();
		acr.name = "example-agent-config";
		acr.operators = new LinkedList<>();
		acr.operators.add(opc);
		acr.componentClasses.add("fully.class.name");
		acr.componentClasses.add("would.look.like");
		acr.componentClasses.add("angerona.fw.knowhow.KnowhowBase");
		
		// Output example agent configuration file:
		System.out.println("Agent - Configuration - An example XML:");
		sh.outputXml(acr, System.out);
		
	}

	public static AgentInstance getTestAgentInstance() {
		AgentInstance test = new AgentInstance();
		test.name = "Employee";
		test.additionalData.put("Confidential", "(Boss, a.b.reasoner{param=1}, attend_scm)");
		test.alternativeBBName = "asp";
		test.config = getAgentConfig();
		test.beliefbaseConfig = getBeliefbaseConfig();
		test.fileViewMap.put("Bob", new File("config/beliefbases/asp_beliefbase.xml"));
		test.desires.add(new FOLAtom(new Predicate("attend_scm")));
		test.capabilities.add("fully.qualified.class.name");
		
		return test;
	}
	
	public static AgentConfigImport getAgentConfig()  {
		AgentConfigImport reval = new AgentConfigImport();
		reval.source = new File("config/agents/default_agent.xml");
		return reval;
	}
	
	public static BeliefbaseConfigImport getBeliefbaseConfig()  {
		BeliefbaseConfigImport reval = new BeliefbaseConfigImport();
		reval.source = new File("config/beliefbases/asp_beliefbase.xml");
		return reval;
	}
}
