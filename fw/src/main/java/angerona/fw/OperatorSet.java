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
 * belief base. It is structured into multiple operation-sets. An operation-set contains
 * operators which perform the same operation. Thus means the operators have the same type
 * of input and output parameters.
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
	 * Adds the given operator to the operator-set.
	 * @param clsNameAndParams		A string starting with the full java-class name of the type
	 * 								of the operator and having an optional map as parameter in 
	 * 								the form {d=1.0} as postfix.
	 * @return 	true if the operator was added and false if an error occurred.
	 */
	public Pair<String, BaseOperator> addOperator(String clsNameAndParams) {
		Pair<String, BaseOperator> p = null;
		try {
			p = fetch(clsNameAndParams);
		} catch (ClassNotFoundException e) {
			LOG.error("Cannot add operator '{}' because it cannot be found: '{}'", clsNameAndParams, e.getMessage());
			return null;
		} catch (ParseException e) {
			LOG.error("Cannot add operator '{}' because of Parser error: '{}'", clsNameAndParams, e.getMessage());
			return null;
		}
		realAdd(p);
		return p;
	}
	
	/**
	 * adds the given pair to the map of operators and
	 * sets the owner.
	 * @param p	the pair to add (null means do nothing)
	 */
	private void realAdd(Pair<String, BaseOperator> p) {
		if(p != null) {
			operators.put(p.first, p.second);
			String opName = p.second.getOperationType().first;
			if(!operatorsByOperationType.containsKey(opName)) {
				operatorsByOperationType.put(opName, new OperationSet(opName));
			}
			operatorsByOperationType.get(opName).addOperator(p.second);
		}
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
	 * Uses the XML serialization class OperationSetConfig to add a new operation set to the
	 * set of operators.
	 * @param config	reference to the serialization object containing the raw-data 
	 * 					which are strings of java-class-names.
	 */
	public boolean addOperationSet(OperationSetConfig config) {
		OperationSet os = getOperationSetByType(config.getOperationType());
		if(os != null) {
			LOG.warn("The operation-set of type '{}' already exists.", os.getOperationName());
		}
		
		Pair<String, BaseOperator> defPair = addOperator(config.getDefaultClassName());
		BaseOperator def = defPair.second;
		if(def == null) {
			LOG.error("Default operator for '{}' cannot be added.", config.getOperationType());
			return false;
		}
		if(!def.getOperationType().first.equals(config.getOperationType())) {
			LOG.error("The operation type in the configuration file: '{}' " +
					"does not match the operation type of the default operator: '{}'", 
					config.getOperationType(), def.getOperationType().first);
			return false;
		}
		os = getOperationSetByType(config.getOperationType());
		os.setPrefered(defPair.first);
		
		// add alternative operators:
		for(String clsNameAndParams : config.getOperatorClassNames()) {
			addOperator(clsNameAndParams);
		}
		return true;
	}

	/** 
	 * Helper method: Fetches a class with the given classname.
	 * The method proofs if the class is a subclass of BaseOperator.
	 * The method also creates the initial parameter map for the operator.
	 * @param clsNameAndParams	The full java-name of the class with the parameter 
	 * 							map appended in string representation.
	 * @return	A pair of the real java-class name and the reference to the newly created
	 * 			operator or null if the operator type is already registered.
	 * @throws ParseException
	 * @throws ClassNotFoundException
	 */
	private Pair<String, BaseOperator> fetch(String clsNameAndParams)
		throws ParseException, ClassNotFoundException {
		PluginInstantiator pi = PluginInstantiator.getInstance();
		
		// TODO: Use a basic parser for stuff like that...
		SecretParser parser = new SecretParser(new StringReader(clsNameAndParams));
		Map<String, String> parameters = new HashMap<String, String>();
		clsNameAndParams = parser.java_cls(parameters);
		
		// return null if the operator of the type already exists in the set.
		if(operators.containsKey(clsNameAndParams)) {
			return null;
		}
		
		BaseOperator op = pi.getOperator(clsNameAndParams);
		if(op == null)
			throw new ClassNotFoundException(clsNameAndParams);
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
