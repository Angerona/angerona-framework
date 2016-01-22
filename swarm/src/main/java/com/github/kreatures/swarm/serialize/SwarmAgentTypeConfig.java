package com.github.kreatures.swarm.serialize;



import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="agentType")
public class SwarmAgentTypeConfig {
	@Attribute(name="id")
	protected int idSwarmAgentType;
	@Attribute(name="name")
	protected String nameSwarmAgentType;
	@Attribute(name="x", required=false)
	protected int xSwarmAgentType;
	@Attribute(name="y", required=false)
	protected int ySwarmAgentType;
	@Attribute(name="count")
	protected int countSwarmAgentType;
	@Attribute(name="interface", required=false)
	protected String interfaceSwarmAgentType;
	@Attribute(name="colorRed", required=false)
	protected int colorRedSwarmAgentType;
	@Attribute(name="colorGreen", required=false)
	protected int colorGreenSwarmAgentype;
	@Attribute(name="colorBlue", required=false)
	protected int colorBlueSwarmAgentype;
	@Element(name="time", required=false)
	protected SwarmAgentTypeConfigTime timeSwarmAgentType;
	@Element(name="size", required=false)
	protected SwarmAgentTypeConfigSize sizeSwarmAgentType;
	
	public int getIdSwarmAgentType() {
		return idSwarmAgentType;
	}
	public String getNameSwarmAgentType() {
		return nameSwarmAgentType;
	}
	public int getxSwarmAgentType() {
		return xSwarmAgentType;
	}
	public int getySwarmAgentType() {
		return ySwarmAgentType;
	}
	public int getCountSwarmAgentType() {
		return countSwarmAgentType;
	}
	
	public int getColorRedSwarmAgentType() {
		return colorRedSwarmAgentType;
	}
	public int getColorGreenSwarmAgentype() {
		return colorGreenSwarmAgentype;
	}
	
	public int getColorBlueSwarmAgentype() {
		return colorBlueSwarmAgentype;
	}
	public String getInterfaceSwarmAgentType() {
		return interfaceSwarmAgentType;
	}
	public SwarmAgentTypeConfigTime getTimeSwarmAgentType() {
		return timeSwarmAgentType;
	}
	public SwarmAgentTypeConfigSize getSizeSwarmAgentType() {
		return sizeSwarmAgentType;
	}
	
}
