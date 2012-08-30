package angerona.fw;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.internal.PluginInstantiator;
import angerona.fw.parser.ParseException;
import angerona.fw.parser.SecretParser;
import angerona.fw.serialize.OperatorSetConfig;
import angerona.fw.util.Pair;

/**
 * A set of Operators of Type T. Also defining a default operator.
 * @author Tim Janus
 *
 * @param <T>	Real type of the operators.
 */
public class OperatorSet<T extends BaseOperator> {
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(OperatorSet.class);
	
	/** reference to the owner of the OperatorSet */
	private Agent owner;
	
	/** map from full java-class names to the right operator instances */
	private Map<String, T> operators = new HashMap<String, T>();
	
	/** reference to the current default operator */
	T defaultOperator;
	
	/** default ctor: Constructs an empty operator-set */
	public OperatorSet() {}
	
	/**
	 * Copy Ctor: Constructs an copy of the given operator set
	 * @param other		reference to the OperatorSet which will be copied.
	 */
	public OperatorSet(OperatorSet<T> other) {
		operators.putAll(other.operators);
		defaultOperator = other.defaultOperator;
	}
	
	/**
	 * Changes the used default operator to the given operator.
	 * @param clsName	The full-java class name of the next operator used as 
	 * 					default operator.
	 * @return			true if the operator with the given class name was found
	 * 					and is the new default-operator, false otherwise.
	 */
	public boolean setDefault(String clsName) {
		if(operators.containsKey(clsName)) {
			defaultOperator = operators.get(clsName);
			return true;
		}
		return false;
	}
	
	
	/** @return reference to the current default-operator. */
	public T def() {
		return defaultOperator;
	}
	
	/**
	 * Adds the given operator to the operator-set if no operator of this type
	 * is already in the set.
	 * Only the full-java-class-name is deciding, the operator might be another instance
	 * with another set of parameters but it will not be added to the set cause the types
	 * are equal.
	 * @param instantiationName		A string starting with the full java-class name of the type
	 * 								of the operator and having an optional map as parameter in 
	 * 								the form {d=1.0} as postfix.
	 * @return 	true if the operator was added and false if the operator was already in the set.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public boolean addOperator(String instantiationName) 
			throws InstantiationException, IllegalAccessException {
		Pair<String, T> p = create(instantiationName);
		if(p != null) {
			operators.put(p.first, p.second);
			p.second.setOwner(owner);
		}
		return p != null;
	}
	
	/**
	 * removes the given operator from the set.
	 * Only the full-java-class-name is deciding, the given operator might be another instance
	 * but if it is of the same type the operator in the set will be removed.
	 * @param operator	reference to the operator
	 * @return	true if the operator was removed, false otherwise (if it was not in the set for example).
	 */
	public boolean removeOperator(String clsName) {
		return operators.remove(clsName) != null;
	}
	
	/**
	 * reads the class used for serialization of the operator-set and creates the
	 * corrospodending runtime object.
	 * @param config	reference to the serialization object containg the raw-data 
	 * 					which are strings of java-class-names.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void set(OperatorSetConfig config) throws InstantiationException, IllegalAccessException {
		Pair<String, T> p = create(config.getDefaultClassName());
		operators.put(p.first, p.second);
		defaultOperator = p.second;
		if(defaultOperator == null) {
			LOG.error("The default operator with name: '{}' was not found, critical error.", 
					config.getDefaultClassName());
		}
		
		for(String instantiationName : config.getOperatorClassNames()) {
			p = create(instantiationName);
			
			if(p == null) {
				LOG.warn("The type '{}' was attempt to add twice to the operator-set and will be skiped this time. " +
						"Did you use the old syntax of the configuration files with mentions " +
						"the default-operator twice?", instantiationName);
			} else {
				operators.put(p.first, p.second);
			}
		}
	}

	/** 
	 * Helper method: Creates a class with the given classname.
	 * The method proofs if the class is a subclass of BaseOperator.
	 * It assumes that the Type of the class is T but there is no
	 * possibility in java to proof that.
	 * The method also creates the initial parameter map for the operator.
	 * @param instantiatonName	The full java-name of the class with the parameter 
	 * 							map appended in string representation.
	 * @return	A pair of the real java-class name and the reference to the newly created
	 * 			operator or null if the operator type is already registered.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Pair<String, T> create(String instantiatonName)
			throws InstantiationException, IllegalAccessException {
		PluginInstantiator pi = PluginInstantiator.getInstance();
		
		// TODO: Use a basic parser for stuff like that...
		SecretParser parser = new SecretParser(new StringReader(instantiatonName));
		Map<String, String> parameters = new HashMap<String, String>();
		try {
			instantiatonName = parser.java_cls(parameters);
		} catch (ParseException e) {
			throw new InstantiationException("Cannot parse clsName-Parameter: " + e.getMessage());
		}
		
		// return null if the operator of the type already exists in the set.
		if(operators.containsKey(instantiatonName)) {
			return null;
		}
		
		Object obj = pi.createInstance(instantiatonName);
		if(obj instanceof BaseOperator) {
			@SuppressWarnings("unchecked")
			T op = (T)obj;
			op.setParameters(parameters);
			return new Pair<String, T>(instantiatonName, op);
		} else {
			throw new InstantiationException(instantiatonName + " has not the correct type." );
		}
	}
	
	/**
	 * gets the operator with the given full-java-class name
	 * @param clsName		the full java-class name
	 * @return				reference to the operator or null if the operator of the given
	 * 						java-class name does not exists in the operator set.
	 */
	public T get(String clsName) {
		if(clsName.equals("__DEFAULT__")) {
			return defaultOperator;
		} else if(operators.containsKey(clsName)) {
			return operators.get(clsName);
		} else {
			return null;
		}
	}
	
	/**
	 * gets the operator with the given full-java-class name uses the default
	 * operator as alternative if the given class-name was not found.
	 * @param clsName		the full java-class name
	 * @return				A Pair containg the reference to the operator or the default operator 
	 * 						if the operator of the given java-class name does 
	 * 						not exists in the operator set. The second Element contains a
	 * 						boolean which is true if the given class-name was found and
	 * 						false otherwise.
	 */
	public Pair<T, Boolean> getFallback(String clsName) {
		Pair<T, Boolean> reval = new Pair<>();
		reval.first = get(clsName);
		reval.second = reval.first != null;
		if(!reval.second)
			reval.first = def();
		return reval;
	}
	
	/**
	 * Changes the owner of all operators in the set to the given agent.
	 * @param owner		reference to the new owner.
	 */
	public void setOwner(Agent owner) {
		this.owner = owner;
		for(BaseOperator op : operators.values()) {
			op.setOwner(owner);
		}
	}
}
