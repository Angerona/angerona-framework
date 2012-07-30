package angerona.fw;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.InvokeException;
import angerona.fw.logic.SecrecyStrengthPair;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;
import angerona.fw.reflection.ContextVisitor;
import angerona.fw.reflection.ReasonVisitor;
import angerona.fw.reflection.SendActionVisitor;
import angerona.fw.reflection.UpdateBeliefbaseVisitor;
import angerona.fw.serialize.SkillConfig;
import angerona.fw.serialize.Statement;

/** A skill represents an atomic intention an agent can perform */
//Should this implement Cloneable? --Daniel
public class Skill extends Intention implements Runnable {

	/** the unique name of the Skill */
	private String name;
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(Skill.class);
	
	/** data-structure containing the configuration of the skill (set of operations) */
	private SkillConfig config;
	
	private boolean violates = false;
	
	/* Begin Daniel's changes */
	
	private Context rememberedContext = null;
	
	public Context getRememberedContext()
	{
		return rememberedContext;
	}
	
	/* These changes need to be isolate or otherwise changed -- Daniel */
	private List<SecrecyStrengthPair> weakenings = null;
	
	
	public Skill(Agent agent, SkillConfig config) {
		super(agent);
		this.name = config.getName();
		this.config = config;
	}
	public Skill(Skill s)
	{
		super(s.agent);
		this.parent = s.parent;
		this.realRun = s.realRun;
		this.objectContainingContext = s.objectContainingContext;
		this.cost = s.cost;
		this.honesty = s.honesty;
		this.name = s.name;
		this.config = s.config;
		this.violates = s.violates;
		this.weakenings = s.weakenings;
	}
	

	public List<SecrecyStrengthPair> getWeakenings()
	{
		return this.weakenings;
	}
	public void setWeakenings(List<SecrecyStrengthPair> weaks)
	{
		this.weakenings = weaks;
	}
	
	
	/* End of Daniel's changes */
	public boolean violates() {
		return violates;
	}
	
	/** @return the unique name of the Skill */
	public String getName() {
		return name;
	}
	
	@Override
	public void run() {
		Agent a = getAgent();
		violates = false;
		Context c = a.getContext();
		Context in = null;
		if(objectContainingContext instanceof Context) {
			in = (Context) objectContainingContext;
		} else {
			in = ContextFactory.createContext(objectContainingContext);
		}
		c.attachContext("in", in);
		
		for(Statement st : config.getStatements()) {
			ContextVisitor cv = null;
			String cmd = st.getCommandoName();
			boolean sendAction = false;
			
			if(cmd.equalsIgnoreCase("UpdateBB")) {
				cv = new UpdateBeliefbaseVisitor();
			} else if(cmd.equalsIgnoreCase("Reason")) {
				cv = new ReasonVisitor();
			} else if(cmd.equalsIgnoreCase("SendAction")) {
				cv = new SendActionVisitor(
						a.getEnvironment().getPerceptionFactory(), realRun, a.getBeliefs());
				sendAction = true;
			}
			
			if(cv != null) {
				try {
					c.Invoke(cv, st);
					if(sendAction && !realRun) {
						violates = ((SendActionVisitor)cv).violates();
						weakenings = ((SendActionVisitor)cv).weakenings();
					}
				} catch(InvokeException ex) {
					ex.printStackTrace();
					LOG.error("Invoke-Exception during Agent cycle: " + ex.getMessage());
				}
			}
		}
		
		c.detachContext("in");
		
		if(parent != null && realRun)
			parent.onSubgoalFinished(this);
	}

	@Override
	public boolean isAtomic() {
		return true;
	}
	
	@Override
	public boolean isPlan() {
		return false;
	}

	@Override
	public boolean isSubPlan() {
		return false;
	}

	@Override
	public void onSubgoalFinished(Intention subgoal) {
		// does nothing here has no subgoals.
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public Object clone() {
		return this;
	}
	
	//The need for this is debatable
	
	public Skill deepCopy()
	{
		return new Skill(this);
	}
	
}
