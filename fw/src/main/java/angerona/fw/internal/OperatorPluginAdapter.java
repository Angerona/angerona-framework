package angerona.fw.internal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import angerona.fw.operators.BaseOperator;

public abstract class OperatorPluginAdapter implements OperatorPlugin {

	private boolean init = false;
	
	private List<Class<? extends BaseOperator>> operators;
	
	protected abstract void registerOperators();
	
	protected void registerOperator(Class<? extends BaseOperator> cls) {
		operators.add(cls);
	}
	
	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		init();
		return Collections.unmodifiableList(operators);
	}
	
	private void init() {
		if(!init) {
			operators = new LinkedList<>();
			registerOperators();
			init = true;
		}
	}
}
