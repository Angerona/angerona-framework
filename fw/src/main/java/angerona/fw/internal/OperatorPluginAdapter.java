package angerona.fw.internal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import angerona.fw.operators.BaseOperator;

/**
 * THis is an abstract base class for operator plugin implementations.
 * Operator plugins extending this class only need to override the 
 * registerOperators method. They can use the registerOperator method
 * to register their operators.
 * 
 * @author Tim Janus
 */
public abstract class OperatorPluginAdapter implements OperatorPlugin {

	/** flag indicating if this plugin is already initalized */
	private boolean init = false;
	
	/** a list containing the class description of the operators provide by this plugin */
	private List<Class<? extends BaseOperator>> operators;
	
	/**
	 * subclasses have to override this method and register their operators here using the
	 * registerOperator method.
	 */
	protected abstract void registerOperators();
	
	/**
	 * Registers the given class description as an operator provided by this interface.
	 * @param cls	The class description.
	 */
	protected void registerOperator(Class<? extends BaseOperator> cls) {
		operators.add(cls);
	}
	
	/**
	 * Ensures that the operator plugin is initalized and returns the operators
	 * provided by this plugin instance.
	 * @return An unmodifiable list of class descriptions of operators implemented by this plugin.
	 */
	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		init();
		return Collections.unmodifiableList(operators);
	}
	
	/**
	 * Helper method: Initializes the operator if it is not initialized yet.
	 */
	private void init() {
		if(!init) {
			operators = new LinkedList<>();
			registerOperators();
			init = true;
		}
	}
}
