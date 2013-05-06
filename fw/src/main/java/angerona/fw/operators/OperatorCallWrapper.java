package angerona.fw.operators;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import angerona.fw.operators.parameter.OperatorParameter;
import angerona.fw.util.Pair;

/**
 * Wraps an Operator and saves the settings on the caller side.
 * @author Tim Janus
 *
 * @param <T>	The type of the wrapped Operator
 */
public class OperatorCallWrapper implements BaseOperator {
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
	public Map<String, String> getSettings() {
		return Collections.unmodifiableMap(settings);
	}
	
	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
	
	public String putSetting(String name, String setting) {
		return settings.put(name, setting);
	}
	
	public String removeSetting(String name) {
		return settings.remove(name);
	}
	
	public void clearSettings() {
		settings.clear();
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
			gop.setParameter("s_"+key, settings.get(key));
		}
		return operator.process(gop);
	}
	
	public Object process(OperatorParameter castParam) {
		//castParam.setSettings(settings);
		return operator.process(castParam);
	}
	
}
