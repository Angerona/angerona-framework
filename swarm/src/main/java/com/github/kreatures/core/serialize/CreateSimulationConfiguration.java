package com.github.kreatures.core.serialize;

import java.util.List;

import com.github.kreatures.core.Perception;
import com.github.kreatures.core.serialize.SimulationConfiguration;

public class CreateSimulationConfiguration extends SimulationConfiguration {

	protected void setName(String name) {
		this.name = name;
	}

	protected void setCategory(String category) {
		this.category = category;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	protected void setBehaviorCls(String behaviorCls) {
		this.behaviorCls = behaviorCls;
	}

	protected void setAgents(List<AgentInstance> agents) {
		this.agents = agents;
	}

	protected void setPerceptions(List<Perception> perceptions) {
		this.perceptions = perceptions;
	}
}
