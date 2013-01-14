package angerona.fw.logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.listener.AgentListener;
import angerona.fw.parser.ParseException;
import angerona.fw.parser.SecretParser;
import angerona.fw.util.Pair;

/**
 * Data component of an agent containing a set of personal secrets.
 * 
 * @author Tim Janus
 */
public class SecrecyKnowledge extends BaseAgentComponent implements
		AgentListener, PropertyChangeListener {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(SecrecyKnowledge.class);

	/** set of secrets */
	private Set<Secret> secrets = new HashSet<Secret>();

	/**
	 * This map is used for optimization purposes. It contains Sets of secrets
	 * as values and it keys are pairs describing the used Reasoner (ClassName,
	 * ParamterMap). This map made it easy to use a minimal amount of infere
	 * calls to because it defines a sort on the secrets based on the used
	 * reasoner.
	 */
	private Map<Pair<String, Map<String, String>>, Set<Secret>> optimizationMap = new HashMap<Pair<String, Map<String, String>>, Set<Secret>>();

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
	 * adds secrets to the data storage.
	 * 
	 * @param cf
	 *            the new confidential target which will be added to the
	 *            beliefbase.
	 * @return true if the beliefbase didn't contain the confidential target,
	 *         false otherwise.
	 */
	public boolean addSecret(Secret cf) {
		boolean reval = secrets.add(cf);
		if (reval) {
			addToMap(cf, cf.getPair());
			cf.addPropertyListener(this);
			LOG.info("Add secret '{}' to confidential knowledge of '{}'", cf,
					getAgent().getName());
		}
		return reval;
	}

	public boolean removeSecret(Secret cf) {
		boolean reval = secrets.remove(cf);
		if (reval) {
			removeFromMap(cf, cf.getPair());
			cf.removePropertyListener(this);
		}
		return reval;
	}

	/**
	 * Gets the secret defined by the subject and the information
	 * which is confidential.
	 * 
	 * @param subjectName
	 *            name of the agent who should not get the confidential
	 *            information
	 * @param information
	 *            the confidential information itself.
	 * @return A confidential target if it exists, null otherwise.
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

	@Override
	public void init(Map<String, String> additionalData) {
		getAgent().addListener(this);

		if (!additionalData.containsKey("Confidential")) {
			LOG.warn(
					"Secrecy Knowledge of agent '{}' has no initial data.",
					getAgent().getName());
			return;
		} else {
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

			FolSignature sig = getAgent().getBeliefs().getSignature();
			for (Secret s : secrets) {
				if (!sig.isRepresentable(s.getInformation())) {
					LOG.info(
							"Secret '{}' is not representable by the agents '{}' beliefs signature yet.",
							s, getAgent().getName());
				}
				addSecret(s);
			}

			/* TODO: Find a solution for startup consistency check which does not alter the secrets...
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

	@Override
	public void beliefbaseChanged(BaseBeliefbase bb, Perception percept, String space) {
		if (!space.equals(AgentListener.WORLD)) {
			if(percept == null)
				return;
			// TODO: Find a better concept for the information flow.
			ViolatesResult res = percept.violates();
			if(res == null)
				return;
			
			for(Pair<Secret, Double> p : res.getPairs()) {
				if(p.second != 0) {
					for(Secret s : this.secrets) {
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

	@Override
	public void componentAdded(BaseAgentComponent comp) {
		// does nothing
	}

	@Override
	public void componentRemoved(BaseAgentComponent comp) {
		// does nothing
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
			
			report("Confidential knowledge adapted, Reasoner-Parameters changed from '" + oldValue + "' to '" + newValue + "'." );
		} else {
			// only one observed property...
			throw new RuntimeException(
					"Did you forgot to rename 'reasonerParameters' everywhere?");
		}
	}

	/**
	 * Helper method: Adds a secret to the correct position in the optimization
	 * map
	 * 
	 * @param secret
	 *            reference to the secret
	 * @param key
	 *            reference to the used key (might be secret.getPair())
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
	 * @param cf
	 * @param key
	 */
	private void removeFromMap(Secret cf, Pair<String, Map<String, String>> key) {
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
		secrets.remove(cf);
		LOG.info("Removed secret '{}' from Confidential Knowledge of '{}'", cf,
				getAgent().getName());
		if (secrets.size() == 0) {
			optimizationMap.remove(key);
			LOG.info(
					"Removed Key Pair '{}' from targetsByReasoningOperatorsMap of Agent '{}'",
					cf, getAgent().getName());
		}
	}
}
