package angerona.fw.gui.component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import angerona.fw.BaseOperator;
import angerona.fw.OperatorSet;

/**
 * 
 * @author Tim Janus
 *
 * @param <T>
 */
public class OperatorConfig extends AbstractModel {
	/** the original parameters of the operator */
	private Map<String, String> originalParameters = new HashMap<>();
	
	/** the parameters typed in by the user */
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/** the set of selectable operators */
	private OperatorSet operationSet;
	
	/** the currently selected operator */
	private BaseOperator selectedOperator;
	
	public OperatorConfig(OperatorSet set) {
		if(set == null)
			throw new IllegalArgumentException();
		
		this.operationSet = set;
		selectedOperator = set.getPreferred();
		originalParameters = selectedOperator.getParameters();
		parameters = new HashMap<>(selectedOperator.getParameters());
	}
	
	BaseOperator getSelectedOperator() {
		return selectedOperator;
	}
	
	void setSelectedOperator(BaseOperator operator) {
		firePropertyChange("selectedOperator", this.selectedOperator, 
				operator);
		this.selectedOperator = operator;
	}
	
	Map<String, String> getOriginalParameters() {
		return Collections.unmodifiableMap(originalParameters);
	}
	
	Map<String, String> getParameters() {
		return new HashMap<>(parameters);
	}
	
	void setParameters(Map<String, String> parameters) {
		firePropertyChange("parameters", this.parameters, parameters);
		this.parameters = parameters;
	}
	
	Collection<BaseOperator> getSelectableOperators() {
		return operationSet.getOperators();
	}
	
	public BaseOperator getDefaultOperator() {
		return operationSet.getPreferred();
	}
}
