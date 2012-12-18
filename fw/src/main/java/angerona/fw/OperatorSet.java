package angerona.fw;

import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.internal.PluginInstantiator;
import angerona.fw.parser.ParseException;
import angerona.fw.parser.SecretParser;
import angerona.fw.serialize.OperationSetConfig;
import angerona.fw.util.Pair;

/**
 * A set of operators for an entity which calls operators like the agent or the
 * belief base. 
 *
 * @author Tim Janus
 */
public class OperatorSet {
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(OperatorSet.class);
	
	/** map from full java-class names to the right operator instances */
	protected Map<String, BaseOperator> operators = new HashMap<String, BaseOperator>();
	
	protected Map<String, OperationSet> operatorsByOperationType = new HashMap<>();
	
	/** default ctor: Constructs an empty operator-set */
	public OperatorSet() {}
	
	/**
	 * Copy Ctor: Constructs an copy of the given operator set
	 * @param other		reference to the OperatorSet which will be copied.
	 */
	public OperatorSet(OperatorSet other) {
		operators.putAll(other.operators);
		operatorsByOperationType.putAll(other.operatorsByOperationType);
	}
	
	
	/**
	 * Adds the given operator to the operator-set if no operator of this type
	 * is already in the set.
	 * Only the full-java class name is deciding, the operator might be another instance
	 * with another set of parameters but it will not be added to the set cause the types
	 * are equal.
	 * @param clsNameAndParams		A string starting with the full java-class name of the type
	 * 								of the operator and having an optional map as parameter in 
	 * 								the form {d=1.0} as postfix.
	 * @return 	true if the operator was added and false if the operator was already in the set.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public boolean addOperator(String clsNameAndParams) 
			throws InstantiationException, IllegalAccessException {
		Pair<String, BaseOperator> p = fetch(clsNameAndParams);
		return realAdd(p);
	}
	
	/**
	 * adds the given pair to the map of operators and
	 * sets the owner.
	 * @param p	the pair to add (null means do nothing)
	 * @return	p!=null
	 */
	private boolean realAdd(Pair<String, BaseOperator> p) {
		if(p != null) {
			operators.put(p.first, p.second);
			String opName = p.second.getOperationType().first;
			if(!operatorsByOperationType.containsKey(opName)) {
				operatorsByOperationType.put(opName, new OperationSet(opName));
			}
			operatorsByOperationType.get(opName).addOperator(p.second);

		}
		return p != null;
	}
	
	/**
	 * removes the given operator from the set.
	 * Only the full-java-class-name is deciding, the given operator might be another instance
	 * but if it is of the same type the operator in the set will be removed.
	 * @param clsName	The fully qualified java class name of the operator.
	 * @return	true if the operator was removed, false otherwise (if it was not in the set for example).
	 */
	public boolean removeOperator(String clsName) {
		boolean reval = operators.remove(clsName) != null;
		if(reval) {
			for(OperationSet set : this.operatorsByOperationType.values()) {
				set.removeOperator(clsName);
			}
		}
		return reval;
	}
	
	/**
	 * reads the class used for serialization of the operator-set and creates the
	 * corrospodending runtime object.
	 * @param operationType	The unique name of the operation type for identifing the operation.
	 * @param config	reference to the serialization object containg the raw-data 
	 * 					which are strings of java-class-names.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void addOperationSet(String operationType, OperationSetConfig config) 
			throws InstantiationException, IllegalAccessException {
		
		/* TODO For parsing
		
		if(!config.getDefaultClassName().equalsIgnoreCase("empty")) {
			p = fetch(config.getDefaultClassName());
			operators.put(p.first, p.second);
			defaultOperator = p.second;
			if(defaultOperator == null) {
				LOG.error("The default operator with name: '{}' was not found, critical error.", 
						config.getDefaultClassName());
			}
		}
		
		for(String instantiationName : config.getOperatorClassNames()) {
			if(!instantiationName.equalsIgnoreCase("empty"))
				continue;
			p = fetch(instantiationName);
			
			if(p == null) {
				LOG.warn("The type '{}' was attempt to add twice to the operator-set and will be skiped this time. " +
						"Did you use the old syntax of the configuration files with mentions " +
						"the default-operator twice?", instantiationName);
			} else {
				operators.put(p.first, p.second);
			}
		}
		*/
	}

	/** 
	 * Helper method: Fetches a class with the given classname.
	 * The method proofs if the class is a subclass of BaseOperator.
	 * The method also creates the initial parameter map for the operator.
	 * @param clsNameAndParams	The full java-name of the class with the parameter 
	 * 							map appended in string representation.
	 * @return	A pair of the real java-class name and the reference to the newly created
	 * 			operator or null if the operator type is already registered.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Pair<String, BaseOperator> fetch(String clsNameAndParams)
			throws InstantiationException, IllegalAccessException {
		PluginInstantiator pi = PluginInstantiator.getInstance();
		
		// TODO: Use a basic parser for stuff like that...
		SecretParser parser = new SecretParser(new StringReader(clsNameAndParams));
		Map<String, String> parameters = new HashMap<String, String>();
		try {
			clsNameAndParams = parser.java_cls(parameters);
		} catch (ParseException e) {
			throw new InstantiationException("Cannot parse clsName-Parameter: " + e.getMessage());
		}
		
		// return null if the operator of the type already exists in the set.
		if(operators.containsKey(clsNameAndParams)) {
			return null;
		}
		
		BaseOperator op = pi.getOperator(clsNameAndParams);
		op.setParameters(parameters);
		return new Pair<String, BaseOperator>(clsNameAndParams, op);
	}
	
	public OperationSet getOperationSetByType(String operationType) {
		if(operatorsByOperationType.containsKey(operationType)) {
			return operatorsByOperationType.get(operationType);
		}
		return null;
	}
	
	public BaseOperator getPreferedByType(String operationType) {
		OperationSet set = getOperationSetByType(operationType);
		return set != null ? set.getPrefered() : null;
	}
	
	public Collection<BaseOperator> getOperators() {
		return Collections.unmodifiableCollection( operators.values() );
	}
}
