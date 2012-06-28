package angerona.fw.logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.listener.AgentListener;
import angerona.fw.parser.ParseException;
import angerona.fw.parser.SecretParser;
/**
 * Data-Component of an agent containing a set of personal confidential targets.
 * @author Tim Janus
 */
public class ConfidentialKnowledge extends BaseAgentComponent implements AgentListener {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ConfidentialKnowledge.class);
	
	/** set of confidential targets defining this beliefbase */
	private Set<Secret> confidentialTargets = new HashSet<Secret>();
	
	private FolSignature signature = new FolSignature();
	
	public ConfidentialKnowledge() {
		super();
	}
	
	public ConfidentialKnowledge(ConfidentialKnowledge other) {
		for(Secret ct : other.confidentialTargets) {
			this.confidentialTargets.add((Secret)ct.clone());
		}
	}
	
	@Override
	public Object clone() {
		return new ConfidentialKnowledge(this);
	}

	public void setSignature(FolSignature signature) {
		this.signature = signature;
	}
	
	@Override
	public String toString() {
		String reval = "";
		for(Secret ct : confidentialTargets)
			reval += ct.toString() + "\n";
		return reval;
	}
	
	/**
	 * adds confidential target to the beliefbase.
	 * @param cf	the new confidential target which will be added to the beliefbase.
	 * @return		true if the beliefbase didn't contain the confidential target, false otherwise.
	 */
	public boolean addConfidentialTarget(Secret cf) {
		return confidentialTargets.add(cf);
	}
	public boolean removeConfidentialTarget(Secret cf)
	{
		return confidentialTargets.remove(cf);
	}
	/**
	 * Gets the confidential target defined by the subject and the information which is confidential.
	 * @param subjectName	name of the agent who should not get the confidential information
	 * @param information	the confidential information itself.
	 * @return				A confidential target if it exists, null otherwise.
	 */
	public Secret getTarget(String subjectName, Formula information) {
		for(Secret ct : confidentialTargets)
			if(ct.getSubjectName().compareTo(subjectName) == 0 && ct.getInformation().equals(information))
				return ct;
		return null;
	}
	
	public Set<Secret> getTargets() {
		return Collections.unmodifiableSet(confidentialTargets);
	}

	@Override
	public void init(Map<String, String> additionalData) {
		getAgent().addListener(this);
		Signature worldSig = getAgent().getBeliefs().getWorldKnowledge().getSignature();
		this.signature = new FolSignature( worldSig.getSymbolSet());
		if(!additionalData.containsKey("Confidential")) {
			LOG.warn("Confidential Knowledge of agent '{}' has no initial data.", getAgent().getName());
			return;
		} else {
			this.signature.fromSignature(getAgent().getBeliefs().getWorldKnowledge().getSignature());
			String str = additionalData.get("Confidential");
			SecretParser parser = new SecretParser(str, signature);
			try {
				confidentialTargets.addAll(parser.Input());
			} catch (ParseException e) {
				LOG.error("Cannot parse the secret defined for Agent '{}':\n{}", getAgent().getName(), str);
			}
			
			// Check for startup inconsistency:
			beliefbaseChanged(getAgent().getBeliefs().getWorldKnowledge(), AgentListener.WORLD);
			for(String agName : getAgent().getBeliefs().getViewKnowledge().keySet()) {
				beliefbaseChanged(getAgent().getBeliefs().getViewKnowledge().get(agName), 
					agName);
			}
		}
	}
	
	@Override
	public void beliefbaseChanged(BaseBeliefbase bb, String space) {
		if(!space.equals(AgentListener.WORLD)) {
			// check for unrivaled secrets:
			List<Secret> toRemove = new LinkedList<Secret>();
			for(Secret secret : getTargets()) {
				if(secret.getSubjectName().equals(space)) {
					if(	bb.infere().contains(secret.getInformation()))  {
						toRemove.add(secret);
					}
				}
			}
			
			if(toRemove.size() > 0) {
				for(Secret remove : toRemove) {
					removeConfidentialTarget(remove);
				}
				report("Changes of Beliefbase causes Confidential update, "+ toRemove.size() +" secrets removed.");
			}
		}
	}

	@Override
	public void componentAdded(BaseAgentComponent comp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentRemoved(BaseAgentComponent comp) {
		// TODO Auto-generated method stub
		
	}
}
