package angerona.fw.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Skill;
import angerona.fw.error.InvokeException;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;
import angerona.fw.reflection.ContextProvider;
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
	
	@Override
	public void run() {
		Agent a = getAgent();
		
		// create the context for running the Skills... 
		Context c = new Context();
		c.set("self", a);
		c.set("name", a.getName());
		
		c.set("beliefs", beliefs);
		c.set("world", beliefs.getWorldKnowledge());
		Context views = new Context();
		for(String name : beliefs.getViewKnowledge().keySet()) {
			views.set(name, beliefs.getViewKnowledge().get(name));
		}
		c.attachContext("views", views);
	
		// also define the reason for the action, this might be a perception like
		// query if the XMLSkill is performing an answer.
		Context in = null;
		if(dataObject instanceof Context) {
			in = (Context) dataObject;
		} else if(dataObject instanceof ContextProvider) {
			in = ((ContextProvider)dataObject).getContext();
		} else {
			in = ContextFactory.createContext(dataObject);
		}
		c.attachContext("in", in);
		
		// TODO Move the next block into a class responsible for xml-script execution...
		// iterate through all statements.
		for(Statement st : config.getStatements()) {
			ContextVisitor cv = null;
			String cmd = st.getCommandoName();
			
			// find the command and create correct visitor.
			if(cmd.equalsIgnoreCase("UpdateBB")) {
				cv = new UpdateBeliefbaseVisitor();
				LOG.warn("UpdateBB XML-Commando was not used for a while. Better test if the resuls are correct.");
			} else if(cmd.equalsIgnoreCase("Reason")) {
				cv = new ReasonVisitor();
				LOG.warn("Reason XML-Commando was not used for a while. Better test if the resuls are correct.");
			} else if(cmd.equalsIgnoreCase("SendAction")) {
				cv = new SendActionVisitor(a.getEnvironment().getPerceptionFactory(), actionProcessor, beliefs, this.agent);
			}
			
			// invoke the command if one was found.
			if(cv != null) {
				try {
					c.Invoke(cv, st);
				} catch(InvokeException ex) {
					ex.printStackTrace();
					LOG.error("Invoke-Exception during Agent cycle: " + ex.getMessage());
				}
			}
		}
		
		c.detachContext("in");
		
		if(parent != null && isRealRun())
			parent.onSubgoalFinished(this);
	}
}
