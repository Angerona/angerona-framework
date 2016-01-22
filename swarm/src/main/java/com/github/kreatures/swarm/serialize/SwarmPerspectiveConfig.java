package com.github.kreatures.swarm.serialize;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="perspective")
public class SwarmPerspectiveConfig {
	@Attribute(name="id")
	protected int idSwarmPerspective;
	@Attribute(name="name")
	protected String nameSwarmPerspective;
	@Attribute(name="x")
	protected int xSwarmPerspective;
	@Attribute(name="y")
	protected int ySwarmPerspective;
	@Attribute(name="width")
	protected int widthSwarmPerspective;
	@Attribute (name="height")
	protected int heightSwarmPerspective;
	
	@ElementList(entry="agentType", type=SwarmAgentTypeConfig.class, inline=true, required=true)
	protected List<SwarmAgentTypeConfig> listAgentType;
	
	@ElementList(entry="stationType", type=SwarmStationTypeConfig.class, inline=true, required=true)
	protected List<SwarmStationTypeConfig> listStationType;
	
	
	public List<SwarmAgentTypeConfig> getListAgentType() {
		return listAgentType;
	}
	public List<SwarmStationTypeConfig> getListStationType() {
		return listStationType;
	}
	public int getIdSwarmPerspective() {
		return idSwarmPerspective;
	}
	public String getNameSwarmPerspective() {
		return nameSwarmPerspective;
	}
	public int getxSwarmPerspective() {
		return xSwarmPerspective;
	}
	public int getySwarmPerspective() {
		return ySwarmPerspective;
	}
	
	public int getWidthSwarmPerspective() {
		return widthSwarmPerspective;
	}
	public int getHeightSwarmPerspective() {
		return heightSwarmPerspective;
	}
	
}