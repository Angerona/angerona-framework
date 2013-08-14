package angerona.fw.am.secrecy.components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.OperatorSet;
import angerona.fw.Perception;
import angerona.fw.listener.AgentAdapter;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.Secret;
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
	 * calls because it defines a sort on the secrets based on the used
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
	public SecrecyKnowledge clone() {
		return new SecrecyKnowledge(this);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Secret ct : secrets)
			buf.append(ct.toString() + "\n");
		return buf.toString();
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
	public Set<Secret> getSecrets() {
		return Collections.unmodifiableSet(secrets);
	}

	public Set<Secret> getSecretsBySubject(String agentId) {
		Set<Secret> reval = new HashSet<>();
		for(Secret s : secrets) {
			if(s.getSubjectName().equals(agentId)) {
				reval.add(s);
			}
		}
		return reval;
	}
	
	/**
	 * @return an unmodifiable map. The key is a Pair describing the secrets
	 *         reasoner (class and parameters) and the values are sets of
	 *         secrets
	 */
	public Map<Pair<String, Map<String, String>>, Set<Secret>> getSecretsByReasoningOperator() {
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

			if(secrets != null) {
				// and add them to the secrecy knowledge
				for (Secret s : secrets) {	
					addSecret(s);
				}
			}

			
			//  Check for startup inconsistency:
			this.eventHandler.updateBeliefs(null, getAgent().getBeliefs(), getAgent().getBeliefs());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("reasonerClass")) {
			String old = (String) evt.getOldValue();
			String newValue = (String) evt.getNewValue();
			Secret secret = (Secret) evt.getSource();

			Pair<String, Map<String, String>> key = new Pair<String, Map<String, String>>(
					old, secret.getReasonerSettings());
			removeFromMap(secret, key);
			
			key.first = newValue;
			addToMap(secret, key);
			
			report("Secrecy knowledge adapted, Reasoner-Class changed from '" + old + "' to '" + newValue + "'." );
		} else if (evt.getPropertyName().equals("reasonerParameters")) {

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
					"Did you forgot to rename 'reasonerParameters' anywhere? Current Name: '" + evt.getPropertyName() + "'.");
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
	 * Removes the given secret from the optimization map. If this is the last
	 * secret using the given belief operator the key containing belief operator
	 * and secrets is also removed.
	 * 
	 * @param secret
	 * @param key
	 * 
	 * @throw {@link RuntimeException} if something went wrong internally causing
	 * 			the OptimizationMap to lost it sychronism.
	 */
	private void removeFromMap(Secret secret, Pair<String, Map<String, String>> key) {
		Set<Secret> secrets = optimizationMap.get(key);
		
		// Check if the set of secrets exists, if not something went wrong in
		// the internals of this class instance
		if (secrets == null) {
			String error = "Something went wrong in event hierarchy. The searched secret key is not found in keyset of targetsByReasoningOperator map.\n";
			error += "\nKey: " + key.toString() + " - " + key.hashCode();
			error += "\nMap-Key-Set:\n";
			StringBuffer buf = new StringBuffer();
			for (Pair<String, Map<String, String>> p : optimizationMap.keySet()) {
				buf.append(p.toString());
				buf.append(" - ");
				buf.append(p.hashCode());
				buf.append(" - ");
				buf.append(p.equals(key));
				buf.append("\n");
			}
			error += buf.toString();
			throw new RuntimeException(error);
		}
		
		// remove the secret from the the value set of the optimization map
		secrets.remove(secret);
		LOG.info("Removed secret '{}' from SecrecyKnowledge of '{}'", secret,
				getAgent().getName());
		
		// Remove key from optimization map if the linked set of secrets is empty now
		if (secrets.size() == 0) {
			optimizationMap.remove(key);
			LOG.trace(
					"Removed Key Pair '{}' from optimization map of SecrecyKnowlege of Agent '{}'",
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
			// Get Parameters and Components
			EvaluateParameter param = new EvaluateParameter(getAgent(), oldBeliefs, percept);
			param.report("Check the Secrecy Consistence");
			SecrecyKnowledge sk = getAgent().getComponent(SecrecyKnowledge.class);
			
			// Iterate over each view belief base.
			for(Entry<String, BaseBeliefbase> entry : newBeliefs.getViewKnowledge().entrySet()) {
				// get the old and new version of the belief base and create a inference cache:
				BaseBeliefbase newer = entry.getValue();
				BaseBeliefbase older = oldBeliefs.getViewKnowledge().get(entry.getKey());
				Map<Pair<String, Map<String, String>>, Set<FolFormula>> cache = new HashMap<>();
				
				// only operate if the hash code of the old and new version are different
				if(newer.hashCode() != older.hashCode()) {
					
					// iterate over every secret corresponding the view:
					Set<Secret> secrets = sk.getSecretsBySubject(entry.getKey());
					for(Secret s : secrets) {
						boolean furtherTests = true;
						boolean changed = false;
						String clsName = s.getReasonerClassName();
						Map<String, String> settings = s.getReasonerSettings();
						
						OperatorSet os = newer.getOperators().getOperationSetByType(BaseReasoner.OPERATION_TYPE);
						OperatorCallWrapper current = os.getOperator(clsName);
						current.setSettings(settings);
						
						// find next operator that keeps the secret safe:
						while(furtherTests) {
							Set<FolFormula> beliefSet;
							Pair<String, Map<String, String>> p = new Pair<>(clsName, settings);

							LOG.info("Test Secrets with d='{}'.", settings.get("d") );
							// calculate or use cache version of belief set
							if(cache.containsKey(p)) {
								beliefSet = cache.get(p);
							} else {
								beliefSet = newer.infere(current);
								cache.put(p, beliefSet);
							}
							
							// update variables for next step
							furtherTests = beliefSet.contains(s.getInformation());
							if(furtherTests) {
								changed = true;
								current = older.getBeliefOperatorFamily().getPredecessor(current);
								if(current == null) {
									changed = false;
									break;
								}
								clsName = current.getImplementation().getClass().getName();
								settings = current.getSettings();
							}
						}
						
						// update the belief operator of the secret:
						if(changed) {
							s.setReasonerClassName(clsName);
							s.setReasonerParameters(settings);
						}
					}
				}
			}
		}
	}
}
