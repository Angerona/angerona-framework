package angerona.fw.logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.Signature;
import net.sf.tweety.SymbolSet;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.listener.AgentListener;
import angerona.fw.parser.ParseException;
import angerona.fw.parser.SecretParser;
import angerona.fw.util.Pair;
/**
 * Data-Component of an agent containing a set of personal confidential targets.
 * @author Tim Janus
 */
public class ConfidentialKnowledge 
	extends BaseAgentComponent 
	implements AgentListener, PropertyChangeListener {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ConfidentialKnowledge.class);
	
	/** set of confidential targets defining this beliefbase */
	private Set<Secret> confidentialTargets = new HashSet<Secret>();
	
	/**
	 * This map is used for optimization purposes. It contains Sets of secrets as values and it keys
	 * are pairs describing the used Reasoner (ClassName, ParamterMap). 
	 * This map made it easy to use a minimal amount of infere calls to because it defines a sort
	 * on the secrets based on the used reasoner.
	 */
	private Map<Pair<String, Map<String, String>>, Set<Secret>> optimizationMap = new HashMap<Pair<String, Map<String, String>>, Set<Secret>>();
	
	/** The used signature */
	private FolSignature signature = new FolSignature();
	
	/** Default Ctor */
	public ConfidentialKnowledge() {
		super();
	}
	
	/** Copy Ctor: The property listeners will be registered for each target and are not copied by secrets clone method */
	public ConfidentialKnowledge(ConfidentialKnowledge other) {
		for(Secret ct : other.confidentialTargets) {
			Secret clone = (Secret)ct.clone();
			clone.addPropertyListener(this);
			this.confidentialTargets.add(clone);
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
		boolean reval = confidentialTargets.add(cf);
		if(reval) {
			addToMap(cf, cf.getPair());
			cf.addPropertyListener(this);
			LOG.info("Add secret '{}' to confidential knowledge of '{}'", cf, getAgent().getName());
		}
		return reval;
	}
	
	public boolean removeConfidentialTarget(Secret cf)
	{
		boolean reval = confidentialTargets.remove(cf);
		if(reval) {
			removeFromMap(cf, cf.getPair());
			cf.removePropertyListener(this);
		}
		return reval;
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
	
	/** @return an unmodifiable set of secrets of the confidential knowledge */
	public Set<Secret> getTargets() {
		return Collections.unmodifiableSet(confidentialTargets);
	}
	
	/** @return an unmodifiable map. The key is a Pair describing the secrets reasoner (class and parameters) and the values are sets of secrets */
	public Map<Pair<String, Map<String, String>>, Set<Secret>>  getTargetsByReasoningOperator() {
		return Collections.unmodifiableMap(optimizationMap);
	}

	@Override
	public void init(Map<String, String> additionalData) {
		getAgent().addListener(this);
		BaseBeliefbase world = getAgent().getBeliefs().getWorldKnowledge();
		Signature worldSig = world.getSignature();
		SymbolSet ss = worldSig.getSymbolSet();
		Set<String> views = getAgent().getBeliefs().getViewKnowledge().keySet();
		for(String viewname : views) {
			BaseBeliefbase view = getAgent().getBeliefs().getViewKnowledge().get(viewname);
			Signature sig = view.getSignature();
			ss.add(sig.getSymbolSet());
		}
		
		LOG.info(ss.toString());
		this.signature = new FolSignature(ss);
		if(!additionalData.containsKey("Confidential")) {
			LOG.warn("Confidential Knowledge of agent '{}' has no initial data.", getAgent().getName());
			return;
		} else {
			String str = additionalData.get("Confidential");
			SecretParser parser = new SecretParser(str, signature);
			try {
				Set<Secret> secrets = (parser.Input());
				for(Secret s : secrets)
					addConfidentialTarget(s);
			} catch (ParseException e) {
				LOG.error("Cannot parse the secret defined for Agent '{}':\n{}", getAgent().getName(), e.getMessage());
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
			Set<FolFormula> knowledge = bb.infere();
			for(Secret secret : getTargets()) {
				if(secret.getSubjectName().equals(space)) {
					if(	knowledge.contains(secret.getInformation()))  {
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

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("reasonerParameters")) {
			
			Map<String, String> oldValue = (Map<String, String>)evt.getOldValue();
			Map<String, String> newValue = (Map<String, String>)evt.getNewValue();
			Secret secret = (Secret)evt.getSource();
			
			Pair<String, Map<String, String>> key = new Pair<String, Map<String, String>>(
					secret.getReasonerClassName(), 
					new HashMap<String, String>(oldValue));
			removeFromMap(secret, key);
			
			key.second = new HashMap<String, String>(newValue);
			addToMap(secret, key);
		} else {
			// only one observed property...
			throw new RuntimeException("Did you forgot to rename 'reasonerParameters' everywhere?");
		}
	}
	
	/**
	 * Helper method: Adds a secret to the correct position in the optimization map
	 * @param secret	reference to the secret
	 * @param key		reference to the used key (might be secret.getPair())
	 */
	private void addToMap(Secret secret, Pair<String, Map<String, String>> key) {
		if(!optimizationMap.containsKey(key)) {
			optimizationMap.put(key, new HashSet<Secret>());
		}
		optimizationMap.get(key).add(secret);
	}
	
	/**
	 * Helper method:
	 * @param cf
	 * @param key
	 */
	private void removeFromMap(Secret cf, Pair<String, Map<String, String>> key) {
		Set<Secret> secrets = optimizationMap.get(key);
		if(secrets == null) {
			String error = "Something went wrong in event hierarchy. The searched secret key is not found in keyset of targetsByReasoningOperator map.\n";
			error += "\nKey: " + key.toString() + " - " + key.hashCode();
			error += "\nMap-Key-Set:\n";
			for(Pair<String, Map<String, String>> p : optimizationMap.keySet())
				error += p.toString() + " - " + p.hashCode() + " - " + p.equals(key) + "\n";
			throw new RuntimeException(error);
		}
		secrets.remove(cf);
		LOG.info("Removed secret '{}' from Confidential Knowledge of '{}'", cf, getAgent().getName());
		if(secrets.size() == 0) {
			optimizationMap.remove(key);
			LOG.info("Removed Key Pair '{}' from targetsByReasoningOperatorsMap of Agent '{}'", cf, getAgent().getName());
		}
	}
}
