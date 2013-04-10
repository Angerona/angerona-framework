package angerona.fw.gui.component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import angerona.fw.OperatorSet;
import angerona.fw.operators.BaseOperator;
import angerona.fw.util.ModelAdapter;

/**
 * 
 * @author Tim Janus
 *
 * @param <T>
 */
public class OperatorConfig extends ModelAdapter {
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
		selectedOperator = changeProperty("selectedOperator", selectedOperator, operator);
	}
	
	Map<String, String> getOriginalParameters() {
		return Collections.unmodifiableMap(originalParameters);
	}
	
	Map<String, String> getParameters() {
		return new HashMap<>(parameters);
	}
	
	void setParameters(Map<String, String> parameters) {
		this.parameters = changeProperty("parameters", this.parameters, parameters);
	}
	
	Collection<BaseOperator> getSelectableOperators() {
		return operationSet.getOperators();
	}
	
	public BaseOperator getDefaultOperator() {
		return operationSet.getPreferred();
	}
}
