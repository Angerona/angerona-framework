package com.github.angerona.fw.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.listener.PluginAdapter;
import com.github.angerona.fw.operators.BaseOperator;

/**
 * Global storage for the instances of the BaseOperator implementations, the global
 * flag is realized using the Singleton pattern.
 * 
 * The BaseOperator interface defines an interface for the functionality of an
 * operation. Therefore only one instance of the implementations is needed because
 * they are totally stateless. All the data is provided by the caller allowing easy
 * multi-threading.
 *
 * @author Tim Janus
 */
public class OperatorMap extends PluginAdapter {

	/** reference to logger implementation */
	private Logger LOG = LoggerFactory.getLogger(OperatorMap.class);
	
	/** map from operator class names to an instance of the implementation */
	private Map<String, BaseOperator> mOperatorMap = new HashMap<String, BaseOperator>();
	
	private Map<String, InstantiationException> mErrorMap = new HashMap<>();
	
	/**
	 * @param fullJavaClsName The full java class name of the operator class which shall be returned
	 * @return	The operator with the given class name or null if it does not exists.
	 * @throws InstantiationException 
	 */
	public BaseOperator getOperator(String fullJavaClsName) throws InstantiationException {
		if(mOperatorMap.containsKey(fullJavaClsName)) {
			return mOperatorMap.get(fullJavaClsName);
		} else if(mErrorMap.containsKey(fullJavaClsName)) {
			throw mErrorMap.get(fullJavaClsName);
		}
		return null;
	}
	
	/** @return an unmodifiable version of the OperatorMap */
	public Map<String, BaseOperator> getOperatorMap() {
		return Collections.unmodifiableMap(mOperatorMap);
	}
	
	/**
	 * Helper method: Tests if the given cls implements the BaseOperator interface
	 * and if this is the case creates an operator instance in the operator instance
	 * map.
	 * @todo move in extra class.
	 * @param cls	The cls which might be an implementation of BaseOperator
	 */
	private void createOperatorInstance(Class<?> cls) {
		if( getAllInterfaces(cls).contains(BaseOperator.class) &&
			!mOperatorMap.containsKey(cls.getName())) {		
			PluginInstantiator pi = PluginInstantiator.getInstance();
			BaseOperator op = null;
			try {
				op = (BaseOperator)pi.createInstance(cls.getName());
				mOperatorMap.put(cls.getName(), op);
			} catch (InstantiationException|IllegalAccessException e) {
				e.printStackTrace();
				LOG.error("Cannot instantiate '{}': '{}'", cls.getName(), e.getMessage());
				if(e instanceof InstantiationException)
					mErrorMap.put(cls.getName(), (InstantiationException)e);
			}
		}
	}
	
	/**
	 * Tests if the given class implements the BaseOperator interface and if this
	 * is the case destroy the operator instance in the operator instance map.
	 * @todo move operator instance map in own class
	 * @param cls
	 */
	private void destroyOperatorInstance(Class<?> cls) {
		if( getAllInterfaces(cls).contains(BaseOperator.class) &&
			mOperatorMap.containsKey(cls.getName())) {		
			mOperatorMap.remove(cls.getName());
		}
	}
	
	/**
	 * Helper method: Collecting the set of all interfaces implemented by the
	 * given class
	 * @param cls	The class description
	 * @return	A set containing all interfaces implemented by the given class.
	 */
	private Set<Class<?>> getAllInterfaces(Class<?> cls) {
		Set<Class<?>> reval = new HashSet<>();
		while(cls != null) {
			for(Class<?> i : cls.getInterfaces()) {
				reval.add(i);
			}
			cls = cls.getSuperclass();
		}
		return reval;
	}

	@Override
	public void implementationRegistered(Class<?> base, Class<?> impl) {
		createOperatorInstance(impl);
	}

	@Override
	public void implementationUnregistered(Class<?> base, Class<?> impl) {
		destroyOperatorInstance(impl);
	}
	
	private static OperatorMap mInstance;
	
	public static OperatorMap get() {
		if(mInstance == null) {
			mInstance = new OperatorMap();
		}
		return mInstance;
	}
}
