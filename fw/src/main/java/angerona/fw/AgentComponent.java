package angerona.fw;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import angerona.fw.report.Entity;
import angerona.fw.report.EntityAtomic;

/**
 * Base class for extensions of the Agent-Model, like know-how.
 * This base class provides the general id functionality of the Angerona
 * Entity-Report System.
 * @author Tim Janus
 *
 */
public abstract class AgentComponent implements EntityAtomic {
	
	private Long parentId;
	
	private Long id;
	
	public AgentComponent() {
		id = IdGenerator.generate(this);
	}
	
	public AgentComponent(AgentComponent other) {
		other.id = id;
		other.parentId = parentId;
	}
	
	public void setParent(Long id) {
		parentId = id;
	}

	public Agent getAgent() {
		Entity reval = IdGenerator.getEntityWithId(parentId);
		if(reval != null) {
			return (Agent)reval;
		}
		return null;
	}
	
	/** 
	 * 	is called after the agent is fully created to initialize the component 
	 * 	@param	additionalData	a map containing the addiotinal data defined in the simulation xml file
	 */
	public void init(Map<String, String> additionalData) { }
	
	@Override
	public Long getGUID() {
		return id;
	}

	@Override
	public Long getParent() {
		return parentId;
	}

	@Override
	public List<Long> getChilds() {
		return new LinkedList<Long>();
	}
	
	@Override
	public abstract Object clone();
}
