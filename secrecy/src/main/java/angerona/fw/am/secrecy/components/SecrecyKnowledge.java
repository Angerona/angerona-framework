package angerona.fw.am.secrecy.components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
import angerona.fw.Perception;
import angerona.fw.am.secrecy.operators.BaseViolatesOperator;
import angerona.fw.listener.AgentAdapter;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.Secret;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.operators.OperatorCallWrapper;
import angerona.fw.operators.parameter.EvaluateParameter;
import angerona.fw.parser.ParseException;
import angerona.fw.parser.SecretParser;
import angerona.fw.util.Pair;

/**
 * Data component of an agent containing a set of personal secrets. By
 * setting another event handling strategy the adaption of the secrets
 * can be controlled. The default event handling strategy reacts to changes
 * of the view belief bases and uses the result of the violates operator to
 * determine which secret has to be weaken.
 * 
 * @author Tim Janus
 */
public class SecrecyKnowledge extends BaseAgentComponent 
	implements
	PropertyChangeListener {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(SecrecyKnowledge.class);

	/** set of secrets */
	private Set<Secret> secrets = new HashSet<Secret>();

	/** the strategy responsible to handle agent events */
	private AgentAdapter eventHandler = null;
	
	/**
	 * This map is used for optimization purposes. It contains Sets of secrets
	 * as values and it keys are pairs describing the used Reasoner (ClassName,
	 * ParamterMap). This map made it easy to use a minimal amount of infer
	 * calls to because it defines a sort on the secrets based on the used
	 * reasoner.
	 */
	private Map<Pair<String, Map<String, String>>, Set<Secret>> optimizationMap = 
			new HashMap<Pair<String, Map<String, String>>, Set<Secret>>();

	/** Default Ctor */
	public SecrecyKnowledge() {
		super();
	}

	/**
	 * Copy Ctor: The property listeners will be registered for each target and
	 * are not copied by secrets clone method
	 */
	public SecrecyKnowledge(SecrecyKnowledge other) {
		super(other);
		for (Secret ct : other.secrets) {
			Secret clone = (Secret) ct.clone();
			addSecret(clone);
		}
	}

	@Override
	public Object clone() {
		return new SecrecyKnowledge(this);
	}

	@Override
	public String toString() {
		String reval = "";
		for (Secret ct : secrets)
			reval += ct.toString() + "\n";
		return reval;
	}

	/**
	 * Adds a secret to the data storage.
	 * @param secret	the new secrecy which will be added to the
	 *            		secrecy knowledge.
	 * @return 			true if the belief base does not contain the secret,
	 *         			false otherwise.
	 */
	public boolean addSecret(Secret secret) {
		boolean reval = secrets.add(secret);
		if (reval) {
			addToMap(secret, secret.getPair());
			secret.addPropertyListener(this);
			LOG.info("Add secret '{}' to confidential knowledge of '{}'", secret,
					getAgent().getName());
		}
		return reval;
	}

	/**
	 * Removes the given secret from the secrecy knowledge
	 * @param secret	The secret which will be removed.
	 * @return	true if the secrecy knowledge conatined the secret and
	 * 			it is successfully removed, false otherwise.
	 */
	public boolean removeSecret(Secret secret) {
		boolean reval = secrets.remove(secret);
		if (reval) {
			removeFromMap(secret, secret.getPair());
			secret.removePropertyListener(this);
		}
		return reval;
	}

	/**
	 * Gets the secret defined by the subject and the information
	 * which is confidential.
	 * 
	 * @param subjectName 	name of the agent who should not get the confidential
	 *            			information
	 * @param information	the confidential piece of information.
	 * @return 				A secret which fulfills the given parameter 
	 * 						if such a secret exists in the secrecy knowledge, 
	 * 						null otherwise.
	 */
	public Secret getSecret(String subjectName, Formula information) {
		for (Secret ct : secrets)
			if (ct.getSubjectName().compareTo(subjectName) == 0
					&& ct.getInformation().equals(information))
				return ct;
		return null;
	}

	/** @return an unmodifiable set of secrets of the confidential knowledge */
	public Set<Secret> getTargets() {
		return Collections.unmodifiableSet(secrets);
	}

	/**
	 * @return an unmodifiable map. The key is a Pair describing the secrets
	 *         reasoner (class and parameters) and the values are sets of
	 *         secrets
	 */
	public Map<Pair<String, Map<String, String>>, Set<Secret>> getTargetsByReasoningOperator() {
		return Collections.unmodifiableMap(optimizationMap);
	}

	/**
	 * Sets the default handler if no custom event handler is set and loads/parses
	 * the data saved in the simulation configuration to fill the secrecy knowledge
	 * with secrets.
	 * @todo Find a solution for startup consistency check which does not alter the secrets...
	 */
	@Override
	public void init(Map<String, String> additionalData) {
		if(eventHandler == null) {
			setHandler(new DefaultHandler());
		}

		// if no initial data is set then output a warning:
		if (!additionalData.containsKey("Confidential")) {
			LOG.warn(
					"Secrecy Knowledge of agent '{}' has no initial data.",
					getAgent().getName());
			return;
		} else {
			// otherwise parse the secrets
			String str = additionalData.get("Confidential");
			SecretParser parser = new SecretParser(str);
			Set<Secret> secrets = null;
			try {
				secrets = (parser.Input());
			} catch (ParseException e) {
				LOG.error(
						"Cannot parse the secret defined for Agent '{}':\n{}",
						getAgent().getName(), e.getMessage());
			}

			// and add them to the secrecy knowledge
			for (Secret s : secrets) {	
				addSecret(s);
			}

			/* 
			 *  Check for startup inconsistency:
			beliefbaseChanged(getAgent().getBeliefs().getWorldKnowledge(),
					AgentListener.WORLD);
			for (String agName : getAgent().getBeliefs().getViewKnowledge()
					.keySet()) {
				beliefbaseChanged(getAgent().getBeliefs().getViewKnowledge()
						.get(agName), agName);
			}
			*/
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("reasonerParameters")) {

			Map<String, String> oldValue = (Map<String, String>) evt
					.getOldValue();
			Map<String, String> newValue = (Map<String, String>) evt
					.getNewValue();
			Secret secret = (Secret) evt.getSource();

			Pair<String, Map<String, String>> key = new Pair<String, Map<String, String>>(
					secret.getReasonerClassName(), new HashMap<String, String>(
							oldValue));
			removeFromMap(secret, key);

			key.second = new HashMap<String, String>(newValue);
			addToMap(secret, key);
			
			report("Secrecy knowledge adapted, Reasoner-Parameters changed from '" + oldValue + "' to '" + newValue + "'." );
		} else {
			// only one observed property...
			throw new RuntimeException(
					"Did you forgot to rename 'reasonerParameters' anywhere?");
		}
	}

	/**
	 * Helper method: 	Adds a secret to the correct position in the optimization
	 * 					map
	 * @param secret	reference to the secret
	 * @param key		reference to the used key (might be secret.getPair())
	 */
	private void addToMap(Secret secret, Pair<String, Map<String, String>> key) {
		if (!optimizationMap.containsKey(key)) {
			optimizationMap.put(key, new HashSet<Secret>());
		}
		optimizationMap.get(key).add(secret);
	}

	/**
	 * Helper method:
	 * 
	 * @param secret
	 * @param key
	 */
	private void removeFromMap(Secret secret, Pair<String, Map<String, String>> key) {
		Set<Secret> secrets = optimizationMap.get(key);
		if (secrets == null) {
			String error = "Something went wrong in event hierarchy. The searched secret key is not found in keyset of targetsByReasoningOperator map.\n";
			error += "\nKey: " + key.toString() + " - " + key.hashCode();
			error += "\nMap-Key-Set:\n";
			for (Pair<String, Map<String, String>> p : optimizationMap.keySet())
				error += p.toString() + " - " + p.hashCode() + " - "
						+ p.equals(key) + "\n";
			throw new RuntimeException(error);
		}
		secrets.remove(secret);
		LOG.info("Removed secret '{}' from Confidential Knowledge of '{}'", secret,
				getAgent().getName());
		if (secrets.size() == 0) {
			optimizationMap.remove(key);
			LOG.info(
					"Removed Key Pair '{}' from targetsByReasoningOperatorsMap of Agent '{}'",
					secret, getAgent().getName());
		}
	}
	
	/**
	 * Changes the strategy responsible for handling agent events.
	 * @param handler	A reference to the new strategy or null if
	 * 					the secrecy knowledge shall not response to 
	 * 					agent events.
	 */
	public void setHandler(AgentAdapter handler) {
		// Remove old handler from agent listeners.
		if(this.eventHandler != null) {
			getAgent().removeListener(this.eventHandler);
		}
		
		// Change handler and register as agent listener.
		this.eventHandler = handler;
		if(this.eventHandler != null) {
			getAgent().addListener(this.eventHandler);
		}
	}
	
	/**
	 * An agent event handling strategy which reacts on changes of the belief base. It
	 * adapts the reasoner parameter d for the secrets.
	 * 
	 * @author Tim Janus
	 */
	public class DefaultHandler extends AgentAdapter {
		@Override
		public void updateBeliefs(Perception percept, Beliefs oldBeliefs, Beliefs newBeliefs) {
			if(percept == null)
				return;
			
			EvaluateParameter param = new EvaluateParameter(getAgent(), oldBeliefs, percept);
			OperatorCallWrapper op = getAgent().getOperators().getPreferedByType(BaseViolatesOperator.OPERATION_NAME);
			ViolatesResult res = (ViolatesResult) op.process(param);
			
			for(Pair<Secret, Double> p : res.getPairs()) {
				if(p.second != 0) {
					for(Secret s : secrets) {
						if(s.alike(p.first)) {
							Map<String, String> map = s.getReasonerParameters();
							double oldD = Double.parseDouble(map.get("d"));
							double newD = oldD - p.second;
							map.put("d", new Double(newD).toString());
							s.setReasonerParameters(map);
						}
					}
				}
			}
		}
	}
}
