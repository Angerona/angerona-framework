package com.github.angerona.fw.gui.component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.angerona.fw.operators.BaseOperator;
import com.github.angerona.fw.operators.OperatorCallWrapper;
import com.github.angerona.fw.operators.OperatorSet;
import com.github.angerona.fw.util.ModelAdapter;

/**
 * 
 * @author Tim Janus
 *
 * @param <T>
 * @deprecated
 * @todo implement as MVP (use the {@link OperatorSet} as model)
 */
public class OperatorConfig extends ModelAdapter {
	/** the original parameters of the operator */
	private Map<String, String> originalSettings = new HashMap<>();
	
	/** the parameters typed in by the user */
	private Map<String, String> settings = new HashMap<String, String>();
	
	/** the set of selectable operators */
	private OperatorSet operationSet;
	
	/** the currently selected operator */
	private OperatorCallWrapper selectedOperator;
	
	public OperatorConfig(OperatorSet set) {
		if(set == null)
			throw new IllegalArgumentException();
		
		this.operationSet = set;
		selectedOperator = set.getPreferred();
		originalSettings = selectedOperator.getSettings();
		settings = new HashMap<>(selectedOperator.getSettings());
	}
	
	BaseOperator getSelectedOperator() {
		return selectedOperator;
	}
	
	void setSelectedOperator(OperatorCallWrapper operator) {
		selectedOperator = changeProperty("selectedOperator", selectedOperator, operator);
	}
	
	Map<String, String> getOriginalParameters() {
		return Collections.unmodifiableMap(originalSettings);
	}
	
	Map<String, String> getParameters() {
		return new HashMap<>(settings);
	}
	
	void setParameters(Map<String, String> parameters) {
		this.settings = changeProperty("parameters", this.settings, parameters);
	}
	
	Collection<OperatorCallWrapper> getSelectableOperators() {
		return operationSet.getOperators();
	}
	
	public OperatorCallWrapper getDefaultOperator() {
		return operationSet.getPreferred();
	}
}
