package angerona.fw.gui.component;

import java.util.Collection;
import java.util.Map;

import angerona.fw.BaseOperator;
import angerona.fw.OperatorSet;

/**
 * 
 * @author Tim Janus
 *
 * @param <T>
 */
public class OperatorConfig<T extends BaseOperator> extends AbstractModel {
	/** the original parameters of the operator */
	private Map<String, String> originalParameters;
	
	/** the parameters typed in by the user */
	private Map<String, String> parameters;
	
	/** the set of selectable operators */
	private OperatorSet<T> operatorSet;
	
	/** the currently selected operator */
	private T selectedOperator;
	
	public OperatorConfig(OperatorSet<T> set) {
		if(set == null)
			throw new IllegalArgumentException();
		
		this.operatorSet = set;
		selectedOperator = set.def();
		originalParameters = selectedOperator.getParameters();
	}
	
	T getSelectedOperator() {
		return selectedOperator;
	}
	
	void setSelectedOperator(T operator) {
		firePropertyChange("selectedOperator", this.selectedOperator, 
				operator);
		this.selectedOperator = operator;
	}
	
	Map<String, String> getOriginalParameters() {
		return originalParameters;
	}
	
	Map<String, String> getParameters() {
		return parameters;
	}
	
	void setParameters(Map<String, String> parameters) {
		firePropertyChange("parameters", this.parameters, parameters);
		this.parameters = parameters;
	}
	
	Collection<T> getSelectableOperators() {
		return operatorSet.getOperators();
	}
	
	public T getDefaultOperator() {
		return operatorSet.def();
	}
}
