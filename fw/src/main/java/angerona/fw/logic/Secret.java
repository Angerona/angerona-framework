package angerona.fw.logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.util.Pair;

/**
 * A secret as defined Def. 4 in "Agent-based Epistemic Secrecy" 
 * of Krümpelmann and Kern-Isberner.
 * The belief/reasoning-operator is not dynamically yet, but the
 * reasoning operator linked to the used knowledge base is used.
 * 
 * For example:
 * We have the agents Alice, Bob and Claire. And Bob does not want
 * his wife Alice to know that he has an affair with Claire. Also
 * he does not want Claire to know that he has children.
 * 
 * 
 * @author Tim Janus
 */
public class Secret implements Cloneable {
	public static final String DEFAULT_BELIEFCHANGE = "__DEFAULT__";
	
	/** name of the agent who should not get the information */
	private String name;
	
	/** formula representing the confidential information */
	private FolFormula information;
	
	/** the java class name which should be used to proof this secret */
	private String reasonerClass;
	
	/** a map containing parameters for the used reasoner class */
	private Map<String, String> reasonerParameters;
	
	private List<PropertyChangeListener> listeners = new LinkedList<PropertyChangeListener>();
	
	/** 
	 * adds a property listener to the secret 
	 * @param listener	reference to the instance representing the new property-listener.
	 * @return true 
	 * */
	public boolean addPropertyListener(PropertyChangeListener listener) {
		return listeners.add(listener);
	}
	
	/**
	 * removes an existing property listener from the secret
	 * @param listener reference to the listener representing the property-listener.
	 * @return	true if the Secret contained the specified listener.
	 */
	public boolean removePropertyListener(PropertyChangeListener listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Helper method: Invokes all property listeners with the given arguments.
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	protected void invokePropertyListener(String propertyName, Object oldValue, Object newValue) {
		for(PropertyChangeListener l : listeners) {
			l.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
		}
	}
	
	/** Copy Ctor: Does not copy the listeners into the clone */
	public Secret(Secret other) {
		name = other.name;
		information = other.information;
		reasonerClass = other.reasonerClass;
		reasonerParameters = new HashMap<String, String>(other.reasonerParameters);
	}
	
	/**
	 * Ctor:
	 * @param name			name of the agent
	 * @param information	formula representing the secret
	 * @param reasonerClass	name of the used reasoner-class
	 */
	public Secret(String name, FolFormula information, String reasonerClass) {
		this(name, information, reasonerClass, new HashMap<String, String>());
	}
	
	/**
	 * Ctor:
	 * @param name			name of the agent
	 * @param information	formula representing the secret
	 * @param reasonerClass	name of the used reasoner-class
	 * @param parameters	map containing the parameters used to invoke the reasoner-class.
	 */
	public Secret(String name, FolFormula information,
			String reasonerClass, Map<String, String> parameters) {
		this.name = name;
		this.information = information;
		this.reasonerClass = reasonerClass;
		this.reasonerParameters = parameters;
	}
	
	/**	@return name of the agent who should not get the information */
	public String getSubjectName() {
		return name;
	}
	
	/** @return formula representing the confidential information */
	public FolFormula getInformation() {
		return information;
	}
	
	/** 
	 * @return 	the full java class name which should be used for revision 
	 *			might be DEFAULT_CHANGEBELIEF with the meaning the default
	 *			change operator of the beliefbase should be used.
	 */
	public String getReasonerClassName() {
		return reasonerClass;
	}
	
	/**
	 * @return  a copy of the map containing the reasoner parameters because its a copy changes did not affect
	 * 			the event system.
	 */
	public Map<String, String> getReasonerSettings() {
		return new HashMap<>(reasonerParameters);
	}
	
	/**
	 * @return a pair containing the reasoner class name and the parameters map (copied).
	 */
	public Pair<String, Map<String, String>> getPair() {
		return new Pair<String, Map<String, String>>(reasonerClass, 
				new HashMap<String, String>(reasonerParameters));
	}
	
	/**
	 * changes the reasoner parameters. The change is invoked to interested PropertyListeners like the
	 * ConfidentialKnowledge for example.
	 * @param parameters	A map containing the parameters for the reasoner.
	 */
	public void setReasonerSettings(Map<String, String> parameters) {
		Map<String, String> old = new HashMap<>(reasonerParameters);
		reasonerParameters = parameters;
		invokePropertyListener("reasonerParameters", 
				old, 
				parameters);
	}
	
	public void setReasonerClassName(String clsName) {
		if(clsName.hashCode() != reasonerClass.hashCode()) {
			String old = reasonerClass;
			reasonerClass = clsName;
			invokePropertyListener("reasonerClass", old, reasonerClass);
		}
	}
	
	/**
	 * Tests if two secrets are alike. That means they have the same information
	 * and agent who is not allowed to unravel the information. The used reasoner
	 * and its parameters are not important for this method.
	 * @param other		Reference to the other secret object for the alike-test.
	 * @return			true if the both objects are alike, false otherwise.
	 */
	public boolean alike(Secret other) {
		if(!this.information.equals(other.information))
			return false;
		if(!this.name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public Object clone() {
		return new Secret(this);
	}
	
	@Override
	public String toString() {
		return "(" + name + "," + information +  "," + reasonerClass + "(" + reasonerParameters +  "))";
	}
}
