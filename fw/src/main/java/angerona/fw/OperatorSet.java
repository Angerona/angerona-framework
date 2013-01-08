package angerona.fw;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A operator set is a set of operators which implement the same operation type.
 * This means the input and output parameter so the operators are the same.
 * Every OperatorSet saves the unique operation name which is a string identifying
 * the operation type. So it maps to the used input and output parameters.
 * @author Tim Janus
 */
public class OperatorSet {
	/** the unique name of the operation type which is implemented by operators saved in this set */
	private String operationName;
	
	/** maps the full java class name to the operator instance */
	private Map<String, BaseOperator> operators = new HashMap<>();
	
	/** the preferred operator */
	private BaseOperator preferredOperator;
	
	public OperatorSet(String name) {
		this.operationName = name;
	}
	
	public String getOperationName() {
		return operationName;
	}
	
	/**
	 * Changes the prefered operator to the given operator.
	 * @param fullJavaClsName	The full-java class name of the next operator used as 
	 * 							prefered operator.
	 * @return			true if the operator with the given class name was found
	 * 					and is the new default-operator, false otherwise.
	 */
	public boolean setPrefered(String fullJavaClsName) {
		if(operators.containsKey(fullJavaClsName)) {
			preferredOperator = operators.get(fullJavaClsName);
			return true;
		}
		return false;
	}
	
	public BaseOperator getOperator(String fullJavaClsName) {
		if(operators.containsKey(fullJavaClsName)) {
			return operators.get(fullJavaClsName);
		}
		return null;
	}
	
	/**
	 * @return the preferred operator
	 */
	public BaseOperator getPreferred() {
		return preferredOperator;
	}
	
	public Collection<BaseOperator> getOperators() {
		return Collections.unmodifiableCollection(operators.values());
	}
	
	/**
	 * Adds the given operator to the operator set if the operator implmeents the same
	 * operation type as the set provides.
	 * @param operator	Reference to the operator which shall be added.
	 * @return	true if the operator is successfully added, false if the operation type 
	 * 			of the set and the given operator mismatches.
	 */
	public boolean addOperator(BaseOperator operator) {
		if(operator.getOperationType().first.equals(operationName)) {
			operators.put(operator.getClass().getName(), operator);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the operator with the given java class name from the operator set.
	 * @param fullJavaClsName	The full java class name of the operator which shall be rmoved.
	 * @return	true if operator with the given class name is removed, false if it is not in the set.
	 */
	public boolean removeOperator(String fullJavaClsName) {
		if(operators.containsKey(fullJavaClsName)) {
			operators.remove(fullJavaClsName);
			return true;
		}
		return false;
	}
}
