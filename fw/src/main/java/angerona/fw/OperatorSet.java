package angerona.fw;

import java.util.HashMap;
import java.util.Map;

import angerona.fw.internal.PluginInstantiator;
import angerona.fw.serialize.OperatorSetConfig;

/**
 * A set of Operators of Type T. Also defining a default operator.
 * @author Tim Janus
 *
 * @param <T>	Real type of the operators.
 */
public class OperatorSet<T extends BaseOperator> {
	private Map<String, T> operators = new HashMap<String, T>();
	
	T defaultOperator;
	
	public OperatorSet() {
		
	}
	
	public OperatorSet(OperatorSet<T> other) {
		operators.putAll(other.operators);
		defaultOperator = other.defaultOperator;
	}
	
	public void setDefault(T operator) {
		String clsName = operator.getClass().getName();
		if(!operators.containsKey(clsName)) {
			operators.put(clsName, operator);
		}
	}
	
	public T getDefault() {
		return defaultOperator;
	}
	
	public void addOperator(T operator) {
		if(operator == null)
			throw new IllegalArgumentException("operator must not be null");
		
		String clsName = operator.getClass().getName();
		operators.put(clsName, operator);
	}
	
	public boolean removeOperator(T operator) {
		if(operator == null)
			throw new IllegalArgumentException("operator must not be null");
		
		String clsName = operator.getClass().getName();
		return operators.remove(clsName) != null;
	}
	
	@SuppressWarnings("unchecked")
	public void set(OperatorSetConfig config) throws InstantiationException, IllegalAccessException {
		PluginInstantiator pi = PluginInstantiator.getInstance();
		for(String clsName : config.getOperatorClassNames()) {
			Object obj = pi.createInstance(clsName);
			if(obj instanceof BaseOperator) {
				operators.put(clsName, (T)obj);
			}
		}
		
		String clsName = config.getDefaultClassName();
		if(!operators.containsKey(clsName)) {
			throw new IllegalArgumentException(clsName + " used as default operator but not referenced in the set.");
		}
		defaultOperator = operators.get(clsName);
	}
	
	public T get(String clsName) {
		if(clsName.equals("__DEFAULT__")) {
			return defaultOperator;
		} else if(operators.containsKey(clsName)) {
			return operators.get(clsName);
		} else {
			return null;
		}
	}
	
	public void setOwner(Agent owner) {
		for(BaseOperator op : operators.values()) {
			op.setOwner(owner);
		}
	}
}
