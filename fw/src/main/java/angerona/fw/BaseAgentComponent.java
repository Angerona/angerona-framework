package angerona.fw;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import angerona.fw.internal.Entity;
import angerona.fw.internal.IdGenerator;

/**
 * Base class for extensions of the Agent-Model, like know-how.
 * This base class provides the general id functionality of the Angerona
 * Entity-Report System.
 * @author Tim Janus
 *
 */
public abstract class BaseAgentComponent implements AgentComponent {
	
	/** unique id of the parent (the agent) */
	private Long parentId;
	
	/** own unique id */
	private Long id;
	
	public BaseAgentComponent() {
		id = IdGenerator.generate(this);
	}
	
	public BaseAgentComponent(BaseAgentComponent other) {
		this.id = new Long(other.id);
		this.parentId = new Long(other.parentId);
	}
	
	@Override
	public void setParent(Long id) {
		parentId = id;
	}

	@Override
	public Agent getAgent() {
		Entity reval = IdGenerator.getEntityWithId(parentId);
		if(reval != null) {
			return (Agent)reval;
		}
		return null;
	}
	
	public void report(String msg) {
		Angerona.getInstance().report(msg, getAgent(), this);
	}
		
	@Override
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
