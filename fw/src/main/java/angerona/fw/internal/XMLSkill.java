package angerona.fw.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Skill;
import angerona.fw.error.InvokeException;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;
import angerona.fw.reflection.ContextVisitor;
import angerona.fw.reflection.ReasonVisitor;
import angerona.fw.reflection.SendActionVisitor;
import angerona.fw.reflection.UpdateBeliefbaseVisitor;
import angerona.fw.serialize.SkillConfig;
import angerona.fw.serialize.Statement;

/**
 * The XMLSkill is a generic implementation of a skill allowing the user
 * to define the agents behavior when performing the skill by using XML
 * files.
 * 
 * The Skill reads an xml file for a list of statements which represent commandos:
 * First of all it searches for the correct visitor implementation for the actual commando.
 * Then it uses the Context of the agent to determine what a '$world' or '$in.answer' 
 * in the xml file means. 
 * 
 * $in is determined using the objectContaingContext member of the Intention base class.
 * objectContainingContext is set during subgoal-generation. For example is the Query causing
 * an Answer the objectContainingContext for the Answer Intention. So one can access the 
 * question by using '$in.question'
 * 
 * @author Tim Janus
 *
 */
public class XMLSkill extends Skill {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(XMLSkill.class);
	
	/** data-structure containing the configuration of the skill (set of operations) */
	private SkillConfig config;
	
	/**
	 * Ctor: Creates the xml skill used the deserilaized SkillConfig XML.
	 * @param ag		reference to the agent who is owner of the skill.
	 * @param config	reference to the skill-config object containg the interna of the skill.
	 */
	public XMLSkill(Agent ag, SkillConfig config) {
		super(ag, config.getName());
		this.config = config;
	}
	
	/**
	 * Copy-Ctor: (Daniels Code)
	 * @param other
	 */
	public XMLSkill(XMLSkill other) {
		super(other);
		this.config = other.config;
	}
	
	@Override
	public void run() {
		Agent a = getAgent();
		
		// clear violate flag
		if(!realRun)
			violates = new ViolatesResult();
		
		// create context for xml processing:
		Context c = a.getContext();
		Context in = null;
		if(objectContainingContext instanceof Context) {
			in = (Context) objectContainingContext;
		} else {
			in = ContextFactory.createContext(objectContainingContext);
		}
		c.attachContext("in", in);
		
		// iterate through all statements.
		for(Statement st : config.getStatements()) {
			ContextVisitor cv = null;
			String cmd = st.getCommandoName();
			boolean sendAction = false;
			
			// find the command and create correct visitor.
			if(cmd.equalsIgnoreCase("UpdateBB")) {
				cv = new UpdateBeliefbaseVisitor();
			} else if(cmd.equalsIgnoreCase("Reason")) {
				cv = new ReasonVisitor();
			} else if(cmd.equalsIgnoreCase("SendAction")) {
				cv = new SendActionVisitor(
						a.getEnvironment().getPerceptionFactory(), realRun, actBeliefs);
				sendAction = true;
			}
			
			// invoke the command if one was found.
			if(cv != null) {
				try {
					// TODO: hacky violate shifting... too complex...
					if(sendAction && realRun) {
						((SendActionVisitor)cv).setViolates(violates);
					}
					
					c.Invoke(cv, st);
					
					// send action is an command which needs a violation check:
					if(sendAction && !realRun) {
						violates = violates.combine(((SendActionVisitor)cv).violates());
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
	public Skill deepCopy() {
		return new XMLSkill(this);
	}
}
