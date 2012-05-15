package angerona.fw;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.NotImplementedException;

import net.sf.beenuts.ap.AgentArchitecture;
import net.sf.beenuts.ap.AgentProcess;
import net.sf.beenuts.apc.AgentProcessRunner;
import net.sf.beenuts.apr.APR;

/**
 * An agent process defines the low level communication of the agent with the environment. 
 * @author Tim Janus
 */
public class AngeronaAgentProcess implements AgentProcess {

	/** reference to logback logger */
	private static Logger LOG = LoggerFactory.getLogger(AngeronaAgentProcess.class);
	
	/** Reference to the simulation environment */
	private AngeronaEnvironment environemnt;
	
	/** Reference to the high level agent */
	private Agent architecture;
	
	/** unique name of the agent in the angerona framework */
	private String name;
	
	/** List of perceptions which have to be received by the agent */
	private List<Perception> perceptions = new LinkedList<Perception>();
	
	public AngeronaAgentProcess(String name) {
		if(name == null)
			throw new IllegalArgumentException("Name of the agent mustn't null.");
		this.name = name;
	}
	
	@Override
	public void perceive(Object perception) {
		if(perception instanceof Perception) {
			perceptions.add((Perception) perception);
		} else {
			throw new ClassCastException("Only perceptions object implementing the Angerona Perception interface are allowed.");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AgentArchitecture getAgentArchitecture() {
		return architecture;
	}

	@Override
	public void setAgentArchitecture(AgentArchitecture agArch) {
		if(agArch instanceof Agent) {
			architecture = (Agent)agArch;
		} else {
			throw new ClassCastException("Only Angerona Architecure is allowed by this agent process.");
		}
	}

	@Override
	public void send(Object message, Collection<String> recipients) {
		// Use the act method instead.
		throw new NotImplementedException();
	}

	@Override
	public void act(Object action) {
		if(action instanceof Action) {
			Action a = (Action)action;
			environemnt.sendAction(a.getReceiverId(), a);
			LOG.trace("Action: " + a.toString() + " send to environment.");
		} else {
			throw new ClassCastException("Only classes extending the angerona Action class allowed as Action objects");
		}
	}

	@Override
	public void execCycle() {
		if(hasPerceptions()) {
			architecture.cycle(perceptions.get(0));
			perceptions.remove(0);
		} else {
			architecture.cycle(null);
		}
	}

	@Override
	public void setRunner(AgentProcessRunner aprunner) {
		// A runner is not implemented for this AgentProcess yet.
		throw new NotImplementedException();
	}

	@Override
	public void setAPR(APR apr) {
		if(apr instanceof AngeronaEnvironment) {
			environemnt = (AngeronaEnvironment)apr;
		} else {
			throw new ClassCastException("APR for Angerona must be a subclass of AngeronaEnvironment");
		}
			
	}

	@Override
	public boolean canSleep() {
		return false;
	}

	/** @return true if some perceptions are available, false otherwise. */
	public boolean hasPerceptions() {
		return !perceptions.isEmpty();
	}
	
	public AngeronaEnvironment getEnvironment() {
		return environemnt;
	}
}
