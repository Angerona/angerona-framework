package angerona.fw;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OperationSet {
	private String operationName;
	
	/** maps the full java class name to the operator instance */
	private Map<String, BaseOperator> operators = new HashMap<>();
	
	/** the preferred operator */
	private BaseOperator preferredOperator;
	
	public OperationSet(String name) {
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
	
	public BaseOperator getPrefered() {
		return preferredOperator;
	}
	
	public Collection<BaseOperator> getOperators() {
		return Collections.unmodifiableCollection(operators.values());
	}
	
	public void addOperator(BaseOperator operator) {
		if(operator.getOperationType().first.equals(operationName)) {
			operators.put(operator.getClass().getName(), operator);
		}
	}
	
	public boolean removeOperator(String fullJavaClsName) {
		if(operators.containsKey(fullJavaClsName)) {
			operators.remove(fullJavaClsName);
			return true;
		}
		return false;
	}
}
