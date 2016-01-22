package com.github.kreatures.core.serialize;


import java.util.List;

//import com.github.kreaturesfw.core.serialize.AgentConfigReal;
import com.github.kreatures.core.serialize.BeliefbaseConfig;
//import com.github.kreaturesfw.core.serialize.BeliefbaseConfigReal;
import com.github.kreatures.core.serialize.SimulationConfiguration;

/**
 * 
 * @author donfack
 *
 */
public interface CreateKReaturesXMLDatei {
	
	public SimulationConfiguration createSimulationConfig();
	
	public List<AgentInstance> createAgentConfig();
	
	public BeliefbaseConfig createBeliefbaseConfigReal();

}
