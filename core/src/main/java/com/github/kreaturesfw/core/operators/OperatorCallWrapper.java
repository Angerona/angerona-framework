package com.github.kreaturesfw.core.operators;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.kreaturesfw.core.legacy.SettingsStorage;
import com.github.kreaturesfw.core.operators.parameter.GenericOperatorParameter;
import com.github.kreaturesfw.core.operators.parameter.OperatorParameter;
import com.github.kreaturesfw.core.util.Pair;

/**
 * Wraps an Operator and saves the settings on the caller side.
 * @author Tim Janus
 *
 * @param <T>	The type of the wrapped Operator
 */
public class OperatorCallWrapper 
	implements BaseOperator, SettingsStorage {
	private BaseOperator operator;
	
	private Map<String, String> settings = new HashMap<>();
	
	/**
	 * Ctor: Generates the wrapper around the given operator object
	 * @param operator	The wrapped operator
	 */
	public OperatorCallWrapper(BaseOperator operator) {
		if(operator == null)
			throw new IllegalArgumentException();
		this.operator = operator;
	}
	
	public BaseOperator getImplementation() {
		return operator;
	}
	
	/**
	 * @return An unmodifiable map of settings for the operator.
	 */
	@Override
	public Map<String, String> getSettings() {
		return Collections.unmodifiableMap(settings);
	}
	
	@Override
	public String putSetting(String name, String value) {
		return settings.put(name, value);
	}
	
	@Override
	public String removeSetting(String name) {
		return settings.remove(name);
	}

	@Override
	public String getPosterName() {
		return operator.getPosterName();
	}

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return operator.getOperationType();
	}

	@Override
	public Object process(GenericOperatorParameter gop) {
		for(String key : settings.keySet()) {
			gop.setSetting(key, settings.get(key));
		}
		return operator.process(gop);
	}
	
	public Object process(OperatorParameter castParam) {
		for(String key : settings.keySet()) {
			castParam.putSetting(key, settings.get(key));
		}
		return operator.process(castParam);
	}

	@Override
	public void addSettings(Map<String, String> settings) {
		this.settings.putAll(settings);
	}

	@Override
	public String getSetting(String name, String def) {
		String reval = settings.get(name);
		return reval == null ? def : reval;
	}

	@Override
	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
	
}
